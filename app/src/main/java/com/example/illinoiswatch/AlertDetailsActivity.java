package com.example.illinoiswatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AlertDetailsActivity extends AppCompatActivity {

    private TextView notification_main_text;

    private Button NavButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify); // Replace with your actual layout file name

        notification_main_text = findViewById(R.id.notification_main_text);

        NavButton = findViewById(R.id.read_more_button);

        // Set the seek bar to a fixed value, e.g. 2.17 miles
        // Convert miles to progress as per your seek bar max value
        ProgressBar distanceProgressBar = findViewById(R.id.distance_progress_bar);
        distanceProgressBar.setMax(100); // Assuming the max distance is 1 mile, set max to 100 (as percentage)
        distanceProgressBar.setProgress(20); // Set progress to 20 for 0.2 miles
        // Set up a click listener for the button to open a map view
        NavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlertDetailsActivity.this, navbar.class);
                startActivity(intent);
            }
        });

    }
}
