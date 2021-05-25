package com.example.wayoflife.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wayoflife.R;
import com.example.wayoflife.dialog.TipsDialog;
import com.example.wayoflife.util.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class TipsActivity extends AppCompatActivity /* implements PopupMenu.OnMenuItemClickListener */ {

    public static final String TAG = "TipsActivity";

    /** Variabili per gestione dei box di input */
    private String pesoS;
    private String massaGrassaS;
    private String durataAllenamentoS;
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
    private TextInputEditText durataAllenamentoET;
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

    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        pesoET = findViewById(R.id.etPeso);
        massaGrassaET = findViewById(R.id.etMassaGrassa);
        durataAllenamentoET = findViewById(R.id.etDurataAllenamento);
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

        indiceAttivita = 0.0;
        effettoTermico = 0.0;
        bilancioEnergetico = 0.0;

        resumeDoubleInformation();
    }

    /** Controllo del primo BottomSheet */
    public void indiceAttivita(View v){
        TextView tv = findViewById(R.id.IAText);

        bottomSheetDialog = new BottomSheetDialog(TipsActivity.this,
                R.style.BottomSheetTheme);

        View sheetView = LayoutInflater.from(
                getApplicationContext()).inflate(R.layout.bs_indice_attivita,
                (ViewGroup) findViewById(R.id.indiceAttivitaBS));

        sheetView.findViewById(R.id.sedentario).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        indiceAttivita = 1;
                        tv.setText("Sedentario");
                        bottomSheetDialog.cancel();
                    }
                });
        sheetView.findViewById(R.id.sedentarioPlus).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        indiceAttivita = 1.1;
                        tv.setText("Sedentario Plus");
                        bottomSheetDialog.cancel();
                    }
                });
        sheetView.findViewById(R.id.att_media).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        indiceAttivita = 1.2;
                        tv.setText("Attività Media");
                        bottomSheetDialog.cancel();
                    }
                });
        sheetView.findViewById(R.id.att_moderata).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        indiceAttivita = 1.3;
                        tv.setText("Attività Moderata");
                        bottomSheetDialog.cancel();
                    }
                });
        sheetView.findViewById(R.id.att_elevata).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        indiceAttivita = 1.4;
                        tv.setText("Attività Elevata");
                        bottomSheetDialog.cancel();
                    }
                });

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    /** Controllo del secondo BottomSheet */
    public void effettoTermico(View v){
        TextView tv = findViewById(R.id.ETText);

        bottomSheetDialog = new BottomSheetDialog(TipsActivity.this,
                R.style.BottomSheetTheme);

        View sheetView = LayoutInflater.from(
                getApplicationContext()).inflate(R.layout.bs_effetto_termico,
                (ViewGroup) findViewById(R.id.effettoTermicoBS));

        sheetView.findViewById(R.id.tutto).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        effettoTermico = 1;
                        tv.setText("Mangi di Tutto");
                        bottomSheetDialog.cancel();
                    }
                });
        sheetView.findViewById(R.id.medio).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        effettoTermico = 1.1;
                        tv.setText("Mangi mediamente Pulito");
                        bottomSheetDialog.cancel();
                    }
                });
        sheetView.findViewById(R.id.pulito).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        effettoTermico = 1.25;
                        tv.setText("Mangi Pulito");
                        bottomSheetDialog.cancel();
                    }
                });

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    /** Controllo del terzo BottomSheet */
    public void bilancioEnergetico(View v){
        TextView tv = findViewById(R.id.BEText);

        bottomSheetDialog = new BottomSheetDialog(TipsActivity.this,
                R.style.BottomSheetTheme);

        View sheetView = LayoutInflater.from(
                getApplicationContext()).inflate(R.layout.bs_bilancio_energetico,
                (ViewGroup) findViewById(R.id.bilancioEnergeticoBS));

        sheetView.findViewById(R.id.definizioneAggressiva).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bilancioEnergetico = 0.7;
                        tv.setText("Definizione Aggressiva");
                        bottomSheetDialog.cancel();
                    }
                });
        sheetView.findViewById(R.id.definizione).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bilancioEnergetico = 0.8;
                        tv.setText("Definizione");
                        bottomSheetDialog.cancel();
                    }
                });
        sheetView.findViewById(R.id.mantenimento).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bilancioEnergetico = 1;
                        tv.setText("Mantenimento");
                        bottomSheetDialog.cancel();
                    }
                });
        sheetView.findViewById(R.id.massa).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bilancioEnergetico = 1.05;
                        tv.setText("Massa Magra");
                        bottomSheetDialog.cancel();
                    }
                });
        sheetView.findViewById(R.id.massaSpinta).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bilancioEnergetico = 1.15;
                        tv.setText("Massa Spinta");
                        bottomSheetDialog.cancel();
                    }
                });

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    /**
     * Chiamata con il bottone SALVA nel layout
     */
    public void saveData(View v) {

        pesoS = pesoET.getText().toString();
        massaGrassaS = massaGrassaET.getText().toString();
        durataAllenamentoS = durataAllenamentoET.getText().toString();
        numeroAllenamentiS = numeroAllenamentiET.getText().toString();

        if (pesoS.isEmpty() || massaGrassaS.isEmpty()  || indiceAttivita == 0.0 ||
                durataAllenamentoS.isEmpty()  || bilancioEnergetico == 0.0 ||
                numeroAllenamentiS.isEmpty() || effettoTermico == 0.0) {
            Snackbar.make(findViewById(android.R.id.content), "Inserisci tutti i dati",
                    Snackbar.LENGTH_SHORT).show();
        } else {
            saveInformation();
            resumeDoubleInformation();
            Toast.makeText(TipsActivity.this, "Tabella aggiornata!\nScorri per visualizzarla"
                    , Toast.LENGTH_SHORT).show();
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
        editor.putString(Constants.DURATA_ALLENAMENTO, durataAllenamentoS);
        editor.putString(Constants.NUMERO_ALLENAMENTI, numeroAllenamentiS);

        editor.putString(Constants.INDICE_ATTIVITA, "" + indiceAttivita);
        editor.putString(Constants.EFFETTO_TERMICO, "" + effettoTermico);
        editor.putString(Constants.BILANCIO_ENERGETICO, "" + bilancioEnergetico);

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
            durataAllenamentoET.setText("" + ((int) durataAllenamento));
            numeroAllenamentiET.setText("" + ((int) numeroAllenamenti));

            updateTextView();

            calculateCalories();
        }
    }

    /** Aggiornamento dei TextView relativi ai parametri sottostanti */
    private void updateTextView(){
        TextView et = findViewById(R.id.ETText);
        TextView be = findViewById(R.id.BEText);
        TextView ia = findViewById(R.id.IAText);

        if(indiceAttivita == 1.0){
            ia.setText("Sedentario");
        }
        if(indiceAttivita == 1.1){
            ia.setText("Sedentario Plus");
        }
        if(indiceAttivita == 1.2){
            ia.setText("Attività Media");
        }
        if(indiceAttivita == 1.3){
            ia.setText("Attività Moderata");
        }
        if(indiceAttivita == 1.4){
            ia.setText("Attività Elevata");
        }

        if(effettoTermico == 1){
            et.setText("Mangi di Tutto");
        }
        if(effettoTermico == 1.1){
            et.setText("Mangi mediamente Pulito");
        }
        if(effettoTermico == 1.25){
            et.setText("Mangi Pulito");
        }

        if(bilancioEnergetico == 0.7){
            be.setText("Definizione Aggressiva");
        }
        if(bilancioEnergetico == 0.8){
            be.setText("Definizione");
        }
        if(bilancioEnergetico == 1){
            be.setText("Mantenimento");
        }
        if(bilancioEnergetico == 1.05){
            be.setText("Massa Magra");
        }
        if(bilancioEnergetico == 1.15){
            be.setText("Massa Spinta");
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