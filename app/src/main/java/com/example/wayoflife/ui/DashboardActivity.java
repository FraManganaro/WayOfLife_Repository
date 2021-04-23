package com.example.wayoflife.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.wayoflife.R;
import com.example.wayoflife.workouts.FreestyleActivity;
import com.example.wayoflife.workouts.GenericWorkoutActivity;
import com.example.wayoflife.workouts.PushupCounterActivity;
import com.example.wayoflife.workouts.RunningActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
        Intent intent = new Intent(getApplicationContext(), GenericWorkoutActivity.class);
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
                                intent.putExtra("attivita riconosciuta", "Basket");
                                startActivity(intent);

                                bottomSheetDialog.dismiss();
                            }
                        });
                /** Gestione dell'allenamento calcio */
                sheetView.findViewById(R.id.calcio).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                intent.putExtra("attivita riconosciuta", "Calcio");
                                startActivity(intent);

                                bottomSheetDialog.dismiss();
                            }
                        });
                /** Gestione dell'allenamento Nuoto */
                sheetView.findViewById(R.id.nuoto).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                intent.putExtra("attivita riconosciuta", "Nuoto");
                                startActivity(intent);

                                bottomSheetDialog.dismiss();
                            }
                        });
                /** Gestione dell'allenamento Scalini */
                sheetView.findViewById(R.id.scalini).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                intent.putExtra("attivita riconosciuta", "Scalini");
                                startActivity(intent);

                                bottomSheetDialog.dismiss();
                            }
                        });
                /** Gestione dell'allenamento Tennis */
                sheetView.findViewById(R.id.tennis).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                intent.putExtra("attivita riconosciuta", "Tennis");
                                startActivity(intent);

                                bottomSheetDialog.dismiss();
                            }
                        });

                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
            }
        });

    }

    /**
     * Premendo il RelativeLayout vado al contatore di flessioni
     * @param v
     */
    public void goToPushupCounter(View v){
        Intent intent = new Intent(getApplicationContext(), PushupCounterActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("attivita riconosciuta", "Pushup");
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
        intent.putExtra("attivita riconosciuta", activity);
        startActivity(intent);
    }
    /**
     * Premenedo il RelativeLayout vado all'attività per traccare l'allenamento in freestyle
     * @param v
     */
    public void goToFreestyleActivity(View v){
        Intent intent = new Intent(getApplicationContext(), FreestyleActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("attivita riconosciuta", "Freestyle");
        startActivity(intent);
    }
}