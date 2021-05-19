package com.example.wayoflife.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.wayoflife.R;
import com.example.wayoflife.dialog.TipsDialog;
import com.example.wayoflife.util.Constants;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

public class TipsActivity extends AppCompatActivity {

    public static final String TAG = "TipsActivity";

    /** Variabili per gestione dei box di input */
    private String pesoS;
    private String massaGrassaS;
    private String indiceAttivitaS;
    private String effettoTermicoS;
    private String durataAllenamentoS;
    private String bilancioEnergeticoS;
    private String numeroAllenamentiS;
    private double peso;
    private double massaGrassa;
    private double indiceAttivita;
    private double effettoTermico;
    private double durataAllenamento;
    private double bilancioEnergetico;
    private double numeroAllenamenti;

    /** Variabili per i calcoli delle calorie */
    private double massaMagra;
    private double metabolismoBasale;
    private double calorieBruciateConAllenamento;
    private double calorieBruciateSenzaAllenamento;
    private double dispendioCaloricoGiornateAllenamento;
    private double calorieMantenimentiSettimanali;
    private double obiettivoCaloricoGiornaliero;
    private double obiettivoCaloricoSettimanale;

    private int kcal;

    /** Collegamenti con layout */
    private TextInputEditText pesoET;
    private TextInputEditText massaGrassaET;
    private TextInputEditText indiceAttivitaET;
    private TextInputEditText effettoTermicoET;
    private TextInputEditText durataAllenamentoET;
    private TextInputEditText bilancioEnergeticoET;
    private TextInputEditText numeroAllenamentiET;

    private TextView obiettivoCalorie;
    private TextView obiettivoProteine;
    private TextView obiettivoCarboidrati;
    private TextView obiettivoGrassi;
    private TextView minimoCalorie;
    private TextView minimoCarboidrati;
    private TextView minimoProteine;
    private TextView minimoGrassi;
    private TextView massimoCalorie;
    private TextView massimoCarboidrati;
    private TextView massimoProteine;
    private TextView massimoGrassi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        pesoET = findViewById(R.id.etPeso);
        massaGrassaET = findViewById(R.id.etMassaGrassa);
        indiceAttivitaET = findViewById(R.id.etIndiceAttivita);
        effettoTermicoET = findViewById(R.id.etEffettoTermico);
        durataAllenamentoET = findViewById(R.id.etDurataAllenamento);
        bilancioEnergeticoET = findViewById(R.id.etBilancioEnergetico);
        numeroAllenamentiET = findViewById(R.id.etNumeroAllenamenti);

        obiettivoCalorie = findViewById(R.id.obiettivoCalorie);
        obiettivoCarboidrati = findViewById(R.id.obiettivoCarbo);
        obiettivoProteine = findViewById(R.id.obiettivoProteine);
        obiettivoGrassi = findViewById(R.id.obiettivoGrassi);

        minimoCalorie = findViewById(R.id.minimoCalorie);
        minimoCarboidrati = findViewById(R.id.minimoCarboidrati);
        minimoProteine = findViewById(R.id.minimoProteine);
        minimoGrassi = findViewById(R.id.minimoGrassi);

        massimoCalorie = findViewById(R.id.massimoCalorie);
        massimoCarboidrati = findViewById(R.id.massimoCarboidrati);
        massimoProteine = findViewById(R.id.massimoProteine);
        massimoGrassi = findViewById(R.id.massimoGrassi);

        resumeDoubleInformation();
    }

    /**
     * Chiamata con il bottone SALVA nel layout
     */
    public void saveData(View v) {
        pesoS = pesoET.getText().toString();
        massaGrassaS = massaGrassaET.getText().toString();
        indiceAttivitaS = indiceAttivitaET.getText().toString();
        effettoTermicoS = effettoTermicoET.getText().toString();
        durataAllenamentoS = durataAllenamentoET.getText().toString();
        bilancioEnergeticoS = bilancioEnergeticoET.getText().toString();
        numeroAllenamentiS = numeroAllenamentiET.getText().toString();

        if (pesoS.isEmpty() || massaGrassaS.isEmpty()  || effettoTermicoS.isEmpty()  ||
                durataAllenamentoS.isEmpty()  || bilancioEnergeticoS.isEmpty()  ||
                numeroAllenamentiS.isEmpty() || indiceAttivitaS.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), "Inserisci tutti i dati",
                    Snackbar.LENGTH_SHORT).show();
        } else {
            saveInformation();
            resumeDoubleInformation();
        }
    }

    /**
     * Salvo le informazioni appena prese dall'interfaccia
     */
    private void saveInformation(){
        SharedPreferences sharedPref = getSharedPreferences(
                Constants.PROFILE_INFO_FILENAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(Constants.PESO, pesoS);
        editor.putString(Constants.MASSA_GRASSA, massaGrassaS);
        editor.putString(Constants.INDICE_ATTIVITA, indiceAttivitaS);
        editor.putString(Constants.EFFETTO_TERMICO, effettoTermicoS);
        editor.putString(Constants.DURATA_ALLENAMENTO, durataAllenamentoS);
        editor.putString(Constants.BILANCIO_ENERGETICO, bilancioEnergeticoS);
        editor.putString(Constants.NUMERO_ALLENAMENTI, numeroAllenamentiS);

        editor.apply();
    }

    /**
     * Considero le variabili in double così da poter effettuare i calcoli
     */
    private void resumeDoubleInformation(){
        SharedPreferences sharedPref = getSharedPreferences(
                Constants.PROFILE_INFO_FILENAME,
                Context.MODE_PRIVATE);

        peso = Double.parseDouble(sharedPref.getString(
                Constants.PESO, "0.0"));
        massaGrassa = Double.parseDouble(sharedPref.getString(
                Constants.MASSA_GRASSA, "0.0"));
        indiceAttivita = Double.parseDouble(sharedPref.getString(
                Constants.INDICE_ATTIVITA, "0.0"));
        effettoTermico = Double.parseDouble(sharedPref.getString(
                Constants.EFFETTO_TERMICO, "0.0"));
        durataAllenamento = Double.parseDouble(sharedPref.getString(
                Constants.DURATA_ALLENAMENTO, "0.0"));
        bilancioEnergetico = Double.parseDouble(sharedPref.getString(
                Constants.BILANCIO_ENERGETICO, "0.0"));
        numeroAllenamenti = Double.parseDouble(sharedPref.getString(
                Constants.NUMERO_ALLENAMENTI, "0.0"));

        pesoET.setText("" + peso);

        if (!(massaMagra == 0.0  && indiceAttivita == 0.0  &&
                durataAllenamento == 0.0  && bilancioEnergetico == 0.0  &&
                numeroAllenamenti == 0.0 && indiceAttivita == 0.0)){

            massaGrassaET.setText("" + massaGrassa);
            indiceAttivitaET.setText("" + indiceAttivita);
            effettoTermicoET.setText("" + effettoTermico);
            durataAllenamentoET.setText("" + ((int) durataAllenamento));
            bilancioEnergeticoET.setText("" + bilancioEnergetico);
            numeroAllenamentiET.setText("" + ((int) numeroAllenamenti));

            calculateCalories();
        }
    }

    /**
     * Consideando i dati ricevuti in input calcolo i parametri necessari per il calcolo finale
     */
    private void calculateCalories(){
        massaMagra = peso * ( 1 - massaGrassa / 100.0);

        metabolismoBasale = 370 + (21.6 * massaMagra);

        calorieBruciateConAllenamento = 0.1 * peso * durataAllenamento;

        calorieBruciateSenzaAllenamento = indiceAttivita * effettoTermico * metabolismoBasale;

        dispendioCaloricoGiornateAllenamento = (metabolismoBasale * indiceAttivita + calorieBruciateConAllenamento)
                * effettoTermico;

        calorieMantenimentiSettimanali = (calorieBruciateSenzaAllenamento *
                ( 7 - numeroAllenamenti ) + dispendioCaloricoGiornateAllenamento * numeroAllenamenti);

        obiettivoCaloricoGiornaliero = (calorieMantenimentiSettimanali * bilancioEnergetico) / 7;

        obiettivoCaloricoSettimanale = obiettivoCaloricoGiornaliero * 7;


        SharedPreferences sharedPref = getSharedPreferences(
                Constants.PROFILE_INFO_FILENAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        kcal = (int) obiettivoCaloricoGiornaliero;
        editor.putString(Constants.KCAL, "" + kcal);

        editor.apply();

        updateTable();
    }

    /**
     * Metodo che gestisce l'aaggiornamento della tabella e dei dati contenuti in essa
     */
    private void updateTable(){
        double pro = peso * 1.85;
        double grass = (kcal * 0.25) / 9;
        double carbo = (kcal - pro * 4 - grass * 9) / 4.0;

        double grassMin = ((kcal - 50) * 0.25) / 9;
        double carboMin = ((kcal - 50) - pro * 4 - grassMin * 9) / 4.0;

        double proMax = peso * 2;
        double grassMax = ((kcal + 50) * 0.25) / 9;
        double carboMax = ((kcal + 50) - proMax * 4 - grassMax * 9) / 4.0;

        DecimalFormat numberFormat = new DecimalFormat("0.0");

        obiettivoCalorie.setText("" + kcal);
        obiettivoCarboidrati.setText("" + numberFormat.format(carbo));
        obiettivoProteine.setText("" + numberFormat.format(pro));
        obiettivoGrassi.setText("" + numberFormat.format(grass));

        minimoCalorie.setText("" + (kcal - 50));
        minimoCarboidrati.setText("" + numberFormat.format(carboMin));
        minimoProteine.setText("" + numberFormat.format(pro));
        minimoGrassi.setText("" + numberFormat.format(grassMin));

        massimoCalorie.setText("" + (kcal + 50));
        massimoCarboidrati.setText("" + numberFormat.format(carboMax));
        massimoProteine.setText("" + numberFormat.format(proMax));
        massimoGrassi.setText("" + numberFormat.format(grassMax));
    }

    /** Metodo che gestisce il Dialog contenente le informazioni sull'Indice di attività */
    public void infoDialogIndiceAttivita(View v){
        TipsDialog tipsDialog = new TipsDialog();
        tipsDialog.setType("IndiceAttivita");
        tipsDialog.show(getSupportFragmentManager(), "Dialog informativo");
    }
    /** Metodo che gestisce il Dialog contenente le informazioni sull'Effetto Termico */
    public void infoDialogEffettoTermico(View v){
        TipsDialog tipsDialog = new TipsDialog();
        tipsDialog.setType("EffettoTermico");
        tipsDialog.show(getSupportFragmentManager(), "Dialog informativo");
    }
    /** Metodo che gestisce il Dialog contenente le informazioni sul Bilancio Energetico */
    public void infoDialogBilancioEnergetico(View v){
        TipsDialog tipsDialog = new TipsDialog();
        tipsDialog.setType("BilancioEnergetico");
        tipsDialog.show(getSupportFragmentManager(), "Dialog informativo");
    }
}