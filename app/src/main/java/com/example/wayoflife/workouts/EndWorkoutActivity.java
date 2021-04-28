package com.example.wayoflife.workouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wayoflife.DatabaseHelper;

import com.example.wayoflife.Constants;
import com.example.wayoflife.CustomerModel;
import com.example.wayoflife.DatabaseHelper;
import com.example.wayoflife.R;
import com.example.wayoflife.ui.HomeActivity;

public class EndWorkoutActivity<databaseHelper> extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_workout);

        TextView textView = findViewById(R.id.textView12);

        String message = getIntent().getStringExtra(Constants.ATTIVITA_RILEVATA);
        long tempoPassato = getIntent().getLongExtra(Constants.TEMPO_PASSATO, 0);

        textView.setText(message);
        textView.setText(""+tempoPassato);
    }

    public void saveWorkout(View v){
        //devo salvare l'allenamento

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //Passaggi per il salvataggio nel database
    //da fare in un pop up che chiede se salvare o no i dati
    //oppure due bottoni con scritto: "Salva dati", "Torna alla home senza salvare"
    CustomerModel customerModel;

    String tipologia = "Corsa";

    try {
        if(tipologia.equals("Corsa")  || tipologia.equals("Camminata") || tipologia.equals("Ciclismo")){
            //uso il costruttore con chilometri, senza flessioni
            customerModel = new CustomerModel();
        }
        else{
            if (tipologia.equals("Flessioni")){
                //uso il costruttore senza chilometri, con flessioni
                customerModel = new CustomerModel();
            }
            else{
                //uso il costruttore senza chilometri e flessioni
                customerModel = new CustomerModel();
            }
        }
    }catch (Exception e){
        Toast.makeText(EndWorkoutActivity.this, "Errore nell'inserimento dati", Toast.LENGTH_SHORT).show();
    }

    DatabaseHelper db = new DatabaseHelper(EndWorkoutActivity.this);
    boolean success = db.addWorkout(customerModel);

    if (success){
        Toast.makeText(EndWorkoutActivity.this, "Inserimento effettuato con successo", Toast.LENGTH_SHORT).show();
    }


}