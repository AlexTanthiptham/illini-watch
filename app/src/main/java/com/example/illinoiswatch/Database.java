package com.example.illinoiswatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


// TODO: Hardcoded statues for High Alert, Low Alert, Inactive ; Querry functions
public class Database {
    private static List<DataAlert> dataAlerts;


    // NOTE: Time is a string in format "mm/dd/yyyy 24:00"
    static {
        dataAlerts = new ArrayList<>();
        dataAlerts.add(new DataAlert(1, "High Alert", 10.0, "Gas Leak", "Avoid area. If in or near this location, leave immediately. ", "12/08/2023 17:25", "South 3rd Street & East Green Street, Champaign"));
        dataAlerts.add(new DataAlert(2, "Alert", 5.0, "Building Fire", "Avoid area. If in or near this location, leave immediately. ", "12/07/2023 07:55", "South 3rd Street & East Green Street, Champaign"));
        dataAlerts.add(new DataAlert(3, "Inactive", 8.0, "Stabbing", "The emergency has ended. Please continue to avoid the area.", "12/06/2023 22:05", "211 E Green St, Champaign, IL 61820"));
        dataAlerts.add(new DataAlert(4, "Alert", 12.5, "Robbery", "Police on scene, searching area to investigate.  No additional reports of shots.", "12/05/2023 18:40", "408 E Green St, Champaign, IL 61820"));
        dataAlerts.add(new DataAlert(5, "High Alert", 7.0, "Assault", "Suspect believed to have left the area. Continue to exercise caution.", "12/04/2023 13:15", "1409 W Green St, Urbana, IL 61801"));
        dataAlerts.add(new DataAlert(6, "Inactive", 15.0, "Shots Fired", "The emergency has ended. It is safe to resume regular activities.", "12/03/2023 09:30", "102 E Green St STE 104, Champaign, IL 61820"));
        dataAlerts.add(new DataAlert(7, "Alert", 9.5, "Chemical Spill", "Avoid area. If in or near this location, leave immediately. ", "12/02/2023 11:10", "505 S Mathews Ave, Urbana, IL 61801"));
        dataAlerts.add(new DataAlert(8, "High Alert", 11.0, "Power Outage", "Utility crews working to restore power after an outage at; estimated resolution time pending.", "12/01/2023 16:20", "611 W Park St, Urbana, IL 61801"));
        dataAlerts.add(new DataAlert(9, "Inactive", 6.0, "Traffic Accident", "The emergency has ended. It is safe to resume regular activities.", "11/30/2023 08:45", "1401 W Green St, Urbana, IL 61801"));
        dataAlerts.add(new DataAlert(10, "High Alert", 14.5, "Forest Fire", "Firefighters and aerial support deployed to combat a forest fire.", "11/29/2023 14:35", "201 N Goodwin Ave, Urbana, IL 61801"));

        // Add more dataAlerts as needed
    }

    // Retrieve all DataAlerts
    public static List<DataAlert> getDataAlerts() {
        return dataAlerts;
    }

    // Retrieve an individual DataAlert by ID
    public static DataAlert getDataAlertById(int id) {
        for (DataAlert alert : dataAlerts) {
            if (alert.get_id() == id) {
                return alert;
            }
        }
        return null; // If no alert with the specified ID is found
    }

    public static void sortByField(String field, boolean ascending) {
        Comparator<DataAlert> comparator;
        switch (field.toLowerCase()) {
            case "distance":
                comparator = Comparator.comparing(DataAlert::getDistance);
                break;
            case "status":
                comparator = Comparator.comparing(DataAlert::getStatus);
                break;
            case "time":
                comparator = Comparator.comparing(DataAlert::getTime);
                break;
            case "title":
                comparator = Comparator.comparing(DataAlert::getTitle);
                break;
            default:
                comparator = Comparator.comparing(DataAlert::getTitle);
                break;
        }

        if (!ascending) {
            comparator = comparator.reversed();
        }

        Collections.sort(dataAlerts, comparator);
    }
}