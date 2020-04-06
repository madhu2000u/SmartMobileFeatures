package com.madhu.smartmobilefeatures;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class CreateNotification{

    private static final String CHANNEL_ID="SmartFeatures";

    private String title, contentText;
    private PendingIntent pendingIntent;
    private Context context;
    private static IncomingCallBackgroundService service1;

    public CreateNotification(Context context, String title, String contentText, PendingIntent pendingIntent, IncomingCallBackgroundService service){
        this.contentText=contentText;
        service1=service;
        this.context=context;
        this.pendingIntent=pendingIntent;
        this.title=title;

    }


    public Notification createNotification(){
        Intent stopServiceIntent=new Intent(context, StopService.class);
        PendingIntent pendingIntent1=PendingIntent.getService(context, 1,stopServiceIntent,0);
        Notification.Action action=new Notification.Action(R.drawable.ic_whatshot_black_24dp, "quit", pendingIntent1);
        Notification notification=new Notification.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_whatshot_black_24dp)
                .addAction(action)
                .setContentIntent(pendingIntent)
                .build();

        return notification;
    }

    public void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }


    public static class StopService extends Service {



        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate() {
            stopService(service1);
            stopSelf();
            super.onCreate();
        }




        public void stopService(ServiceStop serviceStop){   //Passed argument "service1" is a Java class object while the parameter of stopService is an Interface,
            Log.d("msg","Inside stopService callback"); //seems to be incompatible. But actually since the java class implements the interface it is fine.
            serviceStop.stopService();
        }

        @Override
        public void onDestroy() {
            Log.d("msg","StopService Service destroyed");
            super.onDestroy();
        }
    }
}




