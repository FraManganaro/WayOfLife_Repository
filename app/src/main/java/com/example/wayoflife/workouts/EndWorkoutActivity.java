package com.example.wayoflife.workouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wayoflife.DatabaseHelper;

import com.example.wayoflife.Constants;
import com.example.wayoflife.CustomerModel;
import com.example.wayoflife.R;
import com.example.wayoflife.ui.HomeActivity;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.wayoflife.R.drawable.ic_walking_color;

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
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_workout);

        TextView tvData = findViewById(R.id.tvData);
        TextView tvDurata = findViewById(R.id.tvTempo);
        TextView tvCalorie = findViewById(R.id.tvCalorie);
        TextView tvExtra = findViewById(R.id.tvExtra);
        TextView tipoAllenamento = findViewById(R.id.tipoAllenamento);

        LinearLayout llExtra = findViewById(R.id.llExtra);
        TextView tvExtraText = findViewById(R.id.tvExtraText);

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
        if (tr == null) {
            chilometri = 0;
        } else {
            chilometri = Float.parseFloat(tr);
        }

        n_flessioni = getIntent().getIntExtra(Constants.FLESSIONI, 0);

        if(chilometri != 0.0f) {
            tvExtraText.setText("Chilometri fatti: ");
            tvExtra.setText(chilometri + " km");
        } else if(n_flessioni != 0) {
            tvExtraText.setText("Flessioni fatte: ");
            tvExtra.setText(""+ n_flessioni);
        } else {
            tvExtra.setVisibility(View.INVISIBLE);
            llExtra.setVisibility(View.INVISIBLE);
            tvExtraText.setVisibility(View.INVISIBLE);
        }
    }

    public void saveWorkout(View v){
        TextInputEditText nomeET = findViewById(R.id.etNome);
        Slider slider = findViewById(R.id.slider);


        nome = nomeET.getText().toString();
        if(nome.equalsIgnoreCase(""))
            nome = "Allenamento " + tipologiaAllenamento;

        state = (int) slider.getValue();


        CustomerModel customerModel = null;

        try {
            if(tipologiaAllenamento.equals("Corsa")  || tipologiaAllenamento.equals("Camminata") || tipologiaAllenamento.equals("Ciclismo")){
                //uso il costruttore con chilometri, senza flessioni
                customerModel = new CustomerModel(nome, data, durata, chilometri, tipologiaAllenamento, calorie, state);
            }
            else{
                if (tipologiaAllenamento.equals("Pushup") || tipologiaAllenamento.equals("Freestyle")){
                    //uso il costruttore senza chilometri, con flessioni
                    customerModel = new CustomerModel(nome, data, durata, tipologiaAllenamento, calorie, n_flessioni, state);
                }
                else{
                    //uso il costruttore senza chilometri e flessioni
                    customerModel = new CustomerModel(nome, data, durata, tipologiaAllenamento, calorie, state);
                }
            }
        }catch (Exception e){
            Toast.makeText(EndWorkoutActivity.this,
                    "Errore nel salvataggio", Toast.LENGTH_SHORT).show();
        }

        DatabaseHelper db = new DatabaseHelper(EndWorkoutActivity.this);
        boolean success = db.addWorkout(customerModel);
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