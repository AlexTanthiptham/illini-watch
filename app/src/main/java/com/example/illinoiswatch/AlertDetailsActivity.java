package com.example.illinoiswatch;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class AlertDetailsActivity extends AppCompatActivity {

    private Button NavButton;

    private TextView alert_description;
    private TextView time;
    private BroadcastReceiver alertReceiver;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify); // Replace with your actual layout file name

        alert_description = findViewById(R.id.Description);
        NavButton = findViewById(R.id.navigate_me_button);
        time = findViewById(R.id.timeText);

        // Set the seek bar to a fixed value, e.g. 2.17 miles
        // Convert miles to progress as per your seek bar max value
        ProgressBar distanceProgressBar = findViewById(R.id.distance_progress_bar);
        distanceProgressBar.setMax(100); // Assuming the max distance is 1 mile, set max to 100 (as percentage)
        distanceProgressBar.setProgress(20); // Set progress to 20 for 0.2 miles
        // Set up a click listener for the button to open a map view

        alertReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "test from activity" );

                if ("com.example.ACTION_SMS_ALERT".equals(intent.getAction())) {
                    AlertDetails details = (AlertDetails) intent.getSerializableExtra("alertDetails");
                    if (details != null) {
                        Log.d(TAG, "test from activity " + details.getEventType());
                        Log.d(TAG,"test from activity "+ details.getAddress());
                        alert_description.setText(details.getMessage());
                        time.setText(details.getAlerttime());


                    }

                }
            }

        };


        NavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlertDetailsActivity.this, navbar.class);
                startActivity(intent);
            }
        });

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
