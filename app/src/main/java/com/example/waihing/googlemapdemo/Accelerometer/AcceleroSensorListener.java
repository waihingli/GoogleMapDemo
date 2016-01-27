package com.example.waihing.googlemapdemo.Accelerometer;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;

/**
 * Created by WaiHing on 14/1/2016.
 */
public abstract class AcceleroSensorListener implements SensorEventListener {
    protected float lastX;
    protected float lastY;
    protected float lastZ;
    public abstract Point getPoint();
    public void onAccuracyChanged(Sensor arg0, int arg1){}
}
