package com.example.hown.lbsftupr;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hown on 20-Feb-18.
 */

public class Custominforuanganjadwal  extends BaseAdapter {

    private ArrayList<String> idruangan;

    private AppCompatActivity activity;

    private int x=0;


    public Custominforuanganjadwal(ArrayList<String> idruangan, AppCompatActivity activity){
        this.idruangan=idruangan;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return idruangan.size();
    }

    @Override
    public Object getItem(int i) {
        return idruangan.get(i);
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.inforuanganjadwal,viewGroup,false);

        ((TextView)view.findViewById(R.id.idruangan)).setText(idruangan.get(i));

        //((TextView)view.findViewById(R.id.dsn_koorlistinfo)).setText(dsn_koor.get(i));

        return view;
    }


}
