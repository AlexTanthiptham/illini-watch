package com.example.illinoiswatch;

import java.io.Serializable;

public class AlertDetails implements Serializable {
    private static final long serialVersionUID = 1L;

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
