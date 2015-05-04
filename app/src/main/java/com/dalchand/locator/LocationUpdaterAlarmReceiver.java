package com.dalchand.locator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by dalchand on 4/5/15.
 */
public class LocationUpdaterAlarmReceiver extends BroadcastReceiver {

    // onReceive must be very quick and not block, so it just fires up a Service
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, LocationUpdaterService.class));
    }
}
