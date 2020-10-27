package com.example.hown.lbsftupr;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
 * Created by hown on 09-Feb-18.
 */

public class CustomListUAdapater extends BaseAdapter {

    private ArrayList<String> matakuliah;
    private ArrayList<String> jam;
    private ArrayList<String> lokasi;
    private ArrayList<String> dsn_koor;
    private ArrayList<String> tanggal;
    private AppCompatActivity activity;

    private int x=0;


    public CustomListUAdapater(ArrayList<String> matakuliah,ArrayList<String> jam,ArrayList<String> lokasi, ArrayList<String> dsn_koor,ArrayList<String> tanggal,AppCompatActivity activity){
        this.matakuliah=matakuliah;
        this.jam=jam;
        this.lokasi=lokasi;
        this.dsn_koor=dsn_koor;
        this.tanggal=tanggal;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return matakuliah.size();
    }

    @Override
    public Object getItem(int i) {
        return matakuliah.get(i);
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.uas_list_row,viewGroup,false);

        final ArrayList<String> mKeys1 = new ArrayList<String>();
        final ArrayList<String> r1 = new ArrayList<String>();
        ((TextView)view.findViewById(R.id.jamlistU)).setText(jam.get(i));
        ((TextView)view.findViewById(R.id.matakuliahlistU)).setText(matakuliah.get(i));
        //((TextView)view.findViewById(R.id.lokasilistU)).setText(lokasi.get(i));
        ((TextView)view.findViewById(R.id.dsn_koorlistU)).setText(dsn_koor.get(i));
        ((TextView)view.findViewById(R.id.tanggallistU)).setText(tanggal.get(i));

        DatabaseReference databaseItem = FirebaseDatabase.getInstance().getReference().child("jadwalU");

        Query q = databaseItem.orderByChild("matakuliah").equalTo(((TextView)view.findViewById(R.id.matakuliahlistU)).getText().toString());


        final DatabaseReference databaseItem1  = FirebaseDatabase.getInstance().getReference().child("ruangan");
        final View finalView = view;
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            public final String TAG = SchedActivity.class.getSimpleName();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {
                        for (DataSnapshot postSnapshot3 : postSnapshot2.getChildren()) {
                            String key = postSnapshot3.getKey();

                            mKeys1.add(key);

                            Log.d(TAG, "isi key " + key);

                            databaseItem1.child(key).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {
                                    if (dataSnapshot.exists()) {
                                        Ruangan value = dataSnapshot.getValue(Ruangan.class);

                                        r1.add(value.getid());

                                        String rtg = value.getid().toString();

                                        StringBuilder sb = new StringBuilder();
                                        for (String s : r1) {
                                            sb.append(s);
                                            sb.append(",");
                                        }
                                        String ss = sb.toString();

                                        //lokasis.add(value.getid());

                                        Log.d(TAG, "isi ruangan " + ss);
                                        //}

                                        ((TextView) finalView.findViewById(R.id.lokasilistU)).setText(ss.toString());
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
