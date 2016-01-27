package com.example.waihing.googlemapdemo.Accelerometer;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;
/**
 * Created by WaiHing on 15/1/2016.
 */
public class Accelerometer extends AcceleroSensorListener {
    private static final int BUFFER_SIZE = 500;
    //calibration
    private float dX = 0;
    private float dY = 0;
    private float dZ = 0;

    //buffer variables
    private float X;
    private float Y;
    private float Z;
    private int cnt = 0;

    public Point getLastPoint() {
        return new Point(lastX, lastY, lastZ, 1);
    }

    @Override
    public Point getPoint() {
        if(cnt==0){
            return new Point(lastX, lastY, lastZ, 1);
        }

        Point p = new Point(X, Y, Z, cnt);

        reset();
        return p;
    }

    public void reset(){
        cnt = 0;
        X = 0;
        Y = 0;
        Z = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[SensorManager.DATA_X] + dX;
        float y = event.values[SensorManager.DATA_Y] + dY;
        float z = event.values[SensorManager.DATA_Z] + dZ;

        lastX = x;
        lastY = y;
        lastZ = z;

        X+= x;
        Y+= y;
        Z+= z;

        if (cnt < BUFFER_SIZE-1) {
            cnt++;
        } else {
            reset();
        }
    }

    public int getCnt(){
        return cnt;
    }

    public  void setdX(float dX) {
        this.dX = dX;
    }

    public  void setdY(float dY) {
        this.dY = dY;
    }

    public  void setdZ(float dZ) {
        this.dZ = dZ;
    }
}
