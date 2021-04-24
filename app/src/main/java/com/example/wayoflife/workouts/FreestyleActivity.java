package com.example.wayoflife.workouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wayoflife.R;
import com.example.wayoflife.ui.HomeActivity;

public class FreestyleActivity extends AppCompatActivity {

    private ImageView playButton;
    private ImageView endButton;

    private Chronometer chronometer;
    private boolean isRunningChronometer;
    private long pauseOffset = 0;

    private long timeElapsed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freestyle);

        String message = getIntent().getStringExtra("attivita riconosciuta");

        /** Attivazione del croometro */
        chronometer = findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        chronometer.start();
        isRunningChronometer = true;


        playButton = findViewById(R.id.buttonPausePlay);
        endButton = findViewById(R.id.endButton);

        endButton.setVisibility(View.INVISIBLE);
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
        startActivity(intent);
    }
    public void pauseWorkout(View v) {
//        if(playButton.getText().toString().equalsIgnoreCase("Pausa")) {
//            playButton.setText(R.string.play);
//            endButton.setVisibility(View.VISIBLE);
//            pauseChronometer(v);
//        } else {
//            playButton.setText(R.string.pausa);
//            endButton.setVisibility(View.INVISIBLE);
//        }
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