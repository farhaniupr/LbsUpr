package com.example.hown.lbsftupr.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hown.lbsftupr.R;
import com.example.hown.lbsftupr.model.user2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KelolaAkun extends AppCompatActivity {

    public ListView l1;
    public DatabaseReference d1;

    CustomAkun cu;

    ArrayList<user2> u11 = new ArrayList<user2>();


    final ArrayList<String> namaS = new ArrayList<String>();
    final ArrayList<String> nimS = new ArrayList<String>();
    final ArrayList<String> passwords = new ArrayList<String>();

    EditText e1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_akun);

        l1 = (ListView) findViewById(R.id.listUser);

        d1 = FirebaseDatabase.getInstance().getReference().child("user");

        //e1 = (EditText) findViewById(R.id.editTextSearch);


        //cu = new CustomAkun(nimS,passwords,this);


    //}

        d1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    user2 u1 = childSnapshot.getValue(user2.class);
                    //Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                u11.add(u1);

                //namaS.add(u1.getNama());
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                nimS.add(u1.getNim().toString());
                passwords.add(u1.getPassword().toString());

                ((CustomAkun) (l1.getAdapter())).notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        l1.
                setAdapter(new CustomAkun(nimS,passwords,this));
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                TextView t1 = (TextView) view.findViewById(R.id.textUser);
                TextView t2 = (TextView) view.findViewById(R.id.textPassword);



                Intent i = new Intent(KelolaAkun.this, kelolaMhs.class);
                i.putExtra("Username",t1.getText().toString()+"@lbsupr.com");
                i.putExtra("Password",t2.getText().toString());

                FirebaseAuth auth;
                auth = FirebaseAuth.getInstance();
                auth.signOut();
                startActivity(i);
                finish();


            }
        });


    }


}
