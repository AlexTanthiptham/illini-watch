package com.example.illinoiswatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // Retrieve the SMS message received
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    System.out.println("test");
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        // Check if the sender is your service's number
                        // in this case it should be remove
                        String messageBody = smsMessage.getMessageBody();
                        System.out.println((messageBody));
                            // Parse the message body
                        parseMessage(messageBody);

                    }
                }
            }
        }
    }

    private boolean checkSender(String sender) {
        // Replace with your service's number or check logic
        return sender.equals("YOUR_SERVICE_NUMBER");
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

        // Check if the message starts with the specified prefix
        if (message.startsWith("Illini-Alert")) {
            // Split the SMS message into sentences
            String[] sentences = message.split("\\.\\s+");
            // Check if there are at least two sentences
            if (sentences.length >= 2) {
                // The second sentence should contain the type of event and address
                String eventDetails = sentences[1];
                System.out.println(eventDetails);
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

