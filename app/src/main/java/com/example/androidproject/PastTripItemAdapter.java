package com.example.androidproject;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PastTripItemAdapter extends RecyclerView.Adapter<PastTripItemAdapter.MyViewHolder> {

    private List<PastTripData> tripList;
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
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Trip item pressed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public PastTripItemAdapter(List<PastTripData> tripList, Context context) {
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

    public Context getContext(){
        return context;
    }
    public void deleteItem(RecyclerView.ViewHolder holder, int position){
        tripList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,tripList.size());
        if(tripList.size()==0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("No trips");
            builder.setMessage("You have no past trips.");
            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}