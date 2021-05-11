package com.example.wayoflife.workouts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.wayoflife.util.CustomerModel;
import com.example.wayoflife.DatabaseHelper;
import com.example.wayoflife.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.List;

public class WorkoutHistoryActivity extends AppCompatActivity {

    private final String TAG = "WorkoutHistoryActivity";
    private String data;

    //Inizializzazioni variabili
    private Button btDate;
    private ListView listView;

    private DatabaseHelper databaseHelper;
    private List<CustomerModel> models;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history);

        btDate = findViewById(R.id.bt_calendar);
        listView = findViewById(R.id.list_view);

//        prova();

        databaseHelper = new DatabaseHelper(WorkoutHistoryActivity.this);
        models = databaseHelper.getAllWorkout();

        arrayAdapter = new ArrayAdapter<CustomerModel>(WorkoutHistoryActivity.this,
                android.R.layout.simple_list_item_1, models);
        listView.setAdapter(arrayAdapter);


        /** Gestione del calendario */
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Seleziona una data");
        MaterialDatePicker materialDatePicker = builder.build();

        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "DATA_PICKER");
            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                data = materialDatePicker.getHeaderText();
                Log.d(TAG, "onPositiveButtonClick: " + data);
                modifyData();
                Log.d(TAG, "onPositiveButtonClick: " + data);
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

    private void modifyData(){
        String[] t = data.split(" ");
        Log.d(TAG, "modifyData: " + t[0]);
        Log.d(TAG, "modifyData: " + t[1]);
        Log.d(TAG, "modifyData: " + t[2]);

        if(Integer.parseInt(t[0]) < 10)
            t[0] = "0" + t[0];

        switch(t[1]){
            case "gen":
                data = t[0] + "/" + "01" + "/" + t[2];
                break;
            case "feb":
                data = t[0] + "/" + "02" + "/" + t[2];
                break;
            case "mar":
                data = t[0] + "/" + "03" + "/" + t[2];
                break;
            case "apr":
                data = t[0] + "/" + "04" + "/" + t[2];
                break;
            case "mag":
                data = t[0] + "/" + "05" + "/" + t[2];
                break;
            case "giu":
                data = t[0] + "/" + "06" + "/" + t[2];
                break;
            case "lug":
                data = t[0] + "/" + "07" + "/" + t[2];
                break;
            case "ago":
                data = t[0] + "/" + "08" + "/" + t[2];
                break;
            case "set":
                data = t[0] + "/" + "09" + "/" + t[2];
                break;
            case "ott":
                data = t[0] + "/" + "10" + "/" + t[2];
                break;
            case "nov":
                data = t[0] + "/" + "11" + "/" + t[2];
                break;
            case "dic":
                data = t[0] + "/" + "12" + "/" + t[2];
                break;
        }
    }
}