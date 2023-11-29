package com.example.illinoiswatch;

import androidx.fragment.app.FragmentActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.location.Location;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;
import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.example.illinoiswatch.databinding.ActivityMapsBinding;
import android.util.Log;
import android.widget.SeekBar;
import com.google.android.gms.maps.model.Circle;

import android.widget.TextView;
import android.content.SharedPreferences;
import android.widget.Button;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private Circle mapCircle;  // Circle object to update its radius
    private SeekBar radiusSeekBar;  // SeekBar for adjusting the radius
    private TextView radiusTextView; // TextView for displaying the radius

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        radiusTextView = findViewById(R.id.radiusTextView);
        // Initialize SeekBar and set up listener
        radiusSeekBar = findViewById(R.id.radiusSeekBar);

        // Initialize and handle the Save button
        Button btnSaveRadius = findViewById(R.id.btnSaveRadius);
        btnSaveRadius.setOnClickListener(v -> {
            int radiusInMeters = radiusSeekBar.getProgress(); // Radius in meters
            SharedPreferences sharedPref = getSharedPreferences("AppSettings", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("radius", radiusInMeters);
            editor.apply();

            finish(); // Close this activity and return to the previous one
        });

        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mapCircle != null) {
                    mapCircle.setRadius(progress);
                }

                double radiusInMiles = progress * 0.000621371; // Convert meters to miles for display
                radiusTextView.setText("Radius: " + String.format("%.2f miles", radiusInMiles));

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: Add code for when the user starts dragging the slider
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional: Add code for when the user stops dragging the slider
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check for location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permissions are granted, initialize the map's location layer
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);

            getUserLocationAndDrawCircle();
        } else {
            // Request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void getUserLocationAndDrawCircle() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        // Got last known location. In some rare situations, this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            drawCircleOnMap(userLocation);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15)); // Move camera to the user's location
                        } else {
                            // Handle the situation when location is null (e.g., show a message to the user)
                        }
                    });
        }
    }

    private void drawCircleOnMap(LatLng location) {
        if (mMap == null) {
            Log.e("MapsActivity", "GoogleMap object is null");
            return;
        }

        int initialRadius = radiusSeekBar.getProgress(); // Get the initial radius from SeekBar

        CircleOptions circleOptions = new CircleOptions()
                .center(location)
                .radius(initialRadius)
                .fillColor(0x30ff0000)
                .strokeColor(Color.RED)
                .strokeWidth(2);

        mapCircle = mMap.addCircle(circleOptions);  // Assign to mapCircle
    }


    @Override
    public boolean onMyLocationButtonClick() {
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Call the superclass method

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    mMap.setOnMyLocationButtonClickListener(this);
                    mMap.setOnMyLocationClickListener(this);

                    getUserLocationAndDrawCircle();
                }
            } else {
                // Permission was denied. You can add a Snackbar or a toast to inform the user.
            }
        }
    }
}