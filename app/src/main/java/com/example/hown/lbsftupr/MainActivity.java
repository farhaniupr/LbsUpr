package com.example.hown.lbsftupr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hown.lbsftupr.admin.BerandaAdmin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {

    public static String TAG =  MainActivity.class.getSimpleName();

    static public boolean geofencesAlreadyRegistered = false;

    //public  Button  customSigninButton, logoutButton ;
    public EditText editText, editText2;
    public TextView textView, textSignup;
    public Button   button;

    private FirebaseAuth auth;
    FirebaseUser user;

    Calendar calender;
    DatabaseReference refnama;

    DatabaseReference databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //bindViews();
        //config = new SmartLoginConfig(this, this);
        //config.setFacebookAppId(getString(R.string.facebook_app_id));
        //config.setFacebookPermissions(null);
        // config.setGoogleApiClient(null);

        calender = Calendar.getInstance();
        Calendar nowCalendar = Calendar.getInstance();
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
                String timeX = "00:00";
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                Log.d("hasil cek", timeX);
            }else
            if (x.after(calendar2.getTime()) && x.before(calendar3.getTime())) {
                String timeX = "07:00";
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                Log.d("hasil cek", timeX);
            } else
            if (x.after(calendar3.getTime()) && x.before(calendar4.getTime())) {
                String timeX = "07:00";
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                Log.d("hasil cek", timeX);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }





        editText2 = (EditText) findViewById(R.id.editText2);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView3);
        textSignup = (TextView) findViewById(R.id.textView2);
        button = (Button) findViewById(R.id.buttonLogin);

       // databaseUser = FirebaseDatabase.getInstance().getReference("User");

        auth = FirebaseAuth.getInstance();
        //progress
        final ProgressDialog[] progress = {new ProgressDialog(MainActivity.this)};
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = (editText2.getText().toString() + "@lbsupr.com");
                final String password = editText.getText().toString();

                if (editText2.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Username tidak boleh kosong", Toast.LENGTH_LONG).show();
                } else if (editText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Password tidak boleh kosong", Toast.LENGTH_LONG).show();
                } else  if (email.equals("admin@lbsupr.com")) {
                    progress[0] = ProgressDialog.show(MainActivity.this, "Loading", "Tunggu...");
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                    if (!task.isSuccessful()) {
                                        if (password.length() < 6) {
                                            progress[0].dismiss();
                                            editText.setError(getString(R.string.minimum_password));
                                        } else {
                                            progress[0].dismiss();
                                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                                            Toast.makeText(MainActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                        }
                                    } else if (task.isSuccessful()){
                                        //Intent i = new Intent(MainActivity.this, BerandaActivity.class);

                                        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                        final DatabaseReference refnama1 = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId);

                                        refnama1.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                //for (DataSnapshot d1 : dataSnapshot.getChildren())
                                                //{
                                                String nama_mhs = "admin";
                                                String password = "123456";
                                                Map<String, Object> user1 = new HashMap<>();
                                                user1.put("nim", nama_mhs);
                                                user1.put("password", password);

                                                refnama1.updateChildren(user1);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        progress[0].dismiss();

                                        Toast.makeText(MainActivity.this, "Berhasil Login",
                                                Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(MainActivity.this, BerandaAdmin.class);
                                        startActivity(i);
                                        finish();

                                    }
                                }
                            });
                } else{
                    progress[0] = ProgressDialog.show(MainActivity.this, "Loading", "Tunggu...");
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                    if (!task.isSuccessful()) {
                                        if (password.length() < 6) {
                                            progress[0].dismiss();
                                            editText.setError(getString(R.string.minimum_password));
                                        } else
                                           {
                                            progress[0].dismiss();
                                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                                            Toast.makeText(MainActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                        }
                                    } else if (task.isSuccessful() && editText2.getText().toString().equals("admin")) {
                                            progress[0].dismiss();
                                            Toast.makeText(MainActivity.this, "Berhasil Login",
                                                    Toast.LENGTH_SHORT).show();

                                            Intent i = new Intent(MainActivity.this, BerandaAdmin.class);
                                            startActivity(i);
                                            finish();
                                    } else if (task.isSuccessful() && !editText2.getText().toString().equals("admin")) {
                                        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        refnama = FirebaseDatabase.getInstance().getReference()
                                                .child("user").child(currentUserId);

                                        refnama.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {


                                                if (dataSnapshot.exists()){
                                                    progress[0].dismiss();
                                                Toast.makeText(MainActivity.this, "Berhasil Login",
                                                        Toast.LENGTH_SHORT).show();

                                                Intent i = new Intent(MainActivity.this, BerandaActivity.class);
                                                i.putExtra("username", email);
                                                startActivity(i);
                                                finish();

                                                } else if (!dataSnapshot.exists())
                                                {
                                                    progress[0].dismiss();
                                                    auth.signOut();
                                                    Toast.makeText(MainActivity.this, "Akun tidak ada",
                                                            Toast.LENGTH_SHORT).show();
                                                }
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

        /**textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress[0].show(MainActivity.this, "Loading", "Tunggu...");
                auth.signInAnonymously()
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInAnonymously:success");
                                    FirebaseUser user = auth.getCurrentUser();
                                    //updateUI(user);
                                    //user.get

                                    progress[0].dismiss();

                                    Toast.makeText(MainActivity.this, "Berhasil Masuk",
                                            Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(MainActivity.this, pilihlantai.class);
                                    startActivity(i);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    progress[0].dismiss();
                                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }


                            }
                        });
                //Intent i = new Intent(MainActivity.this, pilihlantai.class);
                //
                //startActivity(i);
                //finish();
            }
        });*/


        /**textSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                                    Intent i = new Intent(MainActivity.this, createnewuser.class);
                                    startActivity(i);
                                    finish();


            }
        });*/
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();

        //if (user.isAnonymous())
        //{

        //}
        //if (!user.isAnonymous()) {
            updateUI(currentUser);
        //}
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            //txtStatus.setText("User Email: " + user.getEmail() + "(verified: " + user.isEmailVerified() + ")");
            //txtDetail.setText("Firebase User ID: " + user.getUid());

            //Toast.makeText(MainActivity.this, "Masih dalam Akun : " + user.getEmail(), Toast.LENGTH_SHORT).show();
            if (user.getEmail().equals("admin@lbsupr.com")) {
                Intent i = new Intent(MainActivity.this, BerandaAdmin.class);

                startActivity(i);
                finish();
            } else
                if (!user.getEmail().equals("admin@lbsupr.com")){
                    Intent i = new Intent(MainActivity.this, BerandaActivity.class);
                    i.putExtra("username", user.getEmail());
                    startActivity(i);
                    finish();
            }
            else if (user.isAnonymous()){
                    Intent i = new Intent(MainActivity.this, pilihlantai.class);
                    startActivity(i);
                    finish();
                }
        }
        //if (user.isAnonymous())
        else {
            Toast.makeText(MainActivity.this, "Sign Out ", Toast.LENGTH_SHORT).show();

        }
    }

    private void signOut() {
        auth.signOut();
        updateUI(null);
    }

    @Override
    public void onBackPressed() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask ();
            finish();
        } else {
            this.finishAffinity();
            finish();
        }
        super.onBackPressed();
    }
}



