package com.example.illinoiswatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

public class AlertsFragment extends Fragment {

    public AlertsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alerts, container, false);

        // Find FrameLayout containers
        FrameLayout notificationContainer1 = view.findViewById(R.id.notificationContainer1);
        FrameLayout notificationContainer2 = view.findViewById(R.id.notificationContainer2);

        // Add AlertsBoxFragment instances to FrameLayout containers
        getChildFragmentManager().beginTransaction()
                .replace(R.id.notificationContainer1, new AlertsBoxFragment())
                .replace(R.id.notificationContainer2, new AlertsBoxFragment())
                .commit();

        return view;
    }
}
