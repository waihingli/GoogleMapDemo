package com.example.waihing.googlemapdemo;

import android.location.Location;

/**
 * Created by WaiHing on 13/1/2016.
 */
public class CLocation extends Location{
    private boolean bUseMetricUnits = false;

    public CLocation(Location location){
        super(location);
    }

    @Override
    public float distanceTo(Location dest){
        float nDistance = super.distanceTo(dest);
        return nDistance;
    }

    @Override
    public float getAccuracy() {
        float nAccuracy = super.getAccuracy();
        return nAccuracy;
    }

    @Override
    public double getAltitude() {
        double nAltitude = super.getAltitude();
        return nAltitude;
    }

    @Override
    public float getSpeed() {
        float nSpeed = super.getSpeed();
        return nSpeed;
    }
}
