package com.example.hown.lbsftupr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class update_jadwal_hari extends AppCompatActivity {


    private Button b1;
    private Button b2;
    private Button b3, b4, b5, b6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_jadwal_hari);


        b1 = (Button) findViewById(R.id.buttonSenin);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (update_jadwal_hari.this, kelola_jadwalK.class);
                intent.putExtra("hari", "Senin");
                startActivity (intent);
            }
        } );

        b3 = (Button) findViewById(R.id.button4);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (update_jadwal_hari.this, kelola_jadwalK.class);
                intent.putExtra("hari", "Selasa");
                startActivity (intent);
            }
        } );

        b2 = (Button) findViewById(R.id.button5);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (update_jadwal_hari.this, kelola_jadwalK.class);
                intent.putExtra("hari", "Rabu");
                startActivity (intent);
            }
        } );

        b4 = (Button) findViewById(R.id.button7);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (update_jadwal_hari.this, kelola_jadwalK.class);
                intent.putExtra("hari", "Kamis");
                startActivity (intent);
            }
        } );

        b5 = (Button) findViewById(R.id.button8);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (update_jadwal_hari.this, kelola_jadwalK.class);
                intent.putExtra("hari", "Jumat");
                startActivity (intent);
            }
        } );

        b6 = (Button) findViewById(R.id.button3);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (update_jadwal_hari.this, kelola_jadwalK.class);
                intent.putExtra("hari", "Sabtu");
                startActivity (intent);
            }
        } );




    }

}
