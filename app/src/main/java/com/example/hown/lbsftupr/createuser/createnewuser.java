package com.example.hown.lbsftupr.createuser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hown.lbsftupr.BerandaActivity;
import com.example.hown.lbsftupr.MainActivity;
import com.example.hown.lbsftupr.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class createnewuser extends AppCompatActivity {

    private FirebaseAuth mAuth;

    public static String TAG =  createnewuser.class.getSimpleName();

    private Button b1;

    private EditText e1, e2, e3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnewuser);

        mAuth = FirebaseAuth.getInstance();


        b1 = (Button) findViewById(R.id.buttonLanjutkan);

        e1 = (EditText) findViewById(R.id.editUser);
        e2 = (EditText) findViewById(R.id.edipass);
        e3 = (EditText) findViewById(R.id.editText8);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = (e1.getText().toString() + "@lbsupr.com");
                final String password = e2.getText().toString();

                final ProgressDialog progress = new ProgressDialog(createnewuser.this);


                ProgressDialog.show(createnewuser.this, "Loading", "Tunggu...");

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(createnewuser.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in: success
                                    // update UI for current User
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //updateUI(user);

                                    final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DatabaseReference refnama1 = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId);

                                    Map<String, Object> user1 = new HashMap<>();
                                    //user1.put("nama", nama_mhs);
                                    user1.put("nim", e1.getText().toString());
                                    user1.put("password", e2.getText().toString());
                                    user1.put("nama", e3.getText().toString());

                                    refnama1.updateChildren(user1);



                                    progress.dismiss();
                                    Toast.makeText(createnewuser.this, "Berhasil dibuat",
                                            Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(createnewuser.this, createuserlanjutkan.class);
                                    i.putExtra("password", password);
                                    startActivity(i);
                                    finish();
                                } else {
                                    // Sign in: fail
                                    Log.e(TAG, "create Account: Fail!", task.getException());
                                    //updateUI(null);


                                    progress.dismiss();
                                    Toast.makeText(createnewuser.this, "Failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });

            }
        });

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

                Intent i = new Intent(createnewuser.this, BerandaActivity.class);
                i.putExtra("username", user.getEmail());
                startActivity(i);
                finish();

        }
    }
}
