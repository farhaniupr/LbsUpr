package com.example.hown.lbsftupr.model;

/**
 * Created by hown on 18-Jan-18.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.location.Geofence;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


public class Lantai implements Parcelable, Geofence {

    String id;
    String latitude;
    String longitude;
    String radius;
    String altitude;



    public Lantai(){

    }

    public Lantai(String id, String latitude, String longitude, String radius, String altitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.radius = radius;
        this.altitude = altitude;
        //this.dosen_koordinator = dosen_koordinator;
    }

    protected Lantai(Parcel in) {
        latitude = in.readString();
        longitude = in.readString();
        id = in.readString();
        radius = in.readString();
        altitude = in.readString();
        //dosen_koordinator = in.readString();
    }

    public static final Creator<Lantai> CREATOR = new Creator<Lantai>() {
        @Override
        public Lantai createFromParcel(Parcel in) {
            return new Lantai(in);
        }

        @Override
        public Lantai[] newArray(int size) {
            return new Lantai[size];
        }
    };

    public String getlatitude() {
        return latitude;
    }

    public String getlongitude() {
        return longitude;
    }

    public String getid() {
        return id;
    }

    public String getradius() {
        return radius;
    }

    public String getAltitude() { return altitude; }

    // public String getdosen_koordinator() {
    //    return dosen_koordinator;
    //}

    @Override
    public String toString() {
        return longitude + "\n" + id + "\n" + latitude + "\n" + radius;
        //return latitude + "\n" + longitude + "\n" + id + "\n" + radius + "\n" + dosen_koordinator ;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(id);
        dest.writeString(radius);
        dest.writeString(latitude);
        //dest.writeString(dosen_koordinator);

    }


    @Override
    public String getRequestId() {
        return id;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("radius", radius);
        result.put("altitude", altitude);


        return result;
    }
}