package com.example.illinoiswatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;

public class SettingsFragment extends Fragment {

    private ToggleButton toggleButton;
    private EditText editTextNumber;
    private EditText editTextAddress;
    private TextView textViewSafetyRadius; // Make sure this is declared inside the class


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        toggleButton = view.findViewById(R.id.toggleButton);
        editTextNumber = view.findViewById(R.id.editTextNumber);
        editTextAddress = view.findViewById(R.id.editTextText);
        textViewSafetyRadius = view.findViewById(R.id.textViewSafetyRadius); // Correctly placed

        // Handling button clicks
        Button buttonSettings1 = view.findViewById(R.id.btnSettings1);
        buttonSettings1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            startActivity(intent);
        });


        if (getActivity() != null) {
            SharedPreferences sharedPref = getActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
            int radiusInMeters = sharedPref.getInt("radius", 100); // Default value
            double radiusInMiles = radiusInMeters * 0.000621371; // Convert meters to miles
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putFloat("radiusMiles", (float) radiusInMiles);
            editor.apply();        }


        Button buttonSave = view.findViewById(R.id.btnSave);
        buttonSave.setOnClickListener(v -> {
            if (getActivity() != null) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                // Handle radius
                try {
                    int radiusInMeters = Integer.parseInt(editTextNumber.getText().toString()); // Replace with your actual radius input field
                    double radiusInMiles = radiusInMeters * 0.000621371;
                    editor.putInt("radius", radiusInMeters);
                    editor.putFloat("radiusMiles", (float) radiusInMiles);
                } catch (NumberFormatException e) {
                    // Handle invalid radius input
                }

                editor.apply();
            }        });



        updateRadiusDisplay();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRadiusDisplay();
    }

    private void updateRadiusDisplay() {
        if (getActivity() != null) {
            SharedPreferences sharedPref = getActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
            int radiusInMeters = sharedPref.getInt("radius", 100); // Default value in meters
            double radiusInMiles = radiusInMeters * 0.000621371; // Convert meters to miles for display
            textViewSafetyRadius.setText(String.format("Safety Radius: %.2f miles", radiusInMiles));

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putFloat("radiusMiles", (float) radiusInMiles);
            editor.apply();
        }
    }






}
