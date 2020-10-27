package com.example.hown.lbsftupr.model;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hown on 18-Oct-17.
 */

/**
 * Constants used in this sample.
 */

final class Constants {


    private Constants() {
    }

    private static String url = "http://10.216.182.115/lbsuprtest/ruangan.json";

    private String TAG = "Lbs";

    ArrayList<HashMap<String, LatLng>> geostorelist;

    private static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";

    static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

    /**
     * Used to set an expiration time for a geofence. After this amount of time Location Services
     * stops tracking the geofence.
     */
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;

    /**
     * For this sample, geofences expire after twelve hours.
     */
    static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    static final float GEOFENCE_RADIUS_IN_METERS = 25; // 1 mile, 1.6 km


    private class GetRuangan extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            //pDialog = new ProgressDialog(ActivityM.this);
            //pDialog.setMessage("Please wait...");
            //pDialog.setCancelable(false);
            //pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray ruangan = jsonObj.getJSONArray("ruangan");

                    // looping through All Contacts
                    for (int i = 0; i < ruangan.length(); i++) {
                        JSONObject c = ruangan.getJSONObject(i);

                        String id = c.getString("id");
                        double Latitude = c.getDouble("Latitude");
                        double longitude = c.getDouble("longitude");

                        //String radius = c.getString("radius");
                        //String gender = c.getString("gender");

                        // Phone node is JSON Object
                        //JSONObject phone = c.getJSONObject("phone");
                        //String mobile = phone.getString("mobile");
                        //String home = phone.getString("home");
                        //String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, com.google.android.gms.maps.model.LatLng> ruanganlbs = new HashMap<>();

                        // adding each child node to HashMap key => value
                        ruanganlbs.put(id, new LatLng(Latitude, longitude));
                        //ruanganlbs.put("longitude", longitude);
                        //ruanganlbs.put("radius", radius);

                        // adding contact to contact list
                        geostorelist.add(ruanganlbs);
                    }
                    // return geostorelist;
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());


                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");


            }

            return null;
        }
    }


    /**
     * Map for storing information about airports in the San Francisco bay area.
     */
    static final HashMap<String, LatLng> BAY_AREA_LANDMARKS = new HashMap<>();

    static {
        // San Francisco International Airport.
        HttpHandler sh = new HttpHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(url);

        //Log.e(TAG, "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONArray ruangan = jsonObj.getJSONArray("ruangan");

                // looping through All Contacts
                for (int i = 0; i < ruangan.length(); i++) {
                    JSONObject c = ruangan.getJSONObject(i);

                    String id = c.getString("id");
                    double Latitude = c.getDouble("Latitude");
                    double longitude = c.getDouble("longitude");

                    //String radius = c.getString("radius");
                    //String gender = c.getString("gender");

                    // Phone node is JSON Object
                    //JSONObject phone = c.getJSONObject("phone");
                    //String mobile = phone.getString("mobile");
                    //String home = phone.getString("home");
                    //String office = phone.getString("office");

                    // tmp hash map for single contact
                    HashMap<String, com.google.android.gms.maps.model.LatLng> ruanganlbs = new HashMap<>();

                    // adding each child node to HashMap key => value
                    ruanganlbs.put(id, new LatLng(Latitude, longitude));
                    //ruanganlbs.put("longitude", longitude);
                    //ruanganlbs.put("radius", radius);

                    // adding contact to contact list
                    //geostorelist.add(ruanganlbs);
                }
                //return ruangan;
            } catch (final JSONException e) {
                // Log.e(TAG, "Json parsing error: " + e.getMessage());


            }

            //BAY_AREA_LANDMARKS.put("SFO", new LatLng(37.621313, -122.378955));

            // Googleplex.
            //BAY_AREA_LANDMARKS.put("GOOGLE", new LatLng(37.422611,-122.0840577));
        }
    }
}





