package com.example.wayoflife.workouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.wayoflife.Constants;
import com.example.wayoflife.R;
import com.example.wayoflife.ui.HomeActivity;

public class EndWorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_workout);

        TextView textView = findViewById(R.id.textView12);

        String message = getIntent().getStringExtra(Constants.ATTIVITA_RILEVATA);
        long tempoPassato = getIntent().getLongExtra(Constants.TEMPO_PASSATO, 0);

        textView.setText(message);
        textView.setText(""+tempoPassato);
    }

    public void saveWorkout(View v){
        //devo salvare l'allenamento

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}