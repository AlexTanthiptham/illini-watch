package com.example.illinoiswatch;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class AlertsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private int radius; // The radius value from MapsActivity
    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private BroadcastReceiver alertReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Retrieve the radius value from the intent
        Intent intent = getIntent();
        radius = intent.getIntExtra("radius", 100); // Default value if not set

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map); // Ensure this ID matches with your map fragment's ID in activity_alerts.xml
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        String addressString = "South 3rd Street & East Green Street, Champaign";
        Geocoder geocoder = new Geocoder(this);

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);



        try {
            List<Address> addresses = geocoder.getFromLocationName(addressString, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng shotsFiredLocation= new LatLng(address.getLatitude(), address.getLongitude());

                // Add a marker and move the camera
                mMap.addMarker(new MarkerOptions().position(shotsFiredLocation).title("Shots Fired"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Set a click listener for the "Shots Fired" marker
        mMap.setOnMarkerClickListener(marker -> {
            if (marker.getTitle().equals("Shots Fired")) {
                Toast.makeText(AlertsActivity.this, "shots fired. Leave area if safe to do so. Otherwise, secure your location. ", Toast.LENGTH_LONG).show();
                return true;
            }
            return false;
        });

        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);
        } else {
            // Permission already granted
            mMap.setMyLocationEnabled(true);
            getUserLocationAndDrawCircle();
        }
    }


    private void getUserLocationAndDrawCircle() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        // Check if location is not null
                        if (location != null) {
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                            // Move the camera to the user's location
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

                            // Draw a circle around the user's location
                            CircleOptions circleOptions = new CircleOptions()
                                    .center(userLocation) // Center at user's location
                                    .radius(radius) // 'radius' from the intent
                                    .fillColor(0x30ff0000) // Adjust the color and transparency as needed
                                    .strokeColor(Color.RED)
                                    .strokeWidth(2);

                            mMap.addCircle(circleOptions);
                        } else {
                            // Handle the situation when location is null
                            Toast.makeText(AlertsActivity.this, "Location not found", Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);
        }
    }



    private void drawCircleOnMap(LatLng location) {
        CircleOptions circleOptions = new CircleOptions()
                .center(location)
                .radius(radius) // 'radius' from the intent
                .fillColor(0x30ff0000) // Adjust the color and transparency as needed
                .strokeColor(Color.RED)
                .strokeWidth(2);

        mMap.addCircle(circleOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    getUserLocationAndDrawCircle();
                }
            } else {
                // Permission denied
                // Show an explanation to the user
            }
        }
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
