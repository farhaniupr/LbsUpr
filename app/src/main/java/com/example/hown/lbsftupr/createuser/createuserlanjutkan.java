package com.example.hown.lbsftupr.createuser;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hown.lbsftupr.BerandaActivity;
import com.example.hown.lbsftupr.R;
import com.example.hown.lbsftupr.schedule.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class createuserlanjutkan extends AppCompatActivity {

    FirebaseUser user;
    EditText e1, e2, e3;

    Button b1,b2, b3;

    DatabaseReference databaseUserJadwal, databaseJadwal,  refnama1;

    ArrayList<Item> items = new ArrayList<Item>();
    ArrayList<String> mKeys1 = new ArrayList<String>();
    ArrayList<String> mk = new ArrayList<String>();
    ArrayList<String> ds = new ArrayList<String>();

    private static final String TAG = createuserlanjutkan.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuserlanjutkan);

        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String RepEmail = email.replace("@lbsupr.com", "");

        Intent i = getIntent();
        Bundle b = i.getExtras();

        e3 = (EditText) findViewById(R.id.editPassword);

        if (b!=null) {

            final String passwordUser = (String) b.get("password");

            e3.setText(passwordUser);
        } else {
            DatabaseReference refpass = FirebaseDatabase.getInstance().getReference()
                    .child("user").child(currentUserId).child("password");
                refpass.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        String passUSER = (String) dataSnapshot.getValue();
                        e3.setText(passUSER);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        }


        DatabaseReference refnama = FirebaseDatabase.getInstance().getReference()
                .child("user");

        Query query = refnama.child(currentUserId).child("nama");

        e1 = (EditText) findViewById(R.id.editUser);
        e2 = (EditText) findViewById(R.id.edipass);

        e1.setEnabled(false);

        b1 = (Button) findViewById(R.id.buttonSimpanAkun);

        b2 = (Button) findViewById(R.id.ubahmk);

        b3 = (Button) findViewById (R.id.buttonMKUAS);

        e2.setText(RepEmail);

        e2.setEnabled(false);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String namamhs = (String) dataSnapshot.getValue();

                e1.setText(namamhs);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseUserJadwal = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("jadwal");

        databaseJadwal = FirebaseDatabase.getInstance().getReference().child("jadwal");

        databaseUserJadwal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {
                        String key = postSnapshot.getKey();

                        mKeys1.add(key);

                        Log.d(TAG, "Key Matakuliah" + key);

                        databaseJadwal.child(key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Item item = dataSnapshot.getValue(Item.class);

                                mk.add(item.getmatakuliah());
                                ds.add(item.getdosen_koordinator());

                                //items.add(item);

                                ((custommatakuliah) (((ListView) findViewById(R.id.listView)).getAdapter())).notifyDataSetChanged();

                                Log.d(TAG, "" + item);


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                   }
                //}
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ((ListView)findViewById(R.id.listView)).
                setAdapter(new custommatakuliah(mk,ds, this));


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!e1.getText().toString().equals(null)) {

                    refnama1 = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId);

                    refnama1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //for (DataSnapshot d1 : dataSnapshot.getChildren())
                            //{
                            String nama_mhs = e1.getText().toString();
                            String password = e3.getText().toString();
                            Map<String, Object> user1 = new HashMap<>();
                            user1.put("nama", nama_mhs);
                            user1.put("password", password);

                            refnama1.updateChildren(user1);
                            //}

                            Toast.makeText(createuserlanjutkan.this, "Berhasil disimpan",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String newPassword = e3.getText().toString();

                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User password updated.");


                                        refnama1.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                //for (DataSnapshot d1 : dataSnapshot.getChildren())
                                                //{
                                                String nim_mhs = e2.getText().toString();
                                                String password = e3.getText().toString();
                                                Map<String, Object> user1 = new HashMap<>();
                                                user1.put("nim", nim_mhs);
                                                user1.put("password", password);

                                                refnama1.updateChildren(user1);
                                                //}

                                                Toast.makeText(createuserlanjutkan.this, "Berhasil disimpan",
                                                        Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                    }
                                }
                            });

                } else
                if (e1.getText().toString().equals(null)) {

                    refnama1 = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).push();

                    refnama1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //for (DataSnapshot d1 : dataSnapshot.getChildren())
                            //{
                            String nama_mhs = e1.getText().toString();
                            String nim = e2.getText().toString();
                            String password = e3.getText().toString();
                            Map<String, Object> user1 = new HashMap<>();
                            user1.put("nama", nama_mhs);
                            user1.put("nim", nim);
                            user1.put("password", password);

                            refnama1.updateChildren(user1);
                            //}

                            Toast.makeText(createuserlanjutkan.this, "Berhasil disimpan",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String newPassword = e3.getText().toString();


                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User password updated.");


                                        refnama1.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                //for (DataSnapshot d1 : dataSnapshot.getChildren())
                                                //{
                                                String nim_mhs = e2.getText().toString();
                                                String password = e3.getText().toString();
                                                Map<String, Object> user1 = new HashMap<>();
                                                user1.put("nim", nim_mhs);
                                                user1.put("password", password);

                                                refnama1.updateChildren(user1);
                                                //}

                                                Toast.makeText(createuserlanjutkan.this, "Berhasil disimpan",
                                                        Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                    }
                                }
                            });
                }



            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(createuserlanjutkan.this,  ubahmatakuliah.class);
                startActivity(i);
                finish();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(createuserlanjutkan.this,  ubahmatakuliahUAS.class);
                startActivity(i);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(createuserlanjutkan.this, BerandaActivity.class);
        startActivity(i);
        finish();

        super.onBackPressed();
    }


}
