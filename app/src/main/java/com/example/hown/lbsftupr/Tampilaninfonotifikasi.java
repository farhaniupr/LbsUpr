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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Tampilaninfonotifikasi extends AppCompatActivity {

    private static final String TAG = Tampilaninfonotifikasi.class.getSimpleName();
    DatabaseReference databaseItem1, databaseItem2;
    String title;
    ListView l1;
    TextView t1;

    final ArrayList<String> matakuliahs = new ArrayList<>();
    final ArrayList<String> jams = new ArrayList<>();
    final ArrayList<String> hari = new ArrayList<>();
    final ArrayList<String> dsn_koors = new ArrayList<>();

    final ArrayList<String> matakuliahsi = new ArrayList<>();
    final ArrayList<String> dsn_koorsi = new ArrayList<>();
    final ArrayList<Jadwal> jadw = new ArrayList<Jadwal>();
    Calendar calender;
    String timeX, timeX1;

    final String[] keyMk = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampilaninfonotifikasi);

        Bundle b = getIntent().getExtras();

        title = (String) b.get("title");

        calender = Calendar.getInstance();

        DatabaseReference db1  = FirebaseDatabase.getInstance().getReference().child("ruangan");


        Query q = db1.orderByChild("id").equalTo(title);
        final String[] key = new String[1];










        l1 = (ListView) findViewById(R.id.listinfomapn);

        t1 = (TextView) findViewById(R.id.textViewT);

        t1.setText(title);



        ///final ArrayList<String> tanggals = new ArrayList<>();
        calender = Calendar.getInstance();
        final Calendar nowCalendar = Calendar.getInstance();
        final Calendar nowUASCalendar = Calendar.getInstance();
        calender.set(Calendar.HOUR_OF_DAY, nowCalendar.get(Calendar.HOUR_OF_DAY));
        calender.set(Calendar.MINUTE, nowCalendar.get(Calendar.MINUTE));

        calender.set(Calendar.DAY_OF_MONTH, nowCalendar.get(Calendar.DAY_OF_MONTH));

        Log.d("", "Hari" +
                nowCalendar.getDisplayName(Calendar.DAY_OF_WEEK,  Calendar.LONG, Locale.getDefault()));

        Log.d("", "Jam" +
                nowCalendar.get(Calendar.HOUR_OF_DAY )+":"+nowCalendar.get(Calendar.MINUTE));

        long timeCheck = (nowCalendar.get(Calendar.HOUR_OF_DAY )+nowCalendar.get(Calendar.MINUTE));

        //String ti = (nowCalendar.get(Calendar.HOUR_OF_DAY )+":"+nowCalendar.get(Calendar.MINUTE));

        Date currentTime = Calendar.getInstance().getTime();

        Log.d("", "Coba" + currentTime);

        try {

            String string1 = "00:00";
            Date time1  = new SimpleDateFormat("HH:mm").parse(string1);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);

            String string2 = "07:00";
            Date time2 = new SimpleDateFormat("HH:mm").parse(string2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);

            String string3 = "09:30";
            Date time3  = new SimpleDateFormat("HH:mm").parse(string3);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(time3);
            calendar3.add(Calendar.DATE, 1);

            String string4 = "12:00";
            Date time4 = new SimpleDateFormat("HH:mm").parse(string4);
            Calendar calendar4 = Calendar.getInstance();
            calendar4.setTime(time4);
            calendar4.add(Calendar.DATE, 1);

            String string5 = "14:30";
            Date time5  = new SimpleDateFormat("HH:mm").parse(string5);
            Calendar calendar5 = Calendar.getInstance();
            calendar5.setTime(time5);
            calendar5.add(Calendar.DATE, 1);

            String string6 = "17:00";
            Date time6 = new SimpleDateFormat("HH:mm").parse(string6);
            Calendar calendar6 = Calendar.getInstance();
            calendar6.setTime(time6);
            calendar6.add(Calendar.DATE, 1);

            //String someRandomTime = "04:00";
            String ti = (nowCalendar.get(Calendar.HOUR_OF_DAY )+":"+nowCalendar.get(Calendar.MINUTE));
            Date d = new SimpleDateFormat("HH:mm").parse(ti);
            Calendar calendar8 = Calendar.getInstance();
            calendar8.setTime(d);
            calendar8.add(Calendar.DATE, 1);

            Date x = calendar8.getTime();
            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                timeX = "00:00";
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                Log.d("hasil cek1", timeX);
            }else
            if (x.after(calendar2.getTime()) && x.before(calendar3.getTime())) {
                timeX  = "07:00";
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                Log.d("hasil cek2", timeX);
            } else
            if (x.after(calendar3.getTime()) && x.before(calendar4.getTime())) {
                timeX  = "09:30";
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                Log.d("hasil cek3", timeX);
            } else
            if (x.after(calendar4.getTime()) && x.before(calendar5.getTime())) {
                timeX  = "12:00";
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                Log.d("hasil cek4", timeX);
            } else
            if (x.after(calendar5.getTime()) && x.before(calendar6.getTime())) {
                timeX  = "14:30";
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                Log.d("hasil cek5", timeX);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }



        //uas timer
        try {

            String string2 = "07:00";
            Date timeU2 = new SimpleDateFormat("HH:mm").parse(string2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(timeU2);
            calendar2.add(Calendar.DATE, 1);

            String string3 = "09:45";
            Date timeU3  = new SimpleDateFormat("HH:mm").parse(string3);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(timeU3);
            calendar3.add(Calendar.DATE, 1);

            String string4 = "12:30";
            Date timeU4 = new SimpleDateFormat("HH:mm").parse(string4);
            Calendar calendar4 = Calendar.getInstance();
            calendar4.setTime(timeU4);
            calendar4.add(Calendar.DATE, 1);

            String string5 = "15:00";
            Date timeU5 = new SimpleDateFormat("HH:mm").parse(string5);
            Calendar calendar5 = Calendar.getInstance();
            calendar5.setTime(timeU5);
            calendar5.add(Calendar.DATE, 1);


            //String someRandomTime = "04:00";
            String ti = (nowCalendar.get(Calendar.HOUR_OF_DAY )+":"+nowCalendar.get(Calendar.MINUTE));
            Date d = new SimpleDateFormat("HH:mm").parse(ti);
            Calendar calendar8 = Calendar.getInstance();
            calendar8.setTime(d);
            calendar8.add(Calendar.DATE, 1);

            Date x = calendar8.getTime();
            if (x.after(calendar2.getTime()) && x.before(calendar3.getTime())) {
                timeX1 = "07:00";
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                Log.d("hasil cek1", timeX1);
            }else
            if (x.after(calendar3.getTime()) && x.before(calendar4.getTime())) {
                timeX1  = "09:00";
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                Log.d("hasil cek2", timeX1);
            } else
            if (x.after(calendar4.getTime()) && x.before(calendar5.getTime())) {
                timeX1  = "12:30";
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                Log.d("hasil cek2", timeX1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    key[0] = childSnapshot.getKey();
                    //key = keyMk[0];
                    Log.d(TAG, "Key Ruangan : " + key[0]);
                }

                databaseItem1 = FirebaseDatabase.getInstance().getReference().child("ruangan").child(key[0]).child("jadwal");
                DatabaseReference databaseItem3 = FirebaseDatabase.getInstance().getReference().child("ruangan").child(key[0]).child("jadwalU");

                databaseItem1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()){
                            String key = postSnapshot2.getKey();

                            Log.d("", "jadwal key " + key);
                            databaseItem2 = FirebaseDatabase.getInstance().getReference().child("jadwal");
                            databaseItem2.child(key).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                                    if (dataSnapshot.exists()) {
                                        Jadwal jad = dataSnapshot.getValue(Jadwal.class);

                                        matakuliahs.add(jad.getmatakuliah());
                                        dsn_koors.add(jad.getDosen_koordinator());
                                        hari.add(jad.gethari());
                                        jams.add(jad.getjam());

                                        jadw.add(jad);

                                        Log.d("hasil cek firebase", String.valueOf(matakuliahs) + dsn_koors + hari + jams);

                                        Log.d("hasil cek looping4", jadw.toString());
                                        for (Jadwal o : jadw) {
                                            if (o.gethari().equals(nowCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()))) {
                                                Log.d("hasil cek looping3", o.gethari());
                                            }

                                            if (o.gethari().equals(nowCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())) && o.getjam().equals(timeX)) {
                                                Map<String, String> valueMap = new HashMap<String, String>();
                                                valueMap.get(nowCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));

                                                Log.d("hasil cek looping", valueMap.toString());
                                                matakuliahsi.add(o.getmatakuliah());
                                                dsn_koorsi.add(o.getDosen_koordinator());
                                                Log.d("hasil cek looping 2", matakuliahsi.toString() + dsn_koorsi.toString());

                                                l1.setAdapter(new CustomInfo(matakuliahsi,dsn_koorsi, Tampilaninfonotifikasi.this));
                                            }
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d("hasil timeX", timeX);
    }

    @Override
    public void onBackPressed() {

            Intent i = new Intent(Tampilaninfonotifikasi.this, pilihlantai.class);

            startActivity(i);
            finish();
        //}

        super.onBackPressed();
    }
}
