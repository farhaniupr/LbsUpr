package com.example.hown.lbsftupr;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ActvityKelolaTT extends AppCompatActivity {

    EditText ed1;
    Button b1;

    DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_tt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ed1 = (EditText) findViewById(R.id.EDT);

        DatabaseReference refnama = FirebaseDatabase.getInstance().getReference()
                .child("tata_tertib");

        Query query = refnama.child("isi1");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String isi = (String) dataSnapshot.getValue();
                ed1.setText(isi);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        b1 = (Button) findViewById(R.id.buttonSimpan);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String is = ed1.getText().toString();

                db = FirebaseDatabase.getInstance().getReference().child("tata_tertib");

                db.child("isi1").setValue(is);

            }
        });


    }


}
