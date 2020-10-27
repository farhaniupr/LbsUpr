package com.example.hown.lbsftupr.admin;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import java.util.Locale;

/**
 * Created by hown on 25-Feb-18.
 */

public class CustomAkun extends BaseAdapter {
    private ArrayList<String> nama;
    private ArrayList<String> nim;
    private ArrayList<String> password;
    final ArrayList<user2> u11 = new ArrayList<user2>();
    //private ArrayList<String> dsn_koor;
    private AppCompatActivity activity;

    private int x=0;


    public CustomAkun(ArrayList<String> nim,ArrayList<String> password, AppCompatActivity activity){
        //this.nama=nama;
        this.nim=nim;
        this.password=password;
        //this.dsn_koor=dsn_koor;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return nim.size();
    }

    @Override
    public Object getItem(int i) {
        return nim.get(i);
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.row_akun,viewGroup,false);

        final ArrayList<String> mKeys1 = new ArrayList<String>();
        final ArrayList<String> r1 = new ArrayList<String>();

        ((TextView)view.findViewById(R.id.textUser)).setText(nim.get(i));
        ((TextView)view.findViewById(R.id.textPassword)).setText(password.get(i));
        //((TextView)view.findViewById(R.id.lokasilist)).setText(lokasi.get(i));
        //((TextView)view.findViewById(R.id.dsn_koorlist)).setText(password.get(i));


        return view;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        nim.clear();
        password.clear();


        if (charText.length() == 0) {



            DatabaseReference d1 = FirebaseDatabase.getInstance().getReference().child("user");
            d1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        user2 u1 = childSnapshot.getValue(user2.class);
                        //Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                        u11.add(u1);

                        //namaS.add(u1.getNama());
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        nim.add(u1.getNim().toString());
                        password.add(u1.getPassword().toString());
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else
        {
            for (user2 wp : u11)
            {
                if (wp.getNim().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    nim.add(wp.getNim());
                    password.add(wp.getPassword());
                }
            }
        }
        notifyDataSetChanged();
    }


}
