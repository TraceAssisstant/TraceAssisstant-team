package com.example.traceassistant.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/**
 *自定义服务类
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.DPoint;
import com.example.traceassistant.R;
import java.util.List;


public class GeoFenceService extends Service {
    DPoint centerPoint;
    //定义接收广播的action字符串
    public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";

    @Override
    public void onCreate() {
        super.onCreate ();
        //地理围栏的中心点

        centerPoint = new DPoint ();
        centerPoint.setLatitude (41.222825);
        centerPoint.setLongitude (-112.0004);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //实例化地理围栏客户端
        GeoFenceClient mGeoFenceClient = new GeoFenceClient (getApplicationContext ());
        mGeoFenceClient.setActivateAction (GeoFenceClient.GEOFENCE_IN | GeoFenceClient.GEOFENCE_OUT);
        mGeoFenceClient.addGeoFence (centerPoint, 1000, "1");
        GeoFenceListener geoFenceListener = new GeoFenceListener () {
            @Override
            public void onGeoFenceCreateFinished(List<GeoFence> list, int i, String s) {
                if (i == GeoFence.ADDGEOFENCE_SUCCESS) {
                    Toast.makeText (getApplicationContext (),"创建地理围栏成功",Toast.LENGTH_LONG).show ();
                    Log.d("GEOFENCE","创建地理围栏成功");
                }

            }
        };
        mGeoFenceClient.setGeoFenceListener (geoFenceListener);
        //创建并设置PendingIntent
        mGeoFenceClient.createPendingIntent (GEOFENCE_BROADCAST_ACTION);
        IntentFilter filter = new IntentFilter ();
        filter.addAction (GEOFENCE_BROADCAST_ACTION);
        registerReceiver (mGeoFenceReceiver, filter);
        return super.onStartCommand (intent, flags, startId);
    }

    private BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver () {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取Bundle
            Bundle bundle = intent.getExtras ();
            int statu = bundle.getInt (GeoFence.BUNDLE_KEY_FENCESTATUS);
            if (statu == GeoFenceClient.GEOFENCE_IN) {
                Toast.makeText (getApplicationContext (),"你已进入区域",Toast.LENGTH_SHORT).show ();
                Intent intent1 = new Intent(GeoFenceService.this,NotificationService.class);
                startService(intent1);
                Log.e ( "onReceive: ","进入" );
            }
            else {
                Log.e ( "onReceive: ","离开" );
                Toast.makeText (getApplicationContext (),"你已离开区域",Toast.LENGTH_LONG).show ();
            }
        }
    };

    /**
     *显示通知栏
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(Context context,Intent intent){
        NotificationManager manager = (NotificationManager) getSystemService (NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity (context, 0, intent, 0);
        Notification notification;
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder (context, "5996773");
        } else {
            builder = new Notification.Builder (context);
        }
        //设置标题与内容
        builder.setContentTitle ("事务通知").setContentText ("您已进入事务区域!");
        //设置状态栏显示的图标
        builder.setSmallIcon (R.mipmap.ic_launcher);
        // 设置通知灯光（LIGHTS）、铃声（SOUND）、震动（VIBRATE）、（ALL 表示都设置）
        builder.setDefaults (Notification.DEFAULT_ALL);
        //灯光三个参数，颜色（argb）、亮时间（毫秒）、暗时间（毫秒）
        builder.setLights (Color.RED, 200, 200);
        // 震动，传入一个 long 型数组，表示 停、震、停、震 ... （毫秒）
        builder.setVibrate (new long[]{0, 200, 200, 200, 200, 200});
        // 通知栏点击后自动消失
        builder.setAutoCancel (true);
        // 简单通知栏设置 Intent
        builder.setContentIntent (pendingIntent);
        builder.setPriority (Notification.PRIORITY_HIGH);
        //设置下拉之后显示的图片
        builder.setLargeIcon (BitmapFactory.decodeResource (getResources (), R.mipmap.ic_launcher_round));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel ("5996772", "安卓10a", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights (true);//是否在桌面icon右上角展示小红点
            channel.setLightColor (Color.RED);//小红点颜色
            channel.setShowBadge (true); //是否在久按桌面图标时显示此渠道的通知
            manager.createNotificationChannel (channel);
        }
        notification = builder.build ();
        manager.notify (1, notification);

    }
    @Override
    public void onDestroy() {
        super.onDestroy ();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


