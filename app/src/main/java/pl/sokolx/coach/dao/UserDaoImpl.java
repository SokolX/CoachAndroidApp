package pl.sokolx.coach.dao;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pl.sokolx.coach.models.UserModel;

public class UserDaoImpl implements UserDao {

    private static final String TAG = "UserDaoImpl";

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef;
    private final static String USERS_DATABASE_PATH = "users";
    UserModel user = new UserModel();

    public UserDaoImpl() {
        firebaseAuth = FirebaseAuth.getInstance();
        registerListener();

        if(usersRef == null) {
            usersRef = firebaseDatabase.getReference(USERS_DATABASE_PATH);
        }

    }

    @Override
    public void loginUser(Activity activity, String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        } else {
                            Log.i(TAG, "signInWithEmail:failed", task.getException());
                        }
                    }
                });
    }

    @Override
    public void createAccount(final Activity activity, final UserModel userModel, final String password) {

        firebaseAuth.createUserWithEmailAndPassword(userModel.getEmail(), password).
                addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            addUser(userModel);
                            Toast.makeText(activity, "Register OK", Toast.LENGTH_LONG).show();
                            Log.d("Register OK", "Sign OK");
                        } else {
                            Toast.makeText(activity, "tzw chuj", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public UserModel getUser() {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        usersRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user = dataSnapshot.getValue(UserModel.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d(TAG, "po za"  + String.valueOf(user.getHeight()));
        return user;
    }

    private void registerListener() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.i(TAG, "onAuthStateChanged:signed_in " + user.getUid());

                } else {
                    // User is signed out
                    Log.i(TAG, "sign out");
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public boolean isCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public void unregister() {
        firebaseAuth.removeAuthStateListener(authStateListener);
    }


    private void addUser(final UserModel user) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //String uid = firebaseAuth.getUid();
        usersRef.child(uid).setValue(user);
    }
}
