package com.example.androidproject;

import java.util.Date;

public class TripData {
    private String tripName;
    private String startPoint;
    private String endPoint;
    private String notes;
    private Date date;
    private String time;
    private String status;

    public TripData(String tripName, String startPoint, String endPoint, String notes, Date date, String time, String status) {
        this.tripName = tripName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.notes = notes;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
