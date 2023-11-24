package com.example.illinoiswatch;

import java.io.Serializable;

public class AlertDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    private String eventType;
    private String address;
    private String message;

    private String alerttime;

    public AlertDetails(String eventType, String address, String message) {
        this.eventType = eventType;
        this.address = address;
        this.message = message;
    }

    // Getters
    public String getEventType() {
        return eventType;
    }

    public String getAddress() {
        return address;
    }

    public String getAlerttime() {
        return alerttime;
    }

    public void setAlerttime(String alerttime) {
        this.alerttime = alerttime;
    }

    public String getMessage() {
        return message;
    }

}
