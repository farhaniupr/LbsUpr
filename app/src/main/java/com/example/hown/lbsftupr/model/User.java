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
public class User implements Parcelable {
    private String nama;
    private String password;
    private String nim;
    private String jadwal;
    private String jadwalU;


    public User(){

    }

    public User(String nama, String password, String nim, String jadwal, String jadwalU) {
        super();
        this.nama = nama;
        this.password = password;
        this.nim = nim;
        this.jadwal = jadwal;
        this.jadwalU = jadwalU;

    }

    public String getNama() {
        return nama;
    }

    public String getPassword() {
        return nim;
    }

    public String getNim() {
        return nim;
    }

    public String getJadwal() { return jadwal;}

    public String getJadwalU() { return jadwalU;}


    @Override
    public String toString() {
        return nama + "\n" + password + "\n" + nim +"\n"  + jadwal +"\n" + jadwalU ;
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
        dest.writeString(jadwal);
        dest.writeString(jadwalU);
    }

    //toMap() is necessary for the push process //String hari, String dosen_koordinator, String jam, String lokasi, String matakuliah
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nama", nama);
        result.put("password", nim);
        result.put("nim", nim);
        result.put("jadwal", jadwal);
        result.put("jadwalU", jadwalU);
        return result;
    }

    @CSVAnnotation.CSVSetter(info = "Nama")
    public void setNama(String nama) {
        this.nama= nama;
    }

    @CSVAnnotation.CSVSetter(info = "NIM")
    public void setNim(String nim) {
        this.nim = nim;
    }

    @CSVAnnotation.CSVSetter(info = "NIM")
    public void setPassword(String password) {
        this.password = password;
    }

    @CSVAnnotation.CSVSetter(info = "Kode MK")
    public void setJadwal(String jadwal) {
        this.jadwal= jadwal;
    }

    @CSVAnnotation.CSVSetter(info = "Nama MK")
    public void setJadwalUAS(String jadwalU) {
        this.jadwalU= jadwalU;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!nama.equals(user.nama)) return false;
        if (!nim.equals(user.nim)) return false;
        if (!password.equals(user.password)) return false;
        if (!jadwal.equals(user.jadwal)) return false;
        if (!jadwalU.equals(user.jadwalU)) return false;
        //if (!tanggal.equals(user.tanggal)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nama.hashCode();
        result = 31 * result + nim.hashCode();
        result = 31 * result + jadwal.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + jadwalU.hashCode();
        //result = 31 * result + tanggal.hashCode();
        return result;
    }

}

