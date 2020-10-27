package com.example.hown.lbsftupr;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hown.lbsftupr.model.Lantai;
import com.example.hown.lbsftupr.model.Ruangan;
import com.example.hown.lbsftupr.schedule.SchedActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by win10 on 25/09/2019.
 */

public class CustomListRuangan extends BaseAdapter {

    private ArrayList<String> id;
    private AppCompatActivity activity;

    private int x=0;

    public CustomListRuangan(ArrayList<String> id,AppCompatActivity activity){
        this.id=id;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return id.size();
    }

    @Override
    public Object getItem(int i) {
        return id.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.list_ruangan,viewGroup,false);
        final ArrayList<String> mKeys1 = new ArrayList<String>();
        final ArrayList<String> r1 = new ArrayList<String>();
        ((TextView)view.findViewById(R.id.ruanganlist)).setText(id.get(i));

        DatabaseReference databaseItem = FirebaseDatabase.getInstance().getReference().child("ruangan");
        //Query q = databaseItem.orderByChild("id").equalTo(((TextView)view.findViewById(R.id.ruanganlist)).getText().toString());

        final DatabaseReference databaseItem1  = FirebaseDatabase.getInstance().getReference().child("ruangan");

        final View finalView = view;
        final String TAG = lihat_gambar.class.getSimpleName();


                         /**   databaseItem.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {

                                        Lantai value = postSnapshot2.getValue(Lantai.class);

                                        r1.add(value.getid());



                                        Log.d(TAG, "isi ruangan " + r1);
                                        //}

                                        ((TextView) finalView.findViewById(R.id.ruanganlist)).setText(r1.toString());
                                    }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                      //  }
                   // }
                //}
            //}

            /**@Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        return view;
    }
}
