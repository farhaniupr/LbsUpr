package com.example.hown.lbsftupr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ScrollingActivity extends AppCompatActivity {

    private TextView t1;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent i = getIntent();

        t1 = (TextView) findViewById(R.id.t1);

        DatabaseReference refnama = FirebaseDatabase.getInstance().getReference()
                .child("tata_tertib");

        Query query = refnama.child("isi1");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String isi1 = (String) dataSnapshot.getValue();

                t1.setText(isi1);
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
