package com.example.wayoflife.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class MyArrayAdapter extends BaseAdapter {

    private List<CustomerModel> workoutList;
    private Activity activity;

    public MyArrayAdapter(List<CustomerModel> workoutList, Activity activity) {
        this.workoutList = workoutList;
        this.activity = activity;
    }

    @Override
    public int getCount() { return workoutList.size(); }

    @Override
    public Object getItem(int position) { return workoutList.get(position);
    }

    @Override
    public long getItemId(int position) { return 0; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;   //da fare
    }
}
