package pl.sokolx.coach.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pl.sokolx.coach.R;
import pl.sokolx.coach.exception.UserEmailIsEmptyException;
import pl.sokolx.coach.exception.UserPasswordIsEmptyException;
import pl.sokolx.coach.validator.UserValidator;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonSignUp;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignUp;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);

        progressDialog = new ProgressDialog(this);

        buttonSignUp.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
    }

    private void userLogin() throws UserPasswordIsEmptyException, UserEmailIsEmptyException {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        UserValidator userValidator = new UserValidator().getInstance();
        userValidator.isValidate(this, email, password);

        progressDialog.setMessage(getString(R.string.log_in));
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            Toast.makeText(LoginActivity.this, getResources().getText(R.string.logged_in_successfully),Toast.LENGTH_SHORT).show();
                        } else  {
                            Toast.makeText(LoginActivity.this, getResources().getText(R.string.log_in_please_try_again),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view == buttonSignUp) {
            try {
                userLogin();
            } catch (UserPasswordIsEmptyException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (UserEmailIsEmptyException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        if(view == textViewSignUp) {
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }

    private void updateUI(FirebaseUser user) {

        if(user != null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
