package com.example.androidproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button signup;
    Button signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startActivity(new Intent(this,PastTripsActivity.class));
        signup = (Button) findViewById(R.id.signupbtn);
        signin = (Button) findViewById(R.id.signintbtn);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup1 = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(signup1);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login1 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login1);
            }
        });
    }
}
