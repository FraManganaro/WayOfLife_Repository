package com.example.wayoflife.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.wayoflife.R;
import com.example.wayoflife.ui.HomeActivity;

public class TipsDialog extends AppCompatDialogFragment {

    private final String TAG = "TipsDialog";
    private String derivazione = "";

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view;

        if(derivazione.equalsIgnoreCase("IndiceAttivita")) {
            view = inflater.inflate(R.layout.indice_attivita_info_dialog, null);

        } else if(derivazione.equalsIgnoreCase("EffettoTermico")) {
            view = inflater.inflate(R.layout.effetto_termico_info_dialog, null);

        } else {
            view = inflater.inflate(R.layout.bilancio_energetico_info_dialog, null);
        }

        builder.setView(view)
                .setTitle("Informazioni sul calcolatore:")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        return builder.create();
    }

    public void setType(String tipo){
        derivazione = tipo;
    }
}
