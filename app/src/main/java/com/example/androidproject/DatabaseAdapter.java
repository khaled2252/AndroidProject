package com.example.androidproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Khaled on 29-Jan-19.
 */


//To Manipulate the database
public class DatabaseAdapter {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    DatabaseAdapter(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertInitialTripData(String tripName, String startName, String endName, String notes, String type,
                                      String date, String time, String status, String alarmRequestCode) {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tripname", tripName);
        contentValues.put("startname", startName);
        contentValues.put("endname", endName);
        contentValues.put("notes", notes);
        contentValues.put("tripType", type);
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("status", status);
        contentValues.put("requestcode", alarmRequestCode);
        return db.insert("trips", null, contentValues);
    }

    public int updateTripData(String tripName, String colName, String value) {
        db = dbHelper.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(colName, value);
        return db.update("trips", args, "tripname" + "=" + "'"+tripName+"'", null);
    }

    public String getDataFromTrip(String colName, String tripName) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT " + colName + " FROM trips WHERE tripname = '" + tripName + "';";
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            return c.getString(0);
        }
        return "No Data";
    }
    public String getDataFromTripByDateAndTime(String colName, String tripDate,String tripTime) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT " + colName + " FROM trips WHERE date = '" + tripDate + "' and time = '"+ tripTime + "' ;";
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            return c.getString(0);
        }
        return "No Data";
    }

    public boolean nameOfTripAlreadyExists(String tripName) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM trips where tripname = '" + tripName.toLowerCase() + "' ;";
        Cursor c = db.rawQuery(query, null);
        return c.moveToNext();

    }

    public int getAllTrips() {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM trips ;";
        Cursor c = db.rawQuery(query, null);
        int count = 0;
        while (c.moveToNext())
            count++;
        return count;
    }

    public ArrayList<TripData> getAllIncomingTrips() {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM trips WHERE status = 'Incoming' ;";
        // query = query.replace("$sta", "Incoming");
        Cursor c = db.rawQuery(query, null);
        ArrayList<TripData> aL = new ArrayList<>();
        TripData tripData;
        while (c.moveToNext()) {
            tripData = new TripData(c.getString(0), c.getString(1), c.getString(2));
            aL.add(tripData);
            tripData = null;
        }

        return aL;
    }

    public ArrayList getAllDataFromTrip(String tripName) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM trips WHERE tripname = '" + tripName + "';";
        Cursor c = db.rawQuery(query, null);
        ArrayList aL = new ArrayList();
        int i = 0;
        while (c.moveToNext()) {
            for (int w = 0; w < c.getColumnCount(); w++)
                aL.add(c.getString(w));
            i++;
        }
        return aL;
    }
    public ArrayList<TripData> getAllPastTrips() {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM trips WHERE status != 'Incoming' ;";
        Cursor c = db.rawQuery(query, null);
        ArrayList<TripData> aL = new ArrayList<>();
        TripData tripData;
        while (c.moveToNext()) {
            tripData = new TripData(c.getString(0), c.getString(1), c.getString(2));
            aL.add(tripData);
            tripData = null;
        }

        return aL;
    }
    public long deleteTrip(String tripName)
    {
        db = dbHelper.getWritableDatabase();
        return db.delete("trips", "tripname" + " = " +"'" +tripName+"'" +" AND" + " status" + "!=" + "'Incoming'", null);
    }
    public long deleteAllTrips()
    {
        db = dbHelper.getWritableDatabase();
        return db.delete("trips", "status" + "!=" +"'Incoming'", null);
    }
}

