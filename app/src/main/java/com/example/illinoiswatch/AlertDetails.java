package com.example.illinoiswatch;

public class AlertDetails {
    private String eventType;
    private String address;

    public AlertDetails(String eventType, String address) {
        this.eventType = eventType;
        this.address = address;
    }

    // Getters
    public String getEventType() {
        return eventType;
    }

    public String getAddress() {

        return address;
    }
}
