package pl.sokolx.coach.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.sokolx.coach.R;
import pl.sokolx.coach.dao.BmiDao;
import pl.sokolx.coach.dao.BmiDaoImpl;
import pl.sokolx.coach.dao.UserDao;
import pl.sokolx.coach.dao.UserDaoImpl;
import pl.sokolx.coach.exception.BmiIsNotSetException;
import pl.sokolx.coach.exception.UserDateOfBirthIsNotSetException;
import pl.sokolx.coach.exception.UserEmailIsEmptyException;
import pl.sokolx.coach.exception.UserNameIsEmptyException;
import pl.sokolx.coach.exception.UserPasswordIsEmptyException;
import pl.sokolx.coach.exception.UserPasswordShortLengthException;
import pl.sokolx.coach.models.BmiModel;
import pl.sokolx.coach.models.UserModel;
import pl.sokolx.coach.utils.DateUtils;
import pl.sokolx.coach.validator.UserValidator;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = "RegisterAct";

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName, editTextAddress;
    private Button buttonDateOfBirth;
    private TextView textViewFirstBMI;
    private NumberPicker numberPickerSetHeight = null;
    private NumberPicker numberPickerSetWeight = null;
    private Spinner spinnerGender;

    int day, month, year;
    int dayFinal, monthFinal, yearFinal;

    private FirebaseAuth firebaseAuth;

    private UserDao userDao;
    private BmiDao bmiDao;

    private ProgressDialog progressDialog;

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing() ){
            progressDialog.cancel();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDao = new UserDaoImpl();

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        textViewFirstBMI = (TextView) findViewById(R.id.textViewFirstBMI);

        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonDateOfBirth = (Button) findViewById(R.id.buttonDateOfBirth);
        buttonDateOfBirth.setOnClickListener(this);

        numberPickerSetHeight = (NumberPicker) findViewById(R.id.numberPickerHeight);
        numberPickerSetHeight.setMinValue(70);
        numberPickerSetHeight.setMaxValue(260);
        numberPickerSetHeight.setValue(169);

        numberPickerSetWeight = (NumberPicker) findViewById(R.id.numberPickerWeight);
        numberPickerSetWeight.setMinValue(30);
        numberPickerSetWeight.setMaxValue(260);
        numberPickerSetWeight.setValue(70);

        numberPickerSetHeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                setBmi();
            }
        });
        numberPickerSetWeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                setBmi();
            }
        });


        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);
    }

    private void registerUser() throws UserPasswordIsEmptyException, UserEmailIsEmptyException, UserPasswordShortLengthException, UserDateOfBirthIsNotSetException, BmiIsNotSetException, UserNameIsEmptyException {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        UserValidator userValidator = new UserValidator().getInstance();
        userValidator.isValidate(this, email, password, editTextName.getText().toString(), editTextAddress.getText().toString(), buttonDateOfBirth.getText().toString(), textViewFirstBMI.getText().toString());

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgress(100);
        progressDialog.setMessage(getString(R.string.registration_in_progress));
        progressDialog.show();

        UserModel newUser = new UserModel();
        newUser.setEmail(email);
        newUser.setName(String.valueOf(editTextName.getText()));
        newUser.setAddress(String.valueOf(editTextAddress.getText()));
        newUser.setHeight(numberPickerSetHeight.getValue());
        newUser.setWeight(numberPickerSetWeight.getValue());
        newUser.setDateOfBirth(DateUtils.stringDateToLong(buttonDateOfBirth.getText().toString().trim()));
        newUser.setMeasureBMI(Double.parseDouble(textViewFirstBMI.getText().toString()));
        newUser.setGenderType(spinnerGender.getSelectedItem().toString());
        newUser.setUserType("player");
        newUser.setNumberOfWorkout(0);
        newUser.setNumberOfBmi(1);
        newUser.setUserStepsCounterEver(0);

        userDao.createAccount(this, newUser, String.valueOf(editTextPassword.getText()));

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onSignupSuccess();
                progressDialog.dismiss();
            }

        }, 3000);
        onSignupFailed();
    }

    private void createFirstBmi() {
        Integer height = ((Integer) numberPickerSetHeight.getValue());
        Integer weight = ((Integer) numberPickerSetWeight.getValue());
        //Java.text.DecimalFormat dwaMiejscaPoPrzecinku = new java.text.DecimalFormat("0.00");
        //Double bmi = new Double(dwaMiejscaPoPrzecinku.format(measureBMI));
        Double measureBMI = Double.parseDouble(textViewFirstBMI.getText().toString());
        String wynik2 = String.valueOf(measureBMI);
        String pokaz = wynik2.substring(0,5);
        Double bmi = Double.valueOf(pokaz);
        BmiModel firstBmi = new BmiModel(1, System.currentTimeMillis(), bmi, weight, firebaseAuth.getUid(), "bmi1");

        bmiDao = new BmiDaoImpl();
        bmiDao.addBmi(this, firstBmi);
    }

    @Override
    public void onClick(View view) {

        if(view == buttonRegister) {
            try {
                registerUser();
            } catch (UserPasswordIsEmptyException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (UserEmailIsEmptyException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (UserPasswordShortLengthException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (UserDateOfBirthIsNotSetException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (BmiIsNotSetException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (UserNameIsEmptyException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if(view == buttonDateOfBirth) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, RegisterActivity.this, year, month, day);
            datePickerDialog.show();
        }
    }

    public void onSignupSuccess() {
        buttonRegister.setEnabled(true);
        Intent intent = new Intent(this, LoginActivity.class);
        setResult(RESULT_OK, intent);
        createFirstBmi();
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Nie dało się", Toast.LENGTH_LONG).show();
        buttonRegister.setEnabled(true);
        progressDialog.dismiss();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        yearFinal = i;
        monthFinal = i1 + 1;
        dayFinal = i2;

        buttonDateOfBirth.setText(yearFinal + "-" + ((monthFinal>9)?monthFinal:"0"+monthFinal) + "-" + ((dayFinal>9)?dayFinal:"0"+dayFinal));
    }

    public void setBmi() {
        Integer height = ((Integer) numberPickerSetHeight.getValue());
        Integer weight = ((Integer) numberPickerSetWeight.getValue());
        Double measureBMI = weight/ ((double) height * height / 10000); //waga / wzrost * wzrost [6 / 1.65 * 1.65]
        String wynik2 = String.valueOf(measureBMI);
        String pokaz = wynik2.substring(0,5);
        //java.text.DecimalFormat dwaMiejscaPoPrzecinku = new java.text.DecimalFormat("0.00");
        //String wynik = new Double(dwaMiejscaPoPrzecinku.format(measureBMI)).toString(); //dwa miejsca po przecinku
        textViewFirstBMI.setText(pokaz);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
