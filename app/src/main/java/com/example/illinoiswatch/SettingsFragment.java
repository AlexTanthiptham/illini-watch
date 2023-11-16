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

public class SettingsFragment extends Fragment {

    private ToggleButton toggleButton;
    private EditText editTextNumber;
    private EditText editTextAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize views
        toggleButton = view.findViewById(R.id.toggleButton);
        editTextNumber = view.findViewById(R.id.editTextNumber);
        editTextAddress = view.findViewById(R.id.editTextText);

        // Example of handling button click
        Button buttonSettings1 = view.findViewById(R.id.btnSettings1);
        buttonSettings1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the button click for "Set Safety Radius"
                // Add your logic here
            }
        });

        // Example of handling toggle button state change
        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle the state change of the toggle button
            // isChecked will be true if the toggle is ON, false if OFF
            // Add your logic here
        });

        // Example of handling save button click
        Button buttonSave = view.findViewById(R.id.btnSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the button click for "Save"
                // Add your logic here
            }
        });

        return view;
    }
}
