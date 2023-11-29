package com.example.illinoiswatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

public class AlertsFragment extends Fragment {

    public AlertsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alerts, container, false);

        // Find the LinearLayout container
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);

        // Example: Add three AlertsBoxFragment instances to the LinearLayout
        for (int i = 1; i <= 3; i++) {
            FrameLayout notificationContainer = createNotificationContainer(inflater, container, i);
            linearLayout.addView(notificationContainer);
        }

        return view;
    }

    private FrameLayout createNotificationContainer(LayoutInflater inflater, ViewGroup container, int index) {
        // Create a new FrameLayout
        FrameLayout notificationContainer = new FrameLayout(requireContext());
        notificationContainer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        notificationContainer.setPadding(10, 10, 10, 10);
        notificationContainer.setTag("container" + index);

        // Add AlertsBoxFragment instance to the FrameLayout
        getChildFragmentManager().beginTransaction()
                .replace(notificationContainer.getId(), new AlertsBoxFragment())
                .commit();

        return notificationContainer;
    }
}
