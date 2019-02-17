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

    public TripData(String tripName, String startPoint, String endPoint) {
        this.tripName = tripName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public String getTripName() {
        return tripName;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getNotes() {
        return notes;
    }

    public Date getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}
