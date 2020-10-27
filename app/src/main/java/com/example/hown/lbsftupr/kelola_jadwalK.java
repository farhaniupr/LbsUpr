package com.example.hown.lbsftupr;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hown.lbsftupr.model.Jadwal;
import com.example.hown.lbsftupr.model.ju;
import com.example.hown.lbsftupr.reminderr.utils.DateAndTimeUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;

public class kelola_jadwalK extends AppCompatActivity {


    private static final String TAG = kelola_jadwalK.class.getSimpleName();
    private DatabaseReference myDatabaseReference, dbCariKeyMK, dbnext, myd;
    private Query q;
    private String personId;
    private EditText e1;
    Spinner s1, s2;


    private TextView t1, t2, t3, t4, t5;

    String text1, text2, text3, text4, text5;



    private Button b1, b2, b3, b4, b5, b6;

    String  keynext, kAN;

    final String[] key = new String[1];

    final String[] keyMk = new String[1];

    final String[] keyA = new String[1];

    private EditText ed4, ed2, edMK;

    Calendar myCalendar;
    final int[] pos = new int[1];
    final int[] pos2 = new int[1];
    String hari, A, B;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_jadwal_k);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        hari = (String) b.get("hari");
        myCalendar = Calendar.getInstance();

        edMK = (EditText) findViewById(R.id.matakuliah);
        ed2 = (EditText) findViewById(R.id.jam);
        s2 = (Spinner) findViewById(R.id.editKodeMK);
        s1 = (Spinner) findViewById(R.id.dsn_koork);
        ed4 = (EditText) findViewById(R.id.lokasi);

        pos[0] = s1.getSelectedItemPosition();
        pos2[0] = s2.getSelectedItemPosition();

        edMK.setEnabled(false);
        s2.setEnabled(true);

        b1 = (Button) findViewById(R.id.buttonKelolak);
        b2 = (Button) findViewById(R.id.addBtnk);
        b3 = (Button) findViewById(R.id.updateBtnk);
        b4 = (Button) findViewById(R.id.deleteBtnk);
        b5 = (Button) findViewById(R.id.findBtnk);  //cancel
        b6 = (Button) findViewById(R.id.loadBtnk);

        myDatabaseReference=FirebaseDatabase.getInstance().getReference().child("jadwal");

        q = myDatabaseReference.orderByChild("hari").equalTo(hari);

        //ArrayAdapter<String> adapter;
        List<String> list;

        list = new ArrayList<String>();
        list.add("Petugas 1");
        list.add("Petugas 2");
        list.add("Petugas 3");
        list.add("Petugas 4");
        list.add("Petugas 5");
        list.add("Petugas 6");
        list.add("-");

        /**adapter = new ArrayAdapter<String>(kelola_jadwalK.this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);*/
        s1.setAdapter(new ArrayAdapter<String>(kelola_jadwalK.this,
                android.R.layout.simple_spinner_dropdown_item,
                list));
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        List<String> list2;
        final List<String> list3 = new ArrayList<String>();

        list2 = new ArrayList<String>();
        list2.add("BI 111");//1
        list2.add("IKOM 123");//2
        list2.add("LOGMAT 333");//3
        list2.add("KAL 321");//4
        list2.add("BING 234");//5
        list2.add("SI 252");//6

        list3.add("Bahasa Indonesia");
        list3.add("Ilmu Komputer");
        list3.add("Logika Matematika");
        list3.add("Kalkulus");
        list3.add("Bahasa Inggris");
        list3.add("Sistem Informasi");


        /**adapter = new ArrayAdapter<String>(kelola_jadwalK.this,
         android.R.layout.simple_spinner_item, list);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         s1.setAdapter(adapter);*/
        s2.setAdapter(new ArrayAdapter<String>(kelola_jadwalK.this,
                android.R.layout.simple_spinner_dropdown_item,
                list2));
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edMK.setText(list3.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ed4.setEnabled(false);

        ed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((EditText)findViewById(R.id.jam)).getText().toString().trim().isEmpty()){
                    (findViewById(R.id.findBtnk)).setEnabled(true);
                    Toast.makeText(kelola_jadwalK.this, "jam tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else {
                    pos2[0] = s2.getSelectedItemPosition();

                    dbnext = myDatabaseReference.child(s2.getItemAtPosition(pos2[0]).toString());
                    myDatabaseReference.orderByChild("kodeMk").equalTo(s2.getItemAtPosition(pos2[0]).toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){

                                Toast.makeText(kelola_jadwalK.this, "Kode MK sudah ada ganti yang lain", Toast.LENGTH_SHORT).show();
                            } else
                                {
                                    //dbnext.child()
                               /* myDatabaseReference.orderByChild("hari").equalTo(hari).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){


                                            A = "ada";

                                            /*for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                                //jadwalkey[0] = childSnapshot.getKey();
                                                keyA[0] = childSnapshot.getKey();
                                                kAN = keyA[0];
                                                Log.d("Key Kelola Ruangan :", keyA[0]);
                                                //Toast.makeText(kelola_jadwalK.this, key[0], Toast.LENGTH_SHORT).show();
                                            }*/

                                            /*myDatabaseReference.orderByChild("jam").equalTo(ed2.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()){

                                                        Toast.makeText(kelola_jadwalK.this, "Hari dan Jam yang sama sudah ada, ganti yang lain", Toast.LENGTH_SHORT).show();
                                                    } else
                                                    {
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
                                });*/

                                    pos[0] = s1.getSelectedItemPosition();
                                    pos2[0] = s2.getSelectedItemPosition();
                                    addPerson(s2.getItemAtPosition(pos2[0]).toString(), hari, s1.getItemAtPosition(pos[0]).toString(),
                                            ((EditText) findViewById(R.id.jam)).getText().toString(), ((EditText) findViewById(R.id.lokasi)).getText().toString(), edMK.getText().toString());
                                    readData();
                                    (findViewById(R.id.findBtnk)).setEnabled(false);
                                    Toast.makeText(kelola_jadwalK.this, "Berhasil ditambahkan", Toast.LENGTH_SHORT).show();


                                }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EditText) findViewById(R.id.jam)).setText("");
                ((EditText) findViewById(R.id.lokasi)).setText("");
                //((EditText) findViewById(R.id.matakuliah)).setText("");
                //s2.setEnabled(true);
                edMK.setEnabled(false);
                s2.setEnabled(true);

                (findViewById(R.id.addBtnk)).setEnabled(true);
                (findViewById(R.id.loadBtnk)).setEnabled(true);
                (findViewById(R.id.deleteBtnk)).setEnabled(true);
                (findViewById(R.id.updateBtnk)).setEnabled(true);
                (findViewById(R.id.findBtnk)).setEnabled(false);
                readData();

            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((EditText)findViewById(R.id.jam)).getText().toString().trim().isEmpty()){
                    (findViewById(R.id.findBtnk)).setEnabled(true);
                    Toast.makeText(kelola_jadwalK.this, "jam tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    pos[0] = s1.getSelectedItemPosition();
                    pos2[0] = s2.getSelectedItemPosition();
                    //(String matakuliah, final String jam, final String dsn_koors, final String lokasi)
                    updatejadwal(s2.getItemAtPosition(pos2[0]).toString(), edMK.getText().toString(), ((EditText)findViewById(R.id.jam)).getText().toString(),s1.getItemAtPosition(pos[0]).toString());
                    readData();
                    (findViewById(R.id.findBtnk)).setEnabled(false);
                    Toast.makeText(kelola_jadwalK.this, "Berhasil dubah", Toast.LENGTH_SHORT).show();
                    s2.setEnabled(true);
                }
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((EditText)findViewById(R.id.jam)).getText().toString().trim().isEmpty()){
                    (findViewById(R.id.findBtnk)).setEnabled(true);
                    Toast.makeText(kelola_jadwalK.this, "jam tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else
                if (((EditText)findViewById(R.id.lokasi)).getText().toString().trim().isEmpty()){
                    (findViewById(R.id.findBtnk)).setEnabled(true);
                    pos[0] = s1.getSelectedItemPosition();
                    pos2[0] = s2.getSelectedItemPosition();
                    removePerson(edMK.getText().toString(),((EditText)findViewById(R.id.jam)).getText().toString(),s1.getItemAtPosition(pos[0]).toString(),((EditText)findViewById(R.id.lokasi)).getText().toString());
                    readData();
                    (findViewById(R.id.findBtnk)).setEnabled(false);
                    Toast.makeText(kelola_jadwalK.this, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                    s2.setEnabled(true);


                } else
                {
                    Toast.makeText(kelola_jadwalK.this, "Hapus ruangan di edit lokasi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    myDatabaseReference.orderByChild("matakuliah").equalTo(edMK.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                //jadwalkey[0] = childSnapshot.getKey();
                                key[0] = childSnapshot.getKey();
                                keynext = key[0];
                                Log.d("Key Kelola Ruangan :", key[0]);
                            }

                            Intent intent = new Intent(kelola_jadwalK.this, kelola_info_ruangan_jadwal.class);
                            intent.putExtra("jadwal", keynext);
                            intent.putExtra("hari", hari);
                            intent.putExtra("jam", ((EditText)findViewById(R.id.jam)).getText().toString());
                            Log.d(TAG, "Isi Edit Nama Ruangan" + keynext);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData();
            }
        });

//end create
        readData();

    }

    private void addPerson(String kdMK, String hari, String dosen_koordinator, String jam, String lokasi, String matakuliah){
        Jadwal jadwals= new Jadwal(hari, dosen_koordinator, jam, lokasi, matakuliah);
        myDatabaseReference=FirebaseDatabase.getInstance().getReference().child("jadwal");
        String personId = myDatabaseReference.push().getKey();
        myDatabaseReference.child(kdMK).setValue(jadwals);
    }

    @OnClick(R.id.jam)
    public void timePicker() {
    TimePickerDialog TimePicker = new TimePickerDialog(kelola_jadwalK.this, new TimePickerDialog.OnTimeSetListener() {
    @Override
    public void onTimeSet(android.widget.TimePicker timePicker, int hour, int minute) {
        myCalendar.set(Calendar.HOUR_OF_DAY, hour);
        myCalendar.set(Calendar.MINUTE, minute);
        ed2.setText(DateAndTimeUtil.toStringReadableTime(myCalendar, getApplicationContext()));
        }
         }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));
    TimePicker.show();
    }

    private void removePerson(final String matakuliah, final String jam, final String dsn_koors, final String lokasi){


        EditText ed2 = (EditText) findViewById(R.id.jam);
        s1 = (Spinner) findViewById(R.id.dsn_koork);
        EditText ed4 = (EditText) findViewById(R.id.lokasi);

        //String e1 = s2.getItemAtPosition(pos2[0]).toString();


        final String[] jadwalkey = new String[1];
        myDatabaseReference.orderByChild("matakuliah").equalTo(matakuliah).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // for (DataSnapshot childSnapshot1 : childSnapshot.getChildren()) {
                    //jadwalkey[0] = childSnapshot.getKey();
                    jadwalkey[0] = childSnapshot.getKey();
                    Log.d("Key", jadwalkey[0]);
                    Toast.makeText(kelola_jadwalK.this, jadwalkey[0], Toast.LENGTH_SHORT).show();
                    //mylist.add(jadwalkey);
                }
                //}
                DatabaseReference d1 = myDatabaseReference.child(jadwalkey[0]);

                d1.removeValue();


                //FirebaseDatabase.getInstance().getReference().child(jadwalkey[0]).removeValue();

            }

                //d1.updateChildren(jadwal);



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void readData(){
        final ArrayList<String> matakuliahs = new ArrayList<>();
        final ArrayList<String> jams = new ArrayList<>();
        final ArrayList<String> lokasis = new ArrayList<>();
        final ArrayList<String> dsn_koors = new ArrayList<>();

        myd = FirebaseDatabase.getInstance().getReference().child("jadwal");
        //q = myd.orderByChild("hari").equalTo("Senin");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {//for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {
                        // while((iterator.hasNext())){
                        //postSnapshot2.child("hari").
                        Jadwal value = postSnapshot2.getValue(Jadwal.class);
                        matakuliahs.add(value.getmatakuliah());
                        jams.add(value.getjam());
                        //lokasis.add(value.getlokasi());
                        dsn_koors.add(value.getDosen_koordinator());

                        //Log.i("Value :", value.getmatakuliah().toString());

                        ((CustomListAdapater) (((ListView) findViewById(R.id.listJadwal)).getAdapter())).notifyDataSetChanged();

                        //}
                    }
                }

            }
                //}
            //}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ((ListView)findViewById(R.id.listJadwal)).
                setAdapter(new CustomListAdapater(matakuliahs,jams,lokasis,dsn_koors, this));
        ((ListView)findViewById(R.id.listJadwal)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String text = parent.getItemAtPosition(position).toString().trim();

                t2 = (TextView) view.findViewById(R.id.jamlist);
                t1 = (TextView) view.findViewById(R.id.matakuliahlist);
                t4 = (TextView) view.findViewById(R.id.lokasilist);
                t3 = (TextView) view.findViewById(R.id.dsn_koorlist);

                text1 = t1.getText().toString();  //matakuliah
                text2 = t2.getText().toString();
                text3 = t3.getText().toString();
                text4 = t4.getText().toString();




                EditText ed2 = (EditText) findViewById(R.id.jam);
                s1 = (Spinner) findViewById(R.id.dsn_koork);
                EditText ed4 = (EditText) findViewById(R.id.lokasi);


                dbCariKeyMK = FirebaseDatabase.getInstance().getReference().child("jadwal");

                Query  qMK = dbCariKeyMK.orderByChild("matakuliah").equalTo(text1);




                qMK.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            //jadwalkey[0] = childSnapshot.getKey();
                            keyMk[0] = childSnapshot.getKey();
                            Log.d("Key", keyMk[0]);

                            //String mk = Arrays.toString(keyMk[0]);
                            s2.setSelection(((ArrayAdapter<String>)s2.getAdapter()).getPosition(keyMk[0]));
                            //edMK.setText();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //Log.d("update",text1);
                //s2.setSelection(((ArrayAdapter<String>)s2.getAdapter()).getPosition(text1));
                edMK.setText(text1);
                ed2.setText(text2);
                s1.setSelection(((ArrayAdapter<String>)s1.getAdapter()).getPosition(text3));
                ed4.setText(text4);
                //ed1.setEnabled(false);
                s2.setEnabled(false);
                edMK.setEnabled(false);
            }
        });
    }

    private void updatejadwal(final String kdMK, final String matakuliah, final String jam, final String dsn_koors) {
        final DatabaseReference d1 = myDatabaseReference.child(kdMK);
        d1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> jadwal = new HashMap<>();
                jadwal.put("matakuliah",matakuliah);
               // jadwal.put("hari",hari);
                jadwal.put("jam",jam);
                jadwal.put("dosen_koordinator",dsn_koors);

                d1.updateChildren(jadwal);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
