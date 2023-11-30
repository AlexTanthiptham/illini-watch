package com.example.illinoiswatch;
// HistoryFragment.java
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private HistoryItemAdapter historyItemAdapter;
    private Spinner sortFieldSpinner;
    private ToggleButton sortOrderToggle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = view.findViewById(R.id.historyRecyclerView);
        sortFieldSpinner = view.findViewById(R.id.sortFieldSpinner);
        sortOrderToggle = view.findViewById(R.id.sortOrderToggle);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        historyItemAdapter = new HistoryItemAdapter(Database.getDataAlerts());
        recyclerView.setAdapter(historyItemAdapter);

        // Set up Spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_fields,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortFieldSpinner.setAdapter(spinnerAdapter);

        // Set default selection for Spinner and ToggleButton
        sortFieldSpinner.setSelection(spinnerAdapter.getPosition("Time")); // Time as default
        sortOrderToggle.setChecked(true); // Ascending as default

        // Set up listener for Spinner item selection
        sortFieldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Sort dataAlerts based on the selected field and order
                String selectedField = (String) parentView.getItemAtPosition(position);
                boolean ascendingOrder = sortOrderToggle.isChecked();
                Database.sortByField(selectedField.toLowerCase(), ascendingOrder);
                historyItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        // Set up listener for ToggleButton
        sortOrderToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Sort dataAlerts based on the selected field and order
            String selectedField = (String) sortFieldSpinner.getSelectedItem();
            boolean ascendingOrder = isChecked;
            Database.sortByField(selectedField.toLowerCase(), ascendingOrder);
            historyItemAdapter.notifyDataSetChanged();
        });

        return view;
    }
}
