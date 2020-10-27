package com.example.hown.lbsftupr;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class pilihlantai extends AppCompatActivity {

    Button b1, b2;
    GpsTool gpsTool;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilihlantai);

        //if (gpsTool == null) {
        //    gpsTool = new GpsTool(this) {
        //       @Override
        //       public void onGpsLocationChanged(Location location) {
        //            super.onGpsLocationChanged(location);
        //            refreshLocation(location);

        //           Double altitude = location.getAltitude();
        //            if (altitude > 50.0) {
        //               b1.setEnabled(true);
        //              //b1.setClickable(true);
        //               b1.setEnabled(false);

        //          } else
        //           if (altitude < 50.0){
        //               b1.setEnabled(false);
                        //b1.setClickable(true);
        //                b1.setEnabled(true);
        //            }
        //       }
        //    };
        // }


        b1 = (Button) findViewById(R.id.buttonLantai2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchToMap();
                Intent intent = new Intent (pilihlantai.this, Lantai1.class);
                startActivity (intent);
            }
        } );

        //b2 = (Button) findViewById(R.id.buttonLantai1);
        //b2.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        //switchToMap();
        //        Intent intent = new Intent (pilihlantai.this, Lantai2.class);
        //        startActivity (intent);
        //    }
        //} );
    }


    private void refreshLocation(Location location) {

        Double altitude = location.getAltitude();

        if (altitude > 50.0) {
            //Toast.makeText(pilihlantai.this, "Anda berada di lantai 2", Toast.LENGTH_SHORT).show();

        } else
         if (altitude < 50.0){
            //Toast.makeText(pilihlantai.this, "Anda berada di lantai dasar", Toast.LENGTH_SHORT).show();
        } else
         {
             //Toast.makeText(pilihlantai.this, "Tidak diketahui ketinggian user", Toast.LENGTH_SHORT).show();
         }

    }

    @Override
    public void onBackPressed() {
        FirebaseUser currentUser = auth.getInstance().getCurrentUser();
       //auth.signOut();

        updateUI(currentUser);


        Intent i = new Intent(pilihlantai.this, MainActivity.class);
        //Intent i = new Intent(MainActivity.this, BerandaActivity.class);
        //i.putExtra("username", "");
        startActivity(i);
        finish();


        super.onBackPressed();
    }

    private void updateUI(FirebaseUser user) {
        if (user!=null)
        {
            if (user.isAnonymous()){
                auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent i = new Intent(pilihlantai.this, MainActivity.class);
                startActivity(i);
                finish();
            } else {

                Intent i = new Intent(pilihlantai.this, BerandaActivity.class);
                //Intent i = new Intent(MainActivity.this, BerandaActivity.class);
                i.putExtra("username", user.getEmail());
                startActivity(i);
                finish();
            }


        } else {
            //auth = FirebaseAuth.getInstance();
            //auth.signOut();
            Intent i = new Intent(pilihlantai.this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }
}
