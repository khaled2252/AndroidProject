package com.example.androidproject;

//To Create database if not created

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import static java.sql.Types.VARCHAR;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "simpleDB";
    private static int DATABASE_VERSION = 1;
    private static final String tripName = "tripname";
    private static final String startPoint = "startname";
    private static final String endPoint = "endname";
    private static final String notes = "notes";
    private static final String date = "date";
    private static final String time = "time";
    private static final String status = "status";
    private static final String requestCode = "requestcode";
    private static final String thumbnail = "thumbnail";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE trips (tripname VARCHAR(255) PRIMARY KEY," +
                " startname VARCHAR(255),endname VARCHAR(255),notes VARCHAR(255),tripType VARCHAR(255)," +
                "date VARCHAR(255),time VARCHAR(255), status VARCHAR(255),requestcode VARCHAR(255))");

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
