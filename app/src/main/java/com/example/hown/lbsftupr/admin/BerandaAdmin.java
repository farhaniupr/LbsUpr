package com.example.hown.lbsftupr.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.hown.lbsftupr.ActvityKelolaTT;
import com.example.hown.lbsftupr.MainActivity;
import com.example.hown.lbsftupr.R;
import com.example.hown.lbsftupr.kelolaRuangan;
import com.example.hown.lbsftupr.pilih_hari_UAS;
import com.example.hown.lbsftupr.pilih_kelola_ruangan;
import com.example.hown.lbsftupr.update_jadwal_hari;
import com.google.firebase.auth.FirebaseAuth;

public class BerandaAdmin extends AppCompatActivity {



    private Button b1;
    private Button b2;
    private Button b3, b4, b5, b6;


    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda_admin);

        auth = FirebaseAuth.getInstance();


        b6 = (Button) findViewById(R.id.buttonTest);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (BerandaAdmin.this, ImportExcel.class);
                startActivity (intent);
            }
        });

        b1 = (Button) findViewById(R.id.buttonLihatRuangan);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent (BerandaAdmin.this, CreateRuangan.class);
              //  startActivity (intent);

                auth.signOut();
                Intent intent = new Intent (BerandaAdmin.this, MainActivity.class);
                startActivity (intent);
            }
        } );

        b3 = (Button) findViewById(R.id.buttonUpdateRuangan);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (BerandaAdmin.this, kelolaRuangan.class);
                intent.putExtra("altitude", "10");
                startActivity(intent);
                //startActivity (intent);
            }
        } );

        b2 = (Button) findViewById(R.id.buttonUpdateJadwalKuliah);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchToMap();
                Intent intent = new Intent (BerandaAdmin.this, update_jadwal_hari.class);
                startActivity (intent);
            }
        } );

        b4 = (Button) findViewById(R.id.button2);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchToMap();
                Intent intent = new Intent (BerandaAdmin.this, ActvityKelolaTT.class);
                startActivity (intent);
            }
        } );

       /** b5 = (Button) findViewById(R.id.buttonAkunAdmin);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchToMap();
                Intent intent = new Intent (BerandaAdmin.this, KelolaAkun.class);
                startActivity (intent);
            }
        } );**/
    }
}
