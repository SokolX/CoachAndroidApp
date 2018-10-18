package pl.sokolx.coach.fragments;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pl.sokolx.coach.R;
import pl.sokolx.coach.dao.BmiDao;
import pl.sokolx.coach.dao.UserDao;
import pl.sokolx.coach.dao.UserDaoImpl;
import pl.sokolx.coach.models.BmiModel;
import pl.sokolx.coach.models.UserModel;

public class BmiNewFragment extends Fragment implements OnClickListener {

    private static final String TAG = "BmiNewFrag";

    TextView textViewHeightUser;
    Button btnWeightUser;

    private FirebaseAuth mFirebaseAuth;
    FirebaseUser currentUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference userDatabaseReference;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener myAuthListener;

    private String userID;

    public int height;
    public int weight;
    public int numberOfBmi;

    private UserDao userDao;
    private BmiDao bmiDao;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bmi_new, container, false);

        textViewHeightUser = (TextView) view.findViewById(R.id.textViewHeight);
        btnWeightUser = (Button) view.findViewById(R.id.buttonWeight);

        userDao = new UserDaoImpl();
        UserModel userModel = userDao.getUser();

        textViewHeightUser.setText(userModel.getHeight() + " cm");
        btnWeightUser.setText(userModel.getWeight() + " kg");
        numberOfBmi = userModel.getNumberOfBmi();

        btnWeightUser.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAddBmi);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOfBmi++;
                createNewIdBmi(height, weight, numberOfBmi);
                saveNumberOfBmiAndWeight(numberOfBmi, weight, height);
            }
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getText(R.string.new_bmi));
    }

    public void onClick(View v) {
        numberPicker();
    }

    private void createNewIdBmi(int height, int weight, int numberOfBmi) {

        long dataBmi = System.currentTimeMillis();

        int measure_id = numberOfBmi;
        String key = "bmi" + numberOfBmi;
        Double measureBMI = weight / ((double) height * height / 10000); //waga / wzrost * wzrost [6 / 1.65 * 1.65]
        int weightBmi = weight;
        //java.text.DecimalFormat dwaMiejscaPoPrzecinku = new java.text.DecimalFormat("0.00");
        //Double bmi = new Double(dwaMiejscaPoPrzecinku.format(measureBMI));
        String wynik2 = String.valueOf(measureBMI);
        String pokaz = wynik2.substring(0,5);
        Double bmi = Double.valueOf(pokaz);

        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        String userUID = user.getUid().toString();
        BmiModel bmiMeasurment = new BmiModel(measure_id, dataBmi, bmi, weightBmi, userUID, key);
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("bmi");
        userDatabaseReference.child(user.getUid() + "/bmi" + numberOfBmi).setValue(bmiMeasurment);
    }

    private void saveNumberOfBmiAndWeight(int numberOfBmi, int weight, int height) {

        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users");

        Double measureBMI = weight / ((double) height * height / 10000); //waga / wzrost * wzrost [6 / 1.65 * 1.65]
        String wynik2 = String.valueOf(measureBMI);
        String pokaz = wynik2.substring(0,5);
        //java.text.DecimalFormat dwaMiejscaPoPrzecinku = new java.text.DecimalFormat("0.00");
        Double bmi = Double.valueOf(pokaz);
        //new Double(dwaMiejscaPoPrzecinku.format(measureBMI));


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Dodano pomyślnie nowy pomiar BMI! ");
        //głupie formatowanie tresci dialoga
        builder.setMessage("\n\n                              " + bmi);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();

        userDatabaseReference.child(user.getUid()+"/numberOfBmi").setValue(numberOfBmi);
        userDatabaseReference.child(user.getUid()+"/weight").setValue(weight);
        userDatabaseReference.child(user.getUid() + "/measureBMI").setValue(bmi);
    }

    private void numberPicker() {

        final NumberPicker calkowita = new NumberPicker(getContext());
        calkowita.setMinValue(30);
        calkowita.setMaxValue(300);
        calkowita.setValue(weight);
        NumberPicker.OnValueChangeListener myValueChangeListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                btnWeightUser.setText(newVal + " kg");
            }
        };

        calkowita.setOnValueChangedListener(myValueChangeListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setView(calkowita);
        builder.setTitle("Waga [kg]").setIcon(R.mipmap.ic_ludzik);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                weight = calkowita.getValue();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
