package com.example.wayoflife.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.wayoflife.util.Constants;
import com.example.wayoflife.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class FirstAccessActivity extends AppCompatActivity {

    private static final String TAG = "FirstAccessActivity";

    private TextInputEditText nicknameET;
    private TextInputEditText nomeET;
    private TextInputEditText cognomeET;
    private TextInputEditText pesoET;
    private TextInputEditText altezzaET;
    private TextInputEditText sessoET;

    private String nickname;
    private String nome;
    private String cognome;
    private String peso;
    private String altezza;
    private String sesso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_access);

        nicknameET = findViewById(R.id.etNickname);
        nomeET = findViewById(R.id.etNome);
        cognomeET = findViewById(R.id.etCognome);
        pesoET = findViewById(R.id.etPeso);
        altezzaET = findViewById(R.id.etAltezza);
        sessoET = findViewById(R.id.etSesso);
    }

    /**
     * Metodo chiamato quando si preme sul bottone che permette di salvare gli elementi
     * inseriti dall'utente
     * @param v
     */
    public void saveData(View v){
        nickname = nicknameET.getText().toString();
        nome = nomeET.getText().toString();
        cognome = cognomeET.getText().toString();
        peso = pesoET.getText().toString();
        altezza = altezzaET.getText().toString();
        sesso = sessoET.getText().toString();

        if(nickname.isEmpty() || nome.isEmpty() || cognome.isEmpty() ||
                peso.isEmpty() || altezza.isEmpty() || sesso.isEmpty()){
            Snackbar.make(findViewById(android.R.id.content), "Inserisci tutti i dati",
                    Snackbar.LENGTH_SHORT).show();
        }else{
            saveInformation();

            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    /**
     * Salvo le informazioni prelevate dai campi dentro al dispositivo
     */
    private void saveInformation(){
        SharedPreferences sharedPref = getSharedPreferences(
                Constants.PROFILE_INFO_FILENAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(Constants.NICKNAME, nickname);
        editor.putString(Constants.NOME, nome);
        editor.putString(Constants.COGNOME, cognome);
        editor.putString(Constants.PESO, peso);
        editor.putString(Constants.ALTEZZA, altezza);
        editor.putString(Constants.SESSO, sesso);

        editor.apply();
    }
}