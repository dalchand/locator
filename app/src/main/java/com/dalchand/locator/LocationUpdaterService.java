package com.dalchand.locator;

import android.app.Application;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.dalchand.locator.communication.Location;

/**
 * Created by dalchand on 3/5/15.
 */
public class LocationUpdaterService extends IntentService {

    LocatorApp application;

    Intent broadCastIntent;

    public static final String BROADCAST_NAME = "FRIENDS_LOCATION";

    public LocationUpdaterService() {
        super("Location Updater Service");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        broadCastIntent = new Intent(LocationUpdaterService.BROADCAST_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        this.application = (LocatorApp) getApplication();
        android.location.Location location = this.application.getMyLocation();
        if(location != null) {
            Log.d("onHandleIntent", location.toString());
            com.dalchand.locator.dataobjects.Location loc = new com.dalchand.locator.dataobjects.Location();
            loc.latitude = location.getLatitude();
            loc.longitude = location.getLongitude();
            Location.updateLocation(loc);

            try {
                com.dalchand.locator.dataobjects.Location friendLocation = Location.getFriendLocation();
                broadCastIntent.putExtra("friendLocation", friendLocation.toJSONObject().toString());
                sendBroadcast(broadCastIntent);
            } catch (Exception e) {
                Log.e("error in service", e.getMessage(), e);
            }

        }


    }
}
