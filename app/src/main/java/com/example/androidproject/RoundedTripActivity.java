package com.example.androidproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RoundedTripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounded_trip);
        this.setFinishOnTouchOutside(false);// if user click out side the activity it will not be dismissed
    }
}
