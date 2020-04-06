package com.madhu.smartmobilefeatures;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.sip.SipSession;
import android.os.IBinder;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Switch;

import androidx.annotation.Nullable;

public class IncomingCall_Receiver extends BroadcastReceiver implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mProximity;
    private Context context;
    private TelephonyManager telephonyManager;


    private float first=-1f, second=-1f;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PHONE_STATE"))
        {
            Log.d("msg"," inside if Broadcast received "+ intent.getAction()+context.getApplicationContext().getApplicationContext().getApplicationContext().getPackageName());
            this.context=context;


            telephonyManager=(TelephonyManager)context.getSystemService((Context.TELEPHONY_SERVICE));
            if (telephonyManager.getCallState()==TelephonyManager.CALL_STATE_RINGING){
                Log.d("msg","Ringing");
                mSensorManager=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
                mProximity=mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
            }
            //Log.d("msg",)
        }

        Log.d("msg","Broadcast received "+ intent.getAction());

        //Intent backgroundCallService=new Intent(context, IncomingCallBackgroundService.class);
        //context.startService(backgroundCallService);




    }



    @SuppressLint({"NewApi", "MissingPermission"}) //Permission already handled in MainActivity
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {


        if (sensorEvent.sensor.getType()==Sensor.TYPE_PROXIMITY)
        {

            if(sensorEvent.values[0]>1){
                first=sensorEvent.values[0];
            }else if (sensorEvent.values[0]<=1 && first!=-1f){   //Record second event values only after first event value is recorded
                second=sensorEvent.values[0];
            }
            Log.d("msg","outside if first"+first);
            Log.d("msg","outside if second"+second);

            if (first>second && first!=-1f && second!=-1f) { //Both values must be recorded before answering
                Log.d("msg","first"+first);         //Bug where call is answered without even lifting it to ear
                Log.d("msg","second"+second);       // when mobile is in pocket fixed.
                 TelecomManager tm= (TelecomManager)context.getSystemService(Context.TELECOM_SERVICE);

                 try {



                     tm.acceptRingingCall();
                     mSensorManager.unregisterListener(this);
                     //mSensorManager.unregisterListener((SensorEventListener) context.getApplicationContext());
                 }catch (NullPointerException e){
                     Log.d("msg","Error" +e.getCause().getMessage());
                 }
                Log.d("msg", "Proximity distance - " + sensorEvent.values[0]);

                //stopSelf();

            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }








    /*private class PhoneStateListner extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {

        }
    }*/
}



