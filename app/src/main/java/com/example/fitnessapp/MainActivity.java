package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    //defines a variable for views in login page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.Register);
        register.setOnClickListener(this);
//        Intialises Views specified in the XML document

        signIn = (Button) findViewById(R.id.SignIn);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.emailLogin);
        editTextPassword = (EditText) findViewById((R.id.passwordLogin));

        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Register:
                startActivity(new Intent(this,RegisterUser.class));
                break;

            case R.id.SignIn:
                userLogin();
                break;
        }
//        activates buttons on login screen.
    }

    private void userLogin() {
        //collection and validation of users login.
        //collect the string from view
        //convert to string
        //trim string to clear spaces...
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Enter valid Email");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length()<6){
            editTextPassword.setError("Min password length is 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        //user has passed validations.

        progressBar.setVisibility(View.VISIBLE);

        //start authentication with firebase.
        //the OnComplete listener checks if the task has been completed
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    //email verification process
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){

                        //redirect to user profile
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this,"Check your email to verify account!", Toast.LENGTH_LONG).show();

                    }


                     //redirect to user profile.
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(MainActivity.this,ProfileActivity.class));

                }else{
                    Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }


    //added menu to login page for dev acccess please delete in final app

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.quick_menu, menu);
//        return true;
//    }


//    // gives quick access menu functionality
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.logoutMenu:
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(MainActivity.this, MainActivity.class));
//                return true;
//            case R.id.profileMenu:
//                Toast.makeText(this, "Profile was selected", Toast.LENGTH_LONG).show();
//
//                return true;
//            case R.id.workoutMenu:
//                Toast.makeText(this, "Workout was selected", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(MainActivity.this, WorkoutActivity.class));
//                return true;
//            case R.id.item1:
//                Toast.makeText(this, "star was selected", Toast.LENGTH_LONG).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }
}