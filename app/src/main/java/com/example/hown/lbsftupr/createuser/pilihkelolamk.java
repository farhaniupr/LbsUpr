package com.example.hown.lbsftupr.createuser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.hown.lbsftupr.R;

public class pilihkelolamk extends AppCompatActivity {

    Button b1, b2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilihkelolamk);



        b1 = (Button) findViewById(R.id.buttonLantai1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchToMap();
                Intent intent = new Intent (pilihkelolamk.this, ubahmatakuliah.class);
                startActivity (intent);
            }
        } );

        b2 = (Button) findViewById(R.id.buttonLantai2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchToMap();
                Intent intent = new Intent (pilihkelolamk.this,  ubahmatakuliahUAS.class);
                startActivity (intent);
            }
        } );
    }
}
