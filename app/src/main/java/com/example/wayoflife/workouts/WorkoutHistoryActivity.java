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

        prova();

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

    public void prova(){
        CustomerModel customerModel1 = new CustomerModel("Run1", "10/05/2020", "1 ora 15 minuti", 10,"Corsa", 200, 5);
        CustomerModel customerModel2 = new CustomerModel("Pushup1", "22/05/2020", "15 minuti", "Pushup", 25, 100, 5);
        CustomerModel customerModel3 = new CustomerModel("Calcio", "08/06/2020", "2 ora 15 minuti","Calcio", 200, 5);
        DatabaseHelper db = new DatabaseHelper(WorkoutHistoryActivity.this);
        db.addWorkout(customerModel1);
        db.addWorkout(customerModel2);
        db.addWorkout(customerModel3);
    }
}