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

public class AddTripActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddTripActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private EditText mTripName, mTripStartPoint, mTripEndPoint, mTripNotes;
    private RadioButton mOneWayTrip, mRoundedTrip;
    private ImageView mAlarm, mCalendar;
    private AlarmManager alarmManager;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TimePickerDialog mTimePicker;
    private Button mSaveTrip;
    private int mHours, mMinutes, mDayOrNight, mYear, mMonth, mDay;
    private final int REQUEST_CODE_AUTOCOMPLETE = 1023;
    private  int mAlarmRequestCode = 0;
    private final String TOKEN_ID = "pk.eyJ1IjoiYWJkZWxyaG1hbjIiLCJhIjoiY2pzYWdpMWduMDF3OTN6cnAwbjI2aTRuZyJ9.3vox5ROe8b2k7_OSItrDpw";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        mTripName = findViewById(R.id.edt_add_trip_trip_name);
        mTripStartPoint = findViewById(R.id.edt_add_trip_start_point);
        mTripEndPoint = findViewById(R.id.edt_add_trip_end_point);
        mTripNotes = findViewById(R.id.edt_add_trip_trip_notes);
        mOneWayTrip = findViewById(R.id.rd_add_trip_one_way_trip);
        mRoundedTrip = findViewById(R.id.rd_add_trip_round_trip);
        mAlarm = findViewById(R.id.img_add_trip_alarm);
        mCalendar = findViewById(R.id.img_add_trip_calendar);
        mSaveTrip = findViewById(R.id.btn_add_trip_save_trip);

        mOneWayTrip.setChecked(true);

        mRoundedTrip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(AddTripActivity.this, RoundedTripActivity.class);
                    startActivity(intent);

                }
            }
        });
        mSaveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationOfTripInformation()) {
                    if (!checkIfTripNameIsAlreadyExists()) {
                        setAlarm();
                        insertTripToDataBase();
                    } else {
                        mTripName.setError("Name of the trip is already exists");
                    }

                }
            }
        });
        mCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTripActivity.this,
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
        final Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(TOKEN_ID)
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .build(PlaceOptions.MODE_CARDS))
                .build(AddTripActivity.this);
        mTripStartPoint.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    startActivityForResult(intent, 1111);
            }
        });
        mTripEndPoint.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    startActivityForResult(intent, 2222);
            }
        });
        mAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                mTimePicker = new TimePickerDialog(AddTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                        Toast.makeText(AddTripActivity.this, String.valueOf(mHours + "  " + mMinutes), Toast.LENGTH_SHORT).show();


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
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), mAlarmRequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    private boolean checkIfTripNameIsAlreadyExists() {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(AddTripActivity.this);
        return databaseAdapter.nameOfTripAlreadyExists(mTripName.getText().toString());
    }

    private void insertTripToDataBase() {
        String tripName = mTripName.getText().toString();
        String tripStartPoint = mTripStartPoint.getText().toString();
        String tripEndPoint = mTripEndPoint.getText().toString();
        String tripNotes;
        if (mTripNotes.getText().toString().isEmpty()) {
            tripNotes = "No Notes";
        } else {
            tripNotes = mTripNotes.getText().toString();
        }
        String tripType;
        if (mRoundedTrip.isChecked()) {
            tripType = "rounded";
        } else {
            tripType = "one way";
        }
        String tripStatues = "Incoming";
        String tripTime = String.valueOf(mHours + " : " + mMinutes + " " + mDayOrNight);
        String tripDate = String.valueOf(mDay + " / " + mMonth + " / " + mYear);
        String tripAlarmRequestCode = String.valueOf(mAlarmRequestCode);
        // todo insert to db
    }

    private void setmAlarmRequestCode(){
        //todo: select from data base and assign to alarm request code
        mAlarmRequestCode=0;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            if (requestCode == 1111) {
                mTripStartPoint.setText(feature.text());
            } else if (requestCode == 2222) {
                mTripEndPoint.setText(feature.text());
            }
        }
    }
}
