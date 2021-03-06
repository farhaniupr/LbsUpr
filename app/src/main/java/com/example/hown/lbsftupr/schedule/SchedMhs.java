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
import android.util.Log;

import com.example.hown.lbsftupr.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class SchedMhs extends FragmentActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<Item>> {




    ViewPager pager;
    PagerTitleStrip strip;
    public static final String[] DAYS = { "Senin", "Selasa", "Rabu",
            "Kamis", "Jumat" };
    ArrayList<Item> items = new ArrayList<Item>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sched_main);
        pager = (ViewPager) findViewById(R.id.pager);


        strip = (PagerTitleStrip)findViewById(R.id.titlestrip);
        strip.setTextColor(Color.DKGRAY);


        LoaderManager lm = getSupportLoaderManager();
        //if (lm ==null ){
        //getSupportLoaderManager().initLoader(0, null, this);
        lm.initLoader(0, null, this);
        //}

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if(pager.getChildCount() > 0) {
            Calendar calendar = Calendar.getInstance();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek>1 && dayOfWeek<7) {
                pager.setCurrentItem(dayOfWeek - 2);
            } else {
                pager.setCurrentItem(0);
            }
        }
    }

    @Override
    public Loader<ArrayList<Item>> onCreateLoader(int arg0, Bundle arg1) {
        // TODO Auto-generated method stub
        return new AssetLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Item>> loader,
                               final ArrayList<Item> items) {
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return DAYS.length;
            }

            @Override
            public Fragment getItem(int position) {
                //AsyncTaskLoader<ArrayList<Items>>;
                Fragment f = MainFragmentMhs.newInstance(items, position);
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
            pager.setCurrentItem(dayOfWeek - 2);
        } else {
            pager.setCurrentItem(0);
        }





    }



    @Override
    public void onLoaderReset(Loader<ArrayList<Item>> arg0) {
        // TODO Auto-generated method stub

        //getLoaderManager().destroyLoader(0);
    }





    static class AssetLoader extends AsyncTaskLoader<ArrayList<Item>> {

        Context context;
        ArrayList<Item> items = new ArrayList<Item>();
        ArrayList<String> mKeys1 = new ArrayList<String>();
        //ArrayList<String> mKeys2 = new ArrayList<>();
        DatabaseReference databaseJadwal;
        DatabaseReference databaseUserJadwal;
        public static final String[] DAYS = { "Senin", "Selasa", "Rabu",
                "Kamis", "Jumat" };
        int position;
        private static final String TAG = SchedMhs.class.getSimpleName();

        public AssetLoader(Context context) {
            super(context);
            this.context = context;
            // TODO Auto-generated constructor stub
        }

        @Override
        public ArrayList<Item> loadInBackground() {

            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


            //let

            final DatabaseReference databaseItem1  = FirebaseDatabase.getInstance().getReference().child("ruangan");

            databaseUserJadwal = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("jadwal");

            databaseJadwal = FirebaseDatabase.getInstance().getReference().child("jadwal");

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
                                    final Item item = dataSnapshot.getValue(Item.class);


                                    items.add(item);


                                    //items.add(item);
                                    Log.d(TAG, "" + item);


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

        @Override
        protected void onStopLoading() {
            cancelLoad();
        }

    }

}
