package com.example.wayoflife.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.wayoflife.R;

public class InfoDialog extends AppCompatDialogFragment {

    private final String TAG = "HomeInfoDialog";
    private String classeDerivazione = "";

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view;

        if(classeDerivazione.equalsIgnoreCase("home")) {
            view = inflater.inflate(R.layout.home_info_dialog, null);
        } else
            view = inflater.inflate(R.layout.dashboard_info_dialog, null);

        builder.setView(view)
                .setTitle("Informazioni:")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        return builder.create();
    }

    public void setType(String tipo){
        classeDerivazione = tipo;
    }
}
