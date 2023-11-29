package com.example.illinoiswatch;

// HistoryItemViewHolder.java
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryItemViewHolder extends RecyclerView.ViewHolder {
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
