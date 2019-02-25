package com.example.androidproject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;

public class NotesHeadService extends Service {

    private WindowManager mWindowManager;
    private View mNoteHeadView;
    private ImageView mNotesHead, mAddNewNote, mCloseHead, mShowNotes;
    private String mTripName, mTripNotes;
    private Intent intent;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private static final int MAX_CLICK_DURATION = 200;
    private long startClickTime;
    private boolean isFABOpen = false;
    private WindowManager.LayoutParams params;

    public NotesHeadService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mTripName = intent.getStringExtra("tripName");
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();
        //Inflate the chat head layout we created
        mNoteHeadView = LayoutInflater.from(this).inflate(R.layout.notes_layout_circle, null);

        //Add the view to the window.
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        //Specify the chat head position
        //Initially view will be added to top-left corner
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 300;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        assert mWindowManager != null;
        mWindowManager.addView(mNoteHeadView, params);

        //Set the close button.
        mCloseHead = mNoteHeadView.findViewById(R.id.img_notes_close);
        mShowNotes = mNoteHeadView.findViewById(R.id.img_notes_show_notes);
        mCloseHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close the service and remove the chat head from the window
                stopSelf();
            }
        });

        //Drag and move chat head using user's touch action.
        mNotesHead = mNoteHeadView.findViewById(R.id.img_notes_head);
        mAddNewNote = mNoteHeadView.findViewById(R.id.img_notes_add_new_one);
        mShowNotes.setVisibility(View.GONE);
        mCloseHead.setVisibility(View.GONE);
        mAddNewNote.setVisibility(View.GONE);
        mAddNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNewNoteDialog();
            }
        });
        mShowNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), NotesDialogActivity.class);
                intent.putExtra("tripName", mTripName);
                startActivity(intent);
                mShowNotes.setVisibility(View.GONE);
                mCloseHead.setVisibility(View.GONE);
                mAddNewNote.setVisibility(View.GONE);
                isFABOpen = false;
            }
        });
        mNotesHead.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;
                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        startClickTime = Calendar.getInstance().getTimeInMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        //  if it was a click from user
                        long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                        if (clickDuration < MAX_CLICK_DURATION) {
                            if (!isFABOpen) {
                                mShowNotes.setVisibility(View.VISIBLE);
                                mCloseHead.setVisibility(View.VISIBLE);
                                mAddNewNote.setVisibility(View.VISIBLE);
                                isFABOpen = true;
                            } else {
                                mShowNotes.setVisibility(View.GONE);
                                mCloseHead.setVisibility(View.GONE);
                                mAddNewNote.setVisibility(View.GONE);
                                isFABOpen = false;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mNoteHeadView, params);
                        break;
                }
                return true;
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mNoteHeadView != null)
            mWindowManager.removeView(mNoteHeadView);
    }


    private void showAddNewNoteDialog() {
        intent = new Intent(getApplicationContext(), AddNewNoteDialogActivity.class);
        intent.putExtra("tripName", mTripName);
        startActivity(intent);
        mShowNotes.setVisibility(View.GONE);
        mCloseHead.setVisibility(View.GONE);
        mAddNewNote.setVisibility(View.GONE);
        isFABOpen = false;

    }
}
