package com.example.hown.lbsftupr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.hown.lbsftupr.annotation.CSVAnnotation;
import com.google.firebase.database.IgnoreExtraProperties;



import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by win10 on 10/10/2019.
 */



@IgnoreExtraProperties
public class ju implements Parcelable {
    private String kode;
    private String idJadwal;
    private String dosen_koordinator;
    private String jam;
    private String lokasi;
    private String matakuliah;
    private String tanggal;
    private String hari;

    public ju (){

    }

    public ju (String kode, String dosen_koordinator, String jam, String lokasi, String matakuliah, String hari) {
        super();
        this.kode= kode;
        this.hari= hari;
        this.dosen_koordinator = dosen_koordinator;
        this.jam = jam;
        this.lokasi = lokasi;
        this.matakuliah = matakuliah;
        this.tanggal = tanggal;


    }

    protected ju(Parcel in) {
        kode = in.readString();
        hari = in.readString();
        idJadwal = in.readString();
        dosen_koordinator = in.readString();
        jam = in.readString();
        lokasi = in.readString();
        matakuliah = in.readString();
        tanggal = in.readString();
    }

    public static final Creator<ju> CREATOR = new Creator<ju>() {
        @Override
        public ju createFromParcel(Parcel in) {
            return new ju(in);
        }

        @Override
        public ju[] newArray(int size) {
            return new ju[size];
        }
    };

    public String getkode() {
        return kode;
    }

    public String hari() {
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
        dest.writeString(kode);
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
        result.put("kodeMK", kode);
        result.put("hari", hari);
        result.put("dosen_koordinator", dosen_koordinator);
        result.put("jam",jam);
        result.put("lokasi",lokasi);
        result.put("matakuliah",matakuliah);
        result.put("tanggal",tanggal);
        return result;
    }

    @CSVAnnotation.CSVSetter(info = "kode")
    public void setKode(String kode) {
        this.kode = kode;
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

        ju user = (ju) o;

        if (!kode.equals(user.kode)) return false;
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
        int result = kode.hashCode();
        result = 31 * result + matakuliah.hashCode();
        result = 31 * result + jam.hashCode();
        result = 31 * result + lokasi.hashCode();
        result = 31 * result + dosen_koordinator.hashCode();
        result = 31 * result + tanggal.hashCode();
        result = 31 * result + hari.hashCode();
        return result;
    }
}

