package com.example.illinoiswatch;


// Class declaration for use in database
// DataAlert.java
// DataAlert.java
public class DataAlert {
    private String status;
    private String distance;
    private String title;
    private String description;
    private String time;
    private String location; // New field

    public DataAlert(String status, String distance, String title, String description, String time, String location) {
        this.status = status;
        this.distance = distance;
        this.title = title;
        this.description = description;
        this.time = time;
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public String getDistance() {
        return distance;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }
}
