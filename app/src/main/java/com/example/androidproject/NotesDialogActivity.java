package com.example.androidproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NotesDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_dialog);
        this.setFinishOnTouchOutside(false);
        TextView mNotes = findViewById(R.id.txt_notes_trip);
        mNotes.setText("I should buy :" +
                "\n1- sdadass" +
                "\n2-sdadasda" +
                "\n3-dsadasda");
        Button mNotesOK=findViewById(R.id.btn_note_trip);
        mNotesOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
