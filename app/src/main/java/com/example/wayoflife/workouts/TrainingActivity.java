package com.example.wayoflife.workouts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wayoflife.Calories;
import com.example.wayoflife.Constants;
import com.example.wayoflife.R;

import java.util.concurrent.BlockingDeque;

public class TrainingActivity extends AppCompatActivity {

    private final String TAG = "FreestyleActivity";

    private String attivitaDiProvenienza;
    private int flessioni;

    private ConstraintLayout constraintLayout;
    private TextView tvPushup;
    private TextView caloriesTV;
    private ImageView playButton;
    private ImageView endButton;
    private ImageView pushupButton;

    /** Per gestione del cronometro */
    private Chronometer chronometer;
    private boolean isRunningChronometer;
    private long pauseOffset = 0;

    /** Per gestione delle calorie */
    private Thread t;
    private boolean cycle;
    private boolean updateCalories;
    private int secondCounter;
    private int secondiRicevuti;
    private int calorie;
    private int calorieRicevute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freestyle);

        /** Inizializzo variabili per le calorie */
        cycle = true;
        updateCalories = true;
        secondCounter = 0;
        secondiRicevuti = 0;
        calorie = 0;
        calorieRicevute = 0;

        attivitaDiProvenienza = getIntent().getStringExtra(Constants.ATTIVITA_RILEVATA);
        secondiRicevuti = getIntent().getIntExtra(Constants.TEMPO_IN_SECONDI, 0);

        /** Attivazione del cronometro */
        chronometer = findViewById(R.id.chronometer);
        chronometer.start();
        isRunningChronometer = true;


        if(attivitaDiProvenienza.equalsIgnoreCase("Pushup")){
            /** nel caso in cui provenga dall'allenamento Pushup devo ripristinare calorie e tempo */
            chronometer.setBase(getIntent().getLongExtra(Constants.TEMPO_PASSATO, 0));
            calorieRicevute = getIntent().getIntExtra(Constants.CALORIE, 0);
            flessioni = getIntent().getIntExtra(Constants.FLESSIONI, 0);
        } else {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        }


        constraintLayout = findViewById(R.id.clPrincipaleFreestyle);

        playButton = findViewById(R.id.buttonPausePlay);
        endButton = findViewById(R.id.endButton);
        pushupButton = findViewById(R.id.pushupButton);

        tvPushup = findViewById(R.id.tvPushup);
        caloriesTV = findViewById(R.id.caloriesTV);

        endButton.setVisibility(View.INVISIBLE);

        /** Nel caso in cui si avvii un workout generico */
        if(!attivitaDiProvenienza.equalsIgnoreCase("Freestyle") &&
                !attivitaDiProvenienza.equalsIgnoreCase("Pushup")){
            pushupButton.setVisibility(View.INVISIBLE);
            tvPushup.setVisibility(View.INVISIBLE);
            constraintLayout.setBackground(getDrawable(R.drawable.prova3)); //DA CAMBIARE
        }


        /** thread che ogni secondo aggiorna le calorie bruciate durante ogni attività */
        t = new Thread(() -> {
            while(cycle) {
                while(updateCalories) {
                    try {
                        Thread.sleep(1000); //ogni secondo

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "Secondi = " + secondCounter);
                                Log.d(TAG, "Calorie = " + calorie + calorieRicevute);

                                secondCounter++;
                                updateCalories();

                                caloriesTV.setText(calorie + calorieRicevute + " kcal");
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cycle = false;
        updateCalories = false;
    }

    /**
     * Per ogni attività calcolo quante calorie vengono consumate
     */
    private void updateCalories(){
        SharedPreferences sharedPref = getSharedPreferences(
                Constants.PROFILE_INFO_FILENAME,
                Context.MODE_PRIVATE);

        int peso = Integer.parseInt(sharedPref.getString(
                Constants.PESO, "0"));

        switch(attivitaDiProvenienza){
            case "Freestyle":
                calorie = (int) (Calories.FREESTYLE * peso * secondCounter)/3600;
                break;
            case "Basket":
                calorie = (int) (Calories.BASKET * peso * secondCounter)/3600;
                break;
            case "Calcio":
                calorie = (int) (Calories.CALCIO * peso * secondCounter)/3600;
                break;
            case "Nuoto":
                calorie = (int) (Calories.NUOTO * peso * secondCounter)/3600;
                break;
            case "Scalini":
                calorie = (int) (Calories.SCALINI * peso * secondCounter)/3600;
                break;
            case "Tennis":
                calorie = (int) (Calories.TENNIS * peso * secondCounter)/3600;
                break;
            default:
                Log.d(TAG, "Errore in updateCalories");
                break;
        }
    }


    /**
     * Allenamento finito, devo passare alla classe di review le informazioni da salvare
     * @param v
     */
    public void stopWorkout(View v) {
        cycle = false;
        updateCalories = false;

        Intent intent = new Intent(getApplicationContext(), EndWorkoutActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        /** scelgo quale attività passare come parametro extra */
        if(attivitaDiProvenienza.equalsIgnoreCase("Freestyle") ||
                attivitaDiProvenienza.equalsIgnoreCase("Pushup")){

            intent.putExtra(Constants.ATTIVITA_RILEVATA,  "Freestyle");

        } else intent.putExtra(Constants.ATTIVITA_RILEVATA, attivitaDiProvenienza);

        intent.putExtra(Constants.TEMPO_PASSATO, chronometer.getBase());
        intent.putExtra(Constants.TEMPO_IN_SECONDI, secondCounter + secondiRicevuti);
        intent.putExtra(Constants.CALORIE, calorie + calorieRicevute);
        intent.putExtra(Constants.FLESSIONI, flessioni);

        startActivity(intent);
        finish();
    }
    /**
     * Metto in pausa il cronometro e cambio le icone per finire l'allenamento
     * @param v
     */
    public void pauseWorkout(View v) {
        if(isRunningChronometer) {
            playButton.setImageDrawable(getDrawable(R.drawable.ic_play));

            endButton.setVisibility(View.VISIBLE);

            if(attivitaDiProvenienza.equalsIgnoreCase("freestyle") ||
                    attivitaDiProvenienza.equalsIgnoreCase("pushup")) {
                pushupButton.setVisibility(View.INVISIBLE);
                tvPushup.setVisibility(View.INVISIBLE);
            }

            updateCalories = false;
            pauseChronometer(v);

        } else {
            playButton.setImageDrawable(getDrawable(R.drawable.ic_pause));

            endButton.setVisibility(View.INVISIBLE);

            if(attivitaDiProvenienza.equalsIgnoreCase("freestyle") ||
                    attivitaDiProvenienza.equalsIgnoreCase("pushup")) {
                pushupButton.setVisibility(View.VISIBLE);
                tvPushup.setVisibility(View.VISIBLE);
            }

            updateCalories = true;
            pauseChronometer(v);
        }
    }
    /**
     * Metodo che permette di avviare e stoppare il cronometro
     * @param v
     */
    public void pauseChronometer(View v){
        if(isRunningChronometer) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();

            isRunningChronometer = false;
        } else {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();

            isRunningChronometer = true;
        }
    }

    /**
     * Controllo dell'icona per passare al contatore delle flessioni
     * Devo passare il tempo passato, le kcal bruciate e l'attività di arrivo
     * @param v
     */
    public void goToPushupCounter(View v){
        Intent intent = new Intent(getApplicationContext(), PushupCounterActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra(Constants.ATTIVITA_RILEVATA, "Freestyle");
        intent.putExtra(Constants.TEMPO_PASSATO, chronometer.getBase());
        intent.putExtra(Constants.TEMPO_IN_SECONDI, secondCounter + secondiRicevuti);
        intent.putExtra(Constants.CALORIE, calorie + calorieRicevute);
        intent.putExtra(Constants.FLESSIONI, flessioni);

        pauseWorkout(v);

        startActivity(intent);
    }
}