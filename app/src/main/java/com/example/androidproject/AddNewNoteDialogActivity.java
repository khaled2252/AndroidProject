package com.example.androidproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddNewNoteDialogActivity extends AppCompatActivity {
    private String mTripName, mTripNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note_dialog);
        this.setFinishOnTouchOutside(false);
        mTripName = getIntent().getStringExtra("tripName");
        final TextView mAddNewNote = findViewById(R.id.edt_add_new_note);
        Button mSave = findViewById(R.id.btn_add_new_note);
        Button mCancel = findViewById(R.id.btn_cancel_add_new_dialog);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddNewNote.getText().toString().isEmpty()) {
                    mAddNewNote.setError("Please add a note");

                } else {
                    if(mTripNotes.equals("No Notes"))
                        mTripNotes="";
                    mTripNotes = mTripNotes.concat("\n" + mAddNewNote.getText().toString());
                    updateNotes(mTripNotes);
                    finish();
                }
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getNotes();
    }

    private void getNotes() {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(AddNewNoteDialogActivity.this);
        mTripNotes = databaseAdapter.getDataFromTrip("notes", mTripName);
    }

    private void updateNotes(String newNotes) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(AddNewNoteDialogActivity.this);
        databaseAdapter.updateTripData(mTripName, "notes", newNotes);
    }
}
