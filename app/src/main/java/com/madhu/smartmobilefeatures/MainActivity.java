package com.madhu.smartmobilefeatures;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Permission;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private SharedPreferences featurePreferences;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        serviceIntent= new Intent(this, IncomingCallBackgroundService.class);

        featurePreferences=getSharedPreferences("Feature Settings", Context.MODE_PRIVATE);
        if (featurePreferences.getString("newPref","null").equals("null"))
        {
            Log.d("msg","Created new Preferences");
            SharedPreferences.Editor editor=featurePreferences.edit();
            editor.putString("newPref","exists");
            editor.putBoolean("auto-call",false);
            editor.apply();
        }





        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ANSWER_PHONE_CALLS}, 1);
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 3);

        //mSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //mProximity=mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        //Intent backgroundCallService=new Intent(getApplicationContext(), IncomingCallBackgroundService.class);
        //startService(backgroundCallService);

        /*Button callButton=findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
            }
        });*/

        Switch auto_callSwitch=findViewById(R.id.auto_attendCallSwitch);
        auto_callSwitch.setChecked(featurePreferences.getBoolean("auto-call", false));
        if (auto_callSwitch.isChecked()){startForeground(serviceIntent);}
        auto_callSwitch.setOnCheckedChangeListener(this);


        /*Button enable=findViewById(R.id.enable);
        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDrawOverlyPermission()){
                    startService(new Intent(getApplicationContext(), PowerButtonService.class));
                }
            }
        });*/




    }

    /*private boolean checkDrawOverlyPermission(){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        if (!Settings.canDrawOverlays(this)){
            Intent overlayIntent=new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+ getPackageName()));
            startActivityForResult(overlayIntent, 1);
            return false;
        }else return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==1){
            if (Settings.canDrawOverlays(this)){
                startService(new Intent(this, PowerButtonService.class));
            }
        }
    }*/

    private void call() {
        Log.d("msg", "Inside call");
        Log.d("msg","answer phone state "+(ContextCompat.checkSelfPermission(this,Manifest.permission.ANSWER_PHONE_CALLS)==PackageManager.PERMISSION_GRANTED));
        Log.d("msg","read phone state  "+(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)==PackageManager.PERMISSION_GRANTED));
        Log.d("msg","process outgoing calls "+(ContextCompat.checkSelfPermission(this,Manifest.permission.PROCESS_OUTGOING_CALLS)==PackageManager.PERMISSION_GRANTED));
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ANSWER_PHONE_CALLS}, 1);
        }
        else if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
        else if (ContextCompat.checkSelfPermission(this,Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 1);
        }

        /*IntentFilter intentFilter=new IntentFilter();

        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);

        intentFilter.setPriority(100);

        IncomingCall_Receiver call_receiver=new IncomingCall_Receiver();
        registerReceiver(call_receiver, intentFilter);*/



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
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        switch (compoundButton.getId()){

            case R.id.auto_attendCallSwitch:
                SharedPreferences.Editor editor=featurePreferences.edit();
                editor.putBoolean("auto-call",b);
                editor.apply();
                if (b){
                    startForeground(serviceIntent);
                }else {
                    stopService(serviceIntent);
                }

        }

        Log.d("msg",""+compoundButton.getId());

    }

    private void startForeground(Intent serviceIntent){
        serviceIntent.putExtra("title", "Smart Mobile Features");
        serviceIntent.putExtra("content","Smart Mobile Features is running in background to detect and respond " +
                "to various events required for smart features");
        ContextCompat.startForegroundService(this, serviceIntent);
    }
}


