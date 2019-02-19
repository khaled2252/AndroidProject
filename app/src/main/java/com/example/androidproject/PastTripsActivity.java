package com.example.androidproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class PastTripsActivity extends AppCompatActivity {

    public static ArrayList<PastTripData> tripList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PastTripItemAdapter pastTripItemAdapter;
    private SwipeController swipeController;
    private ItemTouchHelper itemTouchHelper;
    private FloatingActionButton fabDeleteAll;
    private FloatingActionButton fabMap;
    private ImageView ivUpcommingTrips;
    public static ImageView ivNoTrips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_trips);

        recyclerView = findViewById(R.id.recycler_view);
        fabDeleteAll = findViewById(R.id.fab_delete_all);
        fabMap = findViewById(R.id.fab_maps);
        ivNoTrips = findViewById(R.id.iv_no_trips);
        //ivUpcommingTrips = findViewById(R.id.i);

        fillTripDataAndCoords();
        if (tripList.isEmpty()) {
            ivNoTrips.setVisibility(View.VISIBLE);
        }
        pastTripItemAdapter = new PastTripItemAdapter(tripList, this);
        recyclerView.setAdapter(pastTripItemAdapter);

        swipeController = new SwipeController(pastTripItemAdapter);
        itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fabDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tripList.size() != 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PastTripsActivity.this);
                    builder.setTitle("Clear all trips");
                    builder.setMessage("Are you sure you want to CLEAR ALL your trips?");
                    builder.setPositiveButton("Clear", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            pastTripItemAdapter.deleteAll();
                            Toast.makeText(PastTripsActivity.this, "All trips cleared successfully.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else
                    Toast.makeText(PastTripsActivity.this, "You have no trips to delete!", Toast.LENGTH_SHORT).show();
            }
        });

        fabMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PastTripsActivity.this, MapsActivity.class));
            }
        });

       /* ivUpcommingTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PastTripsActivity.this, "hhhhhh", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PastTripsActivity.this,MainActivity.class));
                finish();
            }
        });*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PastTripsActivity.this,WelcomeActivity.class));
    }

    private void fillTripData() {
        PastTripData tripData = new PastTripData("Alexandria", "Ramsis", "Sedi-Gaber", R.drawable.alex);
        tripList.add(tripData);

        PastTripData tripData2 = new PastTripData("Masyaf", "Sedi-Gaber", "Matrouh", R.drawable.matro7);
        tripList.add(tripData2);

        PastTripData tripData3 = new PastTripData("Home", "Iti", "home", R.drawable.home);
        tripList.add(tripData3);

        PastTripData tripData4 = new PastTripData("Alexandria", "Ramsis", "Sedi-Gaber", R.drawable.alex);
        tripList.add(tripData4);

        PastTripData tripData5 = new PastTripData("Masyaf", "Sedi-Gaber", "Matrouh", R.drawable.matro7);
        tripList.add(tripData5);

        PastTripData tripData6 = new PastTripData("Home", "Iti", "home", R.drawable.home);
        tripList.add(tripData6);

        PastTripData tripData7 = new PastTripData("Alexandria", "Ramsis", "Sedi-Gaber", R.drawable.alex);
        tripList.add(tripData7);

        PastTripData tripData8 = new PastTripData("Masyaf", "Sedi-Gaber", "Matrouh", R.drawable.matro7);
        tripList.add(tripData8);

        PastTripData tripData9 = new PastTripData("Home", "Iti", "home", R.drawable.home);
        tripList.add(tripData9);

        PastTripData tripData10 = new PastTripData("Alexandria", "Ramsis", "Sedi-Gaber", R.drawable.alex);
        tripList.add(tripData10);

        PastTripData tripData11 = new PastTripData("Masyaf", "Sedi-Gaber", "Matrouh", R.drawable.matro7);
        tripList.add(tripData11);

        PastTripData tripData12 = new PastTripData("Home", "Iti", "home", R.drawable.home);
        tripList.add(tripData12);
    }

    private void fillTripDataAndCoords() {
        PastTripData tripData = new PastTripData("Alexandria", "Ramsis", "Sedi-Gaber", R.drawable.alex, new LatLng(30.06133, 31.249), new LatLng(31.2175, 29.9386));
        tripList.add(tripData);

        PastTripData  tripData2 = new PastTripData ("Masyaf", "Sedi-Gaber", "Matrouh",R.drawable.matro7,new LatLng(31.2175, 29.9386),new LatLng(31.33656,27.1152474));
        tripList.add(tripData2);

        PastTripData  tripData3 = new PastTripData ("Home", "Iti", "home",R.drawable.home,new LatLng(31.19263,29.90647),new LatLng(31.2290,29.9607));
        tripList.add(tripData3);

    }
}


