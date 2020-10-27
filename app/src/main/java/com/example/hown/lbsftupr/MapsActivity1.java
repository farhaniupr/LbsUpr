package com.example.hown.lbsftupr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.hown.lbsftupr.model.MapStateManager;
import com.example.hown.lbsftupr.model.MineField;
import com.example.hown.lbsftupr.model.MineFieldTable;
import com.example.hown.lbsftupr.services.LocationTrackerService;
import com.example.mylibrary.data.Feature;
import com.example.mylibrary.data.kml.KmlContainer;
import com.example.mylibrary.data.kml.KmlLayer;
import com.example.mylibrary.data.kml.KmlPlacemark;
import com.example.mylibrary.data.kml.KmlPolygon;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity1 extends FragmentActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, GoogleMap.OnGroundOverlayClickListener {

    /**private static final String TAG = "Lbs Upr Example";
    private static final String LOCATION_CHANGE_FILTER = "UserLocationChange";
    private static final String USER_LOCATION_TEXT = "My location";*/


    private GoogleMap map;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLocation;
    //private LocationReceiver receiver;

    LatLng latLng;
    //GoogleMap mGoogleMap;
    Marker currLocationMarker;
    private Circle markerCircle;

    //private GestureDetector gestureDetector;

    //private List<MineField> mineFields;

    private boolean cameraMoved;
    private boolean moveRequested;

    private Marker userPositionMarker;


    private static final int TRANSPARENCY_MAX = 100;

    //private static final LatLng NEWARK = new LatLng(-2.216014, 113.898592);

    //private static final LatLng NEAR_NEWARK =
    //new LatLng(NEWARK.latitude - 0.001, NEWARK.longitude - 0.025);

    private final List<BitmapDescriptor> mImages = new ArrayList<BitmapDescriptor>();

    private GroundOverlay mGroundOverlay;

    private GroundOverlay mGroundOverlayRotated;

    private SeekBar mTransparencyBar;

    private int mCurrentEntry = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps1);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /**cameraMoved = false;
        mineFields = MineFieldTable.getInstance().getMineFields();
        receiver = new LocationReceiver();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(receiver, new IntentFilter(LOCATION_CHANGE_FILTER));


        if (!LocationTrackerService.isRunning()) {
            Log.i(TAG, "MapsActivity1: starting LocationTrackerService");
            startService(new Intent(this, LocationTrackerService.class));
        }*/


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
       /* map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setMyLocationEnabled(true);
        map.setTrafficEnabled(true);
       map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);**/
        /**try {
            //this.map = map;
           // map.setMyLocationEnabled(true);
            //retrieveFileFromResource();
            //retrieveFileFromUrl();
        } catch (Exception e) {
            Log.e("Exception caught", e.toString());
        }*/

        buildGoogleApiClient();

        /**MapStateManager mgr = new MapStateManager(this);
        CameraPosition position = mgr.getSavedCameraPosition();
        if (position != null) {
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            googleMap.moveCamera(update);
            googleMap.setMapType(mgr.getSavedMapType());
        }
        displayMineFields();*/

    }


    /**private void retrieveFileFromResource() {
        try {
            KmlLayer kmlLayer = new KmlLayer(map, R.raw.ituprv4, getApplicationContext());
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
                        getApplicationContext());
                kmlLayer.addLayerToMap();
                kmlLayer.setOnFeatureClickListener(new KmlLayer.OnFeatureClickListener() {
                    @Override
                    public void onFeatureClick(Feature feature) {
                        Toast.makeText(MapsActivity1.this,
                                "Feature clicked: " + feature.getId(),
                                Toast.LENGTH_SHORT).show();
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
    }*/

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter  */
       LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

       /** cameraMoved = false;
        mineFields = MineFieldTable.getInstance().getMineFields();
        receiver = new LocationReceiver();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(receiver, new IntentFilter(LOCATION_CHANGE_FILTER));

        // TODO start service to run in the foreground
        // TODO Make static instance of LocationTrackerService for access from SettingsActivity?
        if (!LocationTrackerService.isRunning()) {
            Log.i(TAG, "MainActivity: starting LocationTrackerService");
            startService(new Intent(this, LocationTrackerService.class));
        }

        MapStateManager mgr = new MapStateManager(this);
        CameraPosition position = mgr.getSavedCameraPosition();
        if (position != null) {
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            map.moveCamera(update);
            map.setMapType(mgr.getSavedMapType());
        }

        displayMineFields();

        for (MineField mineField : mineFields) {

            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(mineField.getLatitude(), mineField.getLongitude()))
                    .radius(mineField.getRadius()).strokeColor(Color.RED)
                    .strokeWidth(2).fillColor(Color.rgb(226, 203, 29));
            map.addCircle(circleOptions);
        }*/

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        //place marker at current position
        //mGoogleMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }



    }

/** private class LocationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "MineMapFragment: New location received !");

            Bundle bundle = intent.getExtras();
            double lastLatitude = bundle.getDouble("latitude");
            double lastLongitude = bundle.getDouble("longitude");
            latLng = new LatLng(lastLatitude, lastLongitude);
            updateMarker(latLng);
        }
    }

    private void updateMarker(LatLng latLng) {

        if (userPositionMarker == null) {
            createMarker(latLng, USER_LOCATION_TEXT);
        }
        userPositionMarker.setPosition(latLng);

        if (!cameraMoved || moveRequested) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            map.animateCamera(cameraUpdate);
            cameraMoved = true;
            moveRequested = false;
        }
    }

    private void createMarker(LatLng latLng, String title) {
        userPositionMarker = map.addMarker(new MarkerOptions().position(latLng));
        userPositionMarker.setIcon((BitmapDescriptorFactory.fromResource(R.drawable.ic_location_3v1)));
        userPositionMarker.setTitle(title);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MapStateManager mgr = new MapStateManager(this);
        mgr.saveMapState(map);
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraMoved = false;
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(receiver, new IntentFilter(LOCATION_CHANGE_FILTER));
        setupMapIfNeeded();

    }

    private void setupMapIfNeeded() {
        if (map == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    public void displayMineFields() {

        for (MineField mineField : mineFields) {

            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(mineField.getLatitude(), mineField.getLongitude()))
                    .radius(mineField.getRadius()).strokeColor(Color.RED)
                    .strokeWidth(2).fillColor(Color.rgb(226, 203, 29));
            map.addCircle(circleOptions);
        }
    }*/


    @Override
    public void onGroundOverlayClick(GroundOverlay groundOverlay) {
        mGroundOverlayRotated.setTransparency(0.5f - mGroundOverlayRotated.getTransparency());
    }



}
