package com.madhu.smartmobilefeatures;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class CreateNotification {

    public static final String CHANNEL_ID="SmartFeatures";

    public String title, contentText;
    public PendingIntent pendingIntent;
    public Context context;

    public CreateNotification(Context context, String title, String contentText, PendingIntent pendingIntent){
        this.contentText=contentText;
        this.context=context;
        this.pendingIntent=pendingIntent;
        this.title=title;

    }


    public Notification createNotification(){
        Notification notification=new Notification.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_whatshot_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        return notification;
    }

    public void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
    }


