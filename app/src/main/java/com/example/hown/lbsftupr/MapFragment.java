package com.example.hown.lbsftupr;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.hown.lbsftupr.model.HttpHandler;
import com.example.mylibrary.data.Feature;
import com.example.mylibrary.data.kml.KmlContainer;
import com.example.mylibrary.data.kml.KmlLayer;
import com.example.mylibrary.data.kml.KmlPlacemark;
import com.example.mylibrary.data.kml.KmlPolygon;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment {
	protected SupportMapFragment mapFragment;
	protected GoogleMap map;
	protected Marker myPositionMarker;

	private static String url = "http://10.216.243.93/lbsuprtest/ruangan.json";

	String[] daftarid;
	//String[] daftarnama_wisata;
	//String[] daftarinfo_wisata;
	String[] daftarlatitude;
	String[] daftarlongitude;

	LatLng fitness;
	// contacts JSONArray
	JSONArray menuitemArray = null;

	/**
	 * Geofence Data
	 */

	/**
	 * Geofences Array
	 */
	ArrayList<Geofence> mGeofences;

	/**
	 * Geofence Coordinates
	 */
	ArrayList<LatLng> mGeofenceCoordinates;

	/**
	 * Geofence Radius'
	 */
	ArrayList<Integer> mGeofenceRadius;

	/**
	 * Geofence Store
	 */

	public ProgressDialog pDialog;
	public GeofenceStore mGeofenceStore;


	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				int resultCode = bundle.getInt("done");
				if (resultCode == 1) {
					Double latitude = bundle.getDouble("latitude");
					Double longitude = bundle.getDouble("longitude");

					updateMarker(latitude, longitude);
				}
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		setHasOptionsMenu(true);

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_map, container,
				false);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		mapFragment = SupportMapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getChildFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(R.id.map_container, mapFragment);
		fragmentTransaction.commit();

		return rootView;
	}

	@Override
	public void onPause() {
		super.onPause();

		getActivity().unregisterReceiver(receiver);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mapFragment != null) {
			mapFragment.getMapAsync(new OnMapReadyCallback() {

				@Override
				public void onMapReady(GoogleMap googleMap) {
					map = googleMap;

					try {
						//this.map = map;
						map.setMyLocationEnabled(true);
						retrieveFileFromResource();
						//retrieveFileFromUrl();
					} catch (Exception e) {
						Log.e("Exception caught", e.toString());
					}
					map.animateCamera(CameraUpdateFactory.zoomTo(15));
					//displayGeofences();
					new GetRuangan().execute();
				}
			});
		}

		getActivity().registerReceiver(receiver,
				new IntentFilter("com.example.hown.lbsftupr.geolocation.service"));
	}

	private void retrieveFileFromResource() {
		try {
			KmlLayer kmlLayer = new KmlLayer(map, R.raw.ituprv5, getActivity().getApplicationContext());
			kmlLayer.addLayerToMap();
			moveCameraToKml(kmlLayer);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}



	public class DownloadKmlFile extends AsyncTask<String, Void, byte[]> {
		private final String mUrl;

		public DownloadKmlFile(String url) {
			mUrl = url;
		}

		protected byte[] doInBackground(String... params) {
			try {
				InputStream is = new URL(mUrl).openStream();
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				int nRead;
				byte[] data = new byte[16384];
				while ((nRead = is.read(data, 0, data.length)) != -1) {
					buffer.write(data, 0, nRead);
				}
				buffer.flush();
				return buffer.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(byte[] byteArr) {
			try {
				KmlLayer kmlLayer = new KmlLayer(map, new ByteArrayInputStream(byteArr),
						getActivity().getApplicationContext());
				kmlLayer.addLayerToMap();
				kmlLayer.setOnFeatureClickListener(new KmlLayer.OnFeatureClickListener() {
					@Override
					public void onFeatureClick(Feature feature) {
						//Toast.makeText(MapFragment.this,
						//		"Feature clicked: " + feature.getId(),
						//		Toast.LENGTH_SHORT).show();
					}
				});
				moveCameraToKml(kmlLayer);
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void retrieveFileFromUrl() {
		new MapFragment.DownloadKmlFile(getString(R.string.kml_url)).execute();
	}

	private void moveCameraToKml(KmlLayer kmlLayer) {
		//Retrieve the first container in the KML layer
		KmlContainer container = kmlLayer.getContainers().iterator().next();
		//Retrieve a nested container within the first container
		container = container.getContainers().iterator().next();
		//Retrieve the first placemark in the nested container
		KmlPlacemark placemark = container.getPlacemarks().iterator().next();
		//Retrieve a polygon object in a placemark
		KmlPolygon polygon = (KmlPolygon) placemark.getGeometry();
		//Create LatLngBounds of the outer coordinates of the polygon
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (LatLng latLng : polygon.getOuterBoundaryCoordinates()) {
			builder.include(latLng);
		}

		int width = getResources().getDisplayMetrics().widthPixels;
		int height = getResources().getDisplayMetrics().heightPixels;

		map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width, height, 1));
	}

	protected void displayGeofences() {
		HashMap<String, SimpleGeofence> geofences = SimpleGeofenceStore
				.getInstance().getSimpleGeofences();

		for (Map.Entry<String, SimpleGeofence> item : geofences.entrySet()) {
			SimpleGeofence sg = item.getValue();

			CircleOptions circleOptions1 = new CircleOptions()
					.center(new LatLng(sg.getLatitude(), sg.getLongitude()))
					.radius(sg.getRadius()).strokeColor(Color.BLACK)
					.strokeWidth(2).fillColor(0x500000ff);
			map.addCircle(circleOptions1);
		}



	}

	/**private void populateGeofenceList() {
		//for (Map.Entry<String, LatLng> entry : Constants.ruanganlbs.entrySet()) {
		for (Map.Entry<String, SimpleGeofence> entry : mG.entrySet()) {
			mGeofenceList.add(new Geofence.Builder()
					// Set the request ID of the geofence. This is a string to identify this
					// geofence.
					.setRequestId(entry.getKey())

					// Set the circular region of this geofence.
					.setCircularRegion(
							entry.getValue().latitude,
							entry.getValue().longitude,
							Constants.GEOFENCE_RADIUS_IN_METERS
					)

					// Set the expiration duration of the geofence. This geofence gets automatically
					// removed after this period of time.
					.setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

					// Set the transition types of interest. Alerts are only generated for these
					// transition. We track entry and exit transitions in this sample.
					.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
							Geofence.GEOFENCE_TRANSITION_EXIT)

					// Create the geofence.
					.build());
		}
	}*/

	protected void createMarker(Double latitude, Double longitude) {
		LatLng latLng = new LatLng(latitude, longitude);

		myPositionMarker = map.addMarker(new MarkerOptions().position(latLng));
		map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	}

	protected void updateMarker(Double latitude, Double longitude) {
		if (myPositionMarker == null) {
			createMarker(latitude, longitude);
		}

		LatLng latLng = new LatLng(latitude, longitude);
		myPositionMarker.setPosition(latLng);
		map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.menu_map, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.events:
			Fragment f = new EventsFragment();

			getFragmentManager().beginTransaction()
					.replace(android.R.id.content, f).addToBackStack("events")
					.commit();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class GetRuangan extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			//pDialog = new ProgressMapFragmentDialog(.this);
			//pDialog.setMessage("Please wait...");
			//pDialog.setCancelable(false);
			//pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			HttpHandler sh = new HttpHandler ();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					menuitemArray = jsonObj.getJSONArray("ruangan");

					daftarid = new String[menuitemArray.length()];
					//aftarnama_wisata = new String[menuitemArray.length()];
					//daftarinfo_wisata = new String[menuitemArray.length()];
					daftarlatitude = new String[menuitemArray.length()];
					daftarlongitude = new String[menuitemArray.length()];


				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}



		// @Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			//if (pDialog.isShowing())
			//	pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			HttpHandler sh = new HttpHandler ();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url);

			Log.d("Response: ", "> " + jsonStr);


			try {

				JSONObject jsonObj = new JSONObject(jsonStr);
													// Initializing variables
				mGeofences = new ArrayList<Geofence>();
				//mGeofenceCoordinates = new ArrayList<LatLng>();
				//mGeofenceRadius = new ArrayList<Integer>();

				double latitude = 0;
				double longitude = 0;
				//JSONObject jObject = new JSONObject(builder.toString());
				JSONArray jDays1 = jsonObj.getJSONArray("ruangan");
				for (int i = 0; i < jDays1.length(); i++) {
					latitude = Double.parseDouble(jDays1.getJSONObject(i).getString("latitude").toString());
					longitude = Double.parseDouble(jDays1.getJSONObject(i).getString("longitude").toString());
					//fitness = new LatLng(latitude, longitude);
					//MarkerOptions markerFitness = new MarkerOptions();
					//markerFitness.position(fitness);
					//markerFitness.title(menuitemArray.getJSONObject(i).getString("nama_wisata").toString());
					//markerFitness.snippet(menuitemArray.getJSONObject(i).getString("info_wisata").toString());
					//rkerFitness.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
					//map.addMarker(markerFitness);

					//Instantiates a new CircleOptions object +  center/radius
					CircleOptions circleOptions = new CircleOptions()
							.center( new LatLng(latitude, longitude) )
							.radius( 10 )
							.fillColor(0x40ff0000)
							.strokeColor(Color.TRANSPARENT)
							.strokeWidth(2);

					// Get back the mutable Circle
					Circle circle = map.addCircle(circleOptions);
					// more operations on the circle...

					String id = jDays1.getJSONObject(i).getString("id").toString();
					//daftarnama_wisata[i] = menuitemArray.getJSONObject(i).getString("nama_wisata").toString();
					//daftarinfo_wisata[i] = menuitemArray.getJSONObject(i).getString("info_wisata").toString();
					//String latt = jDays1.getJSONObject(i).getString("latitude").toString();
					//String longt = jDays1.getJSONObject(i).getString("longitude").toString();

					// Adding geofence coordinates to array.
					double lat1= Double.parseDouble(jDays1.getJSONObject(i).getString("latitude").toString());
					double long1= Double.parseDouble(jDays1.getJSONObject(i).getString("longitude").toString());
					int rad1 = Integer.parseInt(jDays1.getJSONObject(i).getString("radius").toString());
					//mGeofenceCoordinates.add(new LatLng(lat1, long1));

					// Adding associated geofence radius' to array.
					//mGeofenceRadius.add(100);

					// Bulding the geofences and adding them to the geofence array.

					// Performing Arts Center
					mGeofences.add(new Geofence.Builder()
							.setRequestId(id)
							// The coordinates of the center of the geofence and the radius in meters.
							.setCircularRegion(lat1, long1, rad1)
							.setExpirationDuration(Geofence.NEVER_EXPIRE)
							// Required when we use the transition type of GEOFENCE_TRANSITION_DWELL
							.setLoiteringDelay(30000)
							.setTransitionTypes(
									Geofence.GEOFENCE_TRANSITION_ENTER
											| Geofence.GEOFENCE_TRANSITION_DWELL
											| Geofence.GEOFENCE_TRANSITION_EXIT).build());

					// Add the geofences to the GeofenceStore object.
					//mGeofenceStore = new GeofenceStore(MapFragment.this, mGeofences);
					//mGeofenceStore = new GeofenceStore(this, mGeofences);
				}

				CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(-2.216207, 113.89811)).zoom(8).build();

				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		mGeofenceStore.disconnect();
		super.onStop();
	}

}
