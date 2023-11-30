package com.example.illinoiswatch;

// HistoryItemAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
            // TODO: Route to map location of the thing

            Toast.makeText(v.getContext(), "Read More clicked for " + dataAlert.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

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
