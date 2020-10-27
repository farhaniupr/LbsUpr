package com.example.hown.lbsftupr.schedule;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;

import com.example.hown.lbsftupr.R;
import com.example.hown.lbsftupr.model.jadwalU;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class SchedMhsU extends FragmentActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<jadwalU>> {



    ViewPager pager;
    PagerTitleStrip strip;
    public static final String[] DAYS = { "Senin", "Selasa", "Rabu",
            "Kamis", "Jumat", "Sabtu" };
    ArrayList<jadwalU> jadwalU = new ArrayList<jadwalU>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sched_main);
        pager = (ViewPager) findViewById(R.id.pager);


        strip = (PagerTitleStrip)findViewById(R.id.titlestrip);
        strip.setTextColor(Color.DKGRAY);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if(pager.getChildCount() > 0) {
            Calendar calendar = Calendar.getInstance();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek>1 && dayOfWeek<7) {
                pager.setCurrentItem(dayOfWeek - 1);
            } else {
                pager.setCurrentItem(0);
            }
        }
    }

    @Override
    public Loader<ArrayList<jadwalU>> onCreateLoader(int arg0, Bundle arg1) {
        // TODO Auto-generated method stub
        return new AssetLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<jadwalU>> loader,
                               final ArrayList<jadwalU> items) {
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return DAYS.length;
            }

            @Override
            public Fragment getItem(int position) {
                //AsyncTaskLoader<ArrayList<Items>>;
                Fragment f = MainFragmentFirebase.newInstance(items, position);
                return f;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return DAYS[position];
            }
        });

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if(dayOfWeek>1 && dayOfWeek<7) {
            pager.setCurrentItem(dayOfWeek - 1);
        } else {
            pager.setCurrentItem(0);
        }


        /**SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
         String session = prefs.getString("session", "");
         String trimester = prefs.getString("trimester", "");
         String group = prefs.getString("group", "");
         if(!session.equals("")) {
         setTitle("Tri " + trimester + " " + session + " Group " + group);
         }*/
    }



    @Override
    public void onLoaderReset(Loader<ArrayList<jadwalU>> arg0) {
        // TODO Auto-generated method stub

    }


    static class AssetLoader extends AsyncTaskLoader<ArrayList<jadwalU>> {

        Context context;
        ArrayList<jadwalU> items = new ArrayList<jadwalU>();

        ArrayList<String> mKeys1 = new ArrayList<String>();
        //DatabaseReference databaseItem;

        DatabaseReference databaseJadwal;
        DatabaseReference databaseUserJadwal;
        public static final String[] DAYS = { "Senin", "Selasa", "Rabu",
                "Kamis", "Jumat", "Sabtu"};
        int position;

        public AssetLoader(Context context) {
            super(context);
            this.context = context;
            // TODO Auto-generated constructor stub
        }

        @Override
        public ArrayList<jadwalU> loadInBackground() {

            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


            //let

            databaseUserJadwal = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("jadwalU");

            databaseJadwal = FirebaseDatabase.getInstance().getReference().child("jadwalU");

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
                                    jadwalU item = dataSnapshot.getValue(jadwalU.class);


                                    items.add(item);


                                    //items.add(item);
                                    //Log.d(TAG, "" + item);
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

            return items;

        }

        @Override
        protected void onStartLoading() {
            if (!items.isEmpty()) {
                deliverResult(items);
            }

            if (takeContentChanged() || items.isEmpty()) {
                forceLoad();
            }
        }

    }

}
