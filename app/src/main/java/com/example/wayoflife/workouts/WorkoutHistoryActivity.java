package com.example.wayoflife.workouts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.wayoflife.CustomerModel;
import com.example.wayoflife.DatabaseHelper;
import com.example.wayoflife.R;

import java.util.ArrayList;
import java.util.List;

public class WorkoutHistoryActivity extends AppCompatActivity {
    //Inizializzazioni variabili
    Button btDate;
    ListView listView;

    DatabaseHelper databaseHelper;
    List<CustomerModel> models;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history);

        btDate = findViewById(R.id.bt_calendar);
        listView = findViewById(R.id.list_view);

        databaseHelper = new DatabaseHelper(WorkoutHistoryActivity.this);
        models = databaseHelper.getAllWorkout();

        arrayAdapter = new ArrayAdapter<CustomerModel>(WorkoutHistoryActivity.this, android.R.layout.simple_list_item_1, models);
        listView.setAdapter(arrayAdapter);

        // Bottone per uscita del calendario
        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}