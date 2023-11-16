package com.example.illinoiswatch;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class HighAlertActivity extends Activity {
    private static final String CHANNEL_ID = "high_alert_channel";
    private static final int NOTIFICATION_ID = 1;


    private BroadcastReceiver alertReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();


        alertReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "test from activity");

                if ("com.example.ACTION_SMS_ALERT".equals(intent.getAction())) {
                    AlertDetails details = (AlertDetails) intent.getSerializableExtra("alertDetails");
                    if (details != null) {
                        Log.d(TAG, "test from activity " + details.getEventType());
                        Log.d(TAG, "test from activity " + details.getAddress());
                        setContentView(R.layout.alert_view);
                        ProgressBar distanceProgressBar = findViewById(R.id.distance_progress_bar);
                        distanceProgressBar.setMax(100); // Assuming the max distance is 1 mile, set max to 100 (as percentage)
                        distanceProgressBar.setProgress(20); // Set progress to 20 for 0.2 miles
                        showHighAlertNotification(details.getEventType(), details.getAddress());

                    }

                }
            }
        };
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "High Alert Notifications";
            String description = "Notifications for high priority alerts";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void showHighAlertNotification(String title, String message) {


        // Create an explicit intent for HighAlertActivity
        Intent intent = new Intent(this, HighAlertActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.pigon) // Replace with your notification icon
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Log.d(TAG, "test in the alert ");

        // Issue the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }



    @Override
    protected void onResume() {
        super.onResume();
        // Register BroadcastReceiver
        IntentFilter filter = new IntentFilter("com.example.ACTION_SMS_ALERT");
        LocalBroadcastManager.getInstance(this).registerReceiver(alertReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister BroadcastReceiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(alertReceiver);
    }
}