package com.example.illinoiswatch;

import static android.content.ContentValues.TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";


    @Override
    public void onReceive(Context context, Intent intent) {

        createNotificationChannel(context);


        if (SMS_RECEIVED.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        try {
                            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                            String messageBody = smsMessage.getMessageBody();

                            AlertDetails details = parseMessage(messageBody);


                            if (details != null) {
                                Intent localIntent = new Intent("com.example.ACTION_SMS_ALERT");
                                localIntent.putExtra("alertDetails", details); // Ensure AlertDetails is Serializable or Parcelable
                                LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);

                                Log.d(TAG, details.getEventType());
                                Log.d(TAG, details.getAddress());
                                showNotification(context, details.toString());

                            }


                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
            }
        }
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "AlertsChannel";
            String description = "Channel for Alert Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("ALERTS_CHANNEL_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void showNotification(Context context, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ALERTS_CHANNEL_ID")
                .setSmallIcon(R.drawable.pigon) // Replace with your notification icon
                .setContentTitle("New Alert")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Intent to open the app when the notification is tapped
        Intent intent = new Intent(context, MainActivity.class); // Replace MainActivity with your activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build()); // Notification ID should be unique for each notification
    }


    private static String extractEventType(String eventDetails) {
        String[] parts = eventDetails.split(" at ");
        return parts[0].trim();
    }

    private static String extractAddress(String eventDetails) {
        String[] parts = eventDetails.split(" at ");
        // This check is necessary in case the event details do not contain the word 'at'
        return parts.length > 1 ? parts[1].trim() : "";
    }

    public AlertDetails parseMessage(String message) {

        // Initialize as null; will stay null if the message doesn't match the criteria
        AlertDetails alertDetails = null;

        //Log.d(TAG, message);

        // Check if the message starts with the specified prefix
        if (message.startsWith("Illini-Alert")) {
            // Split the SMS message into sentences
            String[] sentences = message.split("\\.\\s+");
            // Check if there are at least two sentences
            if (sentences.length >= 2) {
                // The second sentence should contain the type of event and address
                //System.out.println(sentences[0]);
                String eventDetails = sentences[1];
                //System.out.println(eventDetails);
                // Extract the event type and address
                String eventType = extractEventType(eventDetails);
                String address = extractAddress(eventDetails);
                // Create a new AlertDetails object with the extracted information
                alertDetails = new AlertDetails(eventType, address);
            }
        }

        // Return the alert details, which may be null if the message didn't start with the expected prefix
        return alertDetails;
    }


}

