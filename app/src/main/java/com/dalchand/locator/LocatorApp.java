package com.dalchand.locator;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by dalchand on 3/5/15.
 */
public class LocatorApp extends Application {

    private Location myLocation;

    private LocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        Log.d("onReceive", "configuring alarm");
        setUpLocationListener();
    }

    private void setMyLocation(Location location) {
        if(LocationHelpers.isBetterLocation(location, myLocation)) {
            myLocation = location;
        }
    }

    public Location getMyLocation() {
        return myLocation;
    }

    private void setUpLocationListener() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 3, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setMyLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 3, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setMyLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }


}
