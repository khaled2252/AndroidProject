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

    public static ArrayList<TripData> tripList = new ArrayList<>();
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
        ivUpcommingTrips = findViewById(R.id.img_incoming_trips);

        tripList=new DatabaseAdapter(this).getAllPastTrips();
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

        ivUpcommingTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PastTripsActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(PastTripsActivity.this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                        Intent intent=new Intent(PastTripsActivity.this,PastTripsDetails.class);
                        intent.putExtra("tripName",tripList.get(position).getTripName());
                        startActivity(intent);
                        finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PastTripsActivity.this,WelcomeActivity.class));
        finish();
    }

}


