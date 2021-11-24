package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private Button logout;
//    ConstraintLayout WorkoutConstraint, HistoryConstraint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        WorkoutConstraint = (ConstraintLayout) findViewById(R.id.workoutConstraint);
//        HistoryConstraint = (ConstraintLayout) findViewById(R.id.historyConstraint);

        logout = (Button) findViewById(R.id.signOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
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
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                return true;
            case R.id.profileMenu:
                Toast.makeText(this, "Profile was selected", Toast.LENGTH_LONG).show();

                return true;
            case R.id.workoutMenu:
                Toast.makeText(this, "Workout was selected", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ProfileActivity.this, WorkoutActivity.class));
                return true;
            case R.id.item1:
                Toast.makeText(this, "star was selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.historyMenu:
                startActivity(new Intent(ProfileActivity.this, HistoryActivity.class));
                Toast.makeText(this, "History was selected", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}