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

import pl.sokolx.coach.activities.LoginActivity;
import pl.sokolx.coach.R;
import pl.sokolx.coach.adapters.BmiAdapter;
import pl.sokolx.coach.models.BmiModel;

public class BmiHistoryFragment extends Fragment {

    public static final String TAG = "BmiHistory";

    private BmiAdapter bmiAdapter;
    private DatabaseReference bmiDbRef;
    private FirebaseDatabase bmiFbDb;
    private TextView emptyText;
    private FirebaseAuth mFirebaseAuth;
    private RecyclerView recyclerView;
    private List<BmiModel> resultBmiList;
    private DatabaseReference userDatabaseReference;
    private FirebaseDatabase userFirebaseDatabase;
    private String userID;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(1);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bmi_history, container, false);
        mFirebaseAuth = mFirebaseAuth.getInstance();
        bmiFbDb = FirebaseDatabase.getInstance();

        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        userID = user.getUid();
        bmiDbRef = bmiFbDb.getReference("bmi/");

        //pobieranie liczby wykonanych pomiarów
        mFirebaseAuth = FirebaseAuth.getInstance();

        if (mFirebaseAuth.getCurrentUser() == null) {
            //finish();
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
        userFirebaseDatabase = userFirebaseDatabase.getInstance();
        userDatabaseReference = userFirebaseDatabase.getReference();
        userID = user.getUid();

        resultBmiList = new ArrayList<>();

        emptyText = (TextView) view.findViewById(R.id.text_no_data);

        recyclerView = (RecyclerView) view.findViewById(R.id.bmi_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        bmiAdapter = new BmiAdapter(resultBmiList);
        recyclerView.setAdapter(bmiAdapter);

        updateList();
        checkIfEmpty();

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getText(R.string.bmi_history));
    }

    private void updateList() {

        bmiDbRef.child(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.d(TAG, " onChildAdded");
                resultBmiList.add(dataSnapshot.getValue(BmiModel.class));
                bmiAdapter.notifyDataSetChanged();

                checkIfEmpty();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Log.d(TAG, " onChildChanged");
                BmiModel bmiModel = dataSnapshot.getValue(BmiModel.class);
                int index = getItemIndex(bmiModel);
                resultBmiList.set(index, bmiModel);
                bmiAdapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Log.d(TAG, " onChildRemoved");
                BmiModel bmiModel = dataSnapshot.getValue(BmiModel.class);
                int index = getItemIndex(bmiModel);
                resultBmiList.remove(index);
                bmiAdapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private int getItemIndex(BmiModel bmi) {

        int index = -1;

        for(int i = 0; i < resultBmiList.size(); i++) {
            if(resultBmiList.get(i).getKey().equals(bmi.getKey())) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void checkIfEmpty() {

        if(resultBmiList.size() == 0) {
            recyclerView.setVisibility(View.INVISIBLE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.INVISIBLE);
        }
    }
}
