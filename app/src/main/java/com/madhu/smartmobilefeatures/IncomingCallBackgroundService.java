package com.madhu.smartmobilefeatures;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class IncomingCallBackgroundService extends Service implements ServiceStop{

    IncomingCall_Receiver call_receiver;

    private Context context;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*public IncomingCallBackgroundService(Context context){
        this.context=context;


    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        CreateNotification notification =new CreateNotification(this, intent.getStringExtra("title"), intent.getStringExtra("content"), pendingIntent, this);

        notification.createNotificationChannel();
        Notification newNotification= notification.createNotification();
        startForeground(1,newNotification);




        //SensorManager mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        //Sensor mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        //mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        //return START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("msg","Service created");


        IntentFilter intentFilter=new IntentFilter();

        intentFilter.addAction("android.intent.action.PHONE_STATE");

        intentFilter.setPriority(100);

        call_receiver=new IncomingCall_Receiver();
        registerReceiver(call_receiver, intentFilter);



    }

    @Override
    public void onDestroy() {

        Log.d("msg","Incoming call Service destroyed");
        unregisterReceiver(call_receiver);

        //Intent broadcastIntent=new Intent("com.madhu.smartfeatues");

        //sendBroadcast(broadcastIntent);
        super.onDestroy();
    }

    @Override
    public void stopService() {
        Log.d("msg","Reached stopService()");
        stopSelf();
    }

   /* @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType()==Sensor.TYPE_PROXIMITY)
        {
            if (sensorEvent.values[0]<=1) {
                TelecomManager tm= (TelecomManager)this.getSystemService(Context.TELECOM_SERVICE);
                tm.acceptRingingCall();
                Log.d("msg", "Proximity distance - " + sensorEvent.values[0]);
                //stopSelf();

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }*/
}
