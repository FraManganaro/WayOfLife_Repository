package com.example.wayoflife.workouts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.example.wayoflife.Constants;
import com.example.wayoflife.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class RunningActivity extends AppCompatActivity {

    private final String TAG = "RunningActivity";

    private ConstraintLayout constraintLayout;
    private ImageView playButton;
    private ImageView endButton;

    private String attivitaDiProvenienza;
    private int calorie;

    private Chronometer chronometer;
    private boolean isRunningChronometer;
    private long pauseOffset = 0;
    private long timeElapsed = 0;

    /** Variabili per la mappa */
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient client;

    private Thread t;
    private boolean continueFindLocation;
    private boolean cycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        cycle = true;
        continueFindLocation = true;

        attivitaDiProvenienza = getIntent().getStringExtra(Constants.ATTIVITA_RILEVATA);

        /** Attivazione del cronometro */
        chronometer = findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);

        chronometer.start();
        isRunningChronometer = true;

        /** Assegnazione della mappa */
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMaps);

        client = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(RunningActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation(); //da chiamare quando ho il permesso del GPS
        } else {
            ActivityCompat.requestPermissions(RunningActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        t = new Thread(() -> {
            while(cycle) {
                while (continueFindLocation) {
                    try {
                        Thread.sleep(2000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "run");

                                getCurrentLocation();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t.start();

        constraintLayout = findViewById(R.id.clPrincipaleRunning);

        playButton = findViewById(R.id.buttonPausePlay);
        endButton = findViewById(R.id.endButton);

        /**
         * Probabilmente da rimuovere
         */
//        switch (attivitaDiProvenienza) {
//            case "Corsa":
//                constraintLayout.setBackground(getDrawable(R.drawable.image_background_running));
//                break;
//            case "Camminata":
//                constraintLayout.setBackground(getDrawable(R.drawable.image_background_walking));
//                break;
//            case "Ciclismo":
//                constraintLayout.setBackground(getDrawable(R.drawable.image_background_cycling));
//                break;
//            default:
//                constraintLayout.setBackground(getDrawable(R.drawable.background_white_corners));
//                break;
//        }

        endButton.setVisibility(View.INVISIBLE);
    }

    /**
     * Allenamento finito, devo passare alla classe di review le informazioni da salvare
     * @param v
     */
    public void stopWorkout(View v) {
        cycle = false;
        continueFindLocation = false;

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
        if (isRunningChronometer) {
            playButton.setImageDrawable(getDrawable(R.drawable.ic_play));

            endButton.setVisibility(View.VISIBLE);

            continueFindLocation = false;
            pauseChronometer(v);
        } else {
            playButton.setImageDrawable(getDrawable(R.drawable.ic_pause));

            endButton.setVisibility(View.INVISIBLE);

            continueFindLocation = true;
            pauseChronometer(v);
        }
    }

    /**
     * Metodo che permette di avviare e stoppare il cronometro
     * @param v
     */
    public void pauseChronometer(View v) {
        if (isRunningChronometer) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();

            isRunningChronometer = false;
        } else {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();

            isRunningChronometer = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 44){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }
    private void getCurrentLocation() {
        @SuppressLint("MissingPermission") Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        //Trovo latitudine e longitudine
                        LatLng latLng = new LatLng(location.getLatitude(),
                                location.getLongitude());

                        //Creo un marker da mettere sulla mappa
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                                .title("Io sono qui");

                        //Creo animazione per zoommare sul punto prestabilito
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                        //Aggiungo il marker alla mappa
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });
    }

}