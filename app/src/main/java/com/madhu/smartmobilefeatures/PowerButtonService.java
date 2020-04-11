package com.madhu.smartmobilefeatures;


import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

public class PowerButtonService extends Service {

    public PowerButtonService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("msg","Power Service started");

        LinearLayout mLinear=new LinearLayout(getApplicationContext()){

           @Keep
           public void onCloseSystemDialogs(String reason) {
                if ("globalactions".equals(reason)) {
                }
            }

            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {

               Log.d("msg","inside event");

                if (event.getKeyCode()==KeyEvent.KEYCODE_HOME){
                    Log.d("msg","keyevent - "+event.getKeyCode());
                    sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
                }
                return super.dispatchKeyEvent(event);
            }
        };

        mLinear.setFocusable(true);

        View view= LayoutInflater.from(this).inflate(R.layout.power_key_service_layout, mLinear);
        WindowManager wm=(WindowManager)getSystemService(WINDOW_SERVICE);

        final WindowManager.LayoutParams params;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            params=new WindowManager.LayoutParams(
                    100,
                    100,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT

            );

        }else{
            params=new WindowManager.LayoutParams(
                    100,
                    100,
                    WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                    PixelFormat.TRANSLUCENT
            );
        }

        params.gravity= Gravity.START | Gravity.CENTER_VERTICAL;
        wm.addView(view,params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("msg", "Power Service Destroyed");
    }
}
