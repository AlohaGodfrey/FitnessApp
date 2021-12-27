package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private Button logout, mWorkoutNow, mHistory;
    private TextView userName;
    private DatabaseReference dbReference;
    private String userID, name;
    String TAG = "help";
//    ConstraintLayout WorkoutConstraint, HistoryConstraint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        WorkoutConstraint = (ConstraintLayout) findViewById(R.id.workoutConstraint);
//        HistoryConstraint = (ConstraintLayout) findViewById(R.id.historyConstraint);

        userName = (TextView) findViewById(R.id.textView9);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("fullName");

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.getValue(String.class);
                name = snapshot.getValue().toString();
                userName.setText(name);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        logout = (Button) findViewById(R.id.signOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });

        mWorkoutNow = (Button) findViewById(R.id.mWorkoutBtn);
        mWorkoutNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, WorkoutActivity.class));
            }
        });

        mHistory = (Button) findViewById(R.id.mHistoryBtn);
        mHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, HistoryActivity.class));
            }
        });
    }

    private void getUsername(){

    }

//    // adds the quick access menu to activity
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.quick_menu, menu);
//        return true;
//    }

//
//    // gives quick access menu functionality
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.logoutMenu:
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
//                return true;
//            case R.id.profileMenu:
//                Toast.makeText(this, "Profile was selected", Toast.LENGTH_LONG).show();
//
//                return true;
//            case R.id.workoutMenu:
//                Toast.makeText(this, "Workout was selected", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(ProfileActivity.this, WorkoutActivity.class));
//                return true;
////            case R.id.item1:
////                Toast.makeText(this, "star was selected", Toast.LENGTH_LONG).show();
////                return true;
//            case R.id.historyMenu:
//                startActivity(new Intent(ProfileActivity.this, HistoryActivity.class));
//                Toast.makeText(this, "History was selected", Toast.LENGTH_LONG).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }
}