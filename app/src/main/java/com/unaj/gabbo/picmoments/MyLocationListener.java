package com.unaj.gabbo.picmoments;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Gabbo on 1/11/2017.
 */

public class MyLocationListener implements LocationListener {

    private double longitude, latitude;

    @Override
    public void onLocationChanged(Location location) {
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();

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

    public String getLocation (){
        return ""+longitude +',' + latitude;
    }
}
