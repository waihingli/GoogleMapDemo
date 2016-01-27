package com.example.waihing.googlemapdemo;

/**
 * Created by WaiHing on 25/1/2016.
 */
public class Filter {
    private double q; //process noise covariance
    private double r; //measurement noise covariance
    private double x; //value
    private double p; //estimation error covariance
    private double k; //kalman gain


    private static final float ALPHA = 0.15f;

    public Filter(){
    }

    public void setKalmanInit(double q, double r, double p, double ini_value){
        this.q = q;
        this.r = r;
        this.p = p;
        this.x = ini_value;
    }

    public void kalmanUpdate(double measurement){
        p = p + q;

        k = p/(p+r);
        x = x+k *(measurement-x);
        p = (1-k) *p;
    }


    protected static float[] lowPass(float[] input, float[] output){
        if (output == null)
            return input;
        for (int i=0; i<input.length; i++){
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
            //output[i] = (1-ALPHA)*output[i] + ALPHA*input[i];
        }
        return output;
    }
}
