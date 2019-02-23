package com.example.androidproject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import java.util.ArrayList;
import java.util.Calendar;

public class PastTripsDetails extends AppCompatActivity {


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PastTripsDetails.this, PastTripsActivity.class);
        startActivity(intent);
        finish();
    }

    private EditText mTripName, mTripStartPoint, mTripEndPoint, mTripNotes;
    private RadioButton mOneWayTrip, mRoundedTrip;
    private Button mSaveTrip;
    private TextView mDateAndTimeValue, mTripStatue;

    private DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_trips_details);


        mTripName = findViewById(R.id.edt_past_trip_details_trip_name);
        mTripStartPoint = findViewById(R.id.edt_past_trip_details_start_point);
        mTripEndPoint = findViewById(R.id.edt_past_trip_details_end_point);
        mTripNotes = findViewById(R.id.edt_past_trip_details_trip_notes);
        mOneWayTrip = findViewById(R.id.rd_past_trip_details_one_way_trip);
        mRoundedTrip = findViewById(R.id.rd_past_trip_details_round_trip);
        mSaveTrip = findViewById(R.id.btn_past_trip_details_save_trip);
        mDateAndTimeValue = findViewById(R.id.txt_past_trip_details_trip_time_and_date_value);
        mTripStatue = findViewById(R.id.txt_past_trip_details_trip_statue);

        databaseAdapter = new DatabaseAdapter(PastTripsDetails.this);

        // todo : get data from database
        setData();

        mSaveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PastTripsDetails.this, PastTripsActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void setData() {
        Intent intent = getIntent();
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(PastTripsDetails.this);
        ArrayList arrayList = databaseAdapter.getAllDataFromTrip(intent.getStringExtra("tripName"));
        mTripName.setText(arrayList.get(0).toString());
        mTripStartPoint.setText(arrayList.get(1).toString());
        mTripEndPoint.setText(arrayList.get(2).toString());
        mTripNotes.setText(arrayList.get(3).toString());
        if (arrayList.get(4).toString().equals("one way")) {
            mOneWayTrip.setChecked(true);
        } else {
            mRoundedTrip.setChecked(true);
        }
        String mAlarmDate = arrayList.get(5).toString();
        String mAlarmTime = arrayList.get(6).toString();
        mDateAndTimeValue.setText(mAlarmDate + "  " + mAlarmTime);
        mTripStatue.setText(arrayList.get(7).toString());
    }
}

