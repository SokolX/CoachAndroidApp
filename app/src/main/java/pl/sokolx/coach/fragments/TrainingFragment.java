package pl.sokolx.coach.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pl.sokolx.coach.R;
import pl.sokolx.coach.activities.LoginActivity;
import pl.sokolx.coach.models.TrainingModel;
import pl.sokolx.coach.models.UserModel;
import pl.sokolx.coach.utils.pedometer.StepDetector;
import pl.sokolx.coach.utils.pedometer.StepListener;

public class TrainingFragment extends Fragment implements SensorEventListener, StepListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase userFirebaseDatabase;
    private DatabaseReference userDatabaseReference;
    private DatabaseReference stepDatabaseReference;
    private DatabaseReference currentTreningUpdate;

    private String userID;
    public int numberOfWorkouts;
    private int userStepsCounterEver;
    private long timeStart;

    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private int numSteps;

    private TextView textViewEverCounter, textViewCounter, textViewDuration;
    private Button buttonStartSteps;
    private Button buttonStopsSteps;

    Handler customHandler = new Handler();

    Runnable updateTimerThread = new Runnable() {

        @Override
        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startStoper;
            updaterStoper = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updaterStoper / 1000);
            int mins = secs / 60;
            secs %= 60;
            int milliseconds = (int) (updaterStoper%1000);
            textViewDuration.setText(mins + ":"
                    + String.format("%2d", secs));
            customHandler.postDelayed(this, 0);
        }
    };
    long startStoper = 0L, timeInMilliseconds = 0L, timeSwapBuff = 0L, updaterStoper = 0L;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_training, container, false);
        textViewEverCounter = (TextView) view.findViewById(R.id.textViewEverCounter);
        //do komunikacji z bazą - sprawdzenie czy aktualnie jest zalogowany jakiś user
        mFirebaseAuth = FirebaseAuth.getInstance();

        if(mFirebaseAuth.getCurrentUser() == null) {
            //finish();
            startActivity(new Intent(getContext(), LoginActivity.class));
        }


        final FirebaseUser user = mFirebaseAuth.getCurrentUser();
        userID = user.getUid();

        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        userDatabaseReference.child(userID).addValueEventListener(new ValueEventListener() {
            //TO DO dać zmieninne tak by można przekazac nr treningu do tworzenia nowego i itrenacji stary kroków
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserModel user = dataSnapshot.getValue(UserModel.class);
                userStepsCounterEver = user.getUserStepsCounterEver();
                textViewEverCounter.setText(userStepsCounterEver + "");
                numberOfWorkouts = user.getNumberOfWorkout() + 1;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener((StepListener) this);

        textViewDuration = (TextView) view.findViewById(R.id.textViewStoper);
        textViewCounter = (TextView) view.findViewById(R.id.textViewCounter);

        buttonStartSteps = (Button) view.findViewById(R.id.buttonStartSteps);
        buttonStopsSteps = (Button) view.findViewById(R.id.buttonStopSteps);

        buttonStopsSteps.setVisibility(View.INVISIBLE);
        buttonStopsSteps.setActivated(false);
        buttonStartSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonStartSteps.setClickable(false);
                buttonStopsSteps.setVisibility(View.VISIBLE);
                buttonStopsSteps.setClickable(true);
                startStoper = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);
                timeStart = System.currentTimeMillis();
                numSteps = 0;

                if(v == buttonStartSteps) {
                    Toast.makeText(getContext(), getResources().getString(R.string.training_started_string), Toast.LENGTH_SHORT).show();
                    sensorManager.registerListener(TrainingFragment.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
                    Log.d("Nr tre if", String.valueOf(numberOfWorkouts));
                    createNewIdStep(numSteps, timeStart, numberOfWorkouts);
                    currentTreningUpdate = FirebaseDatabase.getInstance().getReference().child("steps/"+ userID.toString() + "/step" + (numberOfWorkouts));
                    currentTreningUpdate.child("/timeStart").setValue(System.currentTimeMillis());
                }

            }
        });



        buttonStopsSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStartSteps.setVisibility(View.VISIBLE);
                buttonStopsSteps.setVisibility(View.INVISIBLE);
                buttonStartSteps.setClickable(true);
                buttonStopsSteps.setClickable(false);
                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);

                userStepsCounterEver += numSteps;
                int stepCounterEver = userStepsCounterEver;
                sensorManager.unregisterListener(TrainingFragment.this);

                saveWorkoutAndCounterEver(numberOfWorkouts, userStepsCounterEver);

                long timeStop = System.currentTimeMillis();

                currentTreningUpdate = FirebaseDatabase.getInstance().getReference().child("steps/"+ userID.toString() + "/step" + (numberOfWorkouts));
                currentTreningUpdate.child("/timeStop").setValue(timeStop);
                Toast.makeText(getContext(), getResources().getString(R.string.training_ended_string), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getText(R.string.training));
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        userStepsCounterEver++;
        textViewCounter.setText("" + numSteps);
        textViewEverCounter.setText((userStepsCounterEver) + "");

        currentTreningUpdate = FirebaseDatabase.getInstance().getReference().child("steps/"+ userID.toString() + "/step" + (numberOfWorkouts));
        currentTreningUpdate.child("/step_id").setValue(numberOfWorkouts);
        currentTreningUpdate.child("/key").setValue("step" + numberOfWorkouts);

        currentTreningUpdate.child("/userUID").setValue(userID);
        currentTreningUpdate.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentTreningUpdate.child("/stepCounter").setValue(numSteps);
                currentTreningUpdate.child("/stepCounterEver").setValue(userStepsCounterEver);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void createNewIdStep(int valueStep, long timeStart, int numberOfWorkouts) {

        long timeStop = System.currentTimeMillis();

        int stepCounterEver = valueStep + userStepsCounterEver;
        int stepCounter = valueStep;
        int step_id = numberOfWorkouts;
        String key = "step"+step_id;

        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        String userUID = user.getUid().toString();

        if(stepCounter > 0) {

            TrainingModel stepInformation = new TrainingModel(step_id, stepCounterEver, stepCounter, timeStart, timeStop, key, userUID);
            stepDatabaseReference.child("steps/" + user.getUid() + key).setValue(stepInformation);

        }
    }

    /*Metoda aktualizująca informacje po każdym trernigu w tabeli users liczbę odbytych treningów
     *oraz liczbę przebytych kroków całościowo
     *param numberOfWorkouts - liczba treningów
     *param userStepsCounterEver - liczba przebytych kroków całościowo
     */
    private void saveWorkoutAndCounterEver(int numberOfWorkouts, int userStepsCounterEver) {
        int numberOfWorkout = numberOfWorkouts;
        int userStepCounterEver = userStepsCounterEver;
        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        userFirebaseDatabase = userFirebaseDatabase.getInstance();
        userDatabaseReference = userFirebaseDatabase.getReference().child("users/" + user.getUid());

        userDatabaseReference.child("/numberOfWorkout").setValue(numberOfWorkout);
        userDatabaseReference.child("/userStepsCounterEver").setValue(userStepCounterEver);
    }
}
