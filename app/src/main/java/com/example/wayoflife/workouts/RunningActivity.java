package com.example.wayoflife.workouts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.example.wayoflife.Constants;
import com.example.wayoflife.R;

public class RunningActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;

    private String attivitaDiProvenienza;

    private ImageView playButton;
    private ImageView endButton;

    private Chronometer chronometer;
    private boolean isRunningChronometer;
    private long pauseOffset = 0;

    private long timeElapsed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        attivitaDiProvenienza = getIntent().getStringExtra(Constants.ATTIVITA_RILEVATA);

        /** Attivazione del cronometro */
        chronometer = findViewById(R.id.chronometer);

        chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);

        chronometer.start();
        isRunningChronometer = true;

        constraintLayout = findViewById(R.id.clPrincipaleRunning);

        playButton = findViewById(R.id.buttonPausePlay);
        endButton = findViewById(R.id.endButton);


        /**
         * Probabilmente da rimuovere
         */
        switch(attivitaDiProvenienza){
            case "Corsa":
                constraintLayout.setBackground(getDrawable(R.drawable.image_background_running));
                break;
            case "Camminata":
                constraintLayout.setBackground(getDrawable(R.drawable.image_background_walking));
                break;
            case "Ciclismo":
                constraintLayout.setBackground(getDrawable(R.drawable.image_background_cycling));
                break;
            default:
                constraintLayout.setBackground(getDrawable(R.drawable.background_white_corners));
                break;
        }

        endButton.setVisibility(View.INVISIBLE);

    }

    /**
     * Allenamento finito, devo passare alla classe di review le informazioni da salvare
     * @param v
     */
    public void stopWorkout(View v) {
        Intent intent = new Intent(getApplicationContext(), EndWorkoutActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra(Constants.ATTIVITA_RILEVATA, attivitaDiProvenienza);
        intent.putExtra(Constants.TEMPO_PASSATO, chronometer.getBase());

        //devo aggiungere altri parametri da passare

        startActivity(intent);
    }
    /**
     * Metto in pausa il cronometro e cambio le icone per finire l'allenamento
     * @param v
     */
    public void pauseWorkout(View v) {
        if(isRunningChronometer) {
            playButton.setImageDrawable(getDrawable(R.drawable.ic_play));

            endButton.setVisibility(View.VISIBLE);

            pauseChronometer(v);
        } else {
            playButton.setImageDrawable(getDrawable(R.drawable.ic_pause));

            endButton.setVisibility(View.INVISIBLE);

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