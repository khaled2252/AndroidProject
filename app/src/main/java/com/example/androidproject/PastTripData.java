package com.example.androidproject;

import java.util.Date;
import com.google.android.gms.maps.model.LatLng;

public class PastTripData {
    private String tripName;
    private String startPoint;
    private LatLng startCoords;
    private String endPoint;
    private LatLng endCoords;
    private String notes;
    private Date date;
    private String time;
    private String status;
    private int thumbnail;

    public PastTripData(String tripName, String startPoint, String endPoint, String notes, Date date, String time, String status, int thumbnail) {
        this.tripName = tripName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.notes = notes;
        this.date = date;
        this.time = time;
        this.status = status;
        this.thumbnail = thumbnail;
    }

    public PastTripData(String tripName, String startPoint, String endPoint, int thumbnail) {
        this.tripName = tripName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.thumbnail = thumbnail;
    }

    public PastTripData(String tripName, String startPoint, String endPoint, int thumbnail,LatLng startCoords, LatLng endCoords) {
        this.tripName = tripName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.thumbnail = thumbnail;
        this.startCoords = startCoords;
        this.endCoords = endCoords;
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

    public String getStatus() { return status;}

    public int getThumbnail() {
        return thumbnail;
    }

    public LatLng getStartCoords() { return startCoords;}

    public LatLng getEndCoords() { return endCoords;}

}