package com.example.wayoflife.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.wayoflife.R;
import com.example.wayoflife.dialog.InfoDialog;
import com.example.wayoflife.dialog.TipsDialog;
import com.example.wayoflife.util.Constants;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class TipsActivity extends AppCompatActivity {

    public static final String TAG = "TipsActivity";

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

    private double massaMagra;
    private double metabolismoBasale;
    private double calorieBruciateConAllenamento;
    private double calorieBruciateSenzaAllenamento;
    private double dispendioCaloricoGiornateAllenamento;
    private double calorieMantenimentiSettimanali;
    private double obiettivoCaloricoGiornaliero;
    private double obiettivoCaloricoSettimanale;

    private TextInputEditText pesoET;
    private TextInputEditText massaGrassaET;
    private TextInputEditText indiceAttivitaET;
    private TextInputEditText effettoTermicoET;
    private TextInputEditText durataAllenamentoET;
    private TextInputEditText bilancioEnergeticoET;
    private TextInputEditText numeroAllenamentiET;

    private TextView obiettivo;
    private TextView proteine;
    private TextView carboidrati;
    private TextView grassi;

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

        obiettivo = findViewById(R.id.obiettivo);
        carboidrati = findViewById(R.id.carboidrati);
        proteine = findViewById(R.id.proteine);
        grassi = findViewById(R.id.grassi);

        resumeDoubleInformation();
    }

    /** Chiamata con il bottone SALVA nel layout */
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

    /** Salvo le informazioni appena prese dall'interfaccia */
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
            durataAllenamentoET.setText("" + durataAllenamento);
            bilancioEnergeticoET.setText("" + bilancioEnergetico);
            numeroAllenamentiET.setText("" + numeroAllenamenti);

            calculateCalories();
        }
    }

    private void calculateCalories(){
        massaMagra = peso * ( 1 - massaGrassa / 100.0);
        metabolismoBasale = 370 + (21.6 * massaMagra);
        calorieBruciateConAllenamento = 0.1 * peso * durataAllenamento;
        calorieBruciateSenzaAllenamento = indiceAttivita * effettoTermico * metabolismoBasale;
        dispendioCaloricoGiornateAllenamento = (metabolismoBasale * indiceAttivita + calorieBruciateConAllenamento) * effettoTermico;
        calorieMantenimentiSettimanali = (calorieBruciateSenzaAllenamento *
                ( 7 - numeroAllenamenti ) + dispendioCaloricoGiornateAllenamento * numeroAllenamenti);
        obiettivoCaloricoGiornaliero = (calorieMantenimentiSettimanali * bilancioEnergetico) / 7;

        obiettivoCaloricoSettimanale = obiettivoCaloricoGiornaliero * 7;

        Log.d(TAG, "massaMagra: " + massaMagra + "\n");
        Log.d(TAG, "metabolismoBasale: " + metabolismoBasale + "\n");
        Log.d(TAG, "calorieBruciateConAllenamento: " + calorieBruciateConAllenamento + "\n");
        Log.d(TAG, "calorieBruciateSenzaAllenamento: " + calorieBruciateSenzaAllenamento + "\n");
        Log.d(TAG, "dispendioCaloricoGiornateAllenamento: " + dispendioCaloricoGiornateAllenamento + "\n");
        Log.d(TAG, "calorieMantenimentiSettimanali: " + calorieMantenimentiSettimanali + "\n");
        Log.d(TAG, "obiettivoCaloricoGiornaliero: " + obiettivoCaloricoGiornaliero + "\n");
        Log.d(TAG, "obiettivoCaloricoSettimanale: " + obiettivoCaloricoSettimanale + "\n");

        updateTable();
    }

    private void updateTable(){
        obiettivo.setText("" + obiettivoCaloricoGiornaliero);
        double pro = peso * 1.85;
        double grass = (obiettivoCaloricoGiornaliero * 0.25) / 9;
        double carbo = (obiettivoCaloricoGiornaliero - pro * 4 - grass * 9) / 4.0;

        proteine.setText("Proteine " + pro);
        carboidrati.setText("Carbo " + carbo);
        grassi.setText("Grassi " + grass);
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