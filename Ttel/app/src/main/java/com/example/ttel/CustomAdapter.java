package com.example.ttel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private Context applicationContext;
    private int sample;
    private List<jsonModel> jsonModels;

    CustomAdapter(Context applicationContext, int sample, List<jsonModel> jsonModels) {
        this.applicationContext = applicationContext;
        this.sample = sample;
        this.jsonModels = jsonModels;
    }

    @Override
    public int getCount() {
        return jsonModels.size();    }
    @Override
    public Object getItem(int i) {
        return jsonModels.get(i);    }
    @Override
    public long getItemId(int i) {
        return i;    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null)  {
            LayoutInflater layoutInflater = (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =  layoutInflater.inflate(R.layout.result_item,viewGroup,false);
        }
        TextView phoneNum,when,duration;

        phoneNum= view.findViewById(R.id.phoneNum);
        when=view.findViewById(R.id.when);
        duration=view.findViewById(R.id.duration);

        String jsonTime = jsonModels.get(i).getInfo().getTime();
        int hh = Integer.parseInt(jsonTime.substring(0,2));
        String mm = jsonTime.substring(2);
        String time;
        if (hh > 12){
            hh = hh - 12;
            time = "오후 " + Integer.toString(hh) + ":" + mm;
        } else{
            time = "오전 " + Integer.toString(hh) + ":" + mm;}

        phoneNum.setText(jsonModels.get(i).getInfo().getNumber());
        when.setText(time);
        duration.setText(jsonModels.get(i).getInfo().getDuration());

        return view;
    }
}