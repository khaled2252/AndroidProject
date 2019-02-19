package com.example.androidproject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import java.util.Calendar;

public class RoundedTripActivity extends AppCompatActivity {


    private EditText mTripName, mTripStartPoint, mTripEndPoint, mTripNotes;
    private ImageView mAlarm, mCalendar;
    private AlarmManager alarmManager;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TimePickerDialog mTimePicker;
    private Button mSaveTrip, mCancelTrip;
    private int mHours, mMinutes, mDayOrNight, mYear, mMonth, mDay;
    private final int REQUEST_CODE_AUTOCOMPLETE = 1023;
    private final String TOKEN_ID = "pk.eyJ1IjoiYWJkZWxyaG1hbjIiLCJhIjoiY2pzYWdpMWduMDF3OTN6cnAwbjI2aTRuZyJ9.3vox5ROe8b2k7_OSItrDpw";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounded_trip);

        mTripName = findViewById(R.id.edt_add_rounded_trip_trip_name);
        mTripStartPoint = findViewById(R.id.edt_add_rounded_trip_start_point);
        mTripEndPoint = findViewById(R.id.edt_add_rounded_trip_end_point);
        mTripNotes = findViewById(R.id.edt_add_rounded_trip_trip_notes);
        mAlarm = findViewById(R.id.img_add_rounded_trip_alarm);
        mCalendar = findViewById(R.id.img_add_rounded_trip_calendar);
        mSaveTrip = findViewById(R.id.btn_add_rounded_trip_save_trip);
        mCancelTrip = findViewById(R.id.btn_add_rounded_trip_cancel_trip);

        mSaveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationOfTripInformation()) {
                    setAlarm();
                }
            }
        });
        mCancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(RoundedTripActivity.this,
                        onDateSetListener,
                        year, month, day);
                datePickerDialog.show();
                onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                    }
                };
            }
        });
        mTripStartPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(TOKEN_ID)
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(RoundedTripActivity.this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE + 1);
            }
        });
        mTripEndPoint.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(TOKEN_ID)
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(RoundedTripActivity.this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE + 2);
            }
        });
        mAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                mTimePicker = new TimePickerDialog(RoundedTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour > 12) {
                            mHours = selectedHour - 12;
                            mDayOrNight = Calendar.PM;
                        } else {
                            mHours = selectedHour;
                            mDayOrNight = Calendar.AM;
                        }
                        mMinutes = selectedMinute;
                        Toast.makeText(RoundedTripActivity.this, String.valueOf(mHours + "  " + mMinutes), Toast.LENGTH_SHORT).show();


                    }
                }, 0, 0, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                mTimePicker.setCancelable(false);
            }
        });

    }

    private boolean validationOfTripInformation() {
        // check that all information are valid
        boolean valid = true;
        if (mTripName.getText().toString().equals("")) {
            mTripName.setError("please enter your trip name");
            valid = false;
        } else if (mTripStartPoint.getText().toString().equals("")) {
            mTripStartPoint.setError("please Choose your Start Point name");
            valid = false;
        } else if (mTripEndPoint.getText().toString().equals("")) {
            mTripEndPoint.setError("please Choose your End Point name");
            valid = false;
        } else if (alarmManager == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Time To Start")
                    .setMessage("Please select time to start your Trip");
            builder.show();
            valid = false;
        } else if (mYear == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Date To Start")
                    .setMessage("Please select Date to start your Trip");
            builder.show();
            valid = false;
        }
        return valid;
    }

    private void setAlarm() {
        AlarmManager alarmManager;
        PendingIntent pendingIntent;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        calendar.set(Calendar.HOUR_OF_DAY, mHours, mDayOrNight);
        calendar.set(Calendar.MINUTE, mMinutes);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("name", "Alarm");
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE + 1) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            mTripStartPoint.setText(feature.text());
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE + 2) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            mTripEndPoint.setText(feature.text());
        }
    }
}