package com.example.wayoflife.workouts.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wayoflife.R;
import com.example.wayoflife.workouts.util.WorkoutModel;

import java.util.List;

public class MyListViewElement extends BaseAdapter {

    private List<WorkoutModel> workoutList;
    private Activity activity;

    public MyListViewElement(List<WorkoutModel> workoutList, Activity activity) {
        this.workoutList = workoutList;
        this.activity = activity;
    }

    @Override
    public int getCount() { return workoutList.size(); }

    @Override
    public WorkoutModel getItem(int position) {
        return workoutList.get(position);
    }

    @Override
    public long getItemId(int position) { return 0; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = activity.getLayoutInflater().inflate(
                    R.layout.history_element,
                    parent,
                    false);
        }

        ((TextView) convertView.findViewById(R.id.titolo)).setText(getItem(position).getNome());
        ((TextView) convertView.findViewById(R.id.data)).setText("Data: " +getItem(position).getData());
        ((TextView) convertView.findViewById(R.id.durata)).setText("Durata: " +getItem(position).getDurata());
        ((TextView) convertView.findViewById(R.id.calorie)).setText("Calorie: " + getItem(position).getCalorie());

        int like = workoutList.get(position).getLike();
        if(like == 1) {
            ((ImageView) convertView.findViewById(R.id.favorites)).setVisibility(View.VISIBLE);
        } else {
            ((ImageView) convertView.findViewById(R.id.favorites)).setVisibility(View.INVISIBLE);
        }

        String tipologia = workoutList.get(position).getTipologia();

        switch(tipologia){
            case "Camminata":
                ((ImageView) convertView.findViewById(R.id.image)).
                        setImageDrawable(activity.getDrawable(R.drawable.ic_walking_color));
                break;
            case "Corsa":
                ((ImageView) convertView.findViewById(R.id.image)).
                        setImageDrawable(activity.getDrawable(R.drawable.ic_running_color));
                break;
            case "Ciclismo":
                ((ImageView) convertView.findViewById(R.id.image)).
                        setImageDrawable(activity.getDrawable(R.drawable.ic_cycling_color));
                break;
            case "Freestyle":
                ((ImageView) convertView.findViewById(R.id.image)).
                        setImageDrawable(activity.getDrawable(R.drawable.ic_workout_color));
                break;
            case "Pushup":
                ((ImageView) convertView.findViewById(R.id.image)).
                        setImageDrawable(activity.getDrawable(R.drawable.ic_pushup_color));
                break;
            case "Squat":
                ((ImageView) convertView.findViewById(R.id.image)).
                        setImageDrawable(activity.getDrawable(R.drawable.ic_squat));
                break;
            case "Calcio":
                ((ImageView) convertView.findViewById(R.id.image)).
                        setImageDrawable(activity.getDrawable(R.drawable.ic_football));
                break;
            case "Basket":
                ((ImageView) convertView.findViewById(R.id.image)).
                        setImageDrawable(activity.getDrawable(R.drawable.ic_basketball));
                break;
            case "Scalini":
                ((ImageView) convertView.findViewById(R.id.image)).
                        setImageDrawable(activity.getDrawable(R.drawable.ic_stairs));
                break;
            case "Nuoto":
                ((ImageView) convertView.findViewById(R.id.image)).
                        setImageDrawable(activity.getDrawable(R.drawable.ic_swimmer));
                break;
            case "Tennis":
                ((ImageView) convertView.findViewById(R.id.image)).
                        setImageDrawable(activity.getDrawable(R.drawable.ic_tennis_racket));
                break;
        }

        return convertView;
    }
}
