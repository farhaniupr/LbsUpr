package com.example.hown.lbsftupr.schedule;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hown.lbsftupr.R;
import com.example.hown.lbsftupr.model.Ruangan;
import com.example.hown.lbsftupr.model.jadwalU;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainFragmentMhsU extends ListFragment {

    private static final String TAG = MainFragmentFirebase.class.getSimpleName();
    ArrayList<jadwalU> allItems = new ArrayList<jadwalU>();
    ArrayList<jadwalU> items = new ArrayList<jadwalU>();
    int position;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        position = args.getInt("position");
        allItems = args.getParcelableArrayList("jadwalU");

        Log.d(TAG, "OnCreate" + allItems);





    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);


        items.clear();



        for(jadwalU item:allItems) {
            if(item.gethari().equals(SchedFirebase.DAYS[position])) {
                items.add(item);
                Log.d(TAG, "onActivityCreated" + items);
            }
        }
        setListAdapter(new CustomAdapter());







    }


    public static Fragment newInstance(ArrayList<jadwalU> items, int position) {
        Fragment f = new MainFragmentFirebase();
        Bundle args = new Bundle();
        args.putParcelableArrayList("jadwalU", items);
        args.putInt("position", position);
        f.setArguments(args);
        return f;
    }


    class CustomAdapter extends ArrayAdapter<jadwalU> {

        //List<Item> items1;


        public CustomAdapter() {
            super(getActivity(), android.R.layout.simple_list_item_1, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view==null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.rowu, null, false);
            }

            final ArrayList<String> mKeys1 = new ArrayList<String>();
            final ArrayList<String> r1 = new ArrayList<String>();

            TextView textViewTime = (TextView)view.findViewById(R.id.textViewTime);
            TextView textViewSubject = (TextView)view.findViewById(R.id.textViewSubject);
            final TextView textViewVenue = (TextView)view.findViewById(R.id.textViewVenue);
            TextView textViewdsn = (TextView)view.findViewById(R.id.textViewdsn);
            TextView textViewtanggal = (TextView)view.findViewById(R.id.textViewtanggal);
            textViewTime.setText(items.get(position).getjam());
            textViewSubject.setText(items.get(position).getmatakuliah());
            //textViewVenue.setText(items.get(position).getlokasi());
            textViewdsn.setText(items.get(position).getDosen_koordinator());
            textViewtanggal.setText(items.get(position).getTanggal());
            Typeface roboto_bold = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-BoldCondensed.ttf");
            textViewTime.setTypeface(roboto_bold);
            Typeface roboto = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Condensed.ttf");
            textViewSubject.setTypeface(roboto);
            textViewVenue.setTypeface(roboto);
            textViewdsn.setTypeface(roboto);

            DatabaseReference databaseItem = FirebaseDatabase.getInstance().getReference().child("jadwal");

            Query q = databaseItem.orderByChild("matakuliah").equalTo(textViewSubject.getText().toString());


            final DatabaseReference databaseItem1  = FirebaseDatabase.getInstance().getReference().child("ruangan");


            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


            //let

            final DatabaseReference databaseItem2  = FirebaseDatabase.getInstance().getReference().child("ruangan");

            DatabaseReference databaseUserJadwal = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("jadwalU");

            final DatabaseReference databaseJadwal = FirebaseDatabase.getInstance().getReference().child("jadwalU");

            databaseUserJadwal.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {
                            String key = postSnapshot.getKey();

                            mKeys1.add(key);
                            /**StringBuilder sb = new StringBuilder();
                             for (String s : mKeys1) {
                             sb.append(s);
                             sb.append("\t");
                             }

                             Log.d(TAG, "" + sb.toString());*/

                            databaseJadwal.child(key).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final jadwalU item = dataSnapshot.getValue(jadwalU.class);


                                    items.add(item);



                                    for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {
                                        //for (DataSnapshot postSnapshot3 : postSnapshot2.getChildren()) {
                                            String key1 = postSnapshot2.getKey();

                                            mKeys1.add(key1);

                                            //.d(TAG, "isi key " + key);

                                            databaseItem1.child(key1).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    //for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {
                                                    Ruangan value = dataSnapshot.getValue(Ruangan.class);

                                                    r1.add(value.getid());

                                                    StringBuilder sb = new StringBuilder();
                                                    for (String s : r1) {
                                                        sb.append(s);
                                                        sb.append(",");
                                                    }
                                                    String ss = sb.toString();

                                                    //lokasis.add(value.getid());

                                                    //Log.d(TAG, "isi ruangan " + ss);
                                                    //}

                                                    textViewVenue.setText(ss.toString());

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    //}

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        //}
                    }


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            return view;
        }



    }

}

