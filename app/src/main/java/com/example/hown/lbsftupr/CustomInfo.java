package com.example.hown.lbsftupr;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hown on 16-Feb-18.
 */

public class CustomInfo extends BaseAdapter {

    private ArrayList<String> matakuliah;


    private ArrayList<String> dsn_koor;

    private AppCompatActivity activity;

    private int x=0;


    public CustomInfo(ArrayList<String> matakuliah,ArrayList<String> dsn_koor,AppCompatActivity activity){
        this.matakuliah=matakuliah;

        this.dsn_koor=dsn_koor;

        this.activity=activity;
    }

    @Override
    public int getCount() {
        return matakuliah.size();
    }

    @Override
    public Object getItem(int i) {
        return matakuliah.get(i);
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.info_list_row,viewGroup,false);

        ((TextView)view.findViewById(R.id.matakuliahlistinfo)).setText(matakuliah.get(i));

        ((TextView)view.findViewById(R.id.dsn_koorlistinfo)).setText(dsn_koor.get(i));

        return view;
    }
}
