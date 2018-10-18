package pl.sokolx.coach.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

import pl.sokolx.coach.R;
import pl.sokolx.coach.activities.LoginActivity;
import pl.sokolx.coach.adapters.CoachAdapter;
import pl.sokolx.coach.adapters.MessageAdapter;
import pl.sokolx.coach.models.CoachModel;
import pl.sokolx.coach.models.MessageModel;

public class MessagesFragment extends Fragment implements OnClickListener {

    private FirebaseDatabase coachFbDb;
    private DatabaseReference coachDbRef;
    private FirebaseDatabase messagesFbDb;
    private DatabaseReference messagesDbRef;

    private FirebaseAuth FbAuth;

    private RecyclerView recyclerView;
    private List<CoachModel> finalContactsList;
    private CoachAdapter coachAdapter;

    private RecyclerView recyclerViewMessages;
    private List<MessageModel> finalMessagesList;
    private MessageAdapter messageAdapter;

    private String userID;
    private TextView emptyText;
    private TextView textViewNoMessages;

    private ImageView buttonSendMessage;
    private EditText editTextcontentMessage;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        buttonSendMessage = (ImageView) view.findViewById(R.id.buttonSendMessage);
        editTextcontentMessage = (EditText) view.findViewById(R.id.editTextContentMessage);

        buttonSendMessage.setOnClickListener(this);

        FbAuth = FbAuth.getInstance();
        FirebaseUser user = FbAuth.getCurrentUser();
        userID = user.getUid();

        //czy jest zalogowany, jeżeli nie to przekieruj do widoku logowania
        FbAuth = FirebaseAuth.getInstance();

        if (FbAuth.getCurrentUser() == null) {

            startActivity(new Intent(getContext(), LoginActivity.class));
        }

        coachFbDb = coachFbDb.getInstance();
        coachDbRef = coachFbDb.getReference("coach");
        coachDbRef.keepSynced(true); //this will Enable offline supports of Firebase database and will load data faste but need to add more thigns
        userID = user.getUid();
        finalContactsList = new ArrayList<>();

        emptyText = (TextView) view.findViewById(R.id.text_no_contact);

        recyclerView = (RecyclerView) view.findViewById(R.id.contact_list);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        coachAdapter = new CoachAdapter(finalContactsList);

        recyclerView.setAdapter(coachAdapter);


        updateListContacts();
        checkIfEmpty();

        messagesFbDb = messagesFbDb.getInstance();
        messagesDbRef = messagesFbDb.getReference("messages");

        userID = user.getUid();

        finalMessagesList = new ArrayList<>();

        textViewNoMessages = (TextView) view.findViewById(R.id.textViewNoMessages);
        recyclerViewMessages = (RecyclerView) view.findViewById(R.id.messages_list);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewMessages.setHasFixedSize(true);
        messageAdapter = new MessageAdapter(finalMessagesList);

        recyclerViewMessages.setAdapter(messageAdapter);
        progressDialog = new ProgressDialog(getContext());
        finalMessagesList.clear();
        updateMessages();
        checkIfEmptyMessage();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getText(R.string.messages));
    }

    private void updateListContacts() {

        coachDbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                finalContactsList.add(dataSnapshot.getValue(CoachModel.class));
                coachAdapter.notifyDataSetChanged();

                checkIfEmpty();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                CoachModel contactsModel = dataSnapshot.getValue(CoachModel.class);

                int index = getItemIndex(contactsModel);

                finalContactsList.set(index, contactsModel);
                coachAdapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                CoachModel contactsModel = dataSnapshot.getValue(CoachModel.class);

                int index = getItemIndex(contactsModel);

                finalContactsList.remove(index);
                coachAdapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private int getItemIndex(CoachModel contact) {

        int index = -1;

        for(int i = 0; i < finalContactsList.size(); i++) {
            if(finalContactsList.get(i).getKey().equals(contact.getKey())) {
                index = i;
                break;
            }
        }
        return index;

    }

    private void checkIfEmpty() {

        if(finalContactsList.size() == 0) {
            recyclerView.setVisibility(View.INVISIBLE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {

        if(view == buttonSendMessage) {
            sendMessage(userID);
            progressDialog.setProgressStyle(2);
            progressDialog.show();
            checkIfEmptyMessage();
        }

    }
    private void sendMessage(String userID) {

        String key = messagesDbRef.push().getKey();
        String contentMessage = editTextcontentMessage.getText().toString().trim();
        long dateSend = System.currentTimeMillis();

        //TO DO
        String keyTo = "";
        String keyFrom = userID;
        MessageModel message = new MessageModel(dateSend, keyTo, contentMessage, key, keyFrom, userID);
        messagesDbRef.child(userID).child(key).setValue(message);
        editTextcontentMessage.setText(null);

    }

    private void updateMessages() {

        finalMessagesList.clear();
        messagesDbRef.child(userID).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshotMessage, String s) {

                finalMessagesList.add(dataSnapshotMessage.getValue(MessageModel.class));
                messageAdapter.notifyDataSetChanged();
                checkIfEmptyMessage();
                progressDialog.dismiss();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshotMessage, String s) {

                MessageModel messageModel = dataSnapshotMessage.getValue(MessageModel.class);
                int indexMessage = getItemMessageIndex(messageModel);

                finalMessagesList.set(indexMessage, messageModel);
                messageAdapter.notifyItemChanged(indexMessage);
                progressDialog.dismiss();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshotMessage) {
                MessageModel messageModel = dataSnapshotMessage.getValue(MessageModel.class);

                int index = getItemMessageIndex(messageModel);

                finalMessagesList.remove(index);
                messageAdapter.notifyItemRemoved(index);
                progressDialog.dismiss();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int getItemMessageIndex(MessageModel message) {

        int indexMessage = -1;
        for(int i = 0; i < finalMessagesList.size(); i++) {
            if(finalMessagesList.get(i).getKey().equals(message.getKey())) {

                indexMessage = i;
                break;
            }
        }
        return indexMessage;
    }

    private void checkIfEmptyMessage() {

        if(finalMessagesList.size() == 0) {
            recyclerViewMessages.setVisibility(View.INVISIBLE);
            textViewNoMessages.setVisibility(View.VISIBLE);

        } else {
            recyclerViewMessages.setVisibility(View.VISIBLE);
            textViewNoMessages.setVisibility(View.INVISIBLE);
            recyclerViewMessages.scrollToPosition(finalMessagesList.size()-1);
        }
    }
}
