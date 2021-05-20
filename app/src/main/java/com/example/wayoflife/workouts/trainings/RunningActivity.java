package com.example.wayoflife.workouts.trainings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.wayoflife.util.Calories;
import com.example.wayoflife.util.Constants;
import com.example.wayoflife.R;
import com.example.wayoflife.dialog.InfoDialog;
import com.example.wayoflife.workouts.ui.EndWorkoutActivity;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class RunningActivity extends AppCompatActivity {

    private final String TAG = "RunningActivity";

    private ConstraintLayout constraintLayout;
    private FloatingActionButton playButton;
    private FloatingActionButton endButton;
    private TextView caloriesTV;
    private TextView chilometriTV;

    private String attivitaDiProvenienza;

    /** Variabili per gestione cronometro */
    private Chronometer chronometer;
    private boolean isRunningChronometer;
    private long pauseOffset = 0;
    private long timeElapsed = 0;

    /** Variabili per la mappa */
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient client;

    /** Variabili per calcolo distanza */
    private LatLng l1;
    private LatLng l2;
    private boolean stopped;
    private float distance;

    /** Disegnare linee sulla mappa */
    private PolylineOptions polylineOptions;
    private Polyline polyline;

    /** Gestione dei permessi per il GPS */
    private LocationManager locationManager;
    private boolean gpsStatus;
    private Context context;

    /** Variabili per aggiornamento posizione e calorie */
    private Thread t;
    private boolean continueFindLocation;
    private boolean cycle;
    private int secondCounter;
    private int calorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        manageNotification();

        cycle = false;
        continueFindLocation = false;
        secondCounter = 0;
        calorie = 0;

        distance = 0;
        stopped = false;

        polylineOptions = new PolylineOptions();

        context = getApplicationContext();

        /** Collegamento con layout */
        attivitaDiProvenienza = getIntent().getStringExtra(Constants.ATTIVITA_RILEVATA);
        constraintLayout = findViewById(R.id.clPrincipaleRunning);
        playButton = findViewById(R.id.buttonPausePlay);
        endButton = findViewById(R.id.endButton);
        caloriesTV = findViewById(R.id.caloriesTV);
        chilometriTV = findViewById(R.id.chilometriTV);
        endButton.setVisibility(View.INVISIBLE);

        /** Assegnazione della mappa */
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMaps);
        client = LocationServices.getFusedLocationProviderClient(this);

        /** Controllo lo stato del GPS se attivo o disattivo */
        checkGpsStatus();

        /** Attivazione del cronometro */
        chronometer = findViewById(R.id.chronometer);
        if(gpsStatus) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            isRunningChronometer = true;
        } else { infoDialog(); }

        /** Controllo i permessi per la posizione */
        if (ActivityCompat.checkSelfPermission(RunningActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(gpsStatus) getFirstCurrentLocation(); //da chiamare quando ho il permesso del GPS
        } else {
            ActivityCompat.requestPermissions(RunningActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        if(gpsStatus) {
            cycle = true;
            continueFindLocation = true;
            startTraining();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cycle = false;
        continueFindLocation = false;
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart:");

        if(!cycle && !continueFindLocation) {

            checkGpsStatus();

            if(gpsStatus) {
                supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.googleMaps);
                client = LocationServices.getFusedLocationProviderClient(this);
                getFirstCurrentLocation();
            }

            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            isRunningChronometer = true;
            startTraining();
        }
    }
    @Override
    public void onBackPressed() {
        InfoDialog infoDialog = new InfoDialog();
        infoDialog.setType("workout");
        infoDialog.show(getSupportFragmentManager(), "Dialog informativo");
        cycle = false;
        continueFindLocation = false;
    }
    /** Modifico variabile che  gestisce le notifiche */
    private void manageNotification(){

        SharedPreferences sharedPref = getSharedPreferences(
                Constants.HOME_INFO_FILENAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putBoolean(Constants.NOTIFICATION_STATUS, false);

        editor.apply();
    }

    /**
     * Metodo che permette di avviare il conteggio dei secondi e di aggiornare la posizine dinamicamente
     */
    private void startTraining(){
        /** Aggiorno la posizione ogni 3 secondi (con anche aggiornamento delle calorie) */
        t = new Thread(() -> {
            while (cycle) {
                while (continueFindLocation) {
                    try {
                        Thread.sleep(3000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "Secondi = " + secondCounter);
                                Log.d(TAG, "Calorie = " + calorie);

                                secondCounter += 3;
                                updateCalories();

                                caloriesTV.setText(calorie + " kcal");

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
    }

    /** Metodo che gestisce il Dialog contenente le informazioni sull'ActivityTransition */
    public void infoDialog(){
        InfoDialog infoDialog = new InfoDialog();
        infoDialog.setType("running");
        infoDialog.show(getSupportFragmentManager(), "Dialog informativo");
    }

    /**
     * Per ogni attività calcolo quante calorie vengono consumate
     */
    private void updateCalories(){
        SharedPreferences sharedPref = getSharedPreferences(
                Constants.PROFILE_INFO_FILENAME,
                Context.MODE_PRIVATE);

        double peso = Double.parseDouble(sharedPref.getString(
                Constants.PESO, "0"));

        switch(attivitaDiProvenienza){
            case "Ciclismo":
                calorie = (int) (Calories.CICLISMO * peso * secondCounter)/3600;
                break;
            case "Camminata":
                calorie = (int) (Calories.CAMMINATA * peso * secondCounter)/3600;
                break;
            case "Corsa":
                calorie = (int) (Calories.CORSA * peso * secondCounter)/3600;
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
        continueFindLocation = false;

        Intent intent = new Intent(getApplicationContext(), EndWorkoutActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra(Constants.ATTIVITA_RILEVATA, attivitaDiProvenienza);
        intent.putExtra(Constants.TEMPO_PASSATO, chronometer.getBase());
        intent.putExtra(Constants.TEMPO_IN_SECONDI, secondCounter);
        intent.putExtra(Constants.CALORIE, calorie);

        DecimalFormat numberFormat = new DecimalFormat("0.00");
        intent.putExtra(Constants.CHILOMETRI, "" + numberFormat.format(distance));

        startActivity(intent);
        finish();
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
            stopped = true;
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
                getFirstCurrentLocation();
            }
        }
    }
    private void getFirstCurrentLocation() {
        @SuppressLint("MissingPermission") Task<Location> task = client.getLastLocation();

        checkGpsStatus();
        if(!gpsStatus) return;

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        if (location != null) {
                            //Trovo latitudine e longitudine
                            l1 = new LatLng(location.getLatitude(),
                                    location.getLongitude());

                            //Creo un marker da mettere sulla mappa
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(l1)
                                    .title("Partenza");

                            //Creo animazione per zoommare sul punto prestabilito
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l1, 12.0f));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l1, 18.5f));

                            //Aggiungo il marker alla mappa
                            googleMap.addMarker(markerOptions);

                            polylineOptions.add(l1);
                        }
                    }
                });
            }
        });
    }
    private void getCurrentLocation() {
        @SuppressLint("MissingPermission") Task<Location> task = client.getLastLocation();

        checkGpsStatus();
        if(!gpsStatus) return;

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        if(location != null) {
                            l2 = new LatLng(location.getLatitude(),
                                    location.getLongitude());

                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(l2)
                                    .title("");

                            if (stopped) {
                                l1 = l2;
                                stopped = false;
                            }

                            if (!(l1.equals(l2))) {
                                float[] results = new float[1];
                                Location.distanceBetween(l1.latitude, l1.longitude, l2.latitude, l2.longitude, results);
                                distance += results[0] / 1000; /** viene restituito il valore in metri */

                                polylineOptions.add(l2).color(R.color.red);
                                polyline = googleMap.addPolyline(polylineOptions);

                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(l2));

                                Log.d(TAG, "distance (float) = " + distance);
                                DecimalFormat numberFormat = new DecimalFormat("0.00");
                                chilometriTV.setText(numberFormat.format(distance) + " km");

                                l1 = l2;
                            }
                        }
                    }
                });
            }
        });
    }

    public void checkGpsStatus() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}