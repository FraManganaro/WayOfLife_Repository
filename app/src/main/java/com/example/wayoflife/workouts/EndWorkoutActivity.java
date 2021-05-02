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
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EndWorkoutActivity<databaseHelper> extends AppCompatActivity {

    private final String TAG = "EndWorkoutActivity";

    private String nome;
    private String data;
    private String durata;
    private double durataInSecondi;
    private double durataInMinuti;
    private double durataInOre;

    private String tipologiaAllenamento;
    private int calorie;
    private float chilometri;
    private int n_flessioni;
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_workout);

        TextView tvData = findViewById(R.id.textView12);
        TextView tvDurata = findViewById(R.id.textView13);
        TextView tvCalorie = findViewById(R.id.textView14);
        TextView tvChilometri = findViewById(R.id.textView15);
        TextView tvFlessioni = findViewById(R.id.textView16);


        /** Recupero data odierna */
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy"); // getting date in this format
        data = df.format(date.getTime());
        tvData.setText(data);

        tipologiaAllenamento = getIntent().getStringExtra(Constants.ATTIVITA_RILEVATA);

        durataInSecondi = 1.0 *
                getIntent().getIntExtra(Constants.TEMPO_IN_SECONDI, 0);
//        durataInSecondi = 2432; -> prova
        manageTime();
        tvDurata.setText(durata);

        calorie = getIntent().getIntExtra(Constants.CALORIE, 0);
        tvCalorie.setText(calorie + " kcal");

        chilometri = getIntent().getIntExtra(Constants.CHILOMETRI, 0);
        tvChilometri.setText(chilometri + " km");

        n_flessioni = getIntent().getIntExtra(Constants.FLESSIONI, 0);
        tvFlessioni.setText(n_flessioni + " Flessioni");

        //state (?)
    }

    public void saveWorkout(View v){
        TextInputEditText nomeET = findViewById(R.id.etNome);
        nome = nomeET.getText().toString();
        if(nome.equalsIgnoreCase(""))
            nome = "Allenamento " + tipologiaAllenamento;

        CustomerModel customerModel = null;

        try {
            if(tipologiaAllenamento.equals("Corsa")  || tipologiaAllenamento.equals("Camminata") || tipologiaAllenamento.equals("Ciclismo")){
                //uso il costruttore con chilometri, senza flessioni
                customerModel = new CustomerModel(nome, data, durata, chilometri, tipologiaAllenamento, calorie, state);
            }
            else{
                if (tipologiaAllenamento.equals("Pushup") || tipologiaAllenamento.equals("Freestyle")){
                    //uso il costruttore senza chilometri, con flessioni
                    customerModel = new CustomerModel(nome, data, durata, tipologiaAllenamento, calorie, n_flessioni, state);
                }
                else{
                    //uso il costruttore senza chilometri e flessioni
                    customerModel = new CustomerModel(nome, data, durata, tipologiaAllenamento, calorie, state);
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
    public void returnHome(View v){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * Metodo che converte il tempo in secondi nel formato:
     *     00:00:00
     * ore:minuti:secondi
     */
    private void manageTime(){
        int tempO, tempM, tempS;

        durataInMinuti = durataInSecondi/60;
        durataInOre = durataInMinuti/60;

        if(durataInOre > 1.0) {
            tempO = (int) durataInOre;
            if(tempO < 10)
                durata = "0" + tempO + ":";
            else
                durata = tempO + ":";
//            durata = tempO + ":";

            tempO *= 60;
            tempM = (int) durataInMinuti;
            tempM -= tempO;
            if(tempM < 10)
                durata += "0" + tempM + ":";
            else
                durata += tempM + ":";
//            durata += tempM + ":";

            tempM *= 60;
            tempS = (int) durataInSecondi;
            tempS -= tempM + 3600;
            if(tempS < 10)
                durata += "0" + tempS + " ore";
            else
                durata += tempS + " ore";
//            durata += tempS;

        }else {
            if (durataInMinuti < 1.0) {
                tempS = (int) durataInSecondi;
                if(tempS < 10)
                    durata = "0" + tempS + " secondi";
                else
                    durata = tempS + " secondi";

//                durata = tempS + " secondi";
            }else {
                tempM = (int) durataInMinuti;
                if(tempM < 10)
                    durata = "0" + tempM + ":";
                else
                    durata = tempM + ":";

                tempM *= 60;
                tempS = (int) durataInSecondi;
                tempS -= tempM;
                if(tempS < 10)
                    durata += "0" + tempS + " minuti";
                else
                    durata += tempS + " minuti";
//                durata += tempS;
            }
        }
    }

}