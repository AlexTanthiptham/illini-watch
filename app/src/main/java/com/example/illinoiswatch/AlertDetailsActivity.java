package com.example.illinoiswatch;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AlertDetailsActivity extends AppCompatActivity {

    private TextView notification_main_text;
    private TextView alertTypeTextView;
    private TextView alertTimeTextView;
    private TextView alertDetailTextView;
    private Button mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify); // Replace with your actual layout file name

        notification_main_text = findViewById(R.id.notification_main_text);

        mapButton = findViewById(R.id.navigate_me_button);

        // Set the seek bar to a fixed value, e.g. 2.17 miles
        // Convert miles to progress as per your seek bar max value
        ProgressBar distanceProgressBar = findViewById(R.id.distance_progress_bar);
        distanceProgressBar.setMax(100); // Assuming the max distance is 1 mile, set max to 100 (as percentage)
        distanceProgressBar.setProgress(20); // Set progress to 20 for 0.2 miles

        // Set up a click listener for the button to open a map view
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to open a map view
            }
        });

        // Populate the views with data from the intent or a data model
        // You can retrieve the notification data from the intent that started this activity
    }
}
