package com.example.illinoiswatch;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class AlertsBoxFragment extends Fragment {

    private TextView alertBoxText;
    private TextView alertBoxTitle;
    private TextView alertBoxTime;
    private TextView alertBoxLocation;
    private TextView alertBoxStatus;
    private Button btnReadMore;
    private Button btnShowLess;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alerts_box, container, false);

        alertBoxText = view.findViewById(R.id.alertBoxText);
        alertBoxTitle = view.findViewById(R.id.alertBoxTitle);
        alertBoxTime = view.findViewById(R.id.alertBoxTime);
        alertBoxLocation = view.findViewById(R.id.alertBoxLocation);
        alertBoxStatus = view.findViewById(R.id.alertBoxStatus);

        btnReadMore = view.findViewById(R.id.btnReadMore);
        btnShowLess = view.findViewById(R.id.btnShowLess);

        // Set initial text
        alertBoxText.setText("Placeholder Description");
        alertBoxTitle.setText("Placeholder Title");
        alertBoxTime.setText("Placeholder Time");
        alertBoxLocation.setText("Placeholder Location");
        alertBoxStatus.setText("Placeholder Status");
        // Set click listeners
        btnReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandText();
            }
        });

        btnShowLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapseText();
            }
        });

        return view;
    }

    private void expandText() {
        // Expand the text view by setting max lines to a large number
        alertBoxText.setMaxLines(Integer.MAX_VALUE);

        // Toggle visibility of buttons
        btnReadMore.setVisibility(View.GONE);
        btnShowLess.setVisibility(View.VISIBLE);
    }

    private void collapseText() {
        // Collapse the text view by setting max lines to 2
        alertBoxText.setMaxLines(2);

        // Toggle visibility of buttons
        btnReadMore.setVisibility(View.VISIBLE);
        btnShowLess.setVisibility(View.GONE);
    }
}
