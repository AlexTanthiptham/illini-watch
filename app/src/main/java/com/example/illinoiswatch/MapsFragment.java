package com.example.illinoiswatch;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import android.graphics.Color;
import android.util.Pair; // Import Pair class
import java.util.ArrayList; // Add this import for ArrayList
import java.util.List; // Import List if it's not already imported
import android.util.Pair; // Import Pair class
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;


public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private LatLng currentUserLocation;
    private List<Pair<String, String>> addresses; // Declare addresses here

    public MapsFragment() {
        // Required empty public constructor
    }

    // Removed newInstance method since we are fetching radius directly from SharedPreferences

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        addresses = new ArrayList<>(); // Initialize addresses here
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Check for location permission
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getUserLocationAndDrawCircle();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
        }

        addresses.clear(); // Clear previous data
        addresses.add(new Pair<>("South 3rd Street & East Green Street, Champaign", "Shots Fired"));
        addresses.add(new Pair<>("408 E Green St, Champaign, IL 61820", "Robbery"));
        addresses.add(new Pair<>("1409 W Green St, Urbana, IL 61801", "Goose Attack"));
        addresses.add(new Pair<>("505 S Mathews Ave, Urbana, IL 61801", "Chemical Spill"));
        addresses.add(new Pair<>("611 W Park St, Urbana, IL 61801", "Power Outage"));
        addresses.add(new Pair<>("201 N Goodwin Ave, Urbana, IL 61801", "Banana Peel"));

        addMultipleMarkers(addresses);
    }

    private void getUserLocationAndDrawCircle() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            currentUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            SharedPreferences sharedPref = getActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
                            int radius = sharedPref.getInt("radius", 100); // Default value if not set
                            drawCircleOnMap(currentUserLocation, radius);
                        } else {
                            Toast.makeText(getActivity(), "Location not found", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mMap != null && getContext() != null) {
            SharedPreferences sharedPref = getActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
            int radius = sharedPref.getInt("radius", 100); // Default value if not set
            if (currentUserLocation != null) {
                drawCircleOnMap(currentUserLocation, radius);
            }
            addMultipleMarkers(addresses); // Now 'addresses' is accessible here
        }
    }
    private void drawCircleOnMap(LatLng location, int radius) {
        if (mMap != null) {
            mMap.clear(); // Clear existing map overlays
            addMultipleMarkers(addresses); // Now 'addresses' is accessible here

            CircleOptions circleOptions = new CircleOptions()
                    .center(location)
                    .radius(radius) // Use the updated radius
                    .fillColor(0x30ff0000)
                    .strokeColor(Color.RED)
                    .strokeWidth(2);

            mMap.addCircle(circleOptions);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    getUserLocationAndDrawCircle();
                }
            } else {
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void addMultipleMarkers(List<Pair<String, String>> addresses) {
        Geocoder geocoder = new Geocoder(getActivity());
        for (Pair<String, String> addressTitlePair : addresses) {
            String addressString = addressTitlePair.first;
            String title = addressTitlePair.second;

            try {
                List<Address> addressesList = geocoder.getFromLocationName(addressString, 1);
                if (addressesList != null && !addressesList.isEmpty()) {
                    Address address = addressesList.get(0);
                    LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions().position(location).title(title);

                    // Check for specific title to change color
                    if ("Shots Fired".equals(title)) {
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    } else {
                    // Blue color for other markers
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                }


                mMap.addMarker(markerOptions);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!addresses.isEmpty()) {
            try {
                Address firstAddress = geocoder.getFromLocationName(addresses.get(0).first, 1).get(0);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(firstAddress.getLatitude(), firstAddress.getLongitude()), 15));
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception or notify the user
            }
        }
    }

}
