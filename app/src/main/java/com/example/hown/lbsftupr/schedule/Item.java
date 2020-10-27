package com.example.hown.lbsftupr.schedule;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;


public class Item implements Parcelable {

    String hari;
    String jam;
    String matakuliah;
    String lokasi;
    String dosen_koordinator;

    public Item(){

    }

    public Item(String matakuliah, String hari, String jam, String lokasi, String dosen_koordinator) {
        super();
        this.hari = hari;
        this.jam = jam;
        this.matakuliah = matakuliah;
        this.lokasi = lokasi;
        this.dosen_koordinator = dosen_koordinator;
    }

    public String gethari() {
        return hari;
    }

    public String getjam() {
        return jam;
    }

    public String getmatakuliah() {
        return matakuliah;
    }

    public String getlokasi() {
        return lokasi;
    }

    public String getdosen_koordinator() {
        return dosen_koordinator;
    }




    @Override
    public String toString() {
        return jam + "\n" + matakuliah + "\n" /**+ lokasi + "\n" */+ dosen_koordinator ;
        //return hari + "\n" + jam + "\n" + matakuliah + "\n" + lokasi + "\n" + dosen_koordinator ;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(hari);
        dest.writeString(jam);
        dest.writeString(matakuliah);
        dest.writeString(lokasi);
        dest.writeString(dosen_koordinator);

    }

    private Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("hari", hari);
        result.put("jam", jam);
        result.put("matakuliah",matakuliah);
        result.put("lokasi",lokasi);
        result.put("dosen_koordinator",dosen_koordinator);
        return result;
    }



}
