package com.example.androidproject;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> {

    private List<TripData> tripList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTripName, mTripStartPoint, mTripEndPoint;

        MyViewHolder(View view) {
            super(view);
            mTripName = view.findViewById(R.id.txt_incoming_trip_name);
            mTripStartPoint = view.findViewById(R.id.txt_incoming_trip_start_point);
            mTripEndPoint = view.findViewById(R.id.txt_incoming_end_point);
        }
    }


    public TripAdapter(List<TripData> tripList) {
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TripData tripData = tripList.get(position);
        holder.mTripName.setText(tripData.getTripName());
        holder.mTripStartPoint.setText(tripData.getStartPoint());
        holder.mTripEndPoint.setText(tripData.getEndPoint());
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }
}
