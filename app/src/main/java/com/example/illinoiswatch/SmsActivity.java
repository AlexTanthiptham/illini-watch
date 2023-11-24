package com.example.illinoiswatch;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SmsActivity extends AppCompatActivity {
    private BroadcastReceiver alertReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navpage_view);

        alertReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "test from activity" );

                if ("com.example.ACTION_SMS_ALERT".equals(intent.getAction())) {
                    AlertDetails details = (AlertDetails) intent.getSerializableExtra("alertDetails");
                    if (details != null) {
                        Log.d(TAG, "The alert event type " + details.getEventType());
                        Log.d(TAG,"The alert address "+ details.getAddress());
                        Log.d(TAG,"The message of the alert "+ details.getMessage());
                        Log.d(TAG,"The time of the alert"+ details.getAlerttime());

                    }

                }
            }

        };

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
