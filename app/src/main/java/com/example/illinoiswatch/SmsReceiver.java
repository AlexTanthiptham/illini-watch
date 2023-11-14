package com.example.illinoiswatch;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";



    @Override
    public void onReceive(Context context, Intent intent) {


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
                                Log.d(TAG,details.getAddress());

                                //Intent localIntent = new Intent("com.example.ACTION_SMS_ALERT");
                                //localIntent.putExtra("alertDetails", (CharSequence) details); // Ensure AlertDetails is Serializable or Parcelable
                                //LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
                            }



                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
            }
        }
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

