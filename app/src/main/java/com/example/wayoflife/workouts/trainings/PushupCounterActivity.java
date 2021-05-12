package com.example.wayoflife.workouts.trainings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wayoflife.dialog.InfoDialog;
import com.example.wayoflife.util.Calories;
import com.example.wayoflife.util.Constants;
import com.example.wayoflife.R;
import com.example.wayoflife.workouts.EndWorkoutActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PushupCounterActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "PushupCounterActivity";

    private SensorManager sensorManager;
    private int pushupCounter;
    private int squat;

    private String attivitaDiProvenienza;

    private TextView tvCounter;
    private TextView tvFreestyle;
    private TextView caloriesTV;
    private FloatingActionButton playButton;
    private FloatingActionButton endButton;
    private FloatingActionButton minusButton;
    private ImageView freestyleButton;

    /** Gestione del cronometro */
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
        setContentView(R.layout.activity_pushup_counter);

        pushupCounter = -1;

        updateCalories = true;
        cycle = true;
        secondCounter = 0;
        calorie = 0;
        calorieRicevute = 0;

        attivitaDiProvenienza = getIntent().getStringExtra(Constants.ATTIVITA_RILEVATA);
        secondiRicevuti = getIntent().getIntExtra(Constants.TEMPO_IN_SECONDI, 0);

        /** Sistema per interagire con i sensori */
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        /** Attivazione del cronometro */
        chronometer = findViewById(R.id.chronometer);
        chronometer.start();
        isRunningChronometer = true;


        if(attivitaDiProvenienza.equalsIgnoreCase("Freestyle")){
            /** nel caso in cui provenga dall'allenamento Freestyle devo ripristinare calorie e tempo */
            chronometer.setBase(getIntent().getLongExtra(Constants.TEMPO_PASSATO, 0));
            calorieRicevute = getIntent().getIntExtra(Constants.CALORIE, 0);
            pushupCounter = getIntent().getIntExtra(Constants.FLESSIONI, 0) - 1;
            squat = getIntent().getIntExtra(Constants.SQUAT, 0);
        } else
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);


        caloriesTV = findViewById(R.id.caloriesTV);

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

        tvCounter = findViewById(R.id.TVCounter);
        tvFreestyle = findViewById(R.id.tvFreestyle);

        playButton = findViewById(R.id.buttonPausePlay);
        endButton = findViewById(R.id.endButton);
        minusButton = findViewById(R.id.minusButton);

        freestyleButton = findViewById(R.id.freestyleButton);

        endButton.setVisibility(View.INVISIBLE);
        tvFreestyle.setVisibility(View.INVISIBLE);
        freestyleButton.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onResume() {
        super.onResume();

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    public void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cycle = false;
        updateCalories = false;
    }
    @Override
    public void onBackPressed() {
        InfoDialog infoDialog = new InfoDialog();
        infoDialog.setType("workout");
        infoDialog.show(getSupportFragmentManager(), "Dialog informativo");
        cycle = false;
        updateCalories = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(isRunningChronometer) {
            int val = (int) event.values[0];

            /**
             * Il sensore di prossimità restituisce i cm di distanza dall'oggetto rilevato
             * Non ci interessa questo parametro, ci interessa che abbia rilevato qualcosa
             */
            if (val > 1)
                val = 1;

            pushupCounter += val;

            tvCounter.setText(String.valueOf(pushupCounter));
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {/** non implementato */}


    /** Calorie consumate da questa attività */
    private void updateCalories(){
        SharedPreferences sharedPref = getSharedPreferences(
                Constants.PROFILE_INFO_FILENAME,
                Context.MODE_PRIVATE);

        double peso = Double.parseDouble(sharedPref.getString(
                Constants.PESO, "0"));

        calorie = (int) (Calories.FLESSIONI * peso * secondCounter)/3600;
    }


    /**
     * Se l'utente vuole aggiungere delle felssioni manualmente ha la possibilità di farlo
     * Cliccando sullo schermo
     * @param v
     */
    public void increaseCounter(View v) {
        if(isRunningChronometer) {
            pushupCounter += 1;

            tvCounter.setText(String.valueOf(pushupCounter));
        }
    }
    /**
     * Caso opposto, nel caso si volessero rimuovere dal contare delle flessioni non fatte
     * @param v
     */
    public void decreaseCounter(View v) {
        if(isRunningChronometer) {
            if(pushupCounter > 0) pushupCounter -= 1;

            tvCounter.setText(String.valueOf(pushupCounter));
        }
    }


    /**
     * Allenamento finito, devo salvare le flessioni che sono state fatte
     * In base a dove é stato richiamata l'attività devo passare cose diverse
     * @param v
     */
    public void stopWorkout(View v) {
        cycle = false;
        updateCalories = false;

        Intent intent;

        if(attivitaDiProvenienza.equalsIgnoreCase("Freestyle")) {
            intent = new Intent(getApplicationContext(), TrainingActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), EndWorkoutActivity.class);
        }

        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra(Constants.FLESSIONI, pushupCounter);
        intent.putExtra(Constants.ATTIVITA_RILEVATA, "Pushup");
        intent.putExtra(Constants.TEMPO_PASSATO, chronometer.getBase());
        intent.putExtra(Constants.TEMPO_IN_SECONDI, secondCounter + secondiRicevuti);
        intent.putExtra(Constants.CALORIE, calorie + calorieRicevute);
        intent.putExtra(Constants.SQUAT, squat);

        startActivity(intent);
        finish();
    }

    /**
     * Metto in pausa allenamento e cambio le icone che vengono visualizzate
     * @param v
     */
    public void pauseWorkout(View v) {
        if(isRunningChronometer) {
            playButton.setImageDrawable(getDrawable(R.drawable.ic_play));

            if(attivitaDiProvenienza.equalsIgnoreCase("Freestyle")){
                tvFreestyle.setVisibility(View.VISIBLE);
                freestyleButton.setVisibility(View.VISIBLE);
            } else
                endButton.setVisibility(View.VISIBLE);

            minusButton.setVisibility(View.INVISIBLE);

            updateCalories = false;
            pauseChronometer(v);

        } else {
            playButton.setImageDrawable(getDrawable(R.drawable.ic_pause));

            if(attivitaDiProvenienza.equalsIgnoreCase("Freestyle")){
                tvFreestyle.setVisibility(View.INVISIBLE);
                freestyleButton.setVisibility(View.INVISIBLE);
            } else
                endButton.setVisibility(View.INVISIBLE);

            minusButton.setVisibility(View.VISIBLE);

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
}