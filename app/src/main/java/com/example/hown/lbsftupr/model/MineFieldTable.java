package com.example.hown.lbsftupr.model;

import com.google.android.gms.location.Geofence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Singleton class.
 * Serves as storage to all known minefields and their exact location.
 */

// TODO read data from file/db that holds location data of mine fields

public class MineFieldTable {

    private static final double CHECK_PERIMETER = 5.0;

    private List<MineField> mineFields;

    private void initialiseMineFields(){
        mineFields = new ArrayList<>();
        mineFields.add(new MineField("Obshaga", -2.2087855832197314,113.93005795776844, 50));
        mineFields.add(new MineField("Glavnoe", -2.2086817248390136,113.92946938052773, 50));
        mineFields.add(new MineField("9k", -2.209426154749313,113.9299875497818, 50));
    }

    private static MineFieldTable instance = new MineFieldTable();

    private MineFieldTable() {
        initialiseMineFields();
    }

    public List<MineField> getMineFields() {
        return Collections.unmodifiableList(mineFields);
    }

    public static MineFieldTable getInstance() {
        return instance;
    }

    public Set<Geofence> getClosestFieldsTo(double latitude, double longitude) {

        Set<Geofence> closestFields = new HashSet<>();

        for (MineField mineField : mineFields) {
            if (mineField.distanceFrom(latitude, longitude) <= CHECK_PERIMETER) {
                closestFields.add(mineField.toGeofence());
            }
        }

        return closestFields;
    }

}
