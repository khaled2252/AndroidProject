package com.example.androidproject;

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

public class ViewDialog extends AppCompatActivity {
    private AlarmReceiver alarmReceiver;

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
        this.setFinishOnTouchOutside(false);// if user click out side the activity it will not be dismissed
        TextView text = findViewById(R.id.text_dialog);
        text.setText(R.string.dialog_title);

        Button dialogButton = findViewById(R.id.btn_dialog_start);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(ViewDialog.this)) {

                    //If the draw over permission is not available open the settings screen
                    //to grant the permission.
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 2002);
                } else {
                    startService(new Intent(ViewDialog.this, NotesHeadService.class));
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr=Alexandria&daddr=Cairo"));
                    startActivity(intent);
                    finish();

                }
            }
        });
        Button dialogButton2 = findViewById(R.id.btn_dialog_snooze);
        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmReceiver = new AlarmReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("com.example.Broadcast");
                registerReceiver(alarmReceiver, intentFilter);
                Intent broadCastIntent = new Intent();
                broadCastIntent.putExtra("name", "Snooze");
                broadCastIntent.setAction("com.example.Broadcast");
                broadCastIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(broadCastIntent);
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2002) {

            //Check if the permission is granted or not.
            // Settings activity never returns proper value so instead check with following method
            if (Settings.canDrawOverlays(this)) {
                startService(new Intent(ViewDialog.this, NotesHeadService.class));
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=Alexandria&daddr=Cairo"));
                startActivity(intent);
                finish();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}