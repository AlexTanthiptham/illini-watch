package com.example.illinoiswatch;


// Class declaration for use in database
// DataAlert.java
// DataAlert.java
// DataAlert.java
public class DataAlert {
    private int _id;
    private String status;
    private double distance;
    private String title;
    private String description;
    private String time;
    private String location;

    public DataAlert(int _id, String status, double distance, String title, String description, String time, String location) {
        this._id = _id;
        this.status = status;
        this.distance = distance;
        this.title = title;
        this.description = description;
        this.time = time;
        this.location = location;
    }

    public int get_id() {
        return _id;
    }

    public String getStatus() {
        return status;
    }

    public double getDistance() {
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
