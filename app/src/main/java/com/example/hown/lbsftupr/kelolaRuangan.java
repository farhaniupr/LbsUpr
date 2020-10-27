package com.example.hown.lbsftupr;

import android.content.Intent;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hown.lbsftupr.model.Ruangan;
import com.example.mylibrary.data.Feature;
import com.example.mylibrary.data.kml.KmlLayer;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class kelolaRuangan extends FragmentActivity implements  OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

    private static final String TAG = kelolaRuangan.class.getSimpleName();
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
    protected  GoogleMap mapMaster;
    private EditText e1, e2, e3;
    private Spinner s1;
    private Button b1, b2, b3;

    ArrayList<Ruangan> rruangan = new ArrayList<Ruangan>();
    DatabaseReference databaseItem;
    private boolean needsInit=false;
    public String at;
    private RadioButton r1, r2, r3;
    String  keynext;

    LatLng lastMarkerChange;

    Marker markerR;
    Query queryMap;

    private ValueEventListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ruangan);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (savedInstanceState == null) {
            needsInit=true;
        }
        mapFragment.setRetainInstance(true);
        mapFragment.getMapAsync(this);
        e1 = (EditText) findViewById(R.id.edtNamaRuanga);
        e2 = (EditText) findViewById(R.id.edtradius);
        //s1 = (Spinner) findViewById(R.id.spin1);
        r1 = (RadioButton) findViewById(R.id.radioButton);  //ubah
        r2 = (RadioButton) findViewById(R.id.radioButton1); // tambah
        r3 = (RadioButton) findViewById(R.id.radioButton2); //hapus

        e2.setText("10");

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setEnabled(false);
                r2.setChecked(false);
                r3.setChecked(false);
                e2.setEnabled(true);

                Toast.makeText(kelolaRuangan.this, "Klik, Tahan, Drag Marker Titik",
                        Toast.LENGTH_SHORT).show();
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setEnabled(true);
                e2.setEnabled(false);
                r1.setChecked(false);
                r3.setChecked(false);

                Toast.makeText(kelolaRuangan.this, "Klik Map",
                        Toast.LENGTH_SHORT).show();

            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setEnabled(false);
                r2.setChecked(false);
                r1.setChecked(false);
                e2.setEnabled(false);

                Toast.makeText(kelolaRuangan.this, "Klik Marker Titik",
                        Toast.LENGTH_SHORT).show();
            }
        });

        b2 = (Button) findViewById(R.id.bedit);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(kelolaRuangan.this, editinforuangan.class);
                intent.putExtra("jadwal", keynext);
                Log.d(TAG, "Isi Edit Nama Ruangan" + keynext);
                startActivity(intent);
            }
        });

        b3 = (Button) findViewById(R.id.buttonHapus);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = e1.getText().toString();
                //int pos = s1.getSelectedItemPosition();
                //final String aket = s1.getItemAtPosition(pos).toString();
                //final String ra1 = e2.getText().toString();

                final Marker addMarker;
                //addMarker.

                if (r3.isChecked()) //hapus
                {
                    hapus(title);
                    //Intent intent = getIntent();
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    //recreate();
                    //finish();
                    //startActivity(intent);
                    markerR.remove();
                    queryMap.removeEventListener(mListener);
                }
            }
        });

        Intent i = getIntent();
        Bundle b = i.getExtras();
        at = (String) b.get("altitude");
        b2.setEnabled(false);
        b3.setEnabled(false);

        e2.setEnabled(false);


        ArrayAdapter<String> adapter;
        List<String> list;

        list = new ArrayList<String>();
        list.add("L1");
        list.add("L2");

        /**adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);*/


    }

    @Override
    public void onMapReady(final GoogleMap map) {
        if (needsInit) {
            map.clear();
            mapMaster = map;

        map.setMyLocationEnabled(true);
        map.setOnMarkerClickListener(this);

            /**databaseItem = FirebaseDatabase.getInstance().getReference().child("ruangan");
            final Query query = databaseItem.orderByChild("altitude").equalTo(at);
            query.addValueEventListener(new ValueEventListener() {
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
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/

            //if (at.equals("10")) {
                retrieveFileFromResource(map);
            // } else
            //if (at.equals("60")) {
                //retrieveFileFromResource1(map);
            //}

            addValueEventListener();

        map.setOnMapClickListener(this);
        map.setOnMarkerDragListener(this);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(-2.216207, 113.89811)).zoom(18).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private void markerLocation(final GoogleMap map1, ArrayList<Ruangan> rng) {
        mapMaster = map1;
        Log.i(TAG, "markerLocation(" + rng + ")");

        ArrayList<Ruangan> rng1 = new ArrayList<Ruangan>(rng);
        //rng1.clear();
        //Log.i(TAG, "Working Please(" + rng1 + ")");
        //if (rng.isEmpty()) {


        if (markerR !=null) {
            markerR.remove();
        }
        //map1.clear();
            for (Ruangan ruang : rng) {
                String title = ruang.getid();
                Double latitude = Double.parseDouble(ruang.getlatitude().toString());
                Double longitude = Double.parseDouble(ruang.getlongitude().toString());
                //LatLng latLng = (latitude +',' + longitude);
                LatLng latLng = new LatLng(latitude, longitude);
                Log.i(TAG, latLng + "," + title);
                //MarkerOptions m =
                markerR = map1.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title(title).draggable(true));

            }
    }

    private void retrieveFileFromResource(final GoogleMap map2) {
        try {
            KmlLayer kmlLayer = new KmlLayer(map2, R.raw.tu2, this.getApplicationContext());
            kmlLayer.addLayerToMap();
            //moveCameraToKml(kmlLayer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private void retrieveFileFromResource1(final GoogleMap map2) {
        try {
            KmlLayer kmlLayer = new KmlLayer(map2, R.raw.ti10l2, this.getApplicationContext());
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

        final Double lat = latLng.latitude;
        final Double lng = latLng.longitude;
        final String title = e1.getText().toString();
        //int pos = s1.getSelectedItemPosition();

        final int[] pos = new int[1];

        //pos[0] = s1.getSelectedItemPosition();
        //final String ket = s1.getItemAtPosition(pos[0]).toString();
        final String ra1 = e2.getText().toString();

        if (e1.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Isi ID ruangan", Toast.LENGTH_LONG).show();
        } else
        if (e2.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Isi Radius", Toast.LENGTH_LONG).show();
        } else
        {
            DatabaseReference dCek =    FirebaseDatabase.getInstance().getReference().child("ruangan");
            dCek.orderByChild("id").equalTo(title).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Toast.makeText(kelolaRuangan.this, "ID Ruangan sudah ada", Toast.LENGTH_LONG).show();
                    } else
                    {
                        tambah(title, Double.toString(lat), Double.toString(lng), ra1);
                        Intent intent = getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        //databaseItem = FirebaseDatabase.getInstance().getReference().child("ruangan").child(marker.getTitle());

        databaseItem = FirebaseDatabase.getInstance().getReference().child("ruangan");
        final Query query = databaseItem.orderByChild("id").equalTo(marker.getTitle());

        markerR = marker;
        final String[] key = new String[1];

       query.addListenerForSingleValueEvent(new ValueEventListener() {
            // public static final String TAG = "lstech.aos.debug";
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    key[0] = postSnapshot.getKey();


                    keynext = key[0];

                    Log.d("Key Marker", key[0]);
                }

                //DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("ruangan").child(key[0]);

                //Query q = db1.orderByChild("id").equalTo(key[0]);

                DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("ruangan").child(key[0]);

                db1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Ruangan ruangan = dataSnapshot.getValue(Ruangan.class);
                        String latitude = ruangan.getlatitude().toString();
                        String longitude = ruangan.getlongitude().toString();

                        String id = (ruangan.getid().toString());

                        String rad1 = ruangan.getradius().toString();
                        Log.d(TAG, "Ruangan" + ruangan);


                        e1.setText(marker.getTitle());

                        e2.setText(rad1);


                        ///s1.setPrompt("Lantai 1");

                        ArrayAdapter<String> adapter;
                        List<String> list;

                        list = new ArrayList<String>();
                        list.add("L1");
                        //list.add("L2");

                        /**adapter = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_spinner_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        s1.setAdapter(adapter);*/


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //b1.setEnabled(true);
        b2.setEnabled(true);
        b3.setEnabled(true);


        return true;
    }

    public class DownloadKmlFile extends AsyncTask<String, Void, byte[]> {
        private final String mUrl;
        final GoogleMap map1;


        public DownloadKmlFile(String url, final GoogleMap map) {
            mUrl = url;
            map1 = map;
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
                KmlLayer kmlLayer = new KmlLayer(map1, new ByteArrayInputStream(byteArr),
                        kelolaRuangan.this);
                kmlLayer.addLayerToMap();
                kmlLayer.setOnFeatureClickListener(new KmlLayer.OnFeatureClickListener() {
                    @Override
                    public void onFeatureClick(Feature feature) {

                    }
                });

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void retrieveFileFromUrl() {
        if (at.equals("10")) {

            new DownloadKmlFile("https://firebasestorage.googleapis.com/v0/b/lbs-teknik-upr.appspot.com/o/itv9.kml?alt=media&token=e2b7f181-9ef1-4794-9359-ccc72c1f5bb5", mapMaster).execute();

        } else
        if (at.equals("60")) {

            new DownloadKmlFile("https://firebasestorage.googleapis.com/v0/b/lbs-teknik-upr.appspot.com/o/it9l2.kml?alt=media&token=33be368f-19c7-422f-88ee-2e42308ce484", mapMaster).execute();
        }
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

        String id = marker.getTitle();
        String lat = Double.toString(position.latitude);
        String longt = Double.toString(position.longitude);

        final int[] pos = new int[1];

       // pos[0] = s1.getSelectedItemPosition();
        final String title = e1.getText().toString();
        final String ket = "10";
        //int pos = s1.getSelectedItemPosition();
        //final String ket = s1.getItemAtPosition(pos[0]).toString();
        final String ra1 = e2.getText().toString();


        Log.d(getClass().getSimpleName(), String.format("Dragged to %f:%f",
                position.latitude,
                position.longitude));


        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());


        if (e1.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Isi ID ruangan", Toast.LENGTH_LONG).show();
        } else
        if (e2.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Isi Radius", Toast.LENGTH_LONG).show();
        } else
        {
            writeNewPost(id, lat, longt, ket, ra1);
            //recreate();

            lastMarkerChange = (new LatLng(Double.parseDouble(lat), Double.parseDouble(longt)));
            queryMap.removeEventListener(mListener);
            //addValueEventListener();


            Toast.makeText(kelolaRuangan.this, "Titik Ruangan berhasil diubah",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        //tvLocInfo.setText("Marker " + marker.getId() + " DragStart");
        LatLng position=marker.getPosition();

        Log.d(getClass().getSimpleName(), String.format("Drag from %f:%f",
                position.latitude,
                position.longitude));
        Toast.makeText(kelolaRuangan.this,String.format("Drag dari %f:%f",
                position.latitude,
                position.longitude),Toast.LENGTH_LONG).show();

        //if (lastMarkerChange != null){
         //   marker.setPosition(lastMarkerChange);
        //}
        //marker.setPosition();


        //marker.remove();


    }

    private void addValueEventListener() {
        //getting the specified artist reference

        if(mapMaster == null) return;

        databaseItem = FirebaseDatabase.getInstance().getReference().child("ruangan");
        queryMap = databaseItem.orderByChild("altitude").equalTo(at);


        mListener = queryMap.addValueEventListener(new ValueEventListener() {
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

                markerLocation(mapMaster, rruangan);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void writeNewPost(final String id, final String latitude, final String longitude, final String alt1, final String rad1) {

        final String[] lantai = new String[1];

        final String[] key = new String[1];

        final DatabaseReference dR1 = FirebaseDatabase.getInstance().getReference().child("ruangan");
       // DatabaseReference dR2 = dR1.child(id);
        /**Map<String, Object> ruangan = new HashMap<>();
        ruangan.put("latitude",latitude);
        ruangan.put("longitude",longitude);*/

        //if (alt1.equals("L1")){
            lantai[0] = "10";

            dR1.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        key[0] = childSnapshot.getKey();
                        Log.d("Key", key[0]);
                    }

                    DatabaseReference dR2 = dR1.child(key[0]);
                    Map<String, Object> ruangan = new HashMap<>();
                    ruangan.put("id",id);
                    ruangan.put("altitude",lantai[0]);
                    ruangan.put("latitude",latitude);
                    ruangan.put("longitude",longitude);
                    ruangan.put("radius",rad1);
                    dR2.updateChildren(ruangan);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        //}else
        /*if (alt1.equals("L2"))
        {
            lantai[0] = "60";
            /*Map<String, Object> ruangan = new HashMap<>();
            ruangan.put("id",id);
            ruangan.put("altitude",lantai[0]);
            ruangan.put("latitude",latitude);
            ruangan.put("longitude",longitude);
            ruangan.put("radius",rad1);
            //dR2.updateChildren(ruangan);*/

            dR1.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        key[0] = childSnapshot.getKey();
                        Log.d("Key", key[0]);
                    }

                    DatabaseReference dR2 = dR1.child(key[0]);
                    Map<String, Object> ruangan = new HashMap<>();
                    ruangan.put("id",id);
                    ruangan.put("altitude",lantai[0]);
                    ruangan.put("latitude",latitude);
                    ruangan.put("longitude",longitude);
                    ruangan.put("radius",rad1);
                    dR2.updateChildren(ruangan);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


       // }*/
        //dR2.updateChildren(ruangan);
    }

    private void ubah(final String id, final String alt1) {

        Log.d(TAG, "Lantai Berapa : "+alt1);

        final DatabaseReference dR1 = FirebaseDatabase.getInstance().getReference().child("ruangan");

        DatabaseReference dR2 = dR1.child(id);
        final String[] lantai = new String[1];
        final String[] key = new String[1];

        if (alt1.equals("L1")){
            lantai[0] = "10";

            Map<String, Object> ruangan = new HashMap<>();
            //ruangan.put("id",id);
            ruangan.put("altitude",lantai[0]);

            //Ruangan ruangan = new Ruangan(id, latitude, longitude, rad1);
            dR2.updateChildren(ruangan);
        }else
        if (alt1.equals("L2"))
        {
            lantai[0] = "60";

            Map<String, Object> ruangan = new HashMap<>();
            //ruangan.put("id",id);
            ruangan.put("altitude",lantai[0]);
            //Ruangan ruangan = new Ruangan(id, latitude, longitude, rad1);


            dR2.updateChildren(ruangan);
        }


    }

    private void tambah(final String id,  final String lat1, final String lng2, final String radi) {

       // Log.d(TAG, " : "+alt1);

        final DatabaseReference dR1 = FirebaseDatabase.getInstance().getReference().child("ruangan");

        String IdR = dR1.push().getKey();

        final DatabaseReference dR2 = dR1.child(IdR);
        final String[] lantai = new String[1];

        //if (alt1.equals("L1")){
            lantai[0] = "10";
            Map<String, Object> ruangan = new HashMap<>();
            ruangan.put("id",id);
            ruangan.put("altitude",lantai[0]);
            ruangan.put("radius",radi);
            ruangan.put("latitude", lat1);
            ruangan.put("longitude", lng2);
            dR2.setValue(ruangan);
        //}else
        /*if (alt1.equals("L2"))
        {
            lantai[0] = "60";
            Map<String, Object> ruangan = new HashMap<>();
            ruangan.put("id",id);
            ruangan.put("altitude",lantai[0]);
            ruangan.put("radius",radi);
            ruangan.put("latitude", lat1);
            ruangan.put("longitude", lng2);
            dR2.setValue(ruangan);
        }*/


    }

    private void hapus(final String id) {

        final String[] key = new String[1];
        final DatabaseReference dR1 = FirebaseDatabase.getInstance().getReference().child("ruangan");

        dR1.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    key[0] = childSnapshot.getKey();
                    Log.d("Key", key[0]);
                }

                DatabaseReference dR2 = dR1.child(key[0]);
                dR2.removeValue();
                Toast.makeText(kelolaRuangan.this, "Berhasil Dihapus", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        if(mapMaster != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            mapMaster.clear();

            addValueEventListener();
            // add markers from database to the map
        }
    }

}



