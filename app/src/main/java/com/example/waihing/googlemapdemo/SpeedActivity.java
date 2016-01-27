package com.example.waihing.googlemapdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class SpeedActivity extends Activity implements IBaseGpsListener, SensorEventListener {

    private Location previousLocation = null;
    private LocationManager locationManager;
    private Sensor acceleromoter;
    private SensorManager sensorManager;
    private long lastTime;
    private float velocity = 0;
    private float timePass = 0;
    protected float[] acceSensorVals;
    TextView tv_speed, tv_acceleration, tv_acceleration2;
    Filter filter = new Filter();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acceleromoter = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this, acceleromoter, SensorManager.SENSOR_DELAY_NORMAL);

        tv_speed = (TextView)findViewById(R.id.tv_speed);
        tv_acceleration = (TextView)findViewById(R.id.tv_speed2);
        tv_acceleration2 = (TextView)findViewById(R.id.tv_speed3);
        lastTime = System.currentTimeMillis();

//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                updateAcceleration();
//            }
//        }, 0, 1000);

        this.updateSpeed(null);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void updateSpeed(CLocation currentLocation){
        float nCurrentSpeed = 0;
        if(previousLocation!=null){
            float distance = currentLocation.distanceTo(previousLocation);
            float timeTaken = ((currentLocation.getTime() - previousLocation.getTime())/1000);

            if(timeTaken > 0) {
                nCurrentSpeed = getAverageSpeed(distance, timeTaken);
            }

            if(nCurrentSpeed >= 0) {
                DecimalFormat df = new DecimalFormat("#.##");
                tv_speed.setText("Speed: " + df.format(nCurrentSpeed) + " meters/second\n"
                                + currentLocation.getSpeed() + " meters/second");
            }
        }
        previousLocation = currentLocation;
    }

    public void updateAcceleration(){
        if(acceSensorVals!=null){
            DecimalFormat df = new DecimalFormat("#.###");
            tv_acceleration2.setText(velocity + " meters/second\n");
        }
        velocity = 0;
        timePass = 0;
    }

    public float getAverageSpeed(float distance, float timeTaken) {
        //float minutes = timeTaken/60;
        //float hours = minutes/60;
        float speed = 0;
        if(distance > 0) {
            float distancePerSecond = timeTaken > 0 ? distance/timeTaken : 0;
            float distancePerMinute = distancePerSecond*60;
            float distancePerHour = distancePerMinute*60;
            speed = distancePerHour > 0 ? (distancePerHour/1000) : 0;
        }

        return speed;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_speed) {
            return true;
        }
        if (id == R.id.action_map) {
            Intent i = new Intent(this, MapsActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_grpah) {
            Intent i = new Intent(this, GraphActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null){
            CLocation myLocation = new CLocation(location);
            this.updateSpeed(myLocation);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long newTime = System.currentTimeMillis();
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            acceSensorVals = filter.lowPass(event.values.clone(), acceSensorVals);
            double acceleration = Math.sqrt(Math.pow(acceSensorVals[0], 2)+Math.pow(acceSensorVals[1], 2)+Math.pow(acceSensorVals[2], 2));
            onAccelerationChangeHandler(acceleration, ((newTime - lastTime) / 1000));
            lastTime = newTime;
        }
        DecimalFormat df = new DecimalFormat("#.###");
        tv_acceleration.setText("X: " + df.format(acceSensorVals[0]) + " meters/second^2\n" +
                "Y: " + df.format(acceSensorVals[1]) + " meters/second^2\n" +
                "Z: " + df.format(acceSensorVals[2]) + " meters/second^2\n" );
    }

    public void onAccelerationChangeHandler(double acceleration, float timePassed) {
        velocity += acceleration*timePassed;
        timePass += timePassed;
        if(timePass>=5){
            updateAcceleration();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
