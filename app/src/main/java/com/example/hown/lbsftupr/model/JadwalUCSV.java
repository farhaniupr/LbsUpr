package com.example.hown.lbsftupr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.hown.lbsftupr.annotation.CSVAnnotation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hown on 06-Mar-18.
 */

public class JadwalUCSV implements Parcelable {
    private String hari;
    private String idJadwal;
    private String dosen_koordinator;
    private String jam;
    private String lokasi;
    private String matakuliah;
    private String tanggal;
    private String kodeMK;

    public JadwalUCSV (){

    }

    public JadwalUCSV (String kodeMK, String hari, String dosen_koordinator, String jam, String lokasi, String matakuliah, String tanggal) {
        super();
        this.hari = hari;
        this.dosen_koordinator = dosen_koordinator;
        this.jam = jam;
        this.lokasi = lokasi;
        this.matakuliah = matakuliah;
        this.tanggal = tanggal;
        this.kodeMK = kodeMK;


    }

    protected JadwalUCSV(Parcel in) {
        hari = in.readString();
        idJadwal = in.readString();
        dosen_koordinator = in.readString();
        jam = in.readString();
        lokasi = in.readString();
        matakuliah = in.readString();
        tanggal = in.readString();
        kodeMK = in.readString();
    }


    public static final Creator<JadwalUCSV> CREATOR = new Creator<JadwalUCSV>() {
        @Override
        public JadwalUCSV createFromParcel(Parcel in) {
            return new JadwalUCSV(in);
        }

        @Override
        public JadwalUCSV[] newArray(int size) {
            return new JadwalUCSV[size];
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

    public String getKodeMK() {
        return kodeMK;
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
        return jam + "\n" + matakuliah + "\n" + lokasi + "\n" + dosen_koordinator + "\n" +kodeMK +  "\n" + hari +  "\n" + tanggal;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(kodeMK);
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
        result.put("kode MK",kodeMK);
        return result;
    }

    @CSVAnnotation.CSVSetter(info = "kode MK")
    public void setKodeMK(String kodeMK) {
        this.kodeMK = kodeMK;
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

        JadwalUCSV user = (JadwalUCSV) o;

        if (!kodeMK.equals(user.kodeMK)) return false;
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
        result = 31 * result + kodeMK.hashCode();
        result = 31 * result + matakuliah.hashCode();
        result = 31 * result + jam.hashCode();
        result = 31 * result + lokasi.hashCode();
        result = 31 * result + dosen_koordinator.hashCode();
        result = 31 * result + tanggal.hashCode();
        return result;
    }
}