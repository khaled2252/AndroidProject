package com.example.androidproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {

    Button signup;
    Button signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //startActivity(new Intent(this,PastTripsActivity.class));
        signup = (Button) findViewById(R.id.signupbtn);
        signin = (Button) findViewById(R.id.signintbtn);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup1 = new Intent(WelcomeActivity.this, SignupActivity.class);
                startActivity(signup1);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login1 = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(login1);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            //Intent user account
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    }
}
