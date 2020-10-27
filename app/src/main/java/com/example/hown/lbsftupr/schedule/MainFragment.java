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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainFragment extends ListFragment {

    ArrayList<Item> allItems = new ArrayList<Item>();
    ArrayList<Item> items = new ArrayList<Item>();
    final ArrayList<String> lokasis = new ArrayList<>();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;

            final ArrayList<String> mKeys1 = new ArrayList<String>();
            final ArrayList<String> r1 = new ArrayList<String>();

            if(view==null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.row, null, false);
            }
            TextView textViewTime = (TextView)view.findViewById(R.id.textViewTime);
            TextView textViewSubject = (TextView)view.findViewById(R.id.textViewSubject);
            final TextView textViewVenue = (TextView)view.findViewById(R.id.textViewVenue);
            TextView textViewdsn = (TextView)view.findViewById(R.id.textViewdsn);
            textViewTime.setText(items.get(position).getjam());
            textViewSubject.setText(items.get(position).getmatakuliah());
            //textViewVenue.setText(items.get(position).getlokasi());


            //textViewVenue.setText(lokasis.get(position).toString());

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

                                            //String rtg = value.getid().toString();

                                            StringBuilder sb = new StringBuilder();
                                            for (String s : r1) {
                                                sb.append(s);
                                                sb.append(",");
                                            }
                                            String ss = sb.toString();

                                            //lokasis.add(value.getid());

                                            Log.d(TAG, "isi ruangan " + ss);
                                            //}

                                            textViewVenue.setText(ss.toString());

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
}

