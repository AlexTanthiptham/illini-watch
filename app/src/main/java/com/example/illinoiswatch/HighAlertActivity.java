package com.example.illinoiswatch;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class HighAlertActivity extends AppCompatActivity {
    private Button NavButton;
    private Button Call_911;
    private Button call_emergency;
    private TextView time;
    private TextView AlertTitle;

    private BroadcastReceiver alertReceiver;
    private TextView alert_description;

    private TextView distance_text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_view); // Replace with your actual layout file name

        SharedPreferences sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        float radiusInMiles = sharedPref.getFloat("radiusMiles", 0.0f); // Default value if not set


        NavButton = findViewById(R.id.read_more_button);
        AlertTitle = findViewById(R.id.notification_title);
        distance_text = findViewById(R.id.distance_text);
        alert_description = findViewById(R.id.Description);
        call_emergency = findViewById(R.id.call_emergency_contact_button);
        Call_911 = findViewById(R.id.call_911_button);
        time = findViewById(R.id.timeText);


        
        alertReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "test from activity" );

                if ("com.example.ACTION_SMS_ALERT".equals(intent.getAction())) {
                    AlertDetails details = (AlertDetails) intent.getSerializableExtra("alertDetails");
                    if (details != null) {
                        AlertTitle.setText(details.getEventType());
                        alert_description.setText(details.getMessage());
                        time.setText(details.getAlerttime());


                    }

                }
            }

        };

        // set the distance away from the alert
        distance_text.setText("0.2 mile away from the alert");


        ProgressBar distanceProgressBar = findViewById(R.id.distance_progress_bar);
        int display = (int) (radiusInMiles * 10);
        distanceProgressBar.setMax(display); // Assuming the max distance is 1 mile, set max to 100 (as percentage)
        distanceProgressBar.setProgress(3);
        NavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), navbar.class);
                startActivity(intent);
            }
        });






        call_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:7323408087")); // Replace with your emergency contact number
                startActivity(intent);
            }
        });

        Call_911.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:911"));
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