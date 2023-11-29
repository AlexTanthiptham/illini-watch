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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alerts_box, container, false);

        alertBoxText = view.findViewById(R.id.alertBoxText);
        alertBoxTitle = view.findViewById(R.id.alertBoxTitle);
        alertBoxTime = view.findViewById(R.id.alertBoxTime);
        alertBoxLocation = view.findViewById(R.id.alertBoxLocation);
        alertBoxStatus = view.findViewById(R.id.alertBoxStatus);

        btnReadMore = view.findViewById(R.id.btnReadMore);
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

        return view;
    }

    private void expandText() {
    }

}
