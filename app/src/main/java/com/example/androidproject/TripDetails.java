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
import android.widget.CheckBox;
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
import java.util.StringTokenizer;

public class TripDetails extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TripDetails.this, MainActivity.class);
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
    private TextView mDateAndTime, mDateAndTimeValue, mTripStatue;
    private int mHours, mMinutes, mDayOrNight, mYear, mMonth, mDay;
    private final String TOKEN_ID = "pk.eyJ1IjoiYWJkZWxyaG1hbjIiLCJhIjoiY2pzYWdpMWduMDF3OTN6cnAwbjI2aTRuZyJ9.3vox5ROe8b2k7_OSItrDpw";
    private int mAlarmRequestCode = 0;
    private DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);


        mTripName = findViewById(R.id.edt_trip_details_trip_name);
        mTripStartPoint = findViewById(R.id.edt_trip_details_start_point);
        mTripEndPoint = findViewById(R.id.edt_trip_details_end_point);
        mTripNotes = findViewById(R.id.edt_trip_details_trip_notes);
        mOneWayTrip = findViewById(R.id.rd_trip_details_one_way_trip);
        mRoundedTrip = findViewById(R.id.rd_trip_details_round_trip);
        mAlarm = findViewById(R.id.img_trip_details_alarm);
        mCalendar = findViewById(R.id.img_trip_details_calendar);
        mSaveTrip = findViewById(R.id.btn_trip_details_save_trip);
        mDateAndTime = findViewById(R.id.txt_trip_details_trip_time_and_date);
        mDateAndTimeValue = findViewById(R.id.txt_trip_details_trip_time_and_date_value);
        mTripStatue = findViewById(R.id.txt_trip_details_trip_statue);

        databaseAdapter = new DatabaseAdapter(TripDetails.this);

        // todo : get data from database
        setData();

        mRoundedTrip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(TripDetails.this, RoundedTripActivity.class);
                    if (!mTripStartPoint.getText().toString().isEmpty())
                        intent.putExtra("tripStartPoint", mTripStartPoint.getText().toString());
                    if (!mTripEndPoint.getText().toString().isEmpty())
                        intent.putExtra("tripEndPoint", mTripEndPoint.getText().toString());
                    startActivity(intent);

                }
            }
        });
        mSaveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSaveTrip.getText().toString().toLowerCase().equals("ok")) {
                    Intent intent = new Intent(TripDetails.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (mSaveTrip.getText().toString().toLowerCase().equals("save")) {
                    if (validationOfTripInformation()) {
                        setAlarm();
                        insertTripToDataBase();
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(TripDetails.this,
                        onDateSetListener,
                        year, month, day);
                datePickerDialog.show();
                onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                        Toast.makeText(TripDetails.this, mYear + "  " + (mMonth + 1) + " " + mDay, Toast.LENGTH_SHORT).show();
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
                .build(TripDetails.this);
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
                mTimePicker = new TimePickerDialog(TripDetails.this, new TimePickerDialog.OnTimeSetListener() {
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
                        Toast.makeText(TripDetails.this, String.valueOf(mHours + "  " + mMinutes), Toast.LENGTH_SHORT).show();


                    }
                }, 0, 0, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                mTimePicker.setCancelable(false);
            }
        });
    }

    private void setData() {
        Intent intent = getIntent();
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(TripDetails.this);
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
        String[] stringTime = mAlarmTime.split(" : ");
        mHours = Integer.parseInt(stringTime[0]);
        mMinutes = Integer.parseInt(stringTime[1]);
        if (stringTime[2].equals("AM"))
            mDayOrNight = 0;
        else
            mDayOrNight = 1;
        String[] stringDate = mAlarmDate.split(" / ");
        mYear = Integer.parseInt(stringDate[2]);
        mMonth = Integer.parseInt(stringDate[1]);
        mDay = Integer.parseInt(stringDate[0]);
        mDateAndTimeValue.setText(mAlarmDate + "  " + mAlarmTime);
        mTripStatue.setText(arrayList.get(7).toString());
        getAlarmRequestCode();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_item_trip_details_delete_trip) {
            createAlertDialog("Do you want to delete this trip ?", "Trip Is deleted Successfully", "Deleted");
        } else if (item.getItemId() == R.id.menu_item_trip_details_cancel_trip) {
            createAlertDialog("Do you want to cancel this trip ?", "Trip Is Canceled Successfully", "Canceled");
        } else if (item.getItemId() == R.id.menu_item_trip_details_done_trip) {
            createAlertDialog("Do you want to mark this trip  as Finished?", "Trip Is Finished Successfully", "Done");
        } else if (item.getItemId() == R.id.menu_item_trip_details_edit_trip) {
            enableAllViews();
        }

        return super.onOptionsItemSelected(item);
    }

    private void createAlertDialog(String msg, final String toastMessage, final String newStatue) {
        AlertDialog.Builder a_builder = new AlertDialog.Builder(this);
        a_builder.setMessage(msg).setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean changed = changeTripStatue(newStatue);
                        if (changed) {
                            Toast.makeText(TripDetails.this, toastMessage, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TripDetails.this, MainActivity.class);
                            startActivity(intent);
                            dialogInterface.cancel();
                            finish();
                        }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Alert!!");
        alert.show();
    }

    private boolean changeTripStatue(String statue) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);
        int numberOfRowsAffected = databaseAdapter.updateTripData(mTripName.getText().toString(), "status", statue);
        cancelAlarm();
        return numberOfRowsAffected > 0;
    }

    private void enableAllViews() {
        mRoundedTrip.setEnabled(true);
        mOneWayTrip.setEnabled(true);
        mDateAndTimeValue.setVisibility(View.GONE);
        mDateAndTime.setVisibility(View.GONE);
        mAlarm.setVisibility(View.VISIBLE);
        mCalendar.setVisibility(View.VISIBLE);
        mTripNotes.setEnabled(true);
        mSaveTrip.setText(getString(R.string.save));
    }


    private void getAlarmRequestCode() {
        mAlarmRequestCode = Integer.parseInt(databaseAdapter.getDataFromTrip("requestcode", mTripName.getText().toString()));
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
        calendar.set(Calendar.HOUR_OF_DAY, mHours);
        calendar.set(Calendar.AM_PM, mDayOrNight);
        calendar.set(Calendar.MINUTE, mMinutes);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("tripName", mTripName.getText().toString());
        intent.putExtra("name", "Alarm");
        getAlarmRequestCode();
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), mAlarmRequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    private void cancelAlarm() {
        AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(TripDetails.this, mAlarmRequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        assert aManager != null;
        aManager.cancel(pIntent);
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
        String mAM_PM = "AM";
        if (mDayOrNight == 1)
            mAM_PM = "PM";
        String tripTime = String.valueOf(mHours + " : " + mMinutes + " " + mAM_PM);
        String tripDate = String.valueOf(mDay + " / " + mMonth + " / " + mYear);
        String tripAlarmRequestCode = String.valueOf(mAlarmRequestCode);

        long result = databaseAdapter.insertInitialTripData(tripName, tripStartPoint, tripEndPoint, tripNotes, tripType, tripDate, tripTime, tripStatues, tripAlarmRequestCode);
        if (result != -1) {
            Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
        }
    }

}
