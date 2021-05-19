package com.example.wayoflife.workouts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wayoflife.R;

import androidx.fragment.app.Fragment;

public class WorkoutFragment extends Fragment {

    private static final String TAG = "WorkoutFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workout, container, false);
    }
}
