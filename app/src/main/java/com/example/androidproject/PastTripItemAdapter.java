package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PastTripItemAdapter extends RecyclerView.Adapter<PastTripItemAdapter.MyViewHolder> {

    private List<TripData> tripList;
    private Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTripName, mTripStartPoint, mTripEndPoint;
        ImageView ivThumbnail;


        MyViewHolder(View view) {
            super(view);
            mTripName = view.findViewById(R.id.tv_trip_name);
            mTripStartPoint = view.findViewById(R.id.tv_start_point);
            mTripEndPoint = view.findViewById(R.id.tv_end_point);
            ivThumbnail = view.findViewById(R.id.iv_thumbnail);

        }
    }

    public PastTripItemAdapter(List<TripData> tripList, Context context) {
        this.tripList = tripList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        TripData tripData = tripList.get(position);
        holder.mTripName.setText(tripData.getTripName());
        holder.mTripStartPoint.setText(tripData.getStartPoint());
        holder.mTripEndPoint.setText(tripData.getEndPoint());
        holder.ivThumbnail.setImageResource(R.drawable.plane);


    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public Context getContext(){
        return context;
    }
    public void deleteItem(int position){
        new DatabaseAdapter(context).deleteTrip(tripList.get(position).getTripName());
        tripList.remove(position);
        notifyDataSetChanged();  //Both works, this statement or the below 2 statments
        /*notifyItemRemoved(position);
        notifyItemRangeChanged(position,tripList.size());*/
        if(tripList.size()==0)
        {
            PastTripsActivity.ivNoTrips.setVisibility(View.VISIBLE);
        }

    }
    public void deleteAll() {
        new DatabaseAdapter(context).deleteAllTrips();
        tripList.clear();
        notifyDataSetChanged();
        PastTripsActivity.ivNoTrips.setVisibility(View.VISIBLE);
    }
}