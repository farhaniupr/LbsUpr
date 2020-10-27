package com.example.hown.lbsftupr;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hown.lbsftupr.createuser.createuserlanjutkan;
import com.example.hown.lbsftupr.reminderr.activities.PreferenceActivity;
import com.example.hown.lbsftupr.reminderr.activities.ReminderActivity;
import com.example.hown.lbsftupr.schedule.SchedActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;


//public class BerandaActivity extends AppCompatActivity  {
public class BerandaActivity extends FragmentActivity {


    private GoogleMap mMap;
    protected LocationManager locationManager;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 6000;

    protected int getLayoutId() {
        return R.layout.activity_beranda;
    }


    private ImageView b4;
    private ImageView ib1, ib2, ib3, ib4, ib5, ib6;
    private TextView t1, t2;
    private ImageView im1;


    private FirebaseAuth auth;
    Location location;
    Calendar calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        setContentView(R.layout.activity_beranda);

        calender = Calendar.getInstance();
        Calendar nowCalendar = Calendar.getInstance();
        calender.set(Calendar.HOUR_OF_DAY, nowCalendar.get(Calendar.HOUR_OF_DAY));
        calender.set(Calendar.MINUTE, nowCalendar.get(Calendar.MINUTE));

        calender.set(Calendar.DAY_OF_MONTH, nowCalendar.get(Calendar.DAY_OF_MONTH));

        Log.d("", "Hari" +
                nowCalendar.getDisplayName(Calendar.DAY_OF_WEEK,  Calendar.LONG, Locale.getDefault()));

        Log.d("", "Jam" +
                nowCalendar.get(Calendar.HOUR_OF_DAY )+":"+nowCalendar.get(Calendar.MINUTE));

        Intent i = getIntent();
        Bundle b = i.getExtras();



        /**if (b != null)
        {
            String username = (String) b.get("username");
            String usernameRep = username.replace("@lbsupr.com ","");
            t1 = (TextView) findViewById(R.id.textUsername);
            t1.setText(usernameRep);
        }*/



        //String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
       // String RepEmail = email.replace("@lbsupr.com", "");

        //t1 = (TextView) findViewById(R.id.textUsername);
        //t1.setText(RepEmail);

        auth = FirebaseAuth.getInstance();


        //String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //t2 = (TextView) findViewById(R.id.textNamaMahasiswa);

        /**DatabaseReference refnama = FirebaseDatabase.getInstance().getReference()
                .child("user");

        Query query = refnama.child(currentUserId).child("nama");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String namamhs = (String) dataSnapshot.getValue();

                t2.setText(namamhs);
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        //t2.setText();

        ib1 = (ImageView) findViewById(R.id.settingbutton);
        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (BerandaActivity.this, PreferenceActivity.class);
                startActivity (intent);
            }
        } );

        ib3 = (ImageView) findViewById(R.id.jadwalButton);
        ib3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (BerandaActivity.this, SchedActivity.class);
                startActivity (intent);
            }
        } );

        ib2 = (ImageView) findViewById(R.id.mapButton2);
        ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchToMap();
                Intent intent = new Intent (BerandaActivity.this, Lantai1.class);
                startActivity (intent);
            }
        } );

        ib4 = (ImageView) findViewById(R.id.ReminderButton);
        ib4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchToMap();
                Intent intent = new Intent (BerandaActivity.this, ReminderActivity.class);
                startActivity (intent);
            }
        } );


        b4 = (ImageView) findViewById(R.id.buttonLogout);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchToMap();
                auth.signOut();
                Intent intent = new Intent (BerandaActivity.this, MainActivity.class);
                startActivity (intent);
            }
        } );

        ib5 = (ImageView) findViewById(R.id.buttonTT);
        ib5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchToMap();
                auth.signOut();
                Intent intent = new Intent (BerandaActivity.this, ScrollingActivity.class);
                startActivity (intent);
            }
        } );

        ib6 = (ImageView) findViewById(R.id.buttonPic);
        ib6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchToMap();
                auth.signOut();
                Intent intent = new Intent (BerandaActivity.this, lihat_gambar.class);
                startActivity (intent);
            }
        } );

        //ib5 = (ImageView) findViewById(R.id.akunButton);
        //ib5.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        //switchToMap();
        //        Intent intent = new Intent (BerandaActivity.this, createuserlanjutkan.class);
        //        startActivity (intent);
        //    }
       // } );


    }
    }
