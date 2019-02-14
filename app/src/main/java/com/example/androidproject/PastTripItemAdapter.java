package com.example.androidproject;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PastTripItemAdapter extends RecyclerView.Adapter<PastTripItemAdapter.MyViewHolder> {

    private List<PastTripData> tripList;

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

    public PastTripItemAdapter(List<PastTripData> tripList) {
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PastTripData tripData = tripList.get(position);
        holder.mTripName.setText(tripData.getTripName());
        holder.mTripStartPoint.setText(tripData.getStartPoint());
        holder.mTripEndPoint.setText(tripData.getEndPoint());
        holder.ivThumbnail.setImageResource(tripData.getThumbnail());
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

}