package com.example.hown.lbsftupr.db;

import com.google.android.gms.location.Geofence;

/**
 * Created by hown on 17-Oct-17.
 */

public class geolbs {

    private static final double CHECK_PERIMETER = 5.0;

    private static String url = "http://192.168.1.18/lbsuprtest/json.php";

    private final String ruangan;
    private final double latitude;
    private final double longitude;
    private final Integer radius;
    private long expirationDuration;
    private int transitionType;
    private int loiteringDelay = 60000;

    public geolbs(String geofenceId, double latitude, double longitude,
                          Integer radius) {
        this.ruangan = geofenceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
       // this.expirationDuration = expiration;
        //this.transitionType = transition;
    }

    public String getId() {
        return ruangan;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getRadius() {
        return radius;
    }

    public long getExpirationDuration() {
        return expirationDuration;
    }

    public int getTransitionType() {
        return transitionType;
    }

    public Geofence toGeofence() {
        Geofence g = new Geofence.Builder().setRequestId(getId())
                .setTransitionTypes(transitionType)
                .setCircularRegion(getLatitude(), getLongitude(), getRadius())
                .setExpirationDuration(expirationDuration)
                .setLoiteringDelay(loiteringDelay).build();
        return g;
    }
}


