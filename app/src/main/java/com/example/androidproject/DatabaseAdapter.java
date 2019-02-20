package com.example.androidproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public long insertData(String name, String startName, String startLat, String startLon, String endName, String endLat, String endLon) {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("startname", startName);
        contentValues.put("startlat", startLat);
        contentValues.put("startlon", startLon);
        contentValues.put("endname", endName);
        contentValues.put("endlat", endLat);
        contentValues.put("endlon", endLon);
        return db.insert("trips", null, contentValues);
    }

    public String getData(String colName) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT $colName FROM trips;";
        query = query.replace("$colName", colName);
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            if (c.isLast())//return only last inserted record
                return c.getString(0);
        }
        return "No Data";
    }

    public boolean nameOfTripAlreadyExists(String tripName) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM trips where name = " + tripName.toLowerCase() + " ;";
        Cursor c = db.rawQuery(query, null);
        return c.moveToNext();

    }
}

