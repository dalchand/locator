package com.dalchand.locator;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.marker.animation.LatLngInterpolator;
import com.google.marker.animation.MarkerAnimation;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private MyLocation myLocation;
    private Marker friendMarker;
    private com.dalchand.locator.dataobjects.Location friendLocation;

    public MyLocation getMyLocation() {
        return myLocation;
    }

    public void setFriendLocation(com.dalchand.locator.dataobjects.Location loc) {
        friendLocation = loc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        scheduleAlarmReceiver();
    }

    private void updateUI(Intent intent) {

        String location = intent.getStringExtra("friendLocation");

        if(location != null) {
            try {
                friendLocation = com.dalchand.locator.dataobjects.Location.parseJSONObject(new JSONObject(location));
                if (friendMarker == null) {
                    friendMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(friendLocation.latitude, friendLocation.longitude))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.red)));
                } else {
                    MarkerAnimation.animateMarkerToGB(friendMarker, new LatLng(friendLocation.latitude, friendLocation.longitude), new LatLngInterpolator.Linear());
                }
            } catch(Exception e) {
                Log.e("updateUI", e.getMessage(), e);
            }
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        registerReceiver(broadcastReceiver, new IntentFilter(LocationUpdaterService.BROADCAST_NAME));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(myLocation != null) {
            myLocation.stop();
        }
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void scheduleAlarmReceiver() {
        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(this, 0, new Intent(this, LocationUpdaterAlarmReceiver.class),
                        PendingIntent.FLAG_CANCEL_CURRENT);

        // Use inexact repeating which is easier on battery (system can phase events and not wake at exact times)
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 10000,
                10000, pendingIntent);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(lastKnownLocation == null) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(0, 0))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.green));

        if(lastKnownLocation != null) {
            markerOptions.position(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
            myLocation = new MyLocation(locationManager, mMap.addMarker(markerOptions));
            myLocation.start();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 15));
        }


    }
}

