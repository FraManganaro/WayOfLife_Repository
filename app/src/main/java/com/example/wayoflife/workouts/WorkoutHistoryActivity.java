package com.example.wayoflife.workouts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.wayoflife.DatabaseHelper;
import com.example.wayoflife.R;

import java.util.ArrayList;

public class WorkoutHistoryActivity extends AppCompatActivity {
    //Inizializzazioni variabili
    Button btDate;
    ListView listView;

    DatabaseHelper databaseHelper;
    ArrayList arrayList;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history);
    }
}