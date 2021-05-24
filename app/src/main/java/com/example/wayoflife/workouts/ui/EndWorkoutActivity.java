package com.example.wayoflife.workouts.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wayoflife.workouts.util.WorkoutDatabase;

import com.example.wayoflife.dialog.InfoDialog;
import com.example.wayoflife.util.Constants;
import com.example.wayoflife.workouts.util.WorkoutModel;
import com.example.wayoflife.R;
import com.example.wayoflife.ui.HomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EndWorkoutActivity<databaseHelper> extends AppCompatActivity {

    private final String TAG = "EndWorkoutActivity";

    private String nome;
    private String data;
    private String durata;
    private double durataInSecondi;
    private double durataInMinuti;
    private double durataInOre;

    private String tipologiaAllenamento;
    private int calorie;
    private float chilometri;
    private int n_flessioni;
    private int n_squat;
    private int state;
    private int like;

    private FloatingActionButton favoritesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_workout);

        TextView tvData = findViewById(R.id.tvData);
        TextView tvDurata = findViewById(R.id.tvTempo);
        TextView tvCalorie = findViewById(R.id.tvCalorie);
        TextView tipoAllenamento = findViewById(R.id.tipoAllenamento);

        TextView tvExtra = findViewById(R.id.tvExtra);
        TextView tvExtraSquat = findViewById(R.id.tvExtraSquat);
        TextView tvExtraText = findViewById(R.id.tvExtraText);
        TextView tvExtraTextSquat = findViewById(R.id.tvExtraTextSquat);

        favoritesButton = findViewById(R.id.favorites_button);
        like = 0;

        /** Recupero data odierna */
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy"); // getting date in this format
        data = df.format(date.getTime());
        tvData.setText(data);

        tipologiaAllenamento = getIntent().getStringExtra(Constants.ATTIVITA_RILEVATA);
        tipoAllenamento.setText("Tipologia allenamento: "+ tipologiaAllenamento);
        updateIcon();

        durataInSecondi = 1.0 *
                getIntent().getIntExtra(Constants.TEMPO_IN_SECONDI, 0);
//        durataInSecondi = 2432; -> per test
        manageTime();
        tvDurata.setText(durata);

        calorie = getIntent().getIntExtra(Constants.CALORIE, 0);
        tvCalorie.setText(calorie + " kcal");

        String tr = getIntent().getStringExtra(Constants.CHILOMETRI);
        if (tr == null || tr.equals("0,00")) {
            chilometri = 0;
            Log.d(TAG, "Chilometri 0");
        } else {
            chilometri = Float.parseFloat(tr);
        }

        n_flessioni = getIntent().getIntExtra(Constants.FLESSIONI, 0);
        n_squat = getIntent().getIntExtra(Constants.SQUAT, 0);

        /** Controllo che TextView attivare e disattivare in base ai parametri che ricevo */
        if(chilometri != 0.0f) {
            tvExtraText.setText("Chilometri fatti: ");
            tvExtra.setText(chilometri + " km");

            tvExtraSquat.setVisibility(View.INVISIBLE);
            tvExtraTextSquat.setVisibility(View.INVISIBLE);

        } else if (n_squat != 0 && n_flessioni != 0) {
            tvExtraText.setText("Flessioni fatte: ");
            tvExtra.setText("" + n_flessioni);
            tvExtraTextSquat.setText("Squat fatti: ");
            tvExtraSquat.setText("" + n_squat);

        } else if (n_flessioni != 0 && n_squat == 0) {
            tvExtraText.setText("Flessioni fatte: ");
            tvExtra.setText("" + n_flessioni);

            tvExtraSquat.setVisibility(View.INVISIBLE);
            tvExtraTextSquat.setVisibility(View.INVISIBLE);

        } else if (n_squat != 0 && n_flessioni == 0) {
            tvExtraText.setText("Squat fatti: ");
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
    @Override
    public void onBackPressed() {
        InfoDialog infoDialog = new InfoDialog();
        infoDialog.setType("workout");
        infoDialog.show(getSupportFragmentManager(), "Dialog informativo");
    }

    public void updateLike(View v){
        if(like == 0) {
            favoritesButton.setImageDrawable(getDrawable(R.drawable.ic_heart_filled));
            like = 1;
        } else {
            favoritesButton.setImageDrawable(getDrawable(R.drawable.ic_heart_empty));
            like = 0;
        }
    }

    public void saveWorkout(View v){
        TextInputEditText nomeET = findViewById(R.id.etNome);
        Slider slider = findViewById(R.id.slider);


        nome = nomeET.getText().toString();
        if(nome.equalsIgnoreCase(""))
            nome = "Allenamento " + tipologiaAllenamento;

        state = (int) slider.getValue();

        WorkoutModel workoutModel = null;

        try {
            if(tipologiaAllenamento.equalsIgnoreCase("Corsa")  ||
                    tipologiaAllenamento.equalsIgnoreCase("Camminata") ||
                    tipologiaAllenamento.equalsIgnoreCase("Ciclismo")){
                //uso il costruttore con chilometri, senza flessioni
                workoutModel = new WorkoutModel(nome, data, durata, chilometri, tipologiaAllenamento, calorie, state, like);
            }
            else{
                if (tipologiaAllenamento.equalsIgnoreCase("Pushup") ||
                        tipologiaAllenamento.equalsIgnoreCase("Freestyle") ||
                        tipologiaAllenamento.equalsIgnoreCase("Squat")){
                    //uso il costruttore senza chilometri, con flessioni e squat
                    workoutModel = new WorkoutModel(nome, data, n_squat, durata, tipologiaAllenamento, calorie, n_flessioni, state, like);
                }
                else{
                    //uso il costruttore senza chilometri e flessioni
                    workoutModel = new WorkoutModel(nome, data, durata, tipologiaAllenamento, calorie, state, like);
                }
            }
        }catch (Exception e){
            Toast.makeText(EndWorkoutActivity.this,
                    "Errore nel salvataggio", Toast.LENGTH_SHORT).show();
        }

        WorkoutDatabase db = new WorkoutDatabase(EndWorkoutActivity.this);
        boolean success = db.addWorkout(workoutModel);
        if (success){
            Toast.makeText(EndWorkoutActivity.this,
                    "Allenamento salvato!", Toast.LENGTH_SHORT).show();
        }

        //Una volta salvato l'allenamento posso tornare alla Home
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void returnHome(View v){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * Metodo che converte il tempo in secondi nel formato:
     *     00:00:00
     * ore:minuti:secondi
     */
    private void manageTime(){
        int tempO, tempM, tempS;

        durataInMinuti = durataInSecondi/60;
        durataInOre = durataInMinuti/60;

        if(durataInOre > 1.0) {
            tempO = (int) durataInOre;
            if(tempO < 10)
                durata = "0" + tempO + ":";
            else
                durata = tempO + ":";

            tempO *= 60;
            tempM = (int) durataInMinuti;
            tempM -= tempO;
            if(tempM < 10)
                durata += "0" + tempM + ":";
            else
                durata += tempM + ":";

            tempM *= 60;
            tempS = (int) durataInSecondi;
            tempS -= tempM + 3600;
            if(tempS < 10)
                durata += "0" + tempS + " ore";
            else
                durata += tempS + " ore";

        }else {
            if (durataInMinuti < 1.0) {
                tempS = (int) durataInSecondi;
                if(tempS < 10)
                    durata = "0" + tempS + " secondi";
                else
                    durata = tempS + " secondi";

            }else {
                tempM = (int) durataInMinuti;
                if(tempM < 10)
                    durata = "0" + tempM + ":";
                else
                    durata = tempM + ":";

                tempM *= 60;
                tempS = (int) durataInSecondi;
                tempS -= tempM;
                if(tempS < 10)
                    durata += "0" + tempS + " minuti";
                else
                    durata += tempS + " minuti";
            }
        }
    }

    private void updateIcon(){
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
}