package com.example.illinoiswatch;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class AlertsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private int radius; // The radius value from MapsActivity
    private static final int MY_LOCATION_REQUEST_CODE = 1;

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

        // Fetch coordinates for the default location
        fetchCoordinatesFromAddress("1406 W Green St, Urbana, IL 61801");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

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
                        // Got last known location
                        if (location != null) {
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            drawCircleOnMap(userLocation);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                        } else {
                            // Handle the situation when location is null
                        }
                    });
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

    private void fetchCoordinatesFromAddress(final String address) {
        new AsyncTask<Void, Void, LatLng>() {
            @Override
            protected LatLng doInBackground(Void... voids) {
                try {
                    String encodedAddress = URLEncoder.encode(address, "UTF-8");
                    String urlString = "https://maps.googleapis.com/maps/api/geocode/json?address=" + encodedAddress + "&key=MAPS_API_KEY";
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Close connections
                    reader.close();
                    connection.disconnect();

                    // Extracting latitude and longitude from the JSON response
                    JSONObject jsonResponse = new JSONObject(result.toString());
                    double lat = jsonResponse.getJSONArray("results")
                            .getJSONObject(0)
                            .getJSONObject("geometry")
                            .getJSONObject("location")
                            .getDouble("lat");

                    double lng = jsonResponse.getJSONArray("results")
                            .getJSONObject(0)
                            .getJSONObject("geometry")
                            .getJSONObject("location")
                            .getDouble("lng");

                    return new LatLng(lat, lng);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(LatLng latLng) {
                super.onPostExecute(latLng);
                if (latLng != null) {
                    // Use latLng for your map (e.g., setting markers or moving camera)
                }
            }
        }.execute();
    }
}
