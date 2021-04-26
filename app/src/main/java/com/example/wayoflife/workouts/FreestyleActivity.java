package com.example.wayoflife.workouts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wayoflife.Constants;
import com.example.wayoflife.R;

public class FreestyleActivity extends AppCompatActivity {

    private final String TAG = "FreestyleActivity";

    private ConstraintLayout constraintLayout;

    private String attivitaDiProvenienza;

    private ImageView playButton;
    private ImageView endButton;
    private ImageView pushupButton;

    private TextView textView;

    private Chronometer chronometer;
    private boolean isRunningChronometer;
    private long pauseOffset = 0;

    private long timeElapsed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freestyle);

        attivitaDiProvenienza = getIntent().getStringExtra(Constants.ATTIVITA_RILEVATA);

        /** Attivazione del cronometro */
        chronometer = findViewById(R.id.chronometer);

        if(attivitaDiProvenienza.equalsIgnoreCase("Pushup")) {
            chronometer.setBase(getIntent().getLongExtra(Constants.TEMPO_PASSATO, 0));
        } else
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);

        chronometer.start();
        isRunningChronometer = true;

        constraintLayout = findViewById(R.id.clPrincipaleFreestyle);

        playButton = findViewById(R.id.buttonPausePlay);
        endButton = findViewById(R.id.endButton);

        pushupButton = findViewById(R.id.pushupButton);
        textView = findViewById(R.id.tvPushup);

        if(!attivitaDiProvenienza.equalsIgnoreCase("freestyle") &&
                !attivitaDiProvenienza.equalsIgnoreCase("pushup")){
            pushupButton.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            constraintLayout.setBackground(getDrawable(R.drawable.background_white_corners)); //DA CAMBIARE
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

        if(attivitaDiProvenienza.equalsIgnoreCase("Freestyle") ||
                attivitaDiProvenienza.equalsIgnoreCase("Pushup")){

            intent.putExtra(Constants.ATTIVITA_RILEVATA,  "Freestyle");
        } else {
            intent.putExtra(Constants.ATTIVITA_RILEVATA, attivitaDiProvenienza);
        }

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

            if(attivitaDiProvenienza.equalsIgnoreCase("freestyle") ||
                    attivitaDiProvenienza.equalsIgnoreCase("pushup")) {
                pushupButton.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
            }

            pauseChronometer(v);
        } else {
            playButton.setImageDrawable(getDrawable(R.drawable.ic_pause));

            endButton.setVisibility(View.INVISIBLE);

            if(attivitaDiProvenienza.equalsIgnoreCase("freestyle") ||
                    attivitaDiProvenienza.equalsIgnoreCase("pushup")) {
                pushupButton.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
            }

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
     * @param v
     */
    public void goToPushupCounter(View v){
        Intent intent = new Intent(getApplicationContext(), PushupCounterActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra(Constants.ATTIVITA_RILEVATA, "Freestyle");
        intent.putExtra(Constants.TEMPO_PASSATO, chronometer.getBase());

        /** devo mettere come extra anche il tempo passato, le kcal */

        pauseWorkout(v);

        startActivity(intent);
    }
}