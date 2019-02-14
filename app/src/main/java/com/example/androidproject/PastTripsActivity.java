package com.example.androidproject;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
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
    SwipeController swipeController = null;
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_trips);

        recyclerView = findViewById(R.id.recycler_view);

        SampleTripData();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());

        pastTripItemAdapter = new PastTripItemAdapter(tripList);
        recyclerView.setAdapter(pastTripItemAdapter);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                //pastTripItemAdapter.players.remove(position);
                //mAdapter.notifyItemRemoved(position);
                //mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }


    private void SampleTripData() {
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


