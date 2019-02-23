package com.example.androidproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewDialog extends AppCompatActivity implements View.OnClickListener {
    private AlarmReceiver alarmReceiver;
    private String mTripName, mStartPoint, mEndPoint;
    private DatabaseAdapter databaseAdapter;
    private Button mStartTrip, mSnoozeTrip, mCancelTrip;

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please select one from previous options", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alarmReceiver != null)
            unregisterReceiver(alarmReceiver);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_activity);

        TextView text = findViewById(R.id.text_dialog);

        mStartTrip = findViewById(R.id.btn_dialog_start);
        mCancelTrip = findViewById(R.id.btn_dialog_cancel);
        mSnoozeTrip = findViewById(R.id.btn_dialog_snooze);

        // if user click out side the activity it will not be dismissed
        this.setFinishOnTouchOutside(false);


        getStartAndEndPointOfTrip();

        text.setText(getString(R.string.dialog_title).concat(" " + mEndPoint));
        mStartTrip.setOnClickListener(this);
        mSnoozeTrip.setOnClickListener(this);
        mCancelTrip.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2002) {
            if (Settings.canDrawOverlays(this)) {
                openGoogleMaps();
            } else {
                //Permission is not available
                Toast.makeText(this, "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getTripName() {
        //get trip name from intent coming from AlarmReceiver
        Intent intent = getIntent();
        mTripName = intent.getStringExtra("tripName");
    }

    private void getStartAndEndPointOfTrip() {
        getTripName();
        databaseAdapter = new DatabaseAdapter(ViewDialog.this);
        mStartPoint = databaseAdapter.getDataFromTrip("startname", mTripName);
        mEndPoint = databaseAdapter.getDataFromTrip("endname", mTripName);
    }

    private boolean changeTripTypeToCanceled() {
        int numberOfRowsAffected = databaseAdapter.updateTripData(mTripName, "status", "Canceled");
        return numberOfRowsAffected > 0;
    }

    private void checkIfPermissionIsGenerated() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(ViewDialog.this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 2002);
        } else {
            // if permission is generated
            openGoogleMaps();
        }
    }

    private void openGoogleMaps() {
        Intent intentService = new Intent(ViewDialog.this, NotesHeadService.class);
        intentService.putExtra("tripName", mTripName);
        startService(intentService);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + mStartPoint + "&daddr=" + mEndPoint));
        startActivity(intent);
        finish();
    }

    private void showCancelConformationDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewDialog.this)
                .setTitle("Cancel Trip")
                .setMessage("Are you sure You Want to cancel the trip")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean changed = changeTripTypeToCanceled();
                        if (changed)
                            finish();
                        else
                            Toast.makeText(ViewDialog.this, "Click Again , Please", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.show();
    }

    private void createSnoozeNotification() {
        alarmReceiver = new AlarmReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.Broadcast");
        registerReceiver(alarmReceiver, intentFilter);
        Intent broadCastIntent = new Intent();
        broadCastIntent.putExtra("name", "Snooze");
        broadCastIntent.putExtra("requestCode", "0");
        broadCastIntent.setAction("com.example.Broadcast");
        broadCastIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(broadCastIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == mStartTrip) {
            // if true , open google maps
            checkIfPermissionIsGenerated();
        } else if (v == mSnoozeTrip) {
            createSnoozeNotification();
        } else if (v == mCancelTrip) {
            showCancelConformationDialog();
        }

    }
}