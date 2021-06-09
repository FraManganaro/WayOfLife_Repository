package com.example.wayoflife.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.wayoflife.dialog.ProfileDialog;
import com.example.wayoflife.util.Constants;
import com.example.wayoflife.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileActivity extends AppCompatActivity implements ProfileDialog.ExampleDialogListener{

    public static final String TAG = "ProfileActivity";

    private FrameLayout frameLayout;

    private String nickname;
    private String nome;
    private String cognome;
    private String peso;
    private String altezza;
    private String sesso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /**
         * Controllore della navigation bar in basso
         */
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.profilo);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return false;

                    case R.id.allenamento:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return false;

                    case R.id.profilo:
                }
                return false;
            }
        });

        FloatingActionButton fb = findViewById(R.id.floatingButton);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        updateProfileData();
    }

    public void openDialog(){
        ProfileDialog profileDialog = new ProfileDialog();
        profileDialog.show(getSupportFragmentManager(), "Dialog per inserimento dati");
    }
    @Override
    public void applyTexts(String nickname, String nome, String cognome,
                           String peso, String altezza, String sesso) {

        SharedPreferences sharedPref = getSharedPreferences(
                Constants.PROFILE_INFO_FILENAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if(!nickname.isEmpty()) editor.putString(Constants.NICKNAME, nickname);
        if(!nome.isEmpty()) editor.putString(Constants.NOME, nome);
        if(!cognome.isEmpty()) editor.putString(Constants.COGNOME, cognome);
        if(!peso.isEmpty()) editor.putString(Constants.PESO, peso);
        if(!altezza.isEmpty()) editor.putString(Constants.ALTEZZA, altezza);
        if(!sesso.isEmpty()) editor.putString(Constants.SESSO, sesso);

        editor.apply();

        updateProfileData();
    }


    public void updateProfileData(){
        SharedPreferences sharedPref = getSharedPreferences(
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

        TextView tvNickname = findViewById(R.id.tvNickname);
        tvNickname.setText(nickname);

        TextView tvNome = findViewById(R.id.tvNome);
        tvNome.setText(nome);

        TextView tvCognome = findViewById(R.id.tvCognome);
        tvCognome.setText(cognome);

        TextView tvPeso = findViewById(R.id.tvPeso);
        tvPeso.setText(peso + " Kg");

        TextView tvAltezza = findViewById(R.id.tvAltezza);
        tvAltezza.setText(altezza + " cm");

        TextView tvSesso = findViewById(R.id.tvSesso);
        if(sesso.equalsIgnoreCase("maschio") ||
                sesso.equalsIgnoreCase("m"))
            tvSesso.setText("Maschio");
        else
            if (sesso.equalsIgnoreCase("femmina") ||
                    sesso.equalsIgnoreCase("f"))
                tvSesso.setText("Femmina");
    }
}