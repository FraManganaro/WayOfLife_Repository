package com.example.wayoflife.workouts;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wayoflife.Constants;
import com.example.wayoflife.R;
import com.example.wayoflife.ui.HomeActivity;

import org.w3c.dom.Text;

public class PushupCounterActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private int count = -1;

    private String attivitaDiProvenienza;

    private TextView tvCounter;
    private TextView tvFreestyle;

    private ImageView playButton;
    private ImageView endButton;
    private ImageView minusButton;
    private ImageView freestyleButton;

    private Chronometer chronometer;
    private boolean isRunningChronometer;
    private long pauseOffset = 0;

    private long timeElapsed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushup_counter);

        attivitaDiProvenienza = getIntent().getStringExtra(Constants.ATTIVITA_RILEVATA);

        /** Sistema per interagire con i sensori */
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        /** Attivazione del cronometro */
        chronometer = findViewById(R.id.chronometer);

        if(attivitaDiProvenienza.equalsIgnoreCase("Freestyle")){
            chronometer.setBase(getIntent().getLongExtra(Constants.TEMPO_PASSATO, 0));
        } else
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);

        chronometer.start();
        isRunningChronometer = true;

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
    public void onSensorChanged(SensorEvent event) {
        if(isRunningChronometer) {
            int val = (int) event.values[0];

            /**
             * Il sensore di prossimità restituisce i cm di distanza dall'oggetto rilevato
             * Non ci interessa questo parametro, ci interessa che abbia rilevato qualcosa
             */
            if (val > 1)
                val = 1;

            count += val;

            tvCounter.setText(String.valueOf(count));
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}


    /**
     * Se l'utente vuole aggiungere delle felssioni manualmente ha la possibilità di farlo
     * Cliccando sullo schermo
     * @param v
     */
    public void increaseCounter(View v) {
        if(isRunningChronometer) {
            count += 1;

            tvCounter.setText(String.valueOf(count));
        }
    }
    /**
     * Caso opposto, nel caso si volessero rimuovere dal contare delle flessioni non fatte
     * @param v
     */
    public void decreaseCounter(View v) {
        if(isRunningChronometer) {
            if(count > 0) count -= 1;

            tvCounter.setText(String.valueOf(count));
        }
    }


    /**
     * Allenamento finito, devo salvare le flessioni che sono state fatte
     * In base a dove é stato richiamata l'attività devo passare cose diverse
     * @param v
     */
    public void stopWorkout(View v) {
        Intent intent;

        if(attivitaDiProvenienza.equalsIgnoreCase("Freestyle")) {
            intent = new Intent(getApplicationContext(), FreestyleActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), EndWorkoutActivity.class);
        }

        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("flessioni", count);
        intent.putExtra(Constants.ATTIVITA_RILEVATA, "Pushup");
        intent.putExtra(Constants.TEMPO_PASSATO, chronometer.getBase());

        startActivity(intent);
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

            pauseChronometer(v);
        } else {
            playButton.setImageDrawable(getDrawable(R.drawable.ic_pause));

            if(attivitaDiProvenienza.equalsIgnoreCase("Freestyle")){
                tvFreestyle.setVisibility(View.INVISIBLE);
                freestyleButton.setVisibility(View.INVISIBLE);
            } else
                endButton.setVisibility(View.INVISIBLE);

            minusButton.setVisibility(View.VISIBLE);

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