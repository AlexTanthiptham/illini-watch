package com.example.illinoiswatch;

// HistoryItemAdapter.java
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.HistoryItemViewHolder> {

    private List<DataAlert> dataAlerts;

    public HistoryItemAdapter(List<DataAlert> dataAlerts) {
        this.dataAlerts = dataAlerts;
    }

    @NonNull
    @Override
    public HistoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new HistoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryItemViewHolder holder, int position) {
        DataAlert dataAlert = dataAlerts.get(position);

        // Bind data to the views in the history item
        holder.titleTextView.setText(dataAlert.getTitle());
        holder.locationTextview.setText(dataAlert.getLocation());
        holder.statusTextview.setText(dataAlert.getStatus());
        holder.timeTextview.setText(dataAlert.getTime());
        holder.descTextview.setText(dataAlert.getDescription());

        // Handle "Read More" button click
        holder.btnReadMore.setOnClickListener(v -> {
            // PLACEHOLDER INTERACTION
//            Toast.makeText(v.getContext(), "Read More clicked for " + dataAlert.getTitle(), Toast.LENGTH_SHORT).show();

            // TODO: Uncomment when merged
            // Navigate to MapsFragment and pass the _id of the DataAlert
             navigateToMapsFragment(v, dataAlert.get_id());
        });
    }

    // Helper method to navigate to MapsFragment
    private void navigateToMapsFragment(View view, int dataAlertId) {
        // Get the NavController
        NavController navController = Navigation.findNavController(view);

        // Create a bundle to pass dataAlertId to MapsFragment
        Bundle bundle = new Bundle();
        bundle.putInt("dataAlertId", dataAlertId);

        // Navigate to MapsFragment
        navController.navigate(R.id.navigation_maps, bundle);
    }

    // TODO: Include the below block in the merged MapsFragment to retrieve the _id
    //    @Override
    //    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    //        View view = inflater.inflate(R.layout.fragment_maps, container, false);
    //
    //        // Retrieve the _id from the bundle
    //        Bundle bundle = getArguments();
    //        if (bundle != null && bundle.containsKey("dataAlertId")) {
    //            int dataAlertId = bundle.getInt("dataAlertId");
    //
    //            // Now you have the dataAlertId, and you can use it as needed
    //            // For example, you can use it to display the corresponding location on the map
    //        }
    //
    //        // ... (rest of your code)
    //
    //        return view;
    //    }


    @Override
    public int getItemCount() {
        return dataAlerts.size();
    }

    public static class HistoryItemViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView locationTextview;
        TextView statusTextview;
        TextView timeTextview;
        TextView descTextview;
        Button btnReadMore;

        public HistoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            locationTextview = itemView.findViewById(R.id.locationTextview);
            statusTextview = itemView.findViewById(R.id.statusTextview);
            timeTextview = itemView.findViewById(R.id.timeTextview);
            descTextview = itemView.findViewById(R.id.descTextview);
            btnReadMore = itemView.findViewById(R.id.btnReadMore);
        }
    }
}
