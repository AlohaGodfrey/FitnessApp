package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
//import java.util.logging.Handler;

public class WorkoutActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //countdown timer in Milliseconds
    private static final long START_TIME_IN_MILLIS = 3000;
    private static final String TAG = "WorkoutActivity";

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private TextView mTextViewCountdown, set1, set2, set3, set4;

    private Button mButtonStartPause, mCompWorkout;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    private String userWeight, userExercise, userID, userDate, sessionData;

    private Vibrator vibrator;

    private int setCounter;
    //    private ConstraintLayout mLayoutSetCounter, mLayoutTimerView, mLayoutButton;
    private DatabaseReference dbReference;
//    private FirebaseUser currentFirebaseUser;
    private Date date;

    private List<String> fitnessHistory;
//handlers runs the download db module twice. if you request once
//    you only get blank result. requrest twice for full deets.
//    final Handler handler = new Handler();
//    final Runnable r = new Runnable() {
//        public void run() {
//            handler.postDelayed(this, 1000);
//            downDbList();
//        }
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        //firebase db resources
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Workout");
        fitnessHistory = new ArrayList<>();

        //timer resources
        mTextViewCountdown = (TextView) findViewById(R.id.countdownText);
        mCompWorkout = (Button) findViewById(R.id.mCompWorkout);
        mButtonStartPause = (Button) findViewById(R.id.start);


        //Counts the number of sets a user done
        setCounter = 0;

        //intializes exercise radio groups
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);


        //Vibrator for the notifications
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        //set counter circles
        set1 = (TextView) findViewById(R.id.circle1);
        set2 = (TextView) findViewById(R.id.circle2);
        set3 = (TextView) findViewById(R.id.circle3);
        set4 = (TextView) findViewById(R.id.circle4);

        //instantiate date
        date = Calendar.getInstance().getTime();
        //Formats Date
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        userDate = df.format(date);


        //used to list the workout selection
        Spinner spinnerWeight = findViewById(R.id.mSelWeightSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.weight_selection, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeight.setAdapter(adapter);
        spinnerWeight.setOnItemSelectedListener(this);


        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mCompWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get userworkout
                //get userWeight
                //get userID?
                //get current date
                //upload data to cloud
                getUserSelection();
            }
        });


        updateCountdownText();
        //add toast message
        //make start button dynamic. and pause break.
        //make complete workout button red
        //change workout counter circles to red and start timer.
        //setBackground(getResources().getDrawable(R.drawable.redcircle));

        //downDbList();
        //downDbList2();
    }


    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setText("START BREAK");
                mButtonStartPause.setEnabled(false);

                //Resets Break timers
                resetTimer();

                //audio and vibration to alert user break is complete
                vibrator.vibrate(500);
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), alarmSound);
                mp.start();

                workoutCounter();
                //change setcounter colour.
                //hide start button and show complete workout button red.

            }
        }.start();

        mTimerRunning = true;
        mButtonStartPause.setText("PAUSE BREAK");
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("CONTINUE BREAK");
    }

    private void resetTimer() {
        //resets timer once it hits zero
        if ((mTextViewCountdown.getText().toString().trim()).equals("00:00")) {
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
        }

        updateCountdownText();
        mButtonStartPause.setEnabled(true);
    }

    private void updateCountdownText() {
        int minutes = (int) mTimeLeftInMillis / 1000 / 60;
        int seconds = (int) mTimeLeftInMillis / 1000 % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountdown.setTextSize(50);
        mTextViewCountdown.setText(timeLeftFormatted);

    }

    private void workoutCounter() {
        setCounter += 1;


        switch (setCounter) {
            case 1:
                set1.setBackground(getResources().getDrawable(R.drawable.redcircle));
                mCompWorkout.setVisibility(View.VISIBLE);
                mButtonStartPause.setVisibility(View.GONE);
                //devops move two functions to case 4 to get buttons to stop activating early
                break;
            case 2:
                set2.setBackground(getResources().getDrawable(R.drawable.redcircle));
                break;
            case 3:
                set3.setBackground(getResources().getDrawable(R.drawable.redcircle));
                break;
            case 4:
                set4.setBackground(getResources().getDrawable(R.drawable.redcircle));

                break;
            default:

        }

    }


    //spinner OnItemSelected Listener.
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userWeight = parent.getItemAtPosition(position).toString();
        //use text for later.
        //display weight spinner.

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void getUserSelection() {
        //collects the user exercise and weight selection
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

//        validation to ensure the user chooses an exercise.
        if (radioGroup.getCheckedRadioButtonId() == -1) {
//            lastRadioBtn.setError("Select Item");//Set error to last Radio button
            Toast.makeText(this, "Select Exercise!", Toast.LENGTH_LONG).show();
        } else {
            //Collects user weight selection
            userExercise = radioButton.getText().toString();

            //Access Workout Class
//            Workout workout = new Workout(userExercise, userWeight, userDate);

            //configure database workoutHistory using arrays
//            r.run();
            //downDbList();
           upDbList(userExercise, userWeight, userDate);
           startActivity(new Intent(WorkoutActivity.this,HistoryActivity.class));

            Log.d(TAG, String.valueOf(fitnessHistory.isEmpty()));
            Log.d(TAG, String.valueOf(fitnessHistory.size()));

        }

    }

    private void downDbList(){
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    fitnessHistory.clear();
                    for(DataSnapshot dss:snapshot.getChildren()){
                        String fitnessEvent = dss.getValue(String.class);
                        fitnessHistory.add(fitnessEvent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        int count = 0;
        String ex = "";
        for(int i=0;i<fitnessHistory.size();i++){
            ex = ex + fitnessHistory.get(i)+",";
        }
        Toast.makeText(getApplicationContext(),ex,Toast.LENGTH_LONG).show();
    }

    private void downDbList2(){
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    fitnessHistory.clear();
                    for(DataSnapshot dss:snapshot.getChildren()){
                        String fitnessEvent = dss.getValue(String.class);
                        fitnessHistory.add(fitnessEvent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        int count = 0;
        String ex = "";
        for(int i=0;i<fitnessHistory.size();i++){
            ex = ex + fitnessHistory.get(i)+",";
        }
        Toast.makeText(getApplicationContext(),ex,Toast.LENGTH_LONG).show();
    }

    private void upDbList(String userExercise, String userWeight, String userDate){
        int count = 0;
        String ex = "";
        for(int i=0;i<fitnessHistory.size();i++){
            ex = ex + fitnessHistory.get(i)+",";
        }
        Toast.makeText(getApplicationContext(),ex,Toast.LENGTH_LONG).show();

        sessionData = userExercise+" ... "+userWeight +"kg ... "+userDate;
        fitnessHistory.add(sessionData);

        dbReference.push().setValue(sessionData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"List is uploaded successfully",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // adds the quick access menu to activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.quick_menu, menu);
        return true;
    }


    // gives quick access menu functionality
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutMenu:
                //sign user out
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(WorkoutActivity.this, MainActivity.class));
                return true;
            case R.id.profileMenu:
                Toast.makeText(this, "Profile was selected", Toast.LENGTH_LONG).show();
                startActivity(new Intent(WorkoutActivity.this, ProfileActivity.class));
                return true;
            case R.id.item1:
                Toast.makeText(this, "star was selected", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}