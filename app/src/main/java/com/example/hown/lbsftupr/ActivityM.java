package com.example.hown.lbsftupr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by hown on 06-Oct-17.
 */

public class ActivityM extends ActionBarActivity{

    public static String TAG = "lstech.aos.debug";

    static public boolean geofencesAlreadyRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);

        Fragment f = new MapFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, f, "home").commit();
        fragmentManager.executePendingTransactions();

        startService(new Intent(this, GeolocationService.class));
        //startService(new Intent(this, GeofenceIntentService.class));
    }
}
