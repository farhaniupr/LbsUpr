package com.example.hown.lbsftupr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class pilih_kelola_ruangan extends AppCompatActivity {

    private Button b1, b2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_kelola_ruangan);

        b1 = (Button) findViewById(R.id.bL1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pilih_kelola_ruangan.this, kelolaRuangan.class);
                intent.putExtra("altitude", "10");
                startActivity(intent);

            }
        });


        /**b2 = (Button) findViewById(R.id.bL2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pilih_kelola_ruangan.this, kelolaRuangan.class);
                intent.putExtra("altitude", "60");
                startActivity(intent);

            }
        });*/
    }
}
