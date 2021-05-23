package com.example.wayoflife.workouts.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wayoflife.R;
import com.example.wayoflife.util.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WorkoutInformation extends AppCompatActivity {

    private final String TAG = "WorkoutInformation";

    private String nome;
    private String data;
    private String durata;
    private String tipologiaAllenamento;
    private int calorie;
    private float chilometri;
    private int n_flessioni;
    private int n_squat;
    private int state;
    private int like;

    private FloatingActionButton favoritesButton;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_workout);

        TextView tvNome = findViewById(R.id.nomeAllenamento);
        TextView tvTipologia = findViewById(R.id.tipoAllenamento);
        TextView tvData = findViewById(R.id.dataWorkout);
        TextView tvDurata = findViewById(R.id.durataWorkout);
        TextView tvCalorie = findViewById(R.id.calorieWorkout);

        /** Nome allenamento **/
        nome = getIntent().getStringExtra(Constants.NOME_ALLENAMENTO);
        tvNome.setText(nome);

        /** Tipologia allenamento **/
        tipologiaAllenamento = getIntent().getStringExtra(Constants.ATTIVITA_RILEVATA);
        tvTipologia.setText(tipologiaAllenamento);
        updateIconWorkout();

        /** Faccina stato **/
        state = getIntent().getIntExtra(Constants.ACTIVITY_TRACKING_STATUS, 8);
        updateIconStatus();

        /** Data **/
        data = getIntent().getStringExtra(Constants.DATA);
        tvData.setText(data);

        /** Durata **/
        durata = getIntent().getStringExtra(Constants.DURATA_ALLENAMENTO);
        tvDurata.setText(durata);

        /** Calorie **/
        calorie = getIntent().getIntExtra(Constants.CALORIE, 0);
        tvDurata.setText(calorie);


    }

    /** Settaggio icona tipologia allenamento **/
    private void updateIconWorkout(){
        ImageView iv = findViewById(R.id.iconaAllenamento);

        switch(tipologiaAllenamento){
            case "Camminata":
                iv.setImageDrawable(getDrawable(R.drawable.ic_walking_color));
                break;
            case "Corsa":
                iv.setImageDrawable(getDrawable(R.drawable.ic_running_color));
                break;
            case "Ciclismo":
                iv.setImageDrawable(getDrawable(R.drawable.ic_cycling_color));
                break;
            case "Freestyle":
                iv.setImageDrawable(getDrawable(R.drawable.ic_workout_color));
                break;
            case "Pushup":
                iv.setImageDrawable(getDrawable(R.drawable.ic_pushup_color));
                break;
            case "Squat":
                iv.setImageDrawable(getDrawable(R.drawable.ic_squats));
                break;
            case "Calcio":
                iv.setImageDrawable(getDrawable(R.drawable.ic_football));
                break;
            case "Basket":
                iv.setImageDrawable(getDrawable(R.drawable.ic_basketball));
                break;
            case "Scalini":
                iv.setImageDrawable(getDrawable(R.drawable.ic_stairs));
                break;
            case "Nuoto":
                iv.setImageDrawable(getDrawable(R.drawable.ic_swimmer));
                break;
            case "Tennis":
                iv.setImageDrawable(getDrawable(R.drawable.ic_tennis_racket));
                break;
        }
    }

    /** Settaggio faccina **/
    private void updateIconStatus(){
        ImageView iv = findViewById(R.id.iconeStatus);

        switch(state){
            case 0:
            case 1:
            case 2:
                iv.setImageDrawable(getDrawable(R.drawable.ic_min));
                break;
            case 3:
            case 4:
                iv.setImageDrawable(getDrawable(R.drawable.ic_mid_min));
                break;
            case 5:
            case 6:
                iv.setImageDrawable(getDrawable(R.drawable.ic_mid));
                break;
            case 7:
            case 8:
                iv.setImageDrawable(getDrawable(R.drawable.ic_mid_max));
                break;
            case 9:
            case 10:
                iv.setImageDrawable(getDrawable(R.drawable.ic_max));
                break;
        }
    }
}
