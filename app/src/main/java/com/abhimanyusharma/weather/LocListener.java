package com.abhimanyusharma.weather;

import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Abhimanyu Sharma on 30-12-2016.
 */

public class LocListener implements LocationListener {
    static double lat, lon;

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            LocListener.lat = location.getLatitude();

            LocListener.lon = location.getLongitude();

        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}