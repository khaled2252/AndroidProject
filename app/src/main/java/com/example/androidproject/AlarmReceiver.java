package com.example.androidproject;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent i) {
        if (i.getStringExtra("name").equals("Alarm")) {
            Intent intent = new Intent(context, ViewDialog.class);
            context.startActivity(intent);
        } else if (i.getStringExtra("name").equals("Snooze")) {

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, ViewDialog.class), 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Your Trip")
                    .setContentText("Time To start your trip");
            builder.setContentIntent(pendingIntent);
           // builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
            builder.setAutoCancel(true);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1);
            notificationManager.notify(1, builder.build());

        }

    }

}