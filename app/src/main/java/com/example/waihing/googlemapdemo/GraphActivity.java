package com.example.waihing.googlemapdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.Circle;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class GraphActivity extends Activity {

    TextView tv;
    SensorManager sensor;
    SensorEventListener svls;
    Handler myhandl= new Handler();
    double temperature=0;


    public float velocite,accel_actuel,accel_app;
    Date last;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        last=new Date(System.currentTimeMillis());

        sensor=(SensorManager)getSystemService(Context.SENSOR_SERVICE);

        sensor.registerListener(listener,
                sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);

        //Mise a jour l interface chaque seconde

        Timer tim=new Timer();
        tim.scheduleAtFixedRate(new TimerTask(){
            public void run () {
                updateGUI();
            }
        },0,1000);


        load();
        setContentView(tv);
    }


    public void load()
    {

        tv= new TextView(this);
        tv.setTextSize(16);
        tv.setTextColor(Color.GREEN);
        tv.setSingleLine(true);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
        tv.setGravity(Gravity.CENTER);

    }
    public void upadatevelocite()
    {

        // Date actuelle en milliseconde
        Date now= new Date(System.currentTimeMillis());
        // ecart en seconde entre date passe et date actuelle
        long ecart= ( now.getTime()-last.getTime())/1000;
        // Mise à jour du temps
        last.setTime(now.getTime());

        float ecartv=accel_app*ecart+0; /* calcule de la vitesse from v=at
+vo;*/


        // Mise à jour
        accel_app=accel_actuel;
        velocite=velocite+ecartv;

        tv.setText(String.valueOf(velocite)+"m/s");






    }

    public final SensorEventListener listener= new SensorEventListener(){
        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {
            // TODO Auto-generated method stub

        }


        @Override
        public void onSensorChanged(SensorEvent event)

        {    float evaleur[]= event.values;
            double calibration = Double.NaN;

            double x= evaleur[0];
            double y= evaleur[1];
            double z= evaleur[2];
            //temperature= evaleur[Sensor.TYPE_AMBIENT_TEMPERATURE];
            // Calcul du changement  acceleration en coordonnnee ca
            double a= -1*Math.sqrt(Math.pow(x, 2)+Math.pow(y,
                    2)+Math.pow(z, 2));

            if(calibration==Double.NaN){
                calibration=a;

            }
            else {
                upadatevelocite();
                accel_actuel=(float)a;
            }

        }
    };

    public void updateGUI()
    {
        myhandl.post(new Runnable(){
            public void run()
            {
                tv.setText("Vit:"+ velocite+"m/s à:"+temperature+"°C\n par SIDIBE");
            }
        });


    }

}
