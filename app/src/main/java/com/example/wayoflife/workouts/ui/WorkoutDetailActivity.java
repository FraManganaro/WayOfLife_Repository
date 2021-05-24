package com.example.wayoflife.workouts.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wayoflife.R;
import com.example.wayoflife.util.Constants;
import com.example.wayoflife.workouts.util.WorkoutDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WorkoutDetailActivity extends AppCompatActivity {

    private final String TAG = "WorkoutDetailActivity";

    private String nome;
    private String data;
    private String durata;
    private String tipologiaAllenamento;
    private String calorie;
    private float chilometri;
    private int n_flessioni;
    private int n_squat;
    private int state;
    private int like;
    private int id;

    private FloatingActionButton favoritesButton;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        favoritesButton = findViewById(R.id.button_favorites);

        TextView tvNome = findViewById(R.id.nomeAllenamento);
        TextView tvTipologia = findViewById(R.id.tipoAllenamento);
        TextView tvData = findViewById(R.id.dataWorkout);
        TextView tvDurata = findViewById(R.id.durataWorkout);
        TextView tvCalorie = findViewById(R.id.calorieWorkout);

        TextView tvExtra = findViewById(R.id.tvExtra);
        TextView tvExtraSquat = findViewById(R.id.tvExtraSquat);
        TextView tvExtraText = findViewById(R.id.tvExtraText);
        TextView tvExtraTextSquat = findViewById(R.id.tvExtraTextSquat);

        /** Id dell'elemento del DB */
        id = getIntent().getIntExtra(Constants.ID, 0);

        /** Nome allenamento */
        nome = getIntent().getStringExtra(Constants.NOME_ALLENAMENTO);
        tvNome.setText(nome);

        /** Tipologia allenamento */
        tipologiaAllenamento = getIntent().getStringExtra(Constants.ATTIVITA_RILEVATA);
        tvTipologia.setText(tipologiaAllenamento);
        updateIconWorkout();

        /** Durata **/
        durata = getIntent().getStringExtra(Constants.DURATA_ALLENAMENTO);
        tvDurata.setText(durata);

        /** Calorie */
        calorie = getIntent().getStringExtra(Constants.CALORIE);
        tvCalorie.setText(calorie + " kcal");

        /** Faccina stato */
        state = Integer.parseInt(getIntent().getStringExtra(Constants.STATE));
        updateIconStatus();

        /** Preferiti */
        like = Integer.parseInt(getIntent().getStringExtra(Constants.LIKE));
        if(like == 1) {
            favoritesButton.setImageDrawable(getDrawable(R.drawable.ic_heart_filled));
        } else {
            favoritesButton.setImageDrawable(getDrawable(R.drawable.ic_heart_empty));
        }

        /** Data */
        data = getIntent().getStringExtra(Constants.DATA);
        tvData.setText(data);

        String tr = getIntent().getStringExtra(Constants.CHILOMETRI);
        if (tr == null || tr.equals("-1.0")) {
            chilometri = 0;
            Log.d(TAG, "Chilometri 0");
        } else {
            chilometri = Float.parseFloat(tr);
        }

        n_flessioni = getIntent().getIntExtra(Constants.FLESSIONI, 0);
        n_squat = getIntent().getIntExtra(Constants.SQUAT, 0);

        Log.d(TAG, "KM: " + chilometri);
        Log.d(TAG, "FLESS: " + n_flessioni);
        Log.d(TAG, "SQUAT: " + n_squat);

        /** Controllo che TextView attivare e disattivare in base ai parametri che ricevo */
        if(chilometri != 0.0f) {
            tvExtraText.setText("Chilometri: ");
            tvExtra.setText(chilometri + " km");

            tvExtraSquat.setVisibility(View.INVISIBLE);
            tvExtraTextSquat.setVisibility(View.INVISIBLE);

        } else if ((n_squat != -1 && n_squat != 0) && (n_flessioni != -1 && n_flessioni != 0)) {
            tvExtraText.setText("Flessioni: ");
            tvExtra.setText("" + n_flessioni);
            tvExtraTextSquat.setText("Squat: ");
            tvExtraSquat.setText("" + n_squat);

        } else if ((n_flessioni != -1 && n_flessioni != 0) && (n_squat == -1 || n_squat == 0)) {
            tvExtraText.setText("Flessioni: ");
            tvExtra.setText("" + n_flessioni);

            tvExtraSquat.setVisibility(View.INVISIBLE);
            tvExtraTextSquat.setVisibility(View.INVISIBLE);

        } else if ((n_squat != -1 && n_squat != 0) && (n_flessioni == -1 || n_flessioni == 0)) {
            tvExtraText.setText("Squat: ");
            tvExtra.setText("" + n_squat);

            tvExtraSquat.setVisibility(View.INVISIBLE);
            tvExtraTextSquat.setVisibility(View.INVISIBLE);

        } else {
            tvExtra.setVisibility(View.INVISIBLE);
            tvExtraText.setText("Nessun elemento extra da mostrare!");
            tvExtraText.setTextColor(getColor(R.color.black));
            tvExtraSquat.setVisibility(View.INVISIBLE);
            tvExtraTextSquat.setVisibility(View.INVISIBLE);
        }
    }

    /** Metodo che permette l'eliminazione di un allenamento */
    public void deleteWorkout(View v){
        WorkoutDatabase db = new WorkoutDatabase(WorkoutDetailActivity.this);
        boolean success = db.deleteWorkout(id);
        if (success){
            Toast.makeText(WorkoutDetailActivity.this,
                    "Allenamento eliminato!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), WorkoutHistoryActivity.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        }
    }

    public void addFavorites(View v) {
        if(like == 0) {
            favoritesButton.setImageDrawable(getDrawable(R.drawable.ic_heart_filled));
            like = 1;
        } else {
            favoritesButton.setImageDrawable(getDrawable(R.drawable.ic_heart_empty));
            like = 0;
        }

        WorkoutDatabase db = new WorkoutDatabase(WorkoutDetailActivity.this);
        db.updateFavorites(like, id);
    }

    public void getBack(View v){
        Intent intent = new Intent(getApplicationContext(), WorkoutHistoryActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    /** Settaggio icona tipologia allenamento **/
    private void updateIconWorkout(){
        ImageView iv = findViewById(R.id.iconaAllenamento);
        ScrollView sv = findViewById(R.id.scrollView);

        switch(tipologiaAllenamento){
            case "Camminata":
                iv.setImageDrawable(getDrawable(R.drawable.ic_walking_color));
                sv.setBackground(getDrawable(R.drawable.image_background_running));
                break;
            case "Corsa":
                iv.setImageDrawable(getDrawable(R.drawable.ic_running_color));
                sv.setBackground(getDrawable(R.drawable.image_background_running));
                break;
            case "Ciclismo":
                iv.setImageDrawable(getDrawable(R.drawable.ic_cycling_color));
                sv.setBackground(getDrawable(R.drawable.image_background_running));
                break;
            case "Freestyle":
                iv.setImageDrawable(getDrawable(R.drawable.ic_workout_color));
                sv.setBackground(getDrawable(R.drawable.image_background_freestyle));
                break;
            case "Pushup":
                iv.setImageDrawable(getDrawable(R.drawable.ic_pushup_color));
                sv.setBackground(getDrawable(R.drawable.image_background_pushup));
                break;
            case "Squat":
                iv.setImageDrawable(getDrawable(R.drawable.ic_squats));
                sv.setBackground(getDrawable(R.drawable.image_background_squat));
                break;
            case "Calcio":
                iv.setImageDrawable(getDrawable(R.drawable.ic_football));
                sv.setBackground(getDrawable(R.drawable.image_background_extra));
                break;
            case "Basket":
                iv.setImageDrawable(getDrawable(R.drawable.ic_basketball));
                sv.setBackground(getDrawable(R.drawable.image_background_extra));
                break;
            case "Scalini":
                iv.setImageDrawable(getDrawable(R.drawable.ic_stairs));
                sv.setBackground(getDrawable(R.drawable.image_background_extra));
                break;
            case "Nuoto":
                iv.setImageDrawable(getDrawable(R.drawable.ic_swimmer));
                sv.setBackground(getDrawable(R.drawable.image_background_extra));
                break;
            case "Tennis":
                iv.setImageDrawable(getDrawable(R.drawable.ic_tennis_racket));
                sv.setBackground(getDrawable(R.drawable.image_background_extra));
                break;
        }
    }

    /** Settaggio faccina **/
    private void updateIconStatus(){
        ImageView iv = findViewById(R.id.iconeStatus);

        switch(state){
            case 0:
            case 1:
                iv.setImageDrawable(getDrawable(R.drawable.ic_min));
                break;
            case 2:
            case 3:
                iv.setImageDrawable(getDrawable(R.drawable.ic_mid_min));
                break;
            case 4:
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
