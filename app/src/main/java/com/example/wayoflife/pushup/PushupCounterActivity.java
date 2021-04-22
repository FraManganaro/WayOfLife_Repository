package com.example.wayoflife.pushup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wayoflife.Constants;
import com.example.wayoflife.R;
import com.example.wayoflife.ui.HomeActivity;
import com.example.wayoflife.ui.ProfileActivity;

public class PushupCounterActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private int count = 0;

    private TextView tvCounter;

    private Chronometer chronometer;
    private boolean isRunningChronometer;
    private long pauseOffset = 0;

//    private boolean justStarted = true;
//    private Calendar dateStarted;

    private long timeElapsed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushup_counter);

        tvCounter = findViewById(R.id.TVCounter);

        /** Sistema per interagire con i sensori */
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        /** Sensore di tipo PROSSIMITÀ */
        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        /** Attivazione del croometro */
        chronometer = findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        chronometer.start();
        isRunningChronometer = true;
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
//        if (justStarted) {
//            justStarted = false;
//            dateStarted = Calendar.getInstance();
//            return;
//        }

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
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    /**
     * Se l'utente vuole aggiungere delle felssioni manualmente ha la possibilità di farlo
     * Cliccando sullo schermo
     * @param v
     */
    public void increaseCounter(View v) {
        count += 1;

        tvCounter.setText(String.valueOf(count));
    }
    /**
     * Caso opposto, nel caso si volessero rimuovere dal contare delle flessioni non fatte
     * @param v
     */
    public void decreaseCounter(View v) {
        count -= 1;

        tvCounter.setText(String.valueOf(count));
    }

    /**
     * Allenamento finito, devo salvare le flessioni che sono state fatte e portare l'utente nella
     * pagina di overview.
     * @param v
     */
    public void stopWorkout(View v) {
//        PushUpData data = new PushUpData(getApplicationContext());
//        Calendar currentDate = Calendar.getInstance();
//
//        timeElapsed = currentDate.getTimeInMillis() -
//                dateStarted.getTimeInMillis();

//        try {
//            data.writeData(count, timeElapsed);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("FlessioniFatte", count);
        startActivity(intent);
    }

    /**
     * Metodo che permette di avviare e stoppare il chronometro toccando sul cronometro
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