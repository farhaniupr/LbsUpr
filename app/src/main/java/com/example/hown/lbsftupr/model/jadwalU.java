package com.example.hown.lbsftupr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.hown.lbsftupr.annotation.CSVAnnotation;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by hown on 07-Jan-18.
 */

@IgnoreExtraProperties
public class jadwalU implements Parcelable {
    private String hari;
    private String idJadwal;
    private String dosen_koordinator;
    private String jam;
    private String lokasi;
    private String matakuliah;
    private String tanggal;

    public jadwalU (){

    }

    public jadwalU (String hari, String dosen_koordinator, String jam, String lokasi, String matakuliah, String tanggal) {
        super();
        this.hari = hari;
        this.dosen_koordinator = dosen_koordinator;
        this.jam = jam;
        this.lokasi = lokasi;
        this.matakuliah = matakuliah;
        this.tanggal = tanggal;


    }

    protected jadwalU(Parcel in) {
        hari = in.readString();
        idJadwal = in.readString();
        dosen_koordinator = in.readString();
        jam = in.readString();
        lokasi = in.readString();
        matakuliah = in.readString();
        tanggal = in.readString();
    }

    public static final Creator<jadwalU> CREATOR = new Creator<jadwalU>() {
        @Override
        public jadwalU createFromParcel(Parcel in) {
            return new jadwalU(in);
        }

        @Override
        public jadwalU[] newArray(int size) {
            return new jadwalU[size];
        }
    };

    public String gethari() {
        return hari;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getIdJadwal() {
        return idJadwal;
    }

    public String getDosen_koordinator() {
        return dosen_koordinator;
    }

    public String getjam() {
        return jam;
    }

    public String getlokasi() {
        return lokasi;
    }

    public String getmatakuliah() {
        return matakuliah;
    }

    @Override
    public String toString() {
        return jam + "\n" + matakuliah + "\n" + lokasi + "\n" + dosen_koordinator;
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
        dest.writeString(tanggal);

    }

    //toMap() is necessary for the push process //String hari, String dosen_koordinator, String jam, String lokasi, String matakuliah
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("hari", hari);
        result.put("dosen_koordinator", dosen_koordinator);
        result.put("jam",jam);
        result.put("lokasi",lokasi);
        result.put("matakuliah",matakuliah);
        result.put("tanggal",tanggal);
        return result;
    }

    @CSVAnnotation.CSVSetter(info = "hari")
    public void setHari(String hari) {
        this.hari = hari;
    }

    @CSVAnnotation.CSVSetter(info = "matakuliah")
    public void setMatakuliah(String matakuliah) {
        this.matakuliah = matakuliah;
    }

    @CSVAnnotation.CSVSetter(info = "jam")
    public void setJam(String jam) {
        this.jam = jam;
    }

    @CSVAnnotation.CSVSetter(info = "dosen koordinator")
    public void setDosen_koordinator(String dosen_koordinator) {
        this.dosen_koordinator= dosen_koordinator;
    }

    @CSVAnnotation.CSVSetter(info = "lokasi")
    public void setLokasi(String lokasi) {
        this.lokasi= lokasi;
    }

    @CSVAnnotation.CSVSetter(info = "tanggal")
    public void setTanggal(String tanggal) {
        this.tanggal= tanggal;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        jadwalU user = (jadwalU) o;

        if (!hari.equals(user.hari)) return false;
        if (!matakuliah.equals(user.matakuliah)) return false;
        if (!jam.equals(user.jam)) return false;
        if (!lokasi.equals(user.lokasi)) return false;
        if (!dosen_koordinator.equals(user.dosen_koordinator)) return false;
        if (!tanggal.equals(user.tanggal)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = hari.hashCode();
        result = 31 * result + matakuliah.hashCode();
        result = 31 * result + jam.hashCode();
        result = 31 * result + lokasi.hashCode();
        result = 31 * result + dosen_koordinator.hashCode();
        result = 31 * result + tanggal.hashCode();
        return result;
    }
}

