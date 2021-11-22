package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Workout extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //countdown timer in Milliseconds
    private static final long START_TIME_IN_MILLIS = 3000;

    private RadioGroup radioGroup;
    private RadioButton radioButton, lastRadioBtn;

    private TextView mTextViewCountdown,set1,set2,set3,set4;;
    private Button mButtonStartPause,mCompWorkout;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    private String userWeight, userExercise;

    private Vibrator vibrator;

    private int setCounter;
//    private ConstraintLayout mLayoutSetCounter, mLayoutTimerView, mLayoutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        //timer resources
        mTextViewCountdown = (TextView) findViewById(R.id.countdownText);
        mCompWorkout = (Button) findViewById(R.id.mCompWorkout);
        mButtonStartPause = (Button) findViewById(R.id.start);


        //Counts the number of sets a user done
        setCounter = 0;

        //intialises exercise radio groups
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        lastRadioBtn = (RadioButton) findViewById(R.id.radio_Deadlft);

        //Vibrator for the notifications
        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);

        //set counter circles
        set1 = (TextView) findViewById(R.id.circle1);
        set2 = (TextView) findViewById(R.id.circle2);
        set3 = (TextView) findViewById(R.id.circle3);
        set4 = (TextView) findViewById(R.id.circle4);



        //used to list the workout selection
        Spinner spinnerWeight = findViewById(R.id.mSelWeightSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.weight_selection, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeight.setAdapter(adapter);
        spinnerWeight.setOnItemSelectedListener(this);


        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTimerRunning){
                    pauseTimer();
                }else{
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


    }


    private void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis,1000) {
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
                Uri alarmSound = RingtoneManager. getDefaultUri (RingtoneManager. TYPE_NOTIFICATION );
                MediaPlayer mp = MediaPlayer. create (getApplicationContext(), alarmSound);
                mp.start();

                workoutCounter();
                //change setcounter colour.
                //hide start button and show complete workout button red.

            }
        }.start();

        mTimerRunning=true;
        mButtonStartPause.setText("PAUSE BREAK");
    }

    private void pauseTimer(){
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("CONTINUE BREAK");
    }

    private void resetTimer(){
        //resets timer once it hits zero
        if((mTextViewCountdown.getText().toString().trim()).equals("00:00")){
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
        }

        updateCountdownText();
        mButtonStartPause.setEnabled(true);
    }

    private void updateCountdownText(){
        int minutes = (int) mTimeLeftInMillis/1000/60;
        int seconds = (int) mTimeLeftInMillis/1000%60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        mTextViewCountdown.setTextSize(50);
        mTextViewCountdown.setText(timeLeftFormatted);

    }

    private void workoutCounter(){
        setCounter+=1;


        switch (setCounter){
            case 1: set1.setBackground(getResources().getDrawable(R.drawable.redcircle));
                mCompWorkout.setVisibility(View.VISIBLE);
                mButtonStartPause.setVisibility(View.GONE);
                //devops move two functions to case 4 to get buttons to stop activating early
                break;
            case 2: set2.setBackground(getResources().getDrawable(R.drawable.redcircle));
                break;
            case 3: set3.setBackground(getResources().getDrawable(R.drawable.redcircle));
                break;
            case 4: set4.setBackground(getResources().getDrawable(R.drawable.redcircle));

                break;
            default:

        }

    }

    private void getUserSelection(){
        //collects the user exercise and weight selection
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

//        validation to ensure the user chooses an exercise.
        if (radioGroup.getCheckedRadioButtonId() == -1) {
//            lastRadioBtn.setError("Select Item");//Set error to last Radio button
            Toast.makeText(this, "Select Exercise!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, userWeight, Toast.LENGTH_LONG).show();
            userExercise = radioButton.getText().toString();
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




}