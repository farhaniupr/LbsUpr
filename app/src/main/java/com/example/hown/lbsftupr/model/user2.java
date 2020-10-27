package com.example.hown.lbsftupr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.hown.lbsftupr.annotation.CSVAnnotation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hown on 05-Mar-18.
 */

public class user2 implements Parcelable {
    private String nama;
    private String password;
    private String nim;



    public user2() {

    }

    public user2(String nama, String password, String nim) {
        super();
        this.nama = nama;
        this.password = password;
        this.nim = nim;

    }

    public String getNama() {
        return nama;
    }

    public String getPassword() {
        return password;
    }

    public String getNim() {
        return nim;
    }



    @Override
    public String toString() {
        return nama + "\n" + password + "\n" + nim;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(nama);
        dest.writeString(password);
        dest.writeString(nim);
    }

    //toMap() is necessary for the push process //String hari, String dosen_koordinator, String jam, String lokasi, String matakuliah
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nama", nama);
        result.put("password", password);
        result.put("nim", nim);
        return result;
    }
}
