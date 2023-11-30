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


public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private LatLng currentUserLocation; // To store the current user location

    public MapsFragment() {
        // Required empty public constructor
    }

    // Removed newInstance method since we are fetching radius directly from SharedPreferences

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
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

        addShotsFiredMarker();
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


    public void onResume() {
        super.onResume();
        if (mMap != null && getContext() != null) {
            SharedPreferences sharedPref = getActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
            int radius = sharedPref.getInt("radius", 100); // Default value if not set
            if (currentUserLocation != null) {
                drawCircleOnMap(currentUserLocation, radius);
            }
        }
    }
    private void drawCircleOnMap(LatLng location, int radius) {
        if (mMap != null) {
            mMap.clear(); // Clear existing map overlays

            CircleOptions circleOptions = new CircleOptions()
                    .center(location)
                    .radius(radius) // Use the updated radius
                    .fillColor(0x30ff0000)
                    .strokeColor(Color.RED)
                    .strokeWidth(2);

            mMap.addCircle(circleOptions);

            addShotsFiredMarker();
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
    private void addShotsFiredMarker() {
        // Define the method logic here
        String addressString = "South 3rd Street & East Green Street, Champaign";
        Geocoder geocoder = new Geocoder(getActivity());

        try {
            List<Address> addresses = geocoder.getFromLocationName(addressString, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng shotsFiredLocation = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(shotsFiredLocation).title("Shots Fired"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shotsFiredLocation, 15));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
