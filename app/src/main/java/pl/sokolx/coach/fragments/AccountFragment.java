package pl.sokolx.coach.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import pl.sokolx.coach.R;
import pl.sokolx.coach.models.UserModel;

public class AccountFragment extends Fragment {

    private static final String TAG = "AccountFrg";

    private ArrayAdapter<String> adapter;
    private ListView list;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private AuthStateListener myAuthListener;
    private DatabaseReference myRef;
    private String userID;

    private ListView ListViewProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        ListViewProfile = (ListView) view.findViewById(R.id.listViewProfile);

        //deklaracje referencji do obiektów. To co dostaniemy mając dostep do danych
        mFirebaseAuth = mFirebaseAuth.getInstance();
        mFirebaseDatabase = mFirebaseDatabase.getInstance();

        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        userID = user.getUid();
        myRef = mFirebaseDatabase.getReference("users");
        myAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Log.d(TAG, "onAuthStateChanged:signedin" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signedout");
                }
            }
        };

        Log.d("Profile/userID ", userID);
        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserModel uInfo = dataSnapshot.getValue(UserModel.class);

                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(uInfo.getDateOfBirth());
                String dateOfBirth = ((c.get(Calendar.DAY_OF_MONTH) < 10) ? "0"+ c.get(Calendar.DAY_OF_MONTH) : c.get(Calendar.DAY_OF_MONTH)) +
                        "-" + ((c.get(Calendar.MONTH) < 9) ? "0"+ (c.get(Calendar.MONTH)+1) : (c.get(Calendar.MONTH)+1)) +
                        "-" + c.get(Calendar.YEAR);
                ArrayList<String> informationProfil = new ArrayList<>();
                informationProfil.add(getResources().getText(R.string.email_string) + ": " + uInfo.getEmail());
                informationProfil.add(getResources().getText(R.string.first_name_string) + ": " + uInfo.getName());
                informationProfil.add(getResources().getText(R.string.adres_string) + ": " + uInfo.getAddress());
                informationProfil.add(getResources().getText(R.string.gender_string) + ": " + uInfo.getGenderType());
                informationProfil.add(getResources().getText(R.string.date_of_birth) + ": " + dateOfBirth);
                informationProfil.add(getResources().getText(R.string.height_string) + ": " + uInfo.getHeight() + " cm");
                informationProfil.add(getResources().getText(R.string.weight_string) + ": " + uInfo.getWeight() + " kg");
                informationProfil.add(getResources().getText(R.string.number_of_workout_string) + ": " + uInfo.getNumberOfWorkout());
                informationProfil.add(getResources().getText(R.string.total_number_of_steps_string) + ": " + uInfo.getUserStepsCounterEver());
                informationProfil.add(getResources().getText(R.string.total_number_of_bmis_string) + ": " + uInfo.getNumberOfBmi());
                informationProfil.add(getResources().getText(R.string.bmi) + ": " + uInfo.getMeasureBMI());

                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, informationProfil);
                ListViewProfile.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getText(R.string.account));
    }
}
