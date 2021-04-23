package com.example.wayoflife.workouts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.wayoflife.R;

public class FreestyleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freestyle);

        TextView textView = findViewById(R.id.textView11);

        String message = getIntent().getStringExtra("attivita riconosciuta");
        textView.setText(message);
    }
}