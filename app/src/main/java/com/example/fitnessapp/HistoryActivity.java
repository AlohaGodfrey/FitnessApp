package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = "HistoryActivity";

    private DatabaseReference dbReference;
    private ArrayList<String> fitnessHistory;
    private String userID;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    final Handler handler = new Handler();
    final Runnable r = new Runnable() {
        public void run() {
            handler.postDelayed(this, 1000);
            downDbList();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //firebase db resources
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Workout");


        //fills in fitnessHistory Array from db
        fitnessHistory = new ArrayList<>();
        fitnessHistory.add("Im am tired monkey");
        fitnessHistory.add("Please help me plz");
        r.run();
        Log.d(TAG, String.valueOf(fitnessHistory.isEmpty()));
        Log.d(TAG, String.valueOf(fitnessHistory.size()));
        Toast.makeText(getApplicationContext(),String.valueOf(fitnessHistory.size()),Toast.LENGTH_LONG).show();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_View);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MainAdapter(fitnessHistory);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void downDbList(){
        //downloads userData from the firebase DB and loads into String Array

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){


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

        //Log.d(TAG, fitnessHistory.get(1));

//        int count = 0;
//        String ex = "";
//        for(int i=0;i<fitnessHistory.size();i++){
//            ex = ex + fitnessHistory.get(i)+",";
//        }
//        Toast.makeText(getApplicationContext(),ex,Toast.LENGTH_LONG).show();


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
                startActivity(new Intent(HistoryActivity.this, MainActivity.class));
                return true;
            case R.id.profileMenu:
                Toast.makeText(this, "Profile was selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.workoutMenu:
                Toast.makeText(this, "Workout was selected", Toast.LENGTH_LONG).show();
                startActivity(new Intent(HistoryActivity.this, WorkoutActivity.class));
                return true;
            case R.id.item1:
                Toast.makeText(this, "star was selected", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}