package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<TripData> tripList = new ArrayList<>();
    private RecyclerView recyclerView;    private TripAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton mAddTrip = findViewById(R.id.fb_incoming_trip_add_trip);
        mAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
                startActivity(intent);
                finish();
            }
        });
        recyclerView = findViewById(R.id.list_of_incoming_trips);

        mAdapter = new TripAdapter(tripList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        // adding inbuilt divider line
        //   recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        ImageView ivPastTrips = findViewById(R.id.img_history_trip);
        ivPastTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent (MainActivity.this,PastTripsActivity.class));
            }
        });

        prepareTripData();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(MainActivity.this, tripList.get(position).getTripName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void prepareTripData() {
        TripData tripData = new TripData("Mad Max: Fury Road", "Action & Adventure", "2015");
        tripList.add(tripData);

        TripData tripData2 = new TripData("Mad Max: Fury Road", "Action & Adventure", "2015");
        tripList.add(tripData2);
        TripData tripData3 = new TripData("Mad Max: Fury Road", "Action & Adventure", "2015");
        tripList.add(tripData3);
    }
}
