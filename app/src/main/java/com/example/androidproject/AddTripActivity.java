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

public class AddTripActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

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
    private int mAlarmRequestCode = 0;
    private final String TOKEN_ID = "pk.eyJ1IjoiYWJkZWxyaG1hbjIiLCJhIjoiY2pzYWdpMWduMDF3OTN6cnAwbjI2aTRuZyJ9.3vox5ROe8b2k7_OSItrDpw";
    private DatePickerDialog datePickerDialog;
    private Intent intent;
    private String tripTime;
    private String tripDate;

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

        initializePlaceCompleteIntent();


        mRoundedTrip.setOnCheckedChangeListener(this);
        mAlarm.setOnClickListener(this);
        mCalendar.setOnClickListener(this);
        mTripStartPoint.setOnClickListener(this);
        mTripEndPoint.setOnClickListener(this);
        mSaveTrip.setOnClickListener(this);

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                Toast.makeText(AddTripActivity.this, mYear + "  " + (mMonth + 1) + " " + mDay, Toast.LENGTH_SHORT).show();
            }
        };

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
        } else if (mTripStartPoint.getText().toString().equals(mTripEndPoint.getText().toString())) {
            mTripEndPoint.setError("End Point can not be the same as Start Point");
            valid = false;
        } else if (checkIfTripDateAndTimeIsInPast()) {
            Toast.makeText(this, "You  a trip date is in the past", Toast.LENGTH_SHORT).show();
            valid = false;
        } else if (checkIfThereIsAnotherTripInTheSameTime()) {
            Toast.makeText(this, "You have a trip at the same date", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    private boolean checkIfThereIsAnotherTripInTheSameTime() {
        createTripDateAndTime();
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(AddTripActivity.this);
        String name = databaseAdapter.getDataFromTripByDateAndTime("tripname", tripDate, tripTime);
        return !name.equals("No Data");
    }

    private boolean checkIfTripDateAndTimeIsInPast() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR);
        int minutes = now.get(Calendar.MINUTE);
        if (year > mYear) {
            return true;
        } else if (month > mMonth) {
            return true;
        } else if (day > mDay) {
            return true;
        } else if (hour > mHours) {
            return true;
        } else if (minutes > mMinutes) {
            return true;
        } else {
            return false;
        }

    }

    private void setAlarm() {
        AlarmManager alarmManager;
        PendingIntent pendingIntent;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        calendar.set(Calendar.HOUR_OF_DAY, mHours);
        calendar.set(Calendar.AM_PM, mDayOrNight);
        calendar.set(Calendar.MINUTE, mMinutes);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("tripName", mTripName.getText().toString());
        intent.putExtra("name", "Alarm");
        setAlarmRequestCode();
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
        String tripName = mTripName.getText().toString().toLowerCase();
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
        createTripDateAndTime();
        String tripAlarmRequestCode = String.valueOf(mAlarmRequestCode);
        // todo insert to db
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);
        long result = databaseAdapter.insertInitialTripData(tripName, tripStartPoint, tripEndPoint, tripNotes, tripType, tripDate, tripTime, tripStatues, tripAlarmRequestCode);
        if (result != -1) {
            Toast.makeText(this, "Save Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void createTripDateAndTime() {
        String mAM_PM = "AM";
        if (mDayOrNight == 1)
            mAM_PM = "PM";
        tripTime = String.valueOf(mHours + " : " + mMinutes + " : " + mAM_PM);
        tripDate = String.valueOf(mDay + " / " + mMonth + " / " + mYear);
    }

    private void setAlarmRequestCode() {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(AddTripActivity.this);
        int numberOfIncomingTrips = databaseAdapter.getAllTrips();
        mAlarmRequestCode = numberOfIncomingTrips + 1;
    }

    private void setHourAndMinutesOfAlarm() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mTimePicker = new TimePickerDialog(AddTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (selectedHour > 12) {
                    mHours = selectedHour - 12;
                    mDayOrNight = Calendar.PM;
                } else if (selectedHour == 12) {
                    mHours = selectedHour;
                    mDayOrNight = Calendar.PM;
                } else {
                    mHours = selectedHour;
                    mDayOrNight = Calendar.AM;
                }
                mMinutes = selectedMinute;
                Toast.makeText(AddTripActivity.this, String.valueOf(mHours + " : " + mMinutes), Toast.LENGTH_SHORT).show();


            }
        }, 0, 0, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
        mTimePicker.setCancelable(false);
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == mRoundedTrip) {
            if (isChecked) {
                startRoundedTripActivity();
            }
        }
    }

    private void initializePlaceCompleteIntent() {
        intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(TOKEN_ID)
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .build(PlaceOptions.MODE_CARDS))
                .build(AddTripActivity.this);

    }

    private void saveTrip() {
        if (validationOfTripInformation()) {
            if (!checkIfTripNameIsAlreadyExists()) {
                setAlarm();
                insertTripToDataBase();
                Intent intent = new Intent(AddTripActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                mTripName.setError("Name of the trip is already exists");
            }

        }
    }

    private void startRoundedTripActivity() {
        Intent intent = new Intent(AddTripActivity.this, RoundedTripActivity.class);
        if (!mTripStartPoint.getText().toString().isEmpty())
            intent.putExtra("tripStartPoint", mTripStartPoint.getText().toString());
        if (!mTripEndPoint.getText().toString().isEmpty())
            intent.putExtra("tripEndPoint", mTripEndPoint.getText().toString());
        startActivity(intent);
    }

    private void setCalender() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(AddTripActivity.this,
                onDateSetListener,
                year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == mSaveTrip) {
            saveTrip();
        } else if (v == mCalendar) {
            setCalender();
        } else if (v == mTripStartPoint) {
            startActivityForResult(intent, 1111);
        } else if (v == mTripEndPoint) {
            startActivityForResult(intent, 2222);
        } else if (v == mAlarm) {
            setHourAndMinutesOfAlarm();
        }
    }
}
