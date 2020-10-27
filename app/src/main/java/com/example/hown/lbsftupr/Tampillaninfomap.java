package com.example.hown.lbsftupr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hown.lbsftupr.model.Jadwal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tampillaninfomap extends AppCompatActivity {

    private static final String TAG = "test";
    //ArrayList<String> matakuliahs = new ArrayList<>();
    final ArrayList<String> jams = new ArrayList<>();
    final ArrayList<String> lokasis = new ArrayList<>();
    //ArrayList<String> dsn_koors = new ArrayList<>();

    String title, l;


    ListView l1;

    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampillaninfomap);

        Bundle b = getIntent().getExtras();

       // ArrayList<String> matakuliahs = b.getStringArrayList("matakuliah");
       // ArrayList<String> dsn_koors  = b.getStringArrayList("dosen");

        //Log.i("List", "Passed Array List :: " + matakuliahs);
       // Log.i("List", "Passed Array List :: " + dsn_koors);


        //if (b!= null) {

            title = (String) b.get("title");

            l = (String) b.get("lantai");

            t1 = (TextView) findViewById(R.id.textView29);

            t1.setText(title);
        //}

        DatabaseReference databaseItem12 = FirebaseDatabase.getInstance().getReference().child("ruangan");
        Query q = databaseItem12.orderByChild("id").equalTo(title);

        //Query q = databaseItem1.orderByChild("id").equalTo(marker.getTitle());

        final DatabaseReference databaseItem2 = FirebaseDatabase.getInstance().getReference().child("jadwal");

        final ArrayList<String> matakuliahs = new ArrayList<>();
        final ArrayList<String> jams = new ArrayList<>();
        final ArrayList<String> lokasis = new ArrayList<>();
        final ArrayList<String> dsn_koors = new ArrayList<>();

    q.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {
                    String keyId = postSnapshot2.getKey();
                    Log.d(TAG, "jadwal key " + keyId);

                    DatabaseReference databaseItem1 = FirebaseDatabase.getInstance().getReference().child("ruangan").child(keyId).child("jadwal");

                    databaseItem1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {
                                String key = postSnapshot2.getKey();

                                //Log.d(TAG, "jadwal key " + key);


                                databaseItem2.child(key).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot postSnapshot1 : dataSnapshot.getChildren()) {
                                                Jadwal jad = postSnapshot1.getValue(Jadwal.class);


                                                matakuliahs.add(jad.getmatakuliah());
                                                dsn_koors.add(jad.getDosen_koordinator());

                                                l1 = (ListView) findViewById(R.id.listinfomap);
                                                l1.setAdapter(new CustomInfo(matakuliahs, dsn_koors, Tampillaninfomap.this));
                                            }

                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
              }
            }


        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
    }

    @Override
    public void onBackPressed() {

        if (l.equals("l1")) {
            Intent i = new Intent(Tampillaninfomap.this, Lantai1.class);

            startActivity(i);
            finish();

        }
        else if (l.equals("l2"))
        {
            Intent i = new Intent(Tampillaninfomap.this, Lantai2.class);

            startActivity(i);
            finish();
        }
        else {
            Intent i = new Intent(Tampillaninfomap.this, pilihlantai.class);
            startActivity(i);
            finish();
        }
        super.onBackPressed();
    }
}
