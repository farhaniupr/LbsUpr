package com.example.hown.lbsftupr;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceIntentService extends IntentService {

	private final String TAG = this.getClass().getCanonicalName();

	public GeofenceIntentService() {
		super("GeofenceIntentService");
		Log.v(TAG, "Constructor.");
	}

	/**public void onCreate() {
		super.onCreate();
		Log.v(TAG, "onCreate");
	}

	public void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("LocalService", "Received start id " + startId + ": " + intent);
		return START_NOT_STICKY;
	}*/

	@Override
	protected void onHandleIntent(Intent intent) {
		GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
		Log.v(TAG, "onHandleIntent");
		if(!geofencingEvent.hasError()) {
			int transition = geofencingEvent.getGeofenceTransition();
			String notificationTitle;

			switch(transition) {
				case Geofence.GEOFENCE_TRANSITION_ENTER:
					notificationTitle = "Masuk ke radius ruangan";
					Log.v(TAG, "Masuk ke radius ruangan");
					break;
				case Geofence.GEOFENCE_TRANSITION_DWELL:
					notificationTitle = "Menetap dalam radius ruangan";
					Log.v(TAG, "Menetap dalam radius ruangan");
					break;
				case Geofence.GEOFENCE_TRANSITION_EXIT:
					notificationTitle = "Keluar dari radius ruangan";
					Log.v(TAG, "Geofence Exited");
					break;
				default:
					notificationTitle = "Geofence Unknown";
			}

			sendNotification(this, getTriggeringGeofences(intent), notificationTitle);
		}
	}

	private void sendNotification(Context context, String notificationText,
								  String notificationTitle) {

				PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "");
		wakeLock.acquire();


		Intent viewIntent = new Intent(this, Tampillaninfomap.class);
		//viewIntent.putExtra("NOTIFICATION_ID", reminder.getId());
		//viewIntent.putExtra("NOTIFICATION_DISMISS", true);
		PendingIntent pending = PendingIntent.getActivity(context, 0, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);


		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.lbsupr)
				.setContentTitle(notificationTitle)
				.setContentText(notificationText)
				//.addAction(getNotification(), )
				.setContentIntent(pending) //TAMBAHAN
				.setOngoing(true)  //true tambahan
				//.setDefaults(Notification.DEFAULT_ALL).
				.setAutoCancel(true);

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		//notificationManager.notify(0, notificationBuilder.build());
        notificationManager.notify(0, notificationBuilder.build());

		wakeLock.release();
	}

	private String getTriggeringGeofences(Intent intent) {
		GeofencingEvent geofenceEvent = GeofencingEvent.fromIntent(intent);
		List<Geofence> geofences = geofenceEvent
				.getTriggeringGeofences();

		String[] geofenceIds = new String[geofences.size()];

		for (int i = 0; i < geofences.size(); i++) {
			geofenceIds[i] = geofences.get(i).getRequestId();
		}

		return TextUtils.join(", ", geofenceIds);
	}
}
