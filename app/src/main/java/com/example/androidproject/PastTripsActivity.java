package com.example.androidproject;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class PastTripsActivity extends AppCompatActivity {

    private ArrayList<PastTripData> tripList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PastTripItemAdapter pastTripItemAdapter;
    private SwipeController swipeController;
    private ItemTouchHelper itemTouchHelper;
    private FloatingActionButton fabDeleteAll;
    private FloatingActionButton fabMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_trips);

        recyclerView = findViewById(R.id.recycler_view);
        fabDeleteAll = findViewById(R.id.fab_delete_all);
        fabMap = findViewById(R.id.fab_maps);

        fillTripData();
        if(tripList.isEmpty())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No trips");
            builder.setMessage("You have no past trips anymore.");
            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        }
        pastTripItemAdapter = new PastTripItemAdapter(tripList,this);
        recyclerView.setAdapter(pastTripItemAdapter);

        swipeController = new SwipeController(pastTripItemAdapter);
        itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fabDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PastTripsActivity.this);
                builder.setTitle("Clear all trips");
                builder.setMessage("Are you sure you want to CLEAR ALL your trip?");
                builder.setPositiveButton("Clear", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        pastTripItemAdapter.deleteAll();
                        Toast.makeText(PastTripsActivity.this, "All trips cleared successfully. ", Toast.LENGTH_SHORT).show();
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

            }
        });

    }

    private void fillTripData() {
        PastTripData tripData = new PastTripData ("Alexandria", "Ramsis", "Sedi-Gaber",R.drawable.alex);
        tripList.add(tripData);

        PastTripData  tripData2 = new PastTripData ("Masyaf", "Sedi-Gaber", "Matrouh",R.drawable.matro7);
        tripList.add(tripData2);

        PastTripData  tripData3 = new PastTripData ("Home", "Iti", "home",R.drawable.home);
        tripList.add(tripData3);

        PastTripData tripData4 = new PastTripData ("Alexandria", "Ramsis", "Sedi-Gaber",R.drawable.alex);
        tripList.add(tripData4);

        PastTripData  tripData5 = new PastTripData ("Masyaf", "Sedi-Gaber", "Matrouh",R.drawable.matro7);
        tripList.add(tripData5);

        PastTripData  tripData6 = new PastTripData ("Home", "Iti", "home",R.drawable.home);
        tripList.add(tripData6);

        PastTripData tripData7 = new PastTripData ("Alexandria", "Ramsis", "Sedi-Gaber",R.drawable.alex);
        tripList.add(tripData7);

        PastTripData  tripData8 = new PastTripData ("Masyaf", "Sedi-Gaber", "Matrouh",R.drawable.matro7);
        tripList.add(tripData8);

        PastTripData  tripData9 = new PastTripData ("Home", "Iti", "home",R.drawable.home);
        tripList.add(tripData9);

        PastTripData tripData10 = new PastTripData ("Alexandria", "Ramsis", "Sedi-Gaber",R.drawable.alex);
        tripList.add(tripData10);

        PastTripData  tripData11 = new PastTripData ("Masyaf", "Sedi-Gaber", "Matrouh",R.drawable.matro7);
        tripList.add(tripData11);

        PastTripData  tripData12 = new PastTripData ("Home", "Iti", "home",R.drawable.home);
        tripList.add(tripData12);
    }
}


