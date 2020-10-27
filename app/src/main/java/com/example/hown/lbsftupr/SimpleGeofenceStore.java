package com.example.hown.lbsftupr;

import android.text.format.DateUtils;

import com.google.android.gms.location.Geofence;

import java.util.ArrayList;
import java.util.HashMap;

public class SimpleGeofenceStore {

	private static String url = "http://10.216.143.247/lbsuprtest/ruangan.json";
	private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
	public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = GEOFENCE_EXPIRATION_IN_HOURS
			* DateUtils.HOUR_IN_MILLIS;

	private int loiteringDelay = 60000;

	private ArrayList<Geofence> mGeofenceList;
	protected HashMap<String, SimpleGeofence> geofences = new HashMap<String, SimpleGeofence>();
	private static SimpleGeofenceStore instance = new SimpleGeofenceStore();

	public static SimpleGeofenceStore getInstance() {
		return instance;
	}

	private SimpleGeofenceStore() {

		/**HttpHandler sh = new HttpHandler();

		// Making a request to url and getting response
		String jsonStr = sh.makeServiceCall(url);

		Log.d("Response: ", "> " + jsonStr);

		if (jsonStr != null) {
			try {

				JSONObject jsonObj = new JSONObject(jsonStr);

				JSONArray j1 = jsonObj.getJSONArray("ruangan");
				for (int i = 0; i < j1.length(); i++) {
					JSONObject j2 = j1.getJSONObject(i);
				String id1 = j2.getString("id");
					Double latt = j2.getDouble("latitude");
					Double longt = j2.getDouble("longitude");
					Integer radd = j2.getInt("radius");

					geofences.put(id1, new SimpleGeofence(id1, latt, longt,
							radd, GEOFENCE_EXPIRATION_IN_MILLISECONDS,
							Geofence.GEOFENCE_TRANSITION_ENTER
									| Geofence.GEOFENCE_TRANSITION_DWELL
									| Geofence.GEOFENCE_TRANSITION_EXIT));

					SimpleGeofence simpleGeofence = new SimpleGeofence(id1, latt, longt, radd, GEOFENCE_EXPIRATION_IN_MILLISECONDS, loiteringDelay);
					mGeofenceList.add(simpleGeofence);
				}
				//return mGeofenceList;

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}*/

		geofences.put("The Shire", new SimpleGeofence("The Shire", -33.87241, 151.27178,
				100, GEOFENCE_EXPIRATION_IN_MILLISECONDS,
				Geofence.GEOFENCE_TRANSITION_ENTER
						| Geofence.GEOFENCE_TRANSITION_DWELL
						| Geofence.GEOFENCE_TRANSITION_EXIT));
	}


	public HashMap<String, SimpleGeofence> getSimpleGeofences() {
		return this.geofences;
	}
}
