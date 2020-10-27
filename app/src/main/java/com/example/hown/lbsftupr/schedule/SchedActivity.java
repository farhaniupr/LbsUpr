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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class SchedActivity extends FragmentActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<Item>> {


    //private static String url = "http://192.168.43.174/lbsuprtest/json.php";
    private static final int LOADER_ID = 1;


    ViewPager pager;
    PagerTitleStrip strip;
    public static final String[] DAYS = { "Senin", "Selasa", "Rabu",
            "Kamis", "Jumat", "Sabtu" };
    ArrayList<Item> items = new ArrayList<Item>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sched_main);
        pager = (ViewPager) findViewById(R.id.pager);

        strip = (PagerTitleStrip)findViewById(R.id.titlestrip);
        strip.setTextColor(Color.DKGRAY);


        LoaderManager lm = getSupportLoaderManager();

        lm.initLoader(0, null, this);


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
                Fragment f = MainFragment.newInstance(items, position);
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

        getSupportLoaderManager().destroyLoader(0);



    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Item>> arg0) {
        // TODO Auto-generated method stub

        //getLoaderManager().destroyLoader(0);
    }

    static class AssetLoader extends AsyncTaskLoader<ArrayList<Item>> {

        Context context;
        ArrayList<Item> items = new ArrayList<Item>();
        final ArrayList<String> mKeys1 = new ArrayList<String>();
        final ArrayList<String> r1 = new ArrayList<String>();
        final ArrayList<String> matakuliahs = new ArrayList<>();
        final ArrayList<String> jams = new ArrayList<>();
        final ArrayList<String> lokasis = new ArrayList<>();
        final ArrayList<String> dsn_koors = new ArrayList<>();
        final ArrayList<String> haris = new ArrayList<>();



        DatabaseReference databaseItem;
        public static final String[] DAYS = { "Senin", "Selasa", "Rabu",
                "Kamis", "Jumat", "Sabtu"};
        int position;

        public AssetLoader(Context context) {
            super(context);
            this.context = context;
            // TODO Auto-generated constructor stub
        }

        @Override
        public ArrayList<Item> loadInBackground() {

            databaseItem = FirebaseDatabase.getInstance().getReference().child("jadwal");


            final DatabaseReference databaseItem1  = FirebaseDatabase.getInstance().getReference().child("ruangan");
            databaseItem.addValueEventListener(new ValueEventListener() {
                public final String TAG = SchedActivity.class.getSimpleName();

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //for (DataSnapshot postSnapshot1 : postSnapshot.getChildren()) {
                            final Item item = postSnapshot.getValue(Item.class);

                            /**matakuliahs.add(item.getmatakuliah());
                             jams.add(item.getjam());
                             haris.add(item.gethari());

                             dsn_koors.add(item.getdosen_koordinator());*/


                            items.add(item);


                            //items.add(item);
                            Log.d(TAG, "isi jadwal " + item);

                            for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {
                                for (DataSnapshot postSnapshot3 : postSnapshot2.getChildren()) {
                                    String key = postSnapshot3.getKey();

                                    mKeys1.add(key);

                                    Log.d(TAG, "isi key " + key);

                                    databaseItem1.child(key).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            //for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {
                                            //Ruangan value = dataSnapshot.getValue(Ruangan.class);

                                            //r1.add(value.getid());

                                            //String rtg = value.getid().toString();

                                            ///lokasis.add(value.getid());

                                            Log.d(TAG, "isi ruangan " + r1);
                                            //}

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }


                        //}
                    }

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
