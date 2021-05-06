package com.example.wayoflife.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.wayoflife.Constants;
import com.example.wayoflife.R;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileDialog extends AppCompatDialogFragment {

    private TextInputEditText nicknameET;
    private TextInputEditText nomeET;
    private TextInputEditText cognomeET;
    private TextInputEditText pesoET;
    private TextInputEditText altezzaET;
    private TextInputEditText sessoET;



    private LinearLayout llNome;

    private String nickname;
    private String nome;
    private String cognome;
    private String peso;
    private String altezza;
    private String sesso;

    private final String TAG = "DialogProfile";
    private ExampleDialogListener listener;


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.profile_dialog, null);

        builder.setView(view)
                .setTitle("Aggiorna dati profilo")
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Salva", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nickname = nicknameET.getText().toString();
                        nome = nomeET.getText().toString();
                        cognome = cognomeET.getText().toString();
                        peso = pesoET.getText().toString();
                        altezza = altezzaET.getText().toString();
                        sesso = sessoET.getText().toString();

                        listener.applyTexts(nickname, nome, cognome, peso, altezza, sesso);
                    }
                });

        restoreInformation();

        /** metto come valore di default i valori attuali */
        nicknameET = view.findViewById(R.id.etNickname);
        nicknameET.setText(nickname);
        nomeET = view.findViewById(R.id.etNome);
        nomeET.setText(nome);
        cognomeET = view.findViewById(R.id.etCognome);
        cognomeET.setText(cognome);
        pesoET = view.findViewById(R.id.etPeso);
        pesoET.setText(peso);
        altezzaET = view.findViewById(R.id.etAltezza);
        altezzaET.setText(altezza);
        sessoET = view.findViewById(R.id.etSesso);
        sessoET.setText(sesso);

        return builder.create();
    }

    /** Recupero le infortmazioni del profilo per mostrarle nel form */
    public void restoreInformation(){
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                Constants.PROFILE_INFO_FILENAME,
                Context.MODE_PRIVATE);

        nickname = sharedPref.getString(
                Constants.NICKNAME, "Nickname");
        nome = sharedPref.getString(
                Constants.NOME, "Nome");
        cognome = sharedPref.getString(
                Constants.COGNOME, "Cognome");
        peso = sharedPref.getString(
                Constants.PESO, "Peso");
        altezza = sharedPref.getString(
                Constants.ALTEZZA, "Altezza");
        sesso = sharedPref.getString(
                Constants.SESSO, "Sesso");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener {
        void applyTexts(String nickname, String nome, String cognome,
                        String peso, String altezza, String sesso);
    }
}
