package pl.sokolx.coach.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import pl.sokolx.coach.adapters.TrainingAdapter;
import pl.sokolx.coach.models.TrainingModel;

public class TrainingHistoryFragment extends Fragment {

    public static final String TAG = "HistoryActivity";
    private FirebaseDatabase userFirebaseDatabase;
    private DatabaseReference userDatabaseReference;

    private RecyclerView recyclerView;
    private List<TrainingModel> resultStepList;
    private TrainingAdapter stepAdapter;

    private FirebaseDatabase stepFbDb;
    private DatabaseReference stepDbRef;
    private FirebaseAuth mFirebaseAuth;

    private String userID;
    private TextView emptyText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training_history, container, false);

        mFirebaseAuth = mFirebaseAuth.getInstance();
        stepFbDb = FirebaseDatabase.getInstance();

        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        userID = user.getUid();
        stepDbRef = stepFbDb.getReference("steps");

        //pobieranie liczby wykonanych treningów
        mFirebaseAuth = FirebaseAuth.getInstance();

        if (mFirebaseAuth.getCurrentUser() == null) {
            //finish();
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
        userFirebaseDatabase = userFirebaseDatabase.getInstance();
        userDatabaseReference = userFirebaseDatabase.getReference();
        userID = user.getUid();

        resultStepList = new ArrayList<>();

        emptyText = (TextView) view.findViewById(R.id.text_no_data);

        recyclerView = (RecyclerView) view.findViewById(R.id.step_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        stepAdapter = new TrainingAdapter(getContext(), resultStepList);

        recyclerView.setAdapter(stepAdapter);

        updateList();
        Log.d(TAG, " on Create -> updateList()");
        checkIfEmpty();
        Log.d(TAG, " on Create -> checkIfEmpty()");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getText(R.string.trainings_history));
    }


    private void updateList() {

        stepDbRef.child(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, " public void onChildAdded()");
                resultStepList.add(dataSnapshot.getValue(TrainingModel.class));
                stepAdapter.notifyDataSetChanged();

                checkIfEmpty();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, " public void onChildChanged()");
                TrainingModel stepModel = dataSnapshot.getValue(TrainingModel.class);

                int index = getItemIndex(stepModel);

                resultStepList.set(index, stepModel);
                stepAdapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, " public void onChildRemoved()");
                TrainingModel stepModel = dataSnapshot.getValue(TrainingModel.class);

                int index = getItemIndex(stepModel);

                resultStepList.remove(index);
                stepAdapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private int getItemIndex(TrainingModel step) {
        Log.d(TAG, " private int getItemIndex(...)");
        int index = -1;

        for(int i = 0; i < resultStepList.size(); i++) {
            if(resultStepList.get(i).getKey().equals(step.getKey())) {
                index = i;
                break;
            }
        }
        return index;

    }

    private void checkIfEmpty() {

        Log.d(TAG, " public void checkIfEmpty()");
        if(resultStepList.size() == 0) {
            recyclerView.setVisibility(View.INVISIBLE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.INVISIBLE);
        }
    }
}
