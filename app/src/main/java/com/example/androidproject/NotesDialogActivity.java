package com.example.androidproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NotesDialogActivity extends AppCompatActivity {

    private TextView mNotes;
    private Button mNotesOK;
    private String mTripName, mTripNotes;
    private DatabaseAdapter mDatabaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_dialog);
        this.setFinishOnTouchOutside(false);
        mNotes = findViewById(R.id.txt_notes_trip);
        mNotesOK = findViewById(R.id.btn_note_trip);
        mDatabaseAdapter=new DatabaseAdapter(this);

        mTripName=getTripName();
        mTripNotes=getTripNotes();

        mNotes.setText(mTripNotes);

        mNotesOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String getTripName() {
        // get the trip name from  NotesDialogService
        Intent intent = getIntent();
        return intent.getStringExtra("tripName");
    }

    private String getTripNotes() {
        // get notes from database
        return mDatabaseAdapter.getDataFromTrip("notes",mTripName);
    }
}
