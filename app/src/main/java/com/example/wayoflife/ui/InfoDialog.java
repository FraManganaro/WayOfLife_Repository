package com.example.wayoflife.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.wayoflife.R;
import com.example.wayoflife.workouts.EndWorkoutActivity;

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
        } else if(classeDerivazione.equalsIgnoreCase("dashboard")) {
            view = inflater.inflate(R.layout.dashboard_info_dialog, null);
        } else {
            view = inflater.inflate(R.layout.dashboard_info_dialog, null);

            builder.setView(view)
                    .setTitle("Attiva il GPS:")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Torna alla Home", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(view.getContext(), HomeActivity.class);
                            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });

            return builder.create();
        }

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
