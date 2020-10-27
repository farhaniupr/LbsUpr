package com.example.hown.lbsftupr;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.hown.lbsftupr.model.HttpHandler;
import com.example.hown.lbsftupr.model.Ruangan;
import com.example.mylibrary.data.Feature;
import com.example.mylibrary.data.kml.KmlContainer;
import com.example.mylibrary.data.kml.KmlLayer;
import com.example.mylibrary.data.kml.KmlPlacemark;
import com.example.mylibrary.data.kml.KmlPolygon;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class MapActivity2 extends FragmentActivity implements OnCameraChangeListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {


	private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 124;
	private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 123;

	private static final int SHORT_DELAY = 2000;

	private static final String TAG = MapActivity2.class.getSimpleName();


	private GpsTool gpsTool;

	private static final long GEO_DURATION = 60 * 60 * 1000;
	private LocationRequest locationRequest;
	private final int REQ_PERMISSION = 999;
	private final int UPDATE_INTERVAL =  1000;
	private final int FASTEST_INTERVAL = 900;


	GoogleApiClient mGoogleApiClient;
	protected SupportMapFragment mapFragment;
	protected GoogleMap map;
	protected Marker myPositionMarker;

	private Location lastLocation;


	private static String url = "https://lbsuprtest.000webhostapp.com/ruangan.json";
	String[] daftarid;
	String[] daftarlatitude;
	String[] daftarlongitude;

	ArrayList<Ruangan> rruangan = new ArrayList<Ruangan>();

	LatLng fitness;
	// ruangan JSONArray
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

	ArrayList<String> mGeofenceId;

	ArrayList<Ruangan> r1 = new ArrayList<Ruangan>();

	private PendingIntent mGeofencePendingIntent;

	/**
	 * Geofence Store
	 */

	public ProgressDialog pDialog;
	public GeofenceStore mGeofenceStore;
	DatabaseReference databaseItem;

	private GeofencingClient mGeofencingClient;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps1);

		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {

			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					Manifest.permission.ACCESS_FINE_LOCATION)) {

			} else {

				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
						MY_PERMISSIONS_REQUEST_FINE_LOCATION);
			}
		}
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_COARSE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {

			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					Manifest.permission.ACCESS_COARSE_LOCATION)) {

			} else {
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
						MY_PERMISSIONS_REQUEST_COARSE_LOCATION);

			}
		}





		setupMapIfNeeded();

		createGoogleApi();



		//startGeofence();

		databaseItem = FirebaseDatabase.getInstance().getReference().child("ruangan");



		mGeofences = new ArrayList<Geofence>();

		databaseItem.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

					Ruangan ruangan = postSnapshot.getValue(Ruangan.class);

					Double latitude = Double.parseDouble(ruangan.getlatitude().toString());
					Double longitude = Double.parseDouble(ruangan.getlongitude().toString());

					String id = (ruangan.getid().toString());

					int rad1 = Integer.parseInt(ruangan.getradius());

					Log.d(TAG, "Ruangan" + ruangan);

					CircleOptions circleOptions = new CircleOptions()
							.center( new LatLng(latitude, longitude) )
							.radius( rad1 )
							.fillColor(0x40ff0000)
							.strokeColor(Color.TRANSPARENT)
							.strokeWidth(2);

					Circle circle = map.addCircle(circleOptions);

					mGeofences.add(new Geofence.Builder()
							.setRequestId(ruangan.getid())
							// The coordinates of the center of the geofence and the radius in meters.
							.setCircularRegion(Double.parseDouble(ruangan.getlatitude()), Double.parseDouble(ruangan.getlongitude()), Integer.parseInt(ruangan.getradius()))
							.setExpirationDuration(Geofence.NEVER_EXPIRE)
							// Required when we use the transition type of GEOFENCE_TRANSITION_DWELL
							.setLoiteringDelay(30000)
							.setTransitionTypes(
									Geofence.GEOFENCE_TRANSITION_ENTER
											| Geofence.GEOFENCE_TRANSITION_DWELL
											| Geofence.GEOFENCE_TRANSITION_EXIT).build());

				}

				mGeofenceStore = new GeofenceStore(MapActivity2.this, mGeofences);

				//mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent());
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
		//new GetRuangan().execute();

		//mGeofenceStore = new GeofenceStore(MapActivity2.this, mGeofences);

		//mGeofencingClient = LocationServices.getGeofencingClient(this);


		LocationManager locationManager;
		LocationProvider locationProvider;

		/* Get LocationManager and LocationProvider for GPS */
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);

		/* Check if GPS LocationProvider supports altitude */
		locationProvider.supportsAltitude();

		if (gpsTool == null) {
			gpsTool = new GpsTool(this) {
				@Override
				public void onGpsLocationChanged(Location location) {
					super.onGpsLocationChanged(location);
					refreshLocation(location);
				}
			};
		}
		//refreshLocation(lastLocation);



	}

	private GeofencingRequest getGeofencingRequest() {
		GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

		// The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
		// GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
		// is already inside that geofence.
		builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

		// Add the geofences to be monitored by geofencing service.
		builder.addGeofences(mGeofences);

		// Return a GeofencingRequest.
		return builder.build();
	}

	private void setupMapIfNeeded() {
		if (map == null) {
			SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);
			mapFragment.getMapAsync(this);

			moveToMyLocation();

			if (map != null) {
				setupMap();
			}
		}
	}

	private void setupMap() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		map.setMyLocationEnabled(true);
		moveToMyLocation();
	}



	private void moveToMyLocation() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions\
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.

			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_FINE_LOCATION);

			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
					MY_PERMISSIONS_REQUEST_COARSE_LOCATION);


			return;
		}
		Location location = locationManager
				.getLastKnownLocation(locationManager.getBestProvider(criteria,
						false));
		//if (location != null) {
		//	map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
		//			location.getLatitude(), location.getLongitude()), 13));
		//}

		//new GetRuangan().execute();

		//map.getUiSettings().setCompassEnabled(true);

		//map.getUiSettings().setZoomControlsEnabled(true);

		/**myMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(final Marker arg0) {
				// TODO Auto-generated method stub

				final CharSequence[] dialogitem = { "Info Lokasi", "Rute Lokasi" };
				AlertDialog.Builder builder = new AlertDialog.Builder(
						WisataActivity.this);
				builder.setTitle("Pilih ?");
				builder.setItems(dialogitem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								switch (item) {
									case 0:
										try {
											Intent ii = new Intent(
													WisataActivity.this,KetWisActivity.class);

											int i;
											for (i = 0; i < daftarnama_wisata.length; i++) {
												if (arg0.getTitle().equals(daftarnama_wisata[i])) {
													ii.putExtra("id", daftarid[i]);
													ii.putExtra("nama_wisata",daftarnama_wisata[i]);
													ii.putExtra("info_wisata",daftarinfo_wisata[i]);

													break;
												}
											}
											startActivity(ii);

										} catch (Exception ee) {
											Toast.makeText(getApplicationContext(),"Tidak terkoneksi ke server",
													Toast.LENGTH_LONG).show();
										}
										break;
									case 1:
										try {
											StringBuilder urlString = new StringBuilder();
											String daddr = (String.valueOf(arg0.getPosition().latitude) + "," + String.valueOf(arg0.getPosition().longitude));
											urlString.append("http://maps.google.com/maps?f=d&hl=en");
											urlString.append("&saddr="+ String.valueOf(myMap.getMyLocation().getLatitude())
													+ ","
													+ String.valueOf(myMap.getMyLocation().getLongitude()));
											urlString.append("&daddr=" + daddr);
											Intent i = new Intent(
													Intent.ACTION_VIEW, Uri
													.parse(urlString
															.toString()));
											startActivity(i);
										} catch (Exception ee) {
											Toast.makeText(
													getApplicationContext(),
													"Lokasi Saat Ini Belum Didapatkan, Coba Nyalakan GPS, Keluar Ruangan dan Tunggu Beberapa Saat",
													Toast.LENGTH_LONG).show();
										}
										break;
								}
							}
						});
				builder.create().show();
			}
		});*/
	}

	private void retrieveFileFromResource(final GoogleMap map2) {
		try {
			KmlLayer kmlLayer = new KmlLayer(map2, R.raw.itv8, this.getApplicationContext());
			kmlLayer.addLayerToMap();
			//moveCameraToKml(kmlLayer);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}



	@Override
	public void onLocationChanged(Location location) {



		Log.d(TAG, "Ruangan" + location.getAltitude());



		refreshLocation(location);
		//Toast.makeText(MapActivity2.this,"Berada di lantai " + location.getAltitude(),Toast.LENGTH_LONG).show();
	}



	@Override
	public void onResult(@NonNull Status status) {

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
						MapActivity2.this);
				kmlLayer.addLayerToMap();
				kmlLayer.setOnFeatureClickListener(new KmlLayer.OnFeatureClickListener() {
					@Override
					public void onFeatureClick(Feature feature) {
						//Toast.makeText(MapFragment.this,
						//		"Feature clicked: " + feature.getId(),
						//		Toast.LENGTH_SHORT).show();
					}
				});
				//moveCameraToKml(kmlLayer);
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


	}



	private void retrieveFileFromUrl() {
		new DownloadKmlFile(getString(R.string.kml_url)).execute();
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


	@Override
	public void onCameraChange(CameraPosition cameraPosition) {

	}

	@Override
	public void onMapReady(final GoogleMap googleMap) {
		map = googleMap;
		//try {
			//this.map = map;
			map.setMyLocationEnabled(true);
			retrieveFileFromResource(map);

			//getLastKnownLocation();

			databaseItem = FirebaseDatabase.getInstance().getReference().child("ruangan");



			databaseItem.addValueEventListener(new ValueEventListener() {
				// public static final String TAG = "lstech.aos.debug";
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

						Ruangan ruangan = postSnapshot.getValue(Ruangan.class);
						String latitude = ruangan.getlatitude().toString();
						String longitude = ruangan.getlongitude().toString();

						String id = (ruangan.getid().toString());

						String rad1 = ruangan.getradius().toString();
						Log.d(TAG, "Ruangan" + ruangan);


						Ruangan ruangan2 = new Ruangan(id, latitude, longitude, rad1);


						rruangan.add(ruangan2);


					}

					markerLocation(map, rruangan);

					//map.addMarker(new MarkerOptions()
					//       .position(new LatLng(-2.216116410721533,113.8985477175574))
					//       .title("dd"));


				}

				@Override
				public void onCancelled(DatabaseError databaseError) {

				}
			});
			//startGeofence();
			//retrieveFileFromUrl();
		//} catch (Exception e) {
		//	Log.e("Exception caught", e.toString());
		//}
		buildGoogleApiClient();
	}

	private void markerLocation(final GoogleMap map1, ArrayList<Ruangan> rng) {
		Log.i(TAG, "markerLocation(" + rng + ")");



		ArrayList<Ruangan> rng1 = new ArrayList<Ruangan>(rng);

		Log.i(TAG, "Working Please(" + rng1 + ")");

		//ArrayList<Ruangan> ruang = new ArrayList<Ruangan>();
		// ArrayList<Ruangan> rng = new ArrayList<Ruangan>();
		for (Ruangan ruang : rng1) {
			String title = ruang.getid();
			Double latitude = Double.parseDouble(ruang.getlatitude().toString());
			Double longitude = Double.parseDouble(ruang.getlongitude().toString());
			//LatLng latLng = (latitude +',' + longitude);
			LatLng latLng = new LatLng(latitude, longitude);






			Log.i(TAG, latLng + "," + title);


			map1.addMarker(new MarkerOptions()
					.position(new LatLng(latitude, longitude))
					.title(title).draggable(true));
			//float zoom = 14f;
			//CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
			// map.animateCamera(cameraUpdate);
			// }

		}
	}

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(MapActivity2.this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
		mGoogleApiClient.connect();
	}

	private void createGoogleApi() {
		//Log.d(TAG, "createGoogleApi()");
		if ( mGoogleApiClient == null ) {
			mGoogleApiClient = new GoogleApiClient.Builder( this )
					.addConnectionCallbacks( this )
					.addOnConnectionFailedListener( this )
					.addApi( LocationServices.API )
					.build();
		}
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {

		Log.i(TAG, "onConnected()");
		//getLastKnownLocation();


	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetRuangan extends AsyncTask<Void, Void, Void> {

		DatabaseReference databaseItem;

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

			/**databaseItem = FirebaseDatabase.getInstance().getReference().child("ruangan");

			databaseItem.addValueEventListener(new ValueEventListener() {
				public static final String TAG = "lstech.aos.debug";
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

							//Ruangan ruangan = postSnapshot.getValue(Ruangan.class);

							//r1.add(ruangan);




							//Log.d(TAG, "" + ruangan);

					}

					//MainFragment.newInstance(items, position);



					//replaceFra
					//return f;

				}

				@Override
				public void onCancelled(DatabaseError databaseError) {

				}
			});*/

			return null;
		}



		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			//if (pDialog.isShowing())
			//	pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			//HttpHandler sh = new HttpHandler ();

			// Making a request to url and getting response
			//String jsonStr = sh.makeServiceCall(url);

			//Log.d("Response: ", "> " + jsonStr);





				try {


					/**InputStream is = new URL(url).openStream();

					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader reader = new BufferedReader(isr);
					StringBuilder builder = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line + "\n");
					}
					JSONObject jObject = new JSONObject(builder.toString());*/
					//SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
					//SharedPreferences.Editor editor = prefs.edit();

					//JSONObject jsonObj = new JSONObject(jsonStr);
					// Initializing variables
					mGeofences = new ArrayList<Geofence>();
					//mGeofenceCoordinates = new ArrayList<LatLng>();
					//mGeofenceRadius = new ArrayList<Integer>();

					double latitude = 0;
					double longitude = 0;
					//JSONObject jObject = new JSONObject(builder.toString());
					//JSONArray jDays1 = jObject.getJSONArray("ruangan");
					for (int i = 0; i < menuitemArray.length(); i++) {
						latitude = Double.parseDouble(menuitemArray.getJSONObject(i).getString("latitude").toString());
						longitude = Double.parseDouble(menuitemArray.getJSONObject(i).getString("longitude").toString());
						//fitness = new LatLng(latitude, longitude);
						//MarkerOptions markerFitness = new MarkerOptions();
						//markerFitness.position(fitness);
						//markerFitness.title(menuitemArray.getJSONObject(i).getString("nama_wisata").toString());
						//markerFitness.snippet(menuitemArray.getJSONObject(i).getString("info_wisata").toString());
						//rkerFitness.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
						//map.addMarker(markerFitness);

						//Instantiates a new CircleOptions object +  center/radius
						CircleOptions circleOptions = new CircleOptions()
								.center(new LatLng(latitude, longitude))
								.radius(10)
								.fillColor(0x40ff0000)
								.strokeColor(Color.TRANSPARENT)
								.strokeWidth(2);

						// Get back the mutable Circle
						Circle circle = map.addCircle(circleOptions);
						// more operations on the circle...

						String id = menuitemArray.getJSONObject(i).getString("id").toString();
						//daftarnama_wisata[i] = menuitemArray.getJSONObject(i).getString("nama_wisata").toString();
						//daftarinfo_wisata[i] = menuitemArray.getJSONObject(i).getString("info_wisata").toString();
						//String latt = jDays1.getJSONObject(i).getString("latitude").toString();
						//String longt = jDays1.getJSONObject(i).getString("longitude").toString();

						// Adding geofence coordinates to array.
						double lat1 = Double.parseDouble(menuitemArray.getJSONObject(i).getString("latitude").toString());
						double long1 = Double.parseDouble(menuitemArray.getJSONObject(i).getString("longitude").toString());
						int rad1 = Integer.parseInt(menuitemArray.getJSONObject(i).getString("radius").toString());
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
						mGeofenceStore = new GeofenceStore(MapActivity2.this, mGeofences);

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
			//}





			/**databaseItem = FirebaseDatabase.getInstance().getReference().child("ruangan");
			Double latitude,longitude;

			String id;
			int rad1;
			CircleOptions circleOptions;
			mGeofences = new ArrayList<Geofence>();

			databaseItem.addValueEventListener(new ValueEventListener() {
				public static final String TAG = "lstech.aos.debug";
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

						Ruangan ruangan = postSnapshot.getValue(Ruangan.class);



						Double latitude = Double.parseDouble(ruangan.getlatitude().toString());
						Double longitude = Double.parseDouble(ruangan.getlongitude().toString());

						String id = (ruangan.getid().toString());

						int rad1 = Integer.parseInt(ruangan.getradius());



						//mGeofenceRadius.add(Integer.valueOf(ruangan.getradius()));





						Log.d(TAG, "" + ruangan);

						CircleOptions circleOptions = new CircleOptions()
								.center( new LatLng(latitude, longitude) )
								.radius( 10 )
								.fillColor(0x40ff0000)
								.strokeColor(Color.TRANSPARENT)
								.strokeWidth(2);

						Circle circle = map.addCircle(circleOptions);

						//mGeofences.add(ruangan);

						mGeofences.add(new Geofence.Builder()
								.setRequestId(ruangan.getid())
								// The coordinates of the center of the geofence and the radius in meters.
								.setCircularRegion(Double.parseDouble(ruangan.getlatitude()), Double.parseDouble(ruangan.getlongitude()), Integer.parseInt(ruangan.getradius()))
								.setExpirationDuration(Geofence.NEVER_EXPIRE)
								// Required when we use the transition type of GEOFENCE_TRANSITION_DWELL
								.setLoiteringDelay(30000)
								.setTransitionTypes(
										Geofence.GEOFENCE_TRANSITION_ENTER
												| Geofence.GEOFENCE_TRANSITION_DWELL
												| Geofence.GEOFENCE_TRANSITION_EXIT).build());


						// Add the geofences to the GeofenceStore object.
						//mGeofenceStore = new GeofenceStore(MapFragment.this, mGeofences);
						mGeofenceStore = new GeofenceStore(MapActivity2.this, mGeofences);


					}

					mGeofenceStore = new GeofenceStore(MapActivity2.this, mGeofences);
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {

				}
			});*/





		}
	}

	private void refreshLocation(Location location) {

		Double altitude = location.getAltitude();

		if (altitude > 50.0) {
				Toast.makeText(MapActivity2.this, "Anda berada di lantai 2", Toast.LENGTH_SHORT).show();
		} else
		if (altitude < 50.0){
				Toast.makeText(MapActivity2.this, "Anda berada di lantai dasar", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected void onStart() {
		super.onStart();

		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		//mGeofenceStore.disconnect();
		super.onStop();

		mGoogleApiClient.disconnect();
	}

	@Override
	protected void onPause() {
		super.onPause();
		gpsTool.stopGpsUpdate();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
			setupMapIfNeeded();
		} else {
			GooglePlayServicesUtil.getErrorDialog(
					GooglePlayServicesUtil.isGooglePlayServicesAvailable(this),
					this, 0);
		}
		gpsTool.startGpsUpdate();
	}


	private PendingIntent getGeofencePendingIntent() {
		// Reuse the PendingIntent if we already have it.
		if (mGeofencePendingIntent != null) {
			return mGeofencePendingIntent;
		}
		Intent intent = new Intent(this, GeofenceTransitionsJobIntentService.class);
		// We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
		// addGeofences() and removeGeofences().
		mGeofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		return mGeofencePendingIntent;
	}

	private void writeActualLocation(Location location) {
		//textLat.setText( "Lat: " + location.getLatitude() );
		//textLong.setText( "Long: " + location.getLongitude() );
	}

	private void getLastKnownLocation() {
		Log.d(TAG, "getLastKnownLocation()");
		if ( checkPermission() ) {
			lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
			if ( lastLocation != null ) {
				Log.i(TAG, "LasKnown location. " +
						"Long: " + lastLocation.getLongitude() +
						" | Lat: " + lastLocation.getLatitude());
				writeLastLocation();
				startLocationUpdates();
			} else {
				Log.w(TAG, "No location retrieved yet");
				startLocationUpdates();
			}
		}
		else askPermission();
	}

	private void startLocationUpdates(){
		Log.i(TAG, "startLocationUpdates()");
		locationRequest = LocationRequest.create()
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
				.setInterval(UPDATE_INTERVAL)
				.setFastestInterval(FASTEST_INTERVAL);

		if ( checkPermission() )
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
	}

	private void writeLastLocation() {
		writeActualLocation(lastLocation);
	}

	// Check for permission to access Location
	private boolean checkPermission() {
		Log.d(TAG, "checkPermission()");
		// Ask for permission if it wasn't granted yet
		return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
				== PackageManager.PERMISSION_GRANTED );
	}

	// Asks for permission
	private void askPermission() {
		Log.d(TAG, "askPermission()");
		ActivityCompat.requestPermissions(
				this,
				new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
				REQ_PERMISSION
		);
	}

	// Verify user's response of the permission requested
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		//Log.d(TAG, "onRequestPermissionsResult()");
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch ( requestCode ) {
			case REQ_PERMISSION: {
				if ( grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED ){
					// Permission granted
					getLastKnownLocation();

				} else {
					// Permission denied
					permissionsDenied();
				}
				break;
			}
		}
	}

	// App cannot work without the permissions
	private void permissionsDenied() {
		Log.w(TAG, "permissionsDenied()");
	}



	private ArrayList<Geofence> mGeofence() {
		Log.d(TAG, "createGeofence");

		databaseItem.addValueEventListener(new ValueEventListener() {
			public static final String TAG = "lstech.aos.debug";
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

					Ruangan ruangan = postSnapshot.getValue(Ruangan.class);



					Double latitude = Double.parseDouble(ruangan.getlatitude().toString());
					Double longitude = Double.parseDouble(ruangan.getlongitude().toString());

					String id = (ruangan.getid().toString());

					int rad1 = Integer.parseInt(ruangan.getradius());





					Log.d(TAG, "" + ruangan);

					CircleOptions circleOptions = new CircleOptions()
							.center( new LatLng(latitude, longitude) )
							.radius( 10 )
							.fillColor(0x40ff0000)
							.strokeColor(Color.TRANSPARENT)
							.strokeWidth(2);

					Circle circle = map.addCircle(circleOptions);





					mGeofences.add(new Geofence.Builder()
							.setRequestId(ruangan.getid())
							// The coordinates of the center of the geofence and the radius in meters.
							.setCircularRegion(Double.parseDouble(ruangan.getlatitude()), Double.parseDouble(ruangan.getlongitude()), Integer.parseInt(ruangan.getradius()))
							.setExpirationDuration(Geofence.NEVER_EXPIRE)
							// Required when we use the transition type of GEOFENCE_TRANSITION_DWELL
							.setLoiteringDelay(30000)
							.setTransitionTypes(
									Geofence.GEOFENCE_TRANSITION_ENTER
											| Geofence.GEOFENCE_TRANSITION_DWELL
											| Geofence.GEOFENCE_TRANSITION_EXIT).build());



				}


				createGeofenceRequest(mGeofences);
				//createGeofence(mGeofences);




			}


			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});

		return mGeofences;
	}

	/*private Geofence createGeofence( String id, Double lat, Double longt, int radius ) {
		Log.d(TAG, "createGeofence");
		return new Geofence.Builder()
				.setRequestId(GEOFENCE_REQ_ID)
				.setCircularRegion( latLng.latitude, latLng.longitude, radius)
				.setExpirationDuration( GEO_DURATION )
				.setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER
						| Geofence.GEOFENCE_TRANSITION_EXIT )
				.build();
	}*/

	private void startGeofence() {
		Log.i(TAG, "startGeofence()");
		//if( geoFenceMarker != null ) {
			//Geofence geofence = createGeofence( geoFenceMarker.getPosition(), GEOFENCE_RADIUS );
			//mGeofence();
		databaseItem.addValueEventListener(new ValueEventListener() {
			public static final String TAG = "lstech.aos.debug";
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

					Ruangan ruangan = postSnapshot.getValue(Ruangan.class);



					Double latitude = Double.parseDouble(ruangan.getlatitude().toString());
					Double longitude = Double.parseDouble(ruangan.getlongitude().toString());

					String id = (ruangan.getid().toString());

					int rad1 = Integer.parseInt(ruangan.getradius());





					Log.d(TAG, "" + ruangan);

					CircleOptions circleOptions = new CircleOptions()
							.center( new LatLng(latitude, longitude) )
							.radius( 10 )
							.fillColor(0x40ff0000)
							.strokeColor(Color.TRANSPARENT)
							.strokeWidth(2);

					Circle circle = map.addCircle(circleOptions);





					mGeofences.add(new Geofence.Builder()
							.setRequestId(ruangan.getid())
							// The coordinates of the center of the geofence and the radius in meters.
							.setCircularRegion(Double.parseDouble(ruangan.getlatitude()), Double.parseDouble(ruangan.getlongitude()), Integer.parseInt(ruangan.getradius()))
							.setExpirationDuration(Geofence.NEVER_EXPIRE)
							// Required when we use the transition type of GEOFENCE_TRANSITION_DWELL
							.setLoiteringDelay(30000)
							.setTransitionTypes(
									Geofence.GEOFENCE_TRANSITION_ENTER
											| Geofence.GEOFENCE_TRANSITION_DWELL
											| Geofence.GEOFENCE_TRANSITION_EXIT).build());

					//mg


				}




				GeofencingRequest geofenceRequest = createGeofenceRequest(mGeofences);
				addGeofence( geofenceRequest );
				//createGeofence(mGeofences);




			}


			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});

			GeofencingRequest geofenceRequest = createGeofenceRequest( mGeofences );
			addGeofence( geofenceRequest );
		//} else {
			Log.e(TAG, "Geofence marker is null");
		//}
	}

	private GeofencingRequest createGeofenceRequest( ArrayList<Geofence> mGeofences) {
		Log.d(TAG, "createGeofenceRequest");
		return new GeofencingRequest.Builder()
				.setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER )
				.addGeofence((Geofence) mGeofences)
				.build();
	}

	private PendingIntent geoFencePendingIntent;
	private final int GEOFENCE_REQ_CODE = 0;
	private PendingIntent createGeofencePendingIntent() {
		Log.d(TAG, "createGeofencePendingIntent");
		if ( geoFencePendingIntent != null )
			return geoFencePendingIntent;

		Intent intent = new Intent( this, GeofenceTrasitionService.class);
		return PendingIntent.getService(
				this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
	}

	private void addGeofence(GeofencingRequest request) {
		Log.d(TAG, "addGeofence");
		if (checkPermission())
			LocationServices.GeofencingApi.addGeofences(
					mGoogleApiClient,
					request,
					createGeofencePendingIntent()
			).setResultCallback(this);
	}

	private Marker geoFenceMarker;
	// Create a marker for the geofence creation
	private void markerForGeofence(LatLng latLng) {
		Log.i(TAG, "markerForGeofence("+latLng+")");
		String title = latLng.latitude + ", " + latLng.longitude;
		// Define marker options
		MarkerOptions markerOptions = new MarkerOptions()
				.position(latLng)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
				.title(title);
		if ( map!=null ) {
			// Remove last geoFenceMarker
			if (geoFenceMarker != null)
				geoFenceMarker.remove();

			geoFenceMarker = map.addMarker(markerOptions);
		}
	}


	private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";
	public static Intent makeNotificationIntent(Context context, String msg) {
		Intent intent = new Intent( context, MapActivity2.class );
		intent.putExtra( NOTIFICATION_MSG, msg );
		return intent;
	}

}

