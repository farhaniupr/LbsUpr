package com.example.hown.lbsftupr.createuser;

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

import com.example.hown.lbsftupr.R;
import com.example.hown.lbsftupr.model.Jadwal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ubahmatakuliah extends AppCompatActivity {

    private Spinner s1;
    private Button b1, b2, b3;

    private ListView l1;

    private TextView t1;

    final ArrayList<String> matakuliahs = new ArrayList<>();
    final ArrayList<String> dsn_koors = new ArrayList<>();

    final ArrayList<String> matakuliahls = new ArrayList<>();
    final ArrayList<String> dsn_koorls = new ArrayList<>();

    final ArrayList<String> mKeys1 = new ArrayList<>();

    private static final String TAG = ubahmatakuliah.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubahmatakuliah);


        l1 = (ListView) findViewById(R.id.listMKyangdiambil);

        s1 = (Spinner) findViewById(R.id.matakuliahyangdiambil);

        readData();

        b1 = (Button) findViewById(R.id.loadBtnMk); // delete
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = s1.getSelectedItemPosition();

                String mt1 = s1.getItemAtPosition(pos).toString();

                final String[] keyMK = new String[1];

                DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("jadwal");
                Query q1  =  db1.orderByChild("matakuliah").equalTo(mt1);
                q1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {
                            keyMK[0] = postSnapshot2.getKey();
                            Log.d(TAG, "key matakuliah yang akan dihapus : " +  keyMK[0]);
                        }
                        remPerson(keyMK[0]);
                        readData();
                        Toast.makeText(ubahmatakuliah.this, "Berhasil dihapus",
                                Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        b2 = (Button) findViewById(R.id.refreshMk); // refresh

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData();
            }
        });


        b3 = (Button) findViewById(R.id.addBtnMk); //add
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = s1.getSelectedItemPosition();

                String mt1 = s1.getItemAtPosition(pos).toString();

                final String[] keyMKa= new String[1];

                DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("jadwal");
                Query q1  =  db1.orderByChild("matakuliah").equalTo(mt1);
                q1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {
                            keyMKa[0] = postSnapshot2.getKey();
                            Log.d(TAG, "key matakuliah yang akan dihapus : " +  keyMKa[0]);
                        }
                        addPerson(keyMKa[0]);
                        readData();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    public void readData(){
        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        matakuliahls.clear();
        dsn_koorls.clear();
        final DatabaseReference dbJadwal= FirebaseDatabase.getInstance().getReference().child("jadwal");
        final DatabaseReference dbUserjadwal = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("jadwal");

        dbUserjadwal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();

                    mKeys1.add(key);

                    dbJadwal.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final Jadwal jadwal1 = dataSnapshot.getValue(Jadwal.class);

                            matakuliahls.add(jadwal1.getmatakuliah());
                            dsn_koorls.add(jadwal1.getDosen_koordinator());

                            ((custommatakuliah) (l1.getAdapter())).notifyDataSetChanged();
                            Log.d(TAG, "" + jadwal1);
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
        dbJadwal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()){
                    Jadwal jadwal1 = postSnapshot2.getValue(Jadwal.class);

                    matakuliahs.add(jadwal1.getmatakuliah());
                    dsn_koors.add(jadwal1.getDosen_koordinator());

                    Log.d(TAG, "isi Spinner " + matakuliahs);

                    //((custommatakuliah) (l1.getAdapter())).notifyDataSetChanged();

                    s1
                            .setAdapter(new ArrayAdapter<String>(ubahmatakuliah.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    matakuliahs));
                    s1
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
        ((ListView)findViewById(R.id.listMKyangdiambil)).
                setAdapter(new custommatakuliah(matakuliahls,dsn_koorls,this));
        ((ListView)findViewById(R.id.listMKyangdiambil)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                TextView t1 = (TextView) view.findViewById(R.id.matakuliahlistinfo);
                String text1 = t1.getText().toString();
                s1.setSelection(((ArrayAdapter<String>)s1.getAdapter()).getPosition(text1));
            }
        });
    }

    private void remPerson(final String key1){
        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference dbUserjadwal = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("jadwal").child(key1);
        dbUserjadwal.removeValue();
    }

    private void addPerson(final String key1){
        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference dbUserjadwal = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("jadwal");

        dbUserjadwal.child(key1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                   if (dataSnapshot.exists()) {
                       Toast.makeText(ubahmatakuliah.this, "Data Sudah Ada",
                               Toast.LENGTH_SHORT).show();
                   } else
                   {
                       Map<String, Object> ruangan = new HashMap<>();
                       ruangan.put(key1,true);
                       dbUserjadwal.updateChildren(ruangan);
                       Toast.makeText(ubahmatakuliah.this, "Berhasil ditambahkan",
                               Toast.LENGTH_SHORT).show();
                   }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

