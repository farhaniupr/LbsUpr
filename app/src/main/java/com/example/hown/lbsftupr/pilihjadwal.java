package com.example.hown.lbsftupr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.hown.lbsftupr.schedule.SchedActivity;
import com.example.hown.lbsftupr.schedule.SchedFirebase;
import com.example.hown.lbsftupr.schedule.SchedMhs;
import com.example.hown.lbsftupr.schedule.SchedMhsU;

public class pilihjadwal extends AppCompatActivity {

    private Button b1, b2, b3, b4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilihjadwal);

        b1 = (Button) findViewById(R.id.buttonJadwalKuliah);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (pilihjadwal.this, SchedActivity.class);
                startActivity (intent);
            }
        } );



        b2 = (Button) findViewById(R.id.buttonJadwalUas);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchToMap();
                Intent intent = new Intent (pilihjadwal.this, SchedFirebase.class);
                startActivity (intent);
            }
        } );

        b3 = (Button) findViewById(R.id.buttonJKM);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchToMap();
                Intent intent = new Intent (pilihjadwal.this, SchedMhs.class);
                startActivity (intent);
            }
        } );

        b4 = (Button) findViewById(R.id.buttonJU);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchToMap();
                Intent intent = new Intent (pilihjadwal.this, SchedMhsU.class);
                startActivity (intent);
            }
        } );


    }
}
