package com.example.wayoflife.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.wayoflife.Constants;
import com.example.wayoflife.R;
import com.example.wayoflife.workouts.TrainingActivity;
import com.example.wayoflife.workouts.PushupCounterActivity;
import com.example.wayoflife.workouts.RunningActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;

public class DashboardActivity extends AppCompatActivity {

    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        /**
         * Controllore della navigation bar
         */
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.allenamento);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return false;

                    case R.id.allenamento:

                    case R.id.profilo:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return false;
                }
                return false;
            }
        });

        RelativeLayout rlEW = findViewById(R.id.rlExtraWorkout);
        Intent intent = new Intent(getApplicationContext(), TrainingActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        rlEW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(DashboardActivity.this,
                        R.style.BottomSheetTheme);

                View sheetView = LayoutInflater.from(
                        getApplicationContext()).inflate(R.layout.workout_extra_bottom_sheet,
                        (ViewGroup) findViewById(R.id.workoutBottomSheet));

                /** Gestione dell'allenamento basket */
                sheetView.findViewById(R.id.basket).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                intent.putExtra(Constants.ATTIVITA_RILEVATA, "Basket");
                                startActivity(intent);

                                bottomSheetDialog.dismiss();
                            }
                        });
                /** Gestione dell'allenamento calcio */
                sheetView.findViewById(R.id.calcio).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                intent.putExtra(Constants.ATTIVITA_RILEVATA, "Calcio");
                                startActivity(intent);

                                bottomSheetDialog.dismiss();
                            }
                        });
                /** Gestione dell'allenamento Nuoto */
                sheetView.findViewById(R.id.nuoto).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                intent.putExtra(Constants.ATTIVITA_RILEVATA, "Nuoto");
                                startActivity(intent);

                                bottomSheetDialog.dismiss();
                            }
                        });
                /** Gestione dell'allenamento Scalini */
                sheetView.findViewById(R.id.scalini).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                intent.putExtra(Constants.ATTIVITA_RILEVATA, "Scalini");
                                startActivity(intent);

                                bottomSheetDialog.dismiss();
                            }
                        });
                /** Gestione dell'allenamento Tennis */
                sheetView.findViewById(R.id.tennis).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                intent.putExtra(Constants.ATTIVITA_RILEVATA, "Tennis");
                                startActivity(intent);

                                bottomSheetDialog.dismiss();
                            }
                        });

                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
            }
        });

        /** Gestisco il bottone che fornisce le informazioni all'utente */
        Chip chip = findViewById(R.id.chipInfo);
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { infoDialog(); }
        });
    }

    /**
     * Premendo il RelativeLayout vado al contatore di flessioni
     * @param v
     */
    public void goToPushupCounter(View v){
        Intent intent = new Intent(getApplicationContext(), PushupCounterActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.ATTIVITA_RILEVATA, "Pushup");
        startActivity(intent);
    }
    /**
     * Premenedo il RelativeLayout vado all'attività per traccare corsa, camminata e ciclismo
     * @param v
     */
    public void goToRunningActivity(View v){
        String activity = "";

        if(v.getId() == R.id.rlCamminata) activity = "Camminata";
        if(v.getId() == R.id.rlCorsa) activity = "Corsa";
        if(v.getId() == R.id.rlCiclismo) activity = "Ciclismo";

        Intent intent = new Intent(getApplicationContext(), RunningActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.ATTIVITA_RILEVATA, activity);
        startActivity(intent);
    }
    /**
     * Premenedo il RelativeLayout vado all'attività per traccare l'allenamento in freestyle
     * @param v
     */
    public void goToFreestyleActivity(View v){
        Intent intent = new Intent(getApplicationContext(), TrainingActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.ATTIVITA_RILEVATA, "Freestyle");
        startActivity(intent);
    }

    /** Metodo che gestisce il Dialog contenente le informazioni sull'ActivityTransition */
    public void infoDialog(){
        InfoDialog infoDialog = new InfoDialog();
        infoDialog.setType("dashboard");
        infoDialog.show(getSupportFragmentManager(), "Dialog informativo");
    }
}