package com.example.hown.lbsftupr.schedule;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hown.lbsftupr.R;
import com.example.hown.lbsftupr.model.Ruangan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainFragmentMhs extends ListFragment {

    ArrayList<Item> allItems = new ArrayList<Item>();
    ArrayList<Item> items = new ArrayList<Item>();
    int position;
    DatabaseReference databaseItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        //if (args ==null) {
        position = args.getInt("position");
        allItems = args.getParcelableArrayList("items");
        // }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        items.clear();

        /**databaseItem = FirebaseDatabase.getInstance().getReference().child("jadwal");

         databaseItem.orderByValue().addValueEventListener(new ValueEventListener()  {
         public static final String TAG = "lstech.aos.debug";

         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
         for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


         for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {

         Item item = postSnapshot2.getValue(Item.class);




         items.add(item);


         Log.d(TAG, "" + item);



         }


         }

         /**for(Iterator<Item> it = items.iterator(); it.hasNext();) {
         Item item = it.next();
         if(item.gethari().equals(SchedActivity.DAYS[position])) {
         it.remove();
         //items.add(item);
         //items.remove(item);
         }
         }
         setListAdapter(new CustomAdapter());




         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
         });*/



        /**for(Item item:allItems) {
         if(item.gethari().equals(SchedActivity.DAYS[position])) {
         items.add(item);
         //items.remove(item);
         }
         }*/


        /**for(Iterator<Item> it = items.iterator(); it.hasNext();) {
         Item item = it.next();
         if(item.gethari().equals(SchedActivity.DAYS[position])) {
         it.remove();
         items.add(item);
         items.remove(item);
         }
         }*/

        for(Item item:allItems) {
            if(item.gethari().equals(SchedActivity.DAYS[position])) {
                items.add(item);
            }
        }


        setListAdapter(new CustomAdapter());


    }


    public static Fragment newInstance(ArrayList<Item> items, int position) {
        Fragment f = new MainFragment();
        Bundle args = new Bundle();
        //if (f == null) {
        args.putParcelableArrayList("items", items);
        args.putInt("position", position);
        f.setArguments(args);
        //}
        return f;
    }


    class CustomAdapter extends ArrayAdapter<Item> {

        public CustomAdapter() {
            super(getActivity(), android.R.layout.simple_list_item_1, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view==null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.row, null, false);
            }


            final ArrayList<String> mKeys1 = new ArrayList<String>();
            final ArrayList<String> r1 = new ArrayList<String>();

            final TextView textViewTime = (TextView)view.findViewById(R.id.textViewTime);
            TextView textViewSubject = (TextView)view.findViewById(R.id.textViewSubject);
            final TextView textViewVenue = (TextView)view.findViewById(R.id.textViewVenue);
            TextView textViewdsn = (TextView)view.findViewById(R.id.textViewdsn);
            textViewTime.setText(items.get(position).getjam());
            textViewSubject.setText(items.get(position).getmatakuliah());
            //textViewVenue.setText(items.get(position).getlokasi());
            textViewdsn.setText(items.get(position).getdosen_koordinator());
            Typeface roboto_bold = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-BoldCondensed.ttf");
            textViewTime.setTypeface(roboto_bold);
            Typeface roboto = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Condensed.ttf");
            textViewSubject.setTypeface(roboto);
            textViewVenue.setTypeface(roboto);
            textViewdsn.setTypeface(roboto);


            databaseItem = FirebaseDatabase.getInstance().getReference().child("jadwal");

            Query q = databaseItem.orderByChild("matakuliah").equalTo(textViewSubject.getText().toString());


            final DatabaseReference databaseItem1  = FirebaseDatabase.getInstance().getReference().child("ruangan");


            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


            //let

            final DatabaseReference databaseItem2  = FirebaseDatabase.getInstance().getReference().child("ruangan");

            DatabaseReference databaseUserJadwal = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("jadwal");

            final DatabaseReference databaseJadwal = FirebaseDatabase.getInstance().getReference().child("jadwal");

            databaseUserJadwal.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {
                            String key = postSnapshot2.getKey();

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
                                    final Item item = dataSnapshot.getValue(Item.class);


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

}

