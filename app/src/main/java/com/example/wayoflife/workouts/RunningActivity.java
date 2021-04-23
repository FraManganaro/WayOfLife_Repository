package com.example.wayoflife.workouts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.wayoflife.R;

public class RunningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        TextView textView = findViewById(R.id.textView5);

        String message = getIntent().getStringExtra("attivita riconosciuta");
        textView.setText(message);
    }
}