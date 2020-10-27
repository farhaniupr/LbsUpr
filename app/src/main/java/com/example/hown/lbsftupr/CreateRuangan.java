package com.example.hown.lbsftupr;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hown.lbsftupr.model.Ruangan;
import com.example.mylibrary.data.Feature;
import com.example.mylibrary.data.kml.KmlContainer;
import com.example.mylibrary.data.kml.KmlLayer;
import com.example.mylibrary.data.kml.KmlPlacemark;
import com.example.mylibrary.data.kml.KmlPolygon;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CreateRuangan extends FragmentActivity implements  OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {


    private static final String TAG = CreateRuangan.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 124;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 123;
    //private static final String TAG = MainActivity.class.getSimpleName();
    private static final long GEO_DURATION = 60 * 60 * 1000;
    private LocationRequest locationRequest;
    private final int REQ_PERMISSION = 999;
    private final int UPDATE_INTERVAL =  1000;
    private final int FASTEST_INTERVAL = 900;

    GoogleApiClient mGoogleApiClient;
    protected SupportMapFragment mapFragment;

    protected GoogleMap map;
    protected Marker locationMarker;

    private Location lastLocation;
    LatLng fitness;

    ArrayList<Geofence> mGeofences;
    ArrayList<Ruangan> rruangan = new ArrayList<Ruangan>();

    ArrayList<Ruangan> rng1 = new ArrayList<Ruangan>();



    public ProgressDialog pDialog;
    public GeofenceStore mGeofenceStore;
    DatabaseReference databaseItem;

    TextView tvLocInfo;
    boolean markerClicked;
    PolygonOptions polygonOptions;
    Polygon polygon;

    private GeofencingClient mGeofencingClient;
    private boolean needsInit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ruangan);

        tvLocInfo = (TextView)findViewById(R.id.locinfo);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (savedInstanceState == null) {
            needsInit=true;
        }

        mapFragment.setRetainInstance(true);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap map) {



        map.setMyLocationEnabled(true);




            // map.setMyLocationEnabled(true);


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



        retrieveFileFromResource(map);

        map.setOnMapClickListener(this);
        map.setOnMapLongClickListener(this);
        map.setOnMarkerDragListener(this);

        markerClicked = false;

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(-2.216207, 113.89811)).zoom(8).build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

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
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
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
                        CreateRuangan.this);
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
        new CreateRuangan.DownloadKmlFile(getString(R.string.kml_url)).execute();
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
    public void onMapLongClick(LatLng point) {
    final GoogleMap map2 = map;

        tvLocInfo.setText("New marker added@" + point.toString());
        map2.addMarker(new MarkerOptions()
                .position(point)
                .draggable(true));

        markerClicked = false;
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        LatLng position=marker.getPosition();

        Log.d(getClass().getSimpleName(),
                String.format("Dragging to %f:%f", position.latitude,
                        position.longitude));


        //tvLocInfo.setText("Marker " + marker.getId() + " Drag@" + marker.getPosition());
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //tvLocInfo.setText("Marker " + marker.getId() + " DragEnd");
        LatLng position=marker.getPosition();

        Log.d(getClass().getSimpleName(), String.format("Dragged to %f:%f",
                position.latitude,
                position.longitude));


        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());


       /** List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();

            Toast.makeText(CreateRuangan.this,"Address: "+
                    address + " " +city,Toast.LENGTH_LONG).show();

            //addMarker();

        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        //tvLocInfo.setText("Marker " + marker.getId() + " DragStart");
        LatLng position=marker.getPosition();

        Log.d(getClass().getSimpleName(), String.format("Drag from %f:%f",
                position.latitude,
                position.longitude));

        Toast.makeText(CreateRuangan.this,String.format("Drag from %f:%f",
                position.latitude,
                position.longitude),Toast.LENGTH_LONG).show();


    }



}
