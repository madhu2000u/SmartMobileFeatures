package com.madhu.smartmobilefeatures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Permission;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mProximity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ANSWER_PHONE_CALLS}, 1);
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 3);

        //mSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //mProximity=mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        //Intent backgroundCallService=new Intent(getApplicationContext(), IncomingCallBackgroundService.class);
        //startService(backgroundCallService);

        Button callButton=findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
            }
        });
    }

    private void call() {
        Log.d("msg", "Inside call");
        Log.d("msg","answer phone state "+(checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS)==PackageManager.PERMISSION_GRANTED));
        Log.d("msg","read phone state  "+(checkSelfPermission(Manifest.permission.READ_PHONE_STATE)==PackageManager.PERMISSION_GRANTED));
        Log.d("msg","process outgoing calls "+(checkSelfPermission(Manifest.permission.PROCESS_OUTGOING_CALLS)==PackageManager.PERMISSION_GRANTED));
        if (checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ANSWER_PHONE_CALLS}, 1);
        }
        else if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
        else if (checkSelfPermission(Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 1);
        }

        /*IntentFilter intentFilter=new IntentFilter();

        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);

        intentFilter.setPriority(100);

        IncomingCall_Receiver call_receiver=new IncomingCall_Receiver();
        registerReceiver(call_receiver, intentFilter);*/

        Intent serviceIntent = new Intent(this, IncomingCallBackgroundService.class);
        serviceIntent.putExtra("title", "Smart Mobile Features");
        serviceIntent.putExtra("content","Smart Mobile Features is running in background to detect and respond " +
                "to various events required for smart features");
        ContextCompat.startForegroundService(this, serviceIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //if (sensorEvent.sensor.getType()==Sensor.TYPE_PROXIMITY)
        /*{
            if (sensorEvent.values[0]<=1) {
                TelecomManager tm= (TelecomManager)this.getSystemService(Context.TELECOM_SERVICE);
                tm.acceptRingingCall();
                Log.d("msg", "Proximity distance - " + sensorEvent.values[0]);
            }
        }*/

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}


