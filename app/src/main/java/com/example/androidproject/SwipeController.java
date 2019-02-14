package com.example.androidproject;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import static android.support.v7.widget.helper.ItemTouchHelper.*;

class SwipeController extends Callback {
    private boolean swipeBack;

    private Drawable icon;
    private PastTripItemAdapter mAdapter;


    public SwipeController(PastTripItemAdapter adapter){
        mAdapter = adapter;
        icon = ContextCompat.getDrawable(mAdapter.getContext(),R.drawable.trash);
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        if(direction==LEFT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mAdapter.getContext());
            builder.setTitle("Delete trip");
            builder.setMessage("Are you sure you want to delete this trip?");
            builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    int position = viewHolder.getAdapterPosition();
                    mAdapter.deleteItem(viewHolder, position);
                    dialog.dismiss();
                    Toast.makeText(mAdapter.getContext(), "Trip deleted successfully. ", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAdapter.notifyItemChanged(viewHolder.getAdapterPosition());//This will make the swiped out view animate back into it's original position.
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        }
        else
            mAdapter.notifyItemChanged(viewHolder.getAdapterPosition()); // fixed a glitch (stuck at most right) when user preforms a full right swipe , so this reverts it back
    }
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        View itemView = viewHolder.itemView;

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        float translationX=dX;

        if (dX > 0) { // Swiping to the right
            translationX = 0; //i.e avoid moving it to right
             } else if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            icon.draw(c);
             }
             else
                 translationX=dX;

            super.onChildDraw(c, recyclerView, viewHolder, translationX,
                dY, actionState, isCurrentlyActive);


    }

    private void setTouchListener(Canvas c,
                                  RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  float dX, float dY,
                                  int actionState, boolean isCurrentlyActive) {

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                return false;
            }
        });
    }
    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

}