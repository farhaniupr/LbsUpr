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
import com.example.hown.lbsftupr.model.jadwalU;
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

public class ubahmatakuliahUAS extends AppCompatActivity {

    private Spinner s1;
    private Button b1, b2, b3;

    private ListView l1;

    private TextView t1;

    final ArrayList<String> matakuliahs = new ArrayList<>();
    final ArrayList<String> dsn_koors = new ArrayList<>();

    final ArrayList<String> matakuliahls = new ArrayList<>();
    final ArrayList<String> dsn_koorls = new ArrayList<>();

    final ArrayList<String> mKeys1 = new ArrayList<>();

    //final ArrayList<String> dsn_koors = new ArrayList<>();


    private DatabaseReference databaseUserJadwal, databaseJadwal, databaseUserJadwalU;

    public DatabaseReference databaseItem1, databaseItem2, db3, db4;

    private static final String TAG = ubahmatakuliah.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_matakuliah_uas);

        l1 = (ListView) findViewById(R.id.listMKyangdiambilU);

        //readData();

        s1 = (Spinner) findViewById(R.id.matakuliahyangdiambilU);

        b1 = (Button) findViewById(R.id.loadBtnMkU);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = s1.getSelectedItemPosition();
                String mt1 = s1.getItemAtPosition(pos).toString();

                final String[] keyMK = new String[1];

                DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("jadwalU");
                Query q1  =  db1.orderByChild("matakuliah").equalTo(mt1);
                q1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {
                            keyMK[0] = postSnapshot2.getKey();
                            Log.d(TAG, "key matakuliah yang akan dihapus : " +  keyMK[0]);
                        }
                        remPerson(keyMK[0]);
                        Toast.makeText(ubahmatakuliahUAS.this, "Berhasil dihapus",
                                Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        b2 = (Button) findViewById(R.id.refreshMkU);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData();
            }
        });

        b3 = (Button) findViewById(R.id.addBtnMkU);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = s1.getSelectedItemPosition();

                String mt1 = s1.getItemAtPosition(pos).toString();

                final String[] keyMKa= new String[1];

                DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("jadwalU");
                Query q1  =  db1.orderByChild("matakuliah").equalTo(mt1);
                q1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {
                            keyMKa[0] = postSnapshot2.getKey();
                            Log.d(TAG, "key matakuliah yang akan dihapus : " +  keyMKa[0]);
                        }
                        addPerson(keyMKa[0]);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });




        readData();


    }

    public void readData(){
        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        matakuliahls.clear();
        dsn_koorls.clear();
        final DatabaseReference dbJadwal= FirebaseDatabase.getInstance().getReference().child("jadwalU");
        final DatabaseReference dbUserjadwal = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("jadwalU");

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
                            final jadwalU jadwal1 = dataSnapshot.getValue(jadwalU.class);

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
                    jadwalU jadwal1 = postSnapshot2.getValue(jadwalU.class);

                    matakuliahs.add(jadwal1.getmatakuliah());
                    dsn_koors.add(jadwal1.getDosen_koordinator());

                    Log.d(TAG, "isi Spinner " + matakuliahs);

                    //((custommatakuliah) (l1.getAdapter())).notifyDataSetChanged();

                    s1
                            .setAdapter(new ArrayAdapter<String>(ubahmatakuliahUAS.this,
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
        ((ListView)findViewById(R.id.listMKyangdiambilU)).
                setAdapter(new custommatakuliah(matakuliahls,dsn_koorls,this));
        ((ListView)findViewById(R.id.listMKyangdiambilU)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        final DatabaseReference dbUserjadwal = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("jadwalU").child(key1);
        dbUserjadwal.removeValue();
    }

    private void addPerson(final String key1){
        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference dbUserjadwal = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("jadwalU");

        dbUserjadwal.child(key1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(ubahmatakuliahUAS.this, "Data Sudah Ada",
                            Toast.LENGTH_SHORT).show();
                } else
                {
                    Map<String, Object> ruangan = new HashMap<>();
                    ruangan.put(key1,true);
                    dbUserjadwal.updateChildren(ruangan);
                    Toast.makeText(ubahmatakuliahUAS.this, "Berhasil ditambahkan",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
