package com.example.wayoflife.workouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wayoflife.DatabaseHelper;

import com.example.wayoflife.Constants;
import com.example.wayoflife.CustomerModel;
import com.example.wayoflife.DatabaseHelper;
import com.example.wayoflife.R;
import com.example.wayoflife.ui.HomeActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EndWorkoutActivity<databaseHelper> extends AppCompatActivity {

    private final String TAG = "EndWorkoutActivity";

    private int id;

    private String nome;
    private String data;
    private String durata; //Da mettere nella classe come String

    private String tipologiaAllenamento;

    private int calorie;
    private float chilometri;
    private int n_flessioni;
    private int state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_workout);
        TextView textView = findViewById(R.id.textView12);

        //nome -> da recuperare da un label sul layout

        /** Recupero data odierna */
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy"); // getting date in this format
        String formattedDate = df.format(date.getDate());
        data = formattedDate;

        tipologiaAllenamento = getIntent().getStringExtra(Constants.ATTIVITA_RILEVATA);

        long tempoPassato = getIntent().getLongExtra(Constants.TEMPO_PASSATO, 0);

        //calorie = getIntent().getFloatExtra(Constants.CALORIE, 0);
        //chilometri = getIntent().getIntExtra(Constants.CHILOMETRI, 0);

        /** In base alla tipologia di allenamento vedo se recuperare flessioni o no */
        if(tipologiaAllenamento.equalsIgnoreCase("Freestyle") ||
                tipologiaAllenamento.equalsIgnoreCase("Pushup")) {
            n_flessioni = getIntent().getIntExtra(Constants.FLESSIONI, 0);
        }

        //state (?)

        // textView.setText(tipologiaAllenamento);
        textView.setText(""+tempoPassato);
    }

    public void saveWorkout(View v){
        //Passaggi per il salvataggio nel database
        //da fare in un pop up che chiede se salvare o no i dati
        //oppure due bottoni con scritto: "Salva dati", "Torna alla home senza salvare"
        CustomerModel customerModel = null;

        try {
            if(tipologiaAllenamento.equals("Corsa")  || tipologiaAllenamento.equals("Camminata") || tipologiaAllenamento.equals("Ciclismo")){
                //uso il costruttore con chilometri, senza flessioni
                customerModel = new CustomerModel();
            }
            else{
                if (tipologiaAllenamento.equals("Pushup") || tipologiaAllenamento.equals("Freestyle")){
                    //uso il costruttore senza chilometri, con flessioni
                    customerModel = new CustomerModel();
                }
                else{
                    //uso il costruttore senza chilometri e flessioni
                    customerModel = new CustomerModel();
                }
            }
        }catch (Exception e){
            Toast.makeText(EndWorkoutActivity.this,
                    "Errore nell'inserimento dati", Toast.LENGTH_SHORT).show();
        }

        DatabaseHelper db = new DatabaseHelper(EndWorkoutActivity.this);
        boolean success = db.addWorkout(customerModel);
        if (success){
            Toast.makeText(EndWorkoutActivity.this,
                    "Inserimento effettuato con successo", Toast.LENGTH_SHORT).show();
        }

        //Una volta salvato l'allenamento posso tornare alla Home
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}