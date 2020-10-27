package com.example.hown.lbsftupr;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hown.lbsftupr.model.jadwalU;
import com.example.hown.lbsftupr.reminderr.utils.DateAndTimeUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.OnClick;

public class kelola_jadwalU extends AppCompatActivity {

    private DatabaseReference myDatabaseReference, dbCariKeyMK;
    private Query q;
    private String personId;
    private EditText ed1, ed2,  ed4, ed5;



    Spinner ed3, ed6;

    Calendar myCalendar;

    DatePickerDialog.OnDateSetListener date;


    String  keynext;

    final String[] keyMk = new String[1];

    final String[] key = new String[1];

    final int[] pos = new int[1];
    final int[] pos2 = new int[1];



    private static final String TAG = kelola_jadwalU.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_jadwalu);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        final String hari = (String) b.get("hari");

         ed1 = (EditText) findViewById(R.id.matakuliahU);
         ed2 = (EditText) findViewById(R.id.jamU);
       ed3 = (Spinner) findViewById(R.id.dsn_koorU);
        ed4 = (EditText) findViewById(R.id.lokasiU);
         ed5 = (EditText) findViewById(R.id.editTextTanggalU);
        ed6 = (Spinner) findViewById(R.id.edKodeMK);

        ed1.setEnabled(false);



        pos[0]= ed3.getSelectedItemPosition();
        pos2[0]= ed6.getSelectedItemPosition();
        //pos = ed3.getSelectedItemPosition();
        myCalendar = Calendar.getInstance();

        //ed5.setText(DateAndTimeUtil.toStringReadableDate(myCalendar));

        //myCalendar = DateAndTimeUtil.parseDateAndTime(reminder.getDateAndTime());

        /**date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };*/

        ed5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                datePicker(v);
            }
        });

        ed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });


        // myDatabaseReference=FirebaseDatabase.getInstance().getReference().child("jadwal").child(hari);

        myDatabaseReference=FirebaseDatabase.getInstance().getReference().child("jadwalU");

        q = myDatabaseReference.orderByChild("hari").equalTo(hari);


        //personId= myDatabaseReference.push().getKey();


        /*((ListView)findViewById(R.id.peopleList)).
                setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.people_list_row, R.id.personNameTv, readData()));*/

        (findViewById(R.id.addBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((EditText)findViewById(R.id.jamU)).getText().toString().trim().isEmpty()){
                    (findViewById(R.id.findBtn)).setEnabled(true);
                    Toast.makeText(kelola_jadwalU.this, "jam tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                if (((EditText)findViewById(R.id.editTextTanggalU)).getText().toString().trim().isEmpty()){
                    (findViewById(R.id.findBtn)).setEnabled(true);
                    Toast.makeText(kelola_jadwalU.this, "Tanggal tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else
                {
                    pos[0]= ed3.getSelectedItemPosition();
                    pos2[0]= ed6.getSelectedItemPosition();
                    myDatabaseReference.child(ed6.getItemAtPosition(pos2[0]).toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(kelola_jadwalU.this, "Kode MK sudah ada ganti yang lain", Toast.LENGTH_SHORT).show();
                            } else
                            {
                                pos[0]= ed3.getSelectedItemPosition();
                                pos2[0]= ed6.getSelectedItemPosition();

                                addPerson(ed6.getItemAtPosition(pos2[0]).toString(), hari, ed3.getItemAtPosition(pos[0]).toString(),
                                        ((EditText) findViewById(R.id.jamU)).getText().toString(), ((EditText) findViewById(R.id.lokasiU)).getText().toString(), ed1.getText().toString(),((EditText) findViewById(R.id.editTextTanggalU)).getText().toString() );
                                readData();
                                (findViewById(R.id.findBtn)).setEnabled(true);
                                Toast.makeText(kelola_jadwalU.this, "Berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        findViewById(R.id.buttonKelolaU).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if (((EditText)findViewById(R.id.matakuliahU)).getText().toString().trim().isEmpty()){
                    (findViewById(R.id.findBtnk)).setEnabled(true);
                    Toast.makeText(kelola_jadwalU.this, "matakuliah tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else*/{

                    myDatabaseReference.orderByChild("matakuliah").equalTo(ed1.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                //jadwalkey[0] = childSnapshot.getKey();
                                key[0] = childSnapshot.getKey();
                                keynext = key[0];
                                Log.d("Key Kelola Ruangan :", key[0]);
                                //Toast.makeText(kelola_jadwalK.this, key[0], Toast.LENGTH_SHORT).show();
                            }

                            Intent intent = new Intent(kelola_jadwalU.this, kelola_info_ruangan_jadwalU.class);
                            intent.putExtra("jadwal", keynext);
                            Log.d(TAG, "Isi Edit Nama Ruangan" + keynext);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
        (findViewById(R.id.updateBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ed1.setEnabled(false);

                //if (((EditText)findViewById(R.id.dsn_koorU)).getText().toString().trim().isEmpty()){
                //    (findViewById(R.id.findBtn)).setEnabled(true);
                 //   Toast.makeText(kelola_jadwalU.this, "dosen koordinator tidak boleh kosong", Toast.LENGTH_SHORT).show();
                //} else
                if (((EditText)findViewById(R.id.jamU)).getText().toString().trim().isEmpty()){
                    (findViewById(R.id.findBtn)).setEnabled(true);;
                    Toast.makeText(kelola_jadwalU.this, "jam tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else
                /*if (((EditText)findViewById(R.id.matakuliahU)).getText().toString().trim().isEmpty()){
                    (findViewById(R.id.findBtn)).setEnabled(true);
                    Toast.makeText(kelola_jadwalU.this, "matakuliah tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else */{

                    pos[0]= ed3.getSelectedItemPosition();
                    pos2[0]= ed6.getSelectedItemPosition();
                    //(String matakuliah, final String jam, final String dsn_koors, final String lokasi)
                    updatejadwal(ed6.getItemAtPosition(pos2[0]).toString(), ed1.getText().toString(),((EditText)findViewById(R.id.jamU)).getText().toString(),ed3.getItemAtPosition(pos[0]).toString(),((EditText)findViewById(R.id.lokasiU)).getText().toString(),((EditText)findViewById(R.id.editTextTanggalU)).getText().toString() );
                    readData();
                    (findViewById(R.id.findBtn)).setEnabled(true);
                    Toast.makeText(kelola_jadwalU.this, "Berhasil dubah", Toast.LENGTH_SHORT).show();
                    //ed1.setEnabled(false);
                    ed6.setEnabled(true);
                }
            }
        });
        (findViewById(R.id.deleteBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if (((EditText)findViewById(R.id.dsn_koorU)).getText().toString().trim().isEmpty()){
               //     (findViewById(R.id.findBtn)).setEnabled(true);
               //     Toast.makeText(kelola_jadwalU.this, "dosen koordinator tidak boleh kosong", Toast.LENGTH_SHORT).show();
                //} else
                //if (ed6.getText().toString().trim().isEmpty()){
                //    (findViewById(R.id.findBtn)).setEnabled(true);
                //    Toast.makeText(kelola_jadwalU.this, "Klik di List View matakuliah yang akan dihapus", Toast.LENGTH_SHORT).show();
               // } else
                /*if (((EditText)findViewById(R.id.matakuliahU)).getText().toString().trim().isEmpty()){
                    (findViewById(R.id.findBtn)).setEnabled(true);
                    Toast.makeText(kelola_jadwalU.this, "matakuliah tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else*/ {
                    pos[0]= ed3.getSelectedItemPosition();
                    pos2[0]= ed6.getSelectedItemPosition();

                    removePerson(ed6.getItemAtPosition(pos2[0]).toString());
                    readData();
                    (findViewById(R.id.findBtn)).setEnabled(true);
                    Toast.makeText(kelola_jadwalU.this, "Berhasil dihapus", Toast.LENGTH_SHORT).show();

                }
            }
        });
        (findViewById(R.id.loadBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData();
            }
        });

        (findViewById(R.id.findBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((Spinner)findViewById(R.id.dsn_koorU)).setText("");
                ((EditText) findViewById(R.id.jamU)).setText("");
                ((EditText) findViewById(R.id.lokasiU)).setText("");
                //((EditText) findViewById(R.id.matakuliahU)).setText("");
                //ed6.setText("");
                ed1.setEnabled(true);
                ed6.setEnabled(true);


                (findViewById(R.id.addBtn)).setEnabled(true);
                (findViewById(R.id.loadBtn)).setEnabled(true);
                (findViewById(R.id.deleteBtn)).setEnabled(true);
                (findViewById(R.id.updateBtn)).setEnabled(true);
                (findViewById(R.id.findBtn)).setEnabled(false);
                readData();
            }
        });


        ArrayAdapter<String> adapter;
        List<String> list;

        list = new ArrayList<String>();
        list.add("V. Abdi Gunawan ST.MT.");
        list.add("Deddy Ronaldo ST.MT.");
        list.add("Widiarty ST.MT.");
        list.add("Felicia Sylviana ST.MM.");
        list.add("Viktor H. Pranatawijaya ST.MT.");
        list.add("Drs. Jadiaman Parhusip M.Kom");
        list.add("Abertun Sagit Sahay ST.M.Eng");
        list.add("Rony Teguh Ph.D");
        list.add("Enny D. Oktaviyani ST.M.Kom");
        list.add("Nahumi Nugrahaningsih Ph.D");
        list.add("Sherly Christina S.Kom M.Kom");
        list.add("Devi Karolita S.Kom M.Kom");
        list.add("Ariesta Lestari S.Kom M.Cs");
        list.add("Agus S. Saragih ST. M.Eng");
        list.add("Licantik S.Kom M.Kom");
        list.add("Marhayu ST.M.Cs");
        list.add("Ade Candra Saputra S.Kom, M.Cs");
        list.add("Putu Bagus A.A.P ST.M.Kom");
        list.add("Nova Noor Kamala Sari ST.M.Kom");
        list.add("-");

        ed3.setAdapter(new ArrayAdapter<String>(kelola_jadwalU.this,
                android.R.layout.simple_spinner_dropdown_item,
                list));
        ed3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        list2.add("DBPK 1011");//1
        list2.add("DBPK 1021");//2
        list2.add("DBPK 1031");//3
        list2.add("DBPK 1041");//4
        list2.add("DBPK 1051");//5
        list2.add("DBPK 1071");//6
        list2.add("DBPK 1081");//7
        list2.add("DBPK 1092");//8

        list2.add("DBKK 1021");//9
        list2.add("DBKK 1041");//10
        list2.add("DBKK 1051");//11
        list2.add("DBKK 1062");//12
        list2.add("DBKK 1081");//13
        list2.add("DBKK 1091");//14
        list2.add("DBKK 1102");//15
        list2.add("DBKK 1111");//16
        list2.add("DBKK 1151");//17
        list2.add("DBKK 1212");//18
        list2.add("DBKK 1221");//19
        list2.add("DBKK 1261");//20
        list2.add("DBKK 1272");//21
        list2.add("DBKK 1271");//22
        list2.add("DBKK 1162");//23
        list2.add("DBKK 1172");//24
        list2.add("DBKK 1191");//25
        list2.add("DBKK 1232");//26


        list2.add("DBKB 1012");//27
        list2.add("DBKB 1171");//28
        list2.add("DBKB 1192");//29
        list2.add("DBKB 1201");//30
        list2.add("DBKB 1212");//31
        list2.add("DBKB 1222");//32
        list2.add("DBKB 1221");//33
        list2.add("DBKB 1042");//34
        list2.add("DBKB 1031");//35
        list2.add("DBKB 1052");//36
        list2.add("DBKB 1061");//37
        list2.add("DBKB 1091");//38
        list2.add("DBKB 1101");//39
        list2.add("DBKB 1132");//40
        list2.add("DBKB 1142");//41
        list2.add("DBKB 1161");//42
        list2.add("DBKB 1232");//43
        list2.add("DBKB 1242");//44
        list2.add("DBKB 1252");//45

        list2.add("DBPB 1012");
        list2.add("DBBB 1030");
        list2.add("DBBB 1042");
        list2.add("DBBB 1010");
        list2.add("DBBB 1020");

        list2.add("DBKP 1010");
        list2.add("DBKP 1030");
        list2.add("DBKP 1040");
        list2.add("DBKP 1130");
        list2.add("DBKP 1060");
        list2.add("DBKP 1080");
        list2.add("DBKP 1140");
        list2.add("DBKP 1110");
        list2.add("DBKP 1120");
        list2.add("DBKP 1150");
        list2.add("DBKP 1160");


        list3.add("Pendidikan Agama Islam");
        list3.add("Pendidikan Agama Kristen");
        list3.add("Pendidikan Agama Katolik");
        list3.add("Pendidikan Agama Hindu / Buddha");
        list3.add("Bahasa Inggris I");
        list3.add("Pendidikan Pancasila");
        list3.add("Bahasa Indonesia");
        list3.add("Bahasa Inggris II");

        list3.add("Fisika");//9
        list3.add("Pengantar Teknologi Komputer & Informasi");//10
        list3.add("Kalkulus I");//11
        list3.add("Kalkulus II");//12
        list3.add("Logika Matematika");//13
        list3.add("Algoritma & Pemrograman I");//14
        list3.add("Algoritma & Pemrograman II");//15
        list3.add("Algoritma & Pemrograman III");//16
        list3.add("Sistem Digital");//17
        list3.add("Statistika I");//18
        list3.add("Statistika II");//19
        list3.add("Sistem Berkas");//20
        list3.add("Pengantar Arsitektur Komputer");//21
        list3.add("Metode Numerik");//22
        list3.add("Aljabar Vektor & Matriks");//23
        list3.add("Struktur Data");//24
        list3.add("Matematika Diskrit");//25
        list3.add("Metodologi Penelitian");//26

        list3.add("Pemrograman Berorientasi Objek");//27
        list3.add("Interaksi Manusia dan Komputer");//28
        list3.add("Basis Data 1");//29
        list3.add("Basis Data 2");//30
        list3.add("Sistem Informasi");//31
        list3.add("Internet & Intranet");//32
        list3.add("Keamanan Jaringan");//33
        list3.add("Sistem Operasi");//34
        list3.add("Organisasi Komputer");//35
        list3.add("Komunikasi Data");
        list3.add("Jaringan Komputer");
        list3.add("Rekayasa Perangkat Lunak");
        list3.add("Teori Bahasa dan Otomata");
        list3.add("Kecerdasan Buatan");
        list3.add("Program Profesional");
        list3.add("Grafika Komputer");
        list3.add("Manajemen Proyek");
        list3.add("Metode Pengembangan Perangkat Lunak");
        list3.add("Multimedia");

        list3.add("Etika dan Profesi");
        list3.add("Tugas Akhir");
        list3.add("Ilmu Sosial Budaya Dasar");
        list3.add("Kerja Praktek");
        list3.add("Kuliah Kerja Nyata");

        list3.add("Sistem Penunjang Keputusan");
        list3.add("Data Warehouse & Data Mining");
        list3.add("Analisa Desain Sistem Informasi");
        list3.add("Business Process Reenginering");
        list3.add("Pemrograman Client Server");
        list3.add("Wireless / Mobile Computing");
        list3.add("Sistem Paralel dan Terdistribusi");
        list3.add("Pemodelan dan Simulasi Data");
        list3.add("Sistem Cerdas");
        list3.add("Jaringan Syaraf Tiruan");
        list3.add("Pemrograman Web");

        ed6.setAdapter(new ArrayAdapter<String>(kelola_jadwalU.this,
                android.R.layout.simple_spinner_dropdown_item,
                list2));
        ed6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                ed1.setText(list3.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        readData();
    }

    @OnClick(R.id.editTextTanggalU)
    public void datePicker(View view) {
        DatePickerDialog DatePicker = new DatePickerDialog(kelola_jadwalU.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker DatePicker, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //String df_long = new SimpleDateFormat("dd-MMM-yyyy").format(myCalendar);
                //String df_full_str = df_long.format(DateAndTimeUtil.toStringReadableDate(myCalendar));
                String myFormat = "dd MMM yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                ed5.setText(sdf.format(myCalendar.getTime()));
                //ed5.setText(getString(String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", myCalendar)));
            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        DatePicker.show();
    }

    @OnClick(R.id.jamU)
    public void timePicker() {
        TimePickerDialog TimePicker = new TimePickerDialog(kelola_jadwalU.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker timePicker, int hour, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hour);
                myCalendar.set(Calendar.MINUTE, minute);
                ed2.setText(DateAndTimeUtil.toStringReadableTime(myCalendar, getApplicationContext()));
            }
        }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));
        TimePicker.show();
    }

    private void addPerson(String kMK, String hari, String dosen_koordinator, String jam, String lokasi, String matakuliah, String tanggal){
        jadwalU jadwals= new jadwalU(hari, dosen_koordinator, jam, lokasi, matakuliah, tanggal);
        String personId = myDatabaseReference.push().getKey();
        myDatabaseReference.child(kMK).setValue(jadwals);
    }

    private void removePerson(final String keyMkP){
        //String personId = myDatabaseReference.push().getKey();
        //myDatabaseReference.child(personId).removeValue();

        //myDatabaseReference = FirebaseDatabase.getInstance().getReference().child("jadwal").child(hari);

        //DatabaseReference d1 = myDatabaseReference.child(matakuliah);

        //EditText ed1 = (EditText) findViewById(R.id.matakuliahU);
        EditText ed2 = (EditText) findViewById(R.id.jamU);
        ed3 = (Spinner) findViewById(R.id.dsn_koorU);
        EditText ed4 = (EditText) findViewById(R.id.lokasiU);

        //String e1 = ed1.getText().toString();
        final String[] jadwalkey = new String[1];
        /*myDatabaseReference.orderByChild("matakuliah").equalTo(matakuliah).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    //jadwalkey[0] = childSnapshot.getKey();
                    jadwalkey[0] = childSnapshot.getKey();
                    Log.d("Key", jadwalkey[0]);
                    Toast.makeText(kelola_jadwalU.this, jadwalkey[0], Toast.LENGTH_SHORT).show();
                    //mylist.add(jadwalkey);
                }*/

        DatabaseReference d1 = myDatabaseReference.child(keyMkP);

        d1.removeValue();


    }
    private void readData(){
        final ArrayList<String> matakuliahs = new ArrayList<>();
        final ArrayList<String> jams = new ArrayList<>();
        final ArrayList<String> lokasis = new ArrayList<>();
        final ArrayList<String> dsn_koors = new ArrayList<>();
        final ArrayList<String> tanggals = new ArrayList<>();
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {
                    // while((iterator.hasNext())){
                    jadwalU value = postSnapshot2.getValue(jadwalU.class);
                    matakuliahs.add(value.getmatakuliah());
                    jams.add(value.getjam());
                    lokasis.add(value.getlokasi());
                    dsn_koors.add(value.getDosen_koordinator());
                    tanggals.add(value.getTanggal());
                    ((CustomListUAdapater) (((ListView) findViewById(R.id.listJadwalU)).getAdapter())).notifyDataSetChanged();
                }
                //}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ((ListView)findViewById(R.id.listJadwalU)).
                setAdapter(new CustomListUAdapater(matakuliahs,jams,lokasis,dsn_koors,tanggals, this));
        ((ListView)findViewById(R.id.listJadwalU)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String text = parent.getItemAtPosition(position).toString().trim();

                TextView t2 = (TextView) view.findViewById(R.id.jamlistU);
                TextView t1 = (TextView) view.findViewById(R.id.matakuliahlistU);
                TextView t4 = (TextView) view.findViewById(R.id.lokasilistU);
                TextView t3 = (TextView) view.findViewById(R.id.dsn_koorlistU);
                TextView t5 = (TextView) view.findViewById(R.id.tanggallistU);

                String text1 = t1.getText().toString();
                String text2 = t2.getText().toString();
                String text3 = t3.getText().toString();
                String text4 = t4.getText().toString();
                String text5 = t5.getText().toString();

                //EditText ed1 = (EditText) findViewById(R.id.matakuliahU);
                EditText ed2 = (EditText) findViewById(R.id.jamU);
                ed3 = (Spinner) findViewById(R.id.dsn_koorU);
                EditText ed4 = (EditText) findViewById(R.id.lokasiU);
                EditText ed5 = (EditText) findViewById(R.id.editTextTanggalU);



                dbCariKeyMK = FirebaseDatabase.getInstance().getReference().child("jadwalU");

                Query  qMK = dbCariKeyMK.orderByChild("matakuliah").equalTo(text1);




                qMK.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            //jadwalkey[0] = childSnapshot.getKey();
                            keyMk[0] = childSnapshot.getKey();
                            Log.d("Key", keyMk[0]);

                            //String mk = Arrays.toString(keyMk[0]);
                            ed6.setSelection(((ArrayAdapter<String>)ed6.getAdapter()).getPosition(keyMk[0]));

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //Log.d("update",text1);

                ed1.setText(text1);
                ed2.setText(text2);
                //ed3.setText(text3);
                ed3.setSelection(((ArrayAdapter<String>)ed3.getAdapter()).getPosition(text3));
                ed4.setText(text4);
                ed5.setText(text5);
                ed6.setEnabled(true);
                ed1.setEnabled(false);


            }
        });


        //setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.people_list_row, R.id.personNameTv,names));
    }

    private void updatejadwal(final String mk, final String matakuliah, final String jam, final String dsn_koors, final String lokasi, final String tanggal) {
        //myDatabaseReference = FirebaseDatabase.getInstance().getReference().child("jadwal").child("Senin");

        //DatabaseReference d1 = myDatabaseReference.child(matakuliah);

        //ed1 = (EditText) findViewById(R.id.matakuliahU);
        ed2 = (EditText) findViewById(R.id.jamU);
        ed3 = (Spinner) findViewById(R.id.dsn_koorU);
        ed4 = (EditText) findViewById(R.id.lokasiU);

        //String e1 = ed1.getText().toString();
        //String jadwalkey;

        //final ArrayList<String> mylist = new ArrayList<String>();
        final int[] pos = new int[1];

        final String[] jadwalkey = new String[1];
        /**myDatabaseReference.child(mk).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               /** for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    //jadwalkey[0] = childSnapshot.getKey();
                    jadwalkey[0] = childSnapshot.getKey();
                    Log.d("Key", jadwalkey[0]);
                    Toast.makeText(kelola_jadwalU.this, jadwalkey[0], Toast.LENGTH_SHORT).show();
                }*/
                DatabaseReference d1 = myDatabaseReference.child(mk);
                Map<String, Object> jadwal = new HashMap<>();
                jadwal.put("matakuliah",matakuliah);
                //jadwal.put("lokasi",lokasi);
                jadwal.put("jam",jam);
                jadwal.put("dosen_koordinator",dsn_koors);
                jadwal.put("tanggal",tanggal);

                d1.updateChildren(jadwal);
           /* }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });*/
    }
}







