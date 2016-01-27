package com.example.waihing.googlemapdemo.Accelerometer;

/**
 * Created by WaiHing on 15/1/2016.
 */
public class Point {
    private float x = 0;
    private float y = 0;
    private float z = 0;
    private int cnt = 1;

    public float getX() {
        return x/(float)cnt;
    }

    public float getY() {
        return y/(float)cnt;
    }

    public float getZ() {
        return z/(float)cnt;
    }

    public Point(float x, float y, float z, int cnt) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.cnt = cnt;
    }


    public float getForce(){
        return getX()*getX()+getY()*getY()+getZ()*getZ();
    }
}
