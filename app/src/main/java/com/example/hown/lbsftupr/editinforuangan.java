package com.example.hown.lbsftupr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hown.lbsftupr.model.Jadwal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class editinforuangan extends AppCompatActivity {

    public DatabaseReference databaseItem1, databaseItem2, db3, db4;
    public Spinner ed1, ed2;
    public Button b1, b2, b3, b4, b5;

    private static final String TAG = editinforuangan.class.getSimpleName();

    final ArrayList<String> matakuliahs = new ArrayList<>();
    final ArrayList<String> dsn_koors = new ArrayList<>();

    final ArrayList<String> matakuliahlengkap = new ArrayList<>();
    final ArrayList<String> dsn_koorslengkap = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editinforuangan);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        ed1 = (Spinner) findViewById(R.id.matakuliahinfo);
        //ed2 = (Spinner) findViewById(R.id.doseninfo);

        b1 = (Button) findViewById(R.id.addBtn);
        b2 = (Button) findViewById(R.id.refresh);
        b4 = (Button) findViewById(R.id.loadBtn);

        final String jadwall = (String) b.get("jadwal");

        databaseItem1 = FirebaseDatabase.getInstance().getReference().child("ruangan").child(jadwall).child("jadwal");

        //databaseItem1 = FirebaseDatabase.getInstance().getReference().child("ruangan");

        //Query q = databaseItem1.orderByChild("id").equalTo(jadwall);

        databaseItem2 = FirebaseDatabase.getInstance().getReference().child("jadwal");

        databaseItem2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()){
                    Jadwal jadwal1 = postSnapshot2.getValue(Jadwal.class);

                    matakuliahlengkap.add(jadwal1.getmatakuliah());
                    dsn_koorslengkap.add(jadwal1.getDosen_koordinator());

                    Log.d(TAG, "isi Spinner " + matakuliahlengkap+ dsn_koorslengkap);

                    ((CustomInfo) (((ListView) findViewById(R.id.listinfo)).getAdapter())).notifyDataSetChanged();

                    ed1
                            .setAdapter(new ArrayAdapter<String>(editinforuangan.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    matakuliahlengkap));
                    ed1
                            .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> arg0,
                                                           View arg1, int position, long arg3) {
                                    // TODO Auto-generated method stub
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                    // TODO Auto-generated method stub
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseItem1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()){
                    String key = postSnapshot2.getKey();
                    Log.d(TAG, "jadwal key " + key);
                    databaseItem2.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                Jadwal jad = dataSnapshot.getValue(Jadwal.class);
                                matakuliahs.add(jad.getmatakuliah());
                                dsn_koors.add(jad.getDosen_koordinator());
                                Log.d(TAG, "isi" + matakuliahs + dsn_koors);
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



        ((ListView)findViewById(R.id.listinfo)).
                setAdapter(new CustomInfo(matakuliahs,dsn_koors, this));
        ((ListView)findViewById(R.id.listinfo)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                TextView t1 = (TextView) view.findViewById(R.id.matakuliahlistinfo);
                TextView t3 = (TextView) view.findViewById(R.id.dsn_koorlistinfo);
                String text1 = t1.getText().toString();
                //String text3 = t3.getText().toString();

                ed1.setSelection(((ArrayAdapter<String>)ed1.getAdapter()).getPosition(text1));
                b4.setEnabled(true);

            }
        });

        final String[] key1 = new String[1];

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = ed1.getSelectedItemPosition();
                String mt1 = ed1.getItemAtPosition(pos).toString();
                db3 = FirebaseDatabase.getInstance().getReference().child("jadwal");
                Query q1  =  db3.orderByChild("matakuliah").equalTo(mt1);
                q1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {

                            key1[0] = postSnapshot2.getKey();

                            Log.d(TAG, "key matakuliah yang akan ditambah : " + key1[0]);


                        }
                        addPerson(key1[0]);
                        refresh();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }


        });

        //refresh
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = ed1.getSelectedItemPosition();

                String mt1 = ed1.getItemAtPosition(pos).toString();

                db3 = FirebaseDatabase.getInstance().getReference().child("jadwal");
                Query q1  =  db3.orderByChild("matakuliah").equalTo(mt1);
                q1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {
                            key1[0] = postSnapshot2.getKey();
                            Log.d(TAG, "key matakuliah yang akan dihapus : " + key1[0]);
                        }
                        remPerson(key1[0]);
                        Toast.makeText(editinforuangan.this, "Berhasil dihapus",
                                Toast.LENGTH_SHORT).show();
                        b4.setEnabled(false);
                        refresh();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        b4.setEnabled(false);
    }

    private void addPerson(final String key1){
        DatabaseReference dR2 = databaseItem1.child(key1);
        databaseItem1.child(key1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(editinforuangan.this, "Data sudah ada",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> ruangan = new HashMap<>();
                    //ruangan.put("id",id);
                    ruangan.put(key1,true);

                    databaseItem1.updateChildren(ruangan);
                    Toast.makeText(editinforuangan.this, "Berhasil ditambahkan",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void remPerson(final String key1){
        databaseItem1.child(key1).removeValue();
    }

    private void refresh(){
        databaseItem1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()){
                    String key = postSnapshot2.getKey();
                    Log.d(TAG, "jadwal key " + key);
                    databaseItem2.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Jadwal jad = dataSnapshot.getValue(Jadwal.class);
                            matakuliahs.add(jad.getmatakuliah());
                            dsn_koors.add(jad.getDosen_koordinator());
                            Log.d(TAG, "isi" + matakuliahs + dsn_koors);
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
        ((ListView)findViewById(R.id.listinfo)).
                setAdapter(new CustomInfo(matakuliahs,dsn_koors, this));
    }
}
