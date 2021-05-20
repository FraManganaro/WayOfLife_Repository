package com.example.wayoflife.workouts.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wayoflife.util.CustomerModel;
import com.example.wayoflife.util.DatabaseHelper;
import com.example.wayoflife.R;
import com.example.wayoflife.util.MyArrayAdapter;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.List;

public class WorkoutHistoryActivity extends AppCompatActivity {

    private final String TAG = "WorkoutHistoryActivity";
    private String data;

    private ImageView btDate;
    private ImageView btFavorites;
    private ListView listView;

    private DatabaseHelper databaseHelper;
    private List<CustomerModel> models;
    private ArrayAdapter arrayAdapter;
    private MyArrayAdapter myArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history);

        btDate = findViewById(R.id.bt_calendar);
        btFavorites = findViewById(R.id.bt_favorites);
        listView = findViewById(R.id.list_view);

        databaseHelper = new DatabaseHelper(WorkoutHistoryActivity.this);
        models = databaseHelper.getAllWorkout();

        arrayAdapter = new ArrayAdapter<CustomerModel>(WorkoutHistoryActivity.this,
                android.R.layout.simple_list_item_1, models);
        listView.setAdapter(arrayAdapter);

        manageListView();

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
                modifyData();
                Log.d(TAG, "Dopo la modifica: " + data);

                Toast.makeText(WorkoutHistoryActivity.this, ("Allenamenti in data - " + data), Toast.LENGTH_LONG).show();
                models = databaseHelper.getWorkoutForDate(data);
                manageListView();
            }
        });

        btFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WorkoutHistoryActivity.this, "Allenamenti preferiti!", Toast.LENGTH_LONG).show();
                models = databaseHelper.getWorkoutForLike();
                manageListView();
            }
        });
    }

    /** Metodo che gestisce la ListView */
    private void manageListView(){
        Log.d(TAG, "Controllo elementi del DB: "+ models.toString());

        myArrayAdapter = new MyArrayAdapter(models, WorkoutHistoryActivity.this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(WorkoutHistoryActivity.this, models.get(position).getTipologia(), Toast.LENGTH_LONG).show();
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentWorkout, new WorkoutFragment()).commit();
            }
        });

        listView.setAdapter(myArrayAdapter);
    }


    /**
     * Modifico la data mettendola nel formato Giorno/Mese/Anno
     */
    private void modifyData(){
        String[] t = data.split(" ");

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