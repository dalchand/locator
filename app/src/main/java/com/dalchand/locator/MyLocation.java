package com.dalchand.locator;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.marker.animation.LatLngInterpolator;
import com.google.marker.animation.MarkerAnimation;

/**
 * Created by dalchand on 2/5/15.
 */
public class MyLocation implements  LocationListener{

    private Location myLocation;

    private Marker marker;

    private LocationManager locationManager;

    public MyLocation(LocationManager locationManager, Marker marker) {
        this.locationManager = locationManager;
        this.marker = marker;
    }

    public Location getMyLocation() {
        return myLocation;
    }

    public Marker getMarker() {
        return marker;
    }

    public void start() {

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 3, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 3, this);

    }

    public void stop() {
        locationManager.removeUpdates(this);
    }


    @Override
    public void onLocationChanged(Location location) {
        if(LocationHelpers.isBetterLocation(location, myLocation)) {
            myLocation = location;
            MarkerAnimation.animateMarkerToGB(marker, new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), new LatLngInterpolator.Linear());
        }

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
}
