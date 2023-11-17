package com.example.illinoiswatch;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the Start Map button by its ID and set a click listener
        Button startMapButton = findViewById(R.id.button_start_map);
        startMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start MapsActivity
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        // Find the Alerts button by its ID and set a click listener
        Button alertsButton = findViewById(R.id.button_alerts);
        alertsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start AlertsActivity
                Intent intent = new Intent(MainActivity.this, AlertsActivity.class);

                // Retrieve the radius value from SharedPreferences
                SharedPreferences sharedPref = getSharedPreferences("AppSettings", MODE_PRIVATE);
                int radius = sharedPref.getInt("radius", 100); // Default value if not set
                intent.putExtra("radius", radius);

                startActivity(intent);
            }
        });
    }
}