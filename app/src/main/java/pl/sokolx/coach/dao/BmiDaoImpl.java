package pl.sokolx.coach.dao;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pl.sokolx.coach.models.BmiModel;

public class BmiDaoImpl implements BmiDao {

    private static final String TAG = "BmiDaoImpl";

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference bmiRef;
    private final static String BMI_DATABASE_PATH = "bmi";

    public BmiDaoImpl() {

        if(bmiRef == null) {
            bmiRef = firebaseDatabase.getReference(BMI_DATABASE_PATH);
        }

    }

    @Override
    public void addBmi(Activity activity, BmiModel bmiModel) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bmiRef.child(uid).child(bmiModel.getKey()).setValue(bmiModel);

    }

}
