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

import com.example.hown.lbsftupr.model.Ruangan;
import com.example.hown.lbsftupr.model.Lantai;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class LantaiGabung extends FragmentActivity implements OnCameraChangeListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {

    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 124;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 123;

    private static final String TAG = Lantai1.class.getSimpleName();


    private GpsTool gpsTool;

    private static final long GEO_DURATION = 10 * 10 * 1000;
    private LocationRequest locationRequest;
    private final int REQ_PERMISSION = 999;
    private final int UPDATE_INTERVAL =  1000;
    private final int FASTEST_INTERVAL = 900;


    GoogleApiClient mGoogleApiClient;
    protected SupportMapFragment mapFragment;
    protected GoogleMap map;
    protected Marker myPositionMarker;

    private Location lastLocation;




    ArrayList<Lantai> rruangan = new ArrayList<Lantai>();

    LatLng fitness;



    ArrayList<Geofence> mGeofences;





    private PendingIntent mGeofencePendingIntent;

    public ProgressDialog pDialog;
    public GeofenceStoreLantai2 mGeofenceStore;
    DatabaseReference databaseItem;

    private GeofencingClient mGeofencingClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lantai2);

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


                    if (location.getAltitude() < 60.0) {
                        databaseItem = FirebaseDatabase.getInstance().getReference().child("lantai");

                        Query query = databaseItem.orderByChild("altitude").equalTo("10");

                        mGeofences = new ArrayList<Geofence>();

                        query.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        Lantai ruangan = postSnapshot.getValue(Lantai.class);

                                        Double latitude = Double.parseDouble(ruangan.getlatitude().toString());
                                        Double longitude = Double.parseDouble(ruangan.getlongitude().toString());

                                        String id = (ruangan.getid().toString());

                                        Double altitude = Double.parseDouble(ruangan.getAltitude());

                                        int rad1 = Integer.parseInt(ruangan.getradius());

                                        Log.d(TAG, "Lantai 2 Create Geofence" + ruangan);


                                        //gpsTool.onGpsLocationChanged(lastLocation);

                                        //refreshLocation(lastLocation);


                                        Double altitude1 = 10.0;


                                        //if (altitude1 > 50.0) {

                                        // if  (altitude > 50.0) {
                                        CircleOptions circleOptions = new CircleOptions()
                                                .center(new LatLng(Double.parseDouble(ruangan.getlatitude()), Double.parseDouble(ruangan.getlongitude())))
                                                .radius(Integer.parseInt(ruangan.getradius()))
                                                .fillColor(0x40ff0000)
                                                .strokeColor(Color.TRANSPARENT)
                                                .strokeWidth(2);

                                        map.addCircle(circleOptions);

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
                                        // }
                                        // }

                                    }


                                    mGeofenceStore = new GeofenceStoreLantai2(LantaiGabung.this, mGeofences);
                                }


                                //mGeofenceStore = new GeofenceStoreLantai1(Lantai1.this, mGeofences);


                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else
                    if  (location.getAltitude() > 60.0){
                        databaseItem = FirebaseDatabase.getInstance().getReference().child("lantai");

                        Query query = databaseItem.orderByChild("altitude").equalTo("60");

                        mGeofences = new ArrayList<Geofence>();

                        query.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        Lantai ruangan = postSnapshot.getValue(Lantai.class);

                                        Double latitude = Double.parseDouble(ruangan.getlatitude().toString());
                                        Double longitude = Double.parseDouble(ruangan.getlongitude().toString());

                                        String id = (ruangan.getid().toString());

                                        Double altitude = Double.parseDouble(ruangan.getAltitude());

                                        int rad1 = Integer.parseInt(ruangan.getradius());

                                        Log.d(TAG, "Lantai 2 Create Geofence" + ruangan);


                                        //gpsTool.onGpsLocationChanged(lastLocation);

                                        //refreshLocation(lastLocation);


                                        Double altitude1 = 60.0;


                                        //if (altitude1 > 50.0) {

                                        // if  (altitude > 50.0) {
                                        CircleOptions circleOptions = new CircleOptions()
                                                .center(new LatLng(Double.parseDouble(ruangan.getlatitude()), Double.parseDouble(ruangan.getlongitude())))
                                                .radius(Integer.parseInt(ruangan.getradius()))
                                                .fillColor(0x40ff0000)
                                                .strokeColor(Color.TRANSPARENT)
                                                .strokeWidth(2);

                                        map.addCircle(circleOptions);

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
                                        // }
                                        // }

                                    }


                                    mGeofenceStore = new GeofenceStoreLantai2(LantaiGabung.this, mGeofences);
                                }


                                //mGeofenceStore = new GeofenceStoreLantai2(Lantai2.this, mGeofences);


                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            };
        }






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



        Log.d(TAG, "Ketinggian" + location.getAltitude());



        refreshLocation(location);
        //Toast.makeText(Lantai1.this,"Berada di lantai " + location.getAltitude(),Toast.LENGTH_LONG).show();
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
                        LantaiGabung.this);
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

        //refreshLocation(location);
        if (gpsTool != null) {
            gpsTool = new GpsTool(this) {
                @Override
                public void onGpsLocationChanged(Location location) {
                    super.onGpsLocationChanged(location);
                    refreshLocation(location);


                    if (location.getAltitude() < 60.0) {


                        databaseItem = FirebaseDatabase.getInstance().getReference().child("lantai");

                        Query query = databaseItem.orderByChild("altitude").equalTo("10");


                        query.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                        Lantai ruangan = postSnapshot.getValue(Lantai.class);
                                        String latitude = ruangan.getlatitude().toString();
                                        String longitude = ruangan.getlongitude().toString();

                                        String id = (ruangan.getid().toString());

                                        String rad1 = ruangan.getradius().toString();

                                        String altitude = ruangan.getAltitude();
                                        Log.d(TAG, "Marker Lantai" + ruangan);


                                        Lantai ruangan2 = new Lantai(id, latitude, longitude, rad1, altitude);


                                        rruangan.add(ruangan2);


                                    }
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
                    } else
                    if (location.getAltitude() > 60.0) {
                        databaseItem = FirebaseDatabase.getInstance().getReference().child("lantai");

                        Query query = databaseItem.orderByChild("altitude").equalTo("60");



                        query.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                        Lantai ruangan = postSnapshot.getValue(Lantai.class);
                                        String latitude = ruangan.getlatitude().toString();
                                        String longitude = ruangan.getlongitude().toString();

                                        String id = (ruangan.getid().toString());

                                        String rad1 = ruangan.getradius().toString();

                                        String  altitude = ruangan.getAltitude();
                                        Log.d(TAG, "Marker Lantai" + ruangan);


                                        Lantai ruangan2 = new Lantai(id, latitude, longitude, rad1, altitude);


                                        rruangan.add(ruangan2);


                                    }
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
                    }

                    //startGeofence();
                    //retrieveFileFromUrl();
                    //} catch (Exception e) {
                    //	Log.e("Exception caught", e.toString());
                    //}
                    buildGoogleApiClient();

                }
            };
        }
        buildGoogleApiClient();
    }

    private void markerLocation(final GoogleMap map1, ArrayList<Lantai> rng) {
        Log.i(TAG, "marker Lantai Location(" + rng + ")");



        ArrayList<Lantai> rng1 = new ArrayList<Lantai>(rng);

        Log.i(TAG, "Temporary Lantai Marker(" + rng1 + ")");

        //ArrayList<Ruangan> ruang = new ArrayList<Ruangan>();
        // ArrayList<Ruangan> rng = new ArrayList<Ruangan>();
        for (Lantai ruang : rng1) {
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
                .addConnectionCallbacks(LantaiGabung.this)
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

    private void refreshLocation(Location location) {

        Double altitude = location.getAltitude();

        if (altitude > 50.0) {
            Toast.makeText(LantaiGabung.this, "Anda berada di lantai 2", Toast.LENGTH_SHORT).show();
        } else
        if (altitude < 50.0){
            Toast.makeText(LantaiGabung.this, "Anda berada di lantai dasar", Toast.LENGTH_SHORT).show();
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


    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";
    public static Intent makeNotificationIntent(Context context, String msg) {
        Intent intent = new Intent( context, Lantai1.class );
        intent.putExtra( NOTIFICATION_MSG, msg );
        return intent;
    }

}

