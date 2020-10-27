package com.example.hown.lbsftupr.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.hown.lbsftupr.R;
import com.example.hown.lbsftupr.MapsActivity1;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;


/**
 * Helper class that listens for geofence transitions and triggers notification alarm.
 */

public class GeofenceTransitionsIntentService extends IntentService {

    public static final String TAG = "Lbs Upr Example";
    public static final String WORKER_THREAD_NAME = "GeofenceTransitionsWorker";

    public GeofenceTransitionsIntentService() {
        super(WORKER_THREAD_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent == null) {
            Log.e(TAG, "Geofencing event is null");
            return;
        }

        if (geofencingEvent.hasError()) {
            Log.e(TAG, "GeofenceTransitionService: " +
                    "There is error with geofence, error code: "
                    + geofencingEvent.getErrorCode());
            return;
        }

        Log.i(TAG, "GeofenceTransitionIntentService: getting geofence transition. ");
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            showNotification("DANGER!", "Mine field is near your location.");
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            showNotification("Exited", "You left the danger zone");
        } else {
            showNotification("Error", "Error");
        }
    }

    // TODO make app go wild, vibrations, buzzing, alarm sound whatever?!
    // TODO different type (sound) of notification based on enter/exit event
    public void showNotification(String text, String bigText) {

        Log.i(TAG, "GeofenceTransitionService: showing notification ");

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, MapsActivity1.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // TODO set sound
        // TODO change icon
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(text)
                .setContentText(text)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 }) // TODO constant
                .setSound(Uri.parse("../../res/raw/danger_alarm.mp3"))
                .setContentIntent(pendingNotificationIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0, notification);
    }
}

