package com.example.hown.lbsftupr.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hown.lbsftupr.MainActivity;
import com.example.hown.lbsftupr.R;
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

import java.util.HashMap;
import java.util.Map;

public class kelolaMhs extends AppCompatActivity {


    public EditText e1, e2;

    public Button b1, b2, b3;

    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_mhs);

        e1 = (EditText) findViewById(R.id.editText3);

        e1.setEnabled(false);
        e2 = (EditText) findViewById(R.id.editText4);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        final String Username = (String) b.get("Username");
        final String Password = (String) b.get("Password");


        auth = FirebaseAuth.getInstance();

            auth.signInWithEmailAndPassword(Username, Password)
                    .addOnCompleteListener(kelolaMhs.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            //progressBar.setVisibility(View.GONE);

                            //Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful()) {
                                // there was an error

                                //Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(kelolaMhs.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                //}
                            } else if (task.isSuccessful()) {



                                final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                final DatabaseReference refnama1 = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("password");

                                refnama1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        //for (DataSnapshot d1 : dataSnapshot.getChildren())
                                        //{
                                        String passUSER = (String) dataSnapshot.getValue();




                                        String us1 = email.replace("@lbsupr.com","");
                                        e1.setText(us1);
                                        e2.setText(passUSER);
                                        //}

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                        }
                    });

        b1 = (Button) findViewById(R.id.buttonSimpan);
        b2 = (Button) findViewById(R.id.buttonHapus);
        b3 = (Button) findViewById(R.id.buttonhapusemua);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updateEmail(e1.getText().toString()+"@lbsupr.com")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(kelolaMhs.this, "Berhasil Diubah", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                user.updatePassword(e2.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {


                                    final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DatabaseReference refnama1 = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId);

                                    Map<String, Object> user1 = new HashMap<>();
                                    //user1.put("nama", nama_mhs);
                                    user1.put("nim", e1.getText().toString());
                                    user1.put("password", e2.getText().toString());

                                    refnama1.updateChildren(user1);

                                    Toast.makeText(kelolaMhs.this, "Berhasil Diubah", Toast.LENGTH_LONG).show();


                                }
                            }
                        });
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference refnama1 = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId);

                refnama1.removeValue();


                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //Log.d(TAG, "User account deleted.");

                                    Toast.makeText(kelolaMhs.this, "Berhasil Dihapus", Toast.LENGTH_LONG).show();

                                    auth.signOut();

                                    auth.signInWithEmailAndPassword("admin@lbsupr.com", "123456")
                                            .addOnCompleteListener(kelolaMhs.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    // If sign in fails, display a message to the user. If sign in succeeds
                                                    // the auth state listener will be notified and logic to handle the
                                                    // signed in user can be handled in the listener.
                                                    //progressBar.setVisibility(View.GONE);

                                                    //Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                                    if (!task.isSuccessful()) {
                                                        // there was an error

                                                        //Log.w(TAG, "signInWithEmail:failed", task.getException());
                                                        Toast.makeText(kelolaMhs.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                                        //}
                                                    } else if (task.isSuccessful()) {

                                                        Intent i = new Intent(kelolaMhs.this, KelolaAkun.class);
                                                        startActivity(i);
                                                        finish();


                                                    }

                                                }
                                            });
                                }
                            }
                        });

            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference refnama1 = FirebaseDatabase.getInstance().getReference().child("user");

                refnama1.removeValue();

                auth = FirebaseAuth.getInstance();

                auth.signOut();

                Intent i = new Intent(kelolaMhs.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {


        auth = FirebaseAuth.getInstance();

        auth.signOut();

        auth.signInWithEmailAndPassword("admin@lbsupr.com", "123456")
                .addOnCompleteListener(kelolaMhs.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        //progressBar.setVisibility(View.GONE);

                        //Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            // there was an error

                            //Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(kelolaMhs.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            //}
                        } else if (task.isSuccessful()) {


                            Intent i = new Intent(kelolaMhs.this, KelolaAkun.class);
                            startActivity(i);
                            finish();


                        }

                    }
                });

        super.onBackPressed();
    }


    }
//}
