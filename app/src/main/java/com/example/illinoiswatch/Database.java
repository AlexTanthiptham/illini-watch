package com.example.illinoiswatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Database {
    private static List<DataAlert> dataAlerts;

    static {
        dataAlerts = new ArrayList<>();
        dataAlerts.add(new DataAlert("Active", "10 km", "Alert 1", "Description 1", "10:00 AM", "Location 1"));
        dataAlerts.add(new DataAlert("Inactive", "5 km", "Alert 2", "Description 2", "12:30 PM", "Location 2"));
        // Add more dataAlerts as needed
    }

    public static List<DataAlert> getDataAlerts() {
        return dataAlerts;
    }

    // Sort dataAlerts by distance
    public static void sortByDistance() {
        Collections.sort(dataAlerts, Comparator.comparing(DataAlert::getDistance));
    }

    // Sort dataAlerts by status
    public static void sortByStatus() {
        Collections.sort(dataAlerts, Comparator.comparing(DataAlert::getStatus));
    }

    // Sort dataAlerts by time
    public static void sortByTime() {
        Collections.sort(dataAlerts, Comparator.comparing(DataAlert::getTime));
    }

    // Sort dataAlerts by title
    public static void sortByTitle() {
        Collections.sort(dataAlerts, Comparator.comparing(DataAlert::getTitle));
    }
}