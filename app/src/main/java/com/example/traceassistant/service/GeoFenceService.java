package com.example.traceassistant.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
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
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Database;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.DPoint;
import com.example.traceassistant.R;
import com.example.traceassistant.Tools.LocalNowLocation;
import com.example.traceassistant.Tools.SerialData;
import com.example.traceassistant.logic.Dao.AffairFormDao;
import com.example.traceassistant.logic.Dao.AffairFormDao_Impl;
import com.example.traceassistant.logic.Entity.AffairForm;
import com.example.traceassistant.logic.Repository;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class GeoFenceService extends Service {
    DPoint centerPoint;
    //定义接收广播的action字符串
    Handler handler;
    int loadAffairList = 1;
    public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";
    public int flags = 1;
    @Override
    public void onCreate() {

        super.onCreate ();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Repository.INSTANCE.initAFDao();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        //实例化地理围栏客户端
        GeoFenceClient mGeoFenceClient = new GeoFenceClient (getApplicationContext ());
        mGeoFenceClient.setActivateAction (GeoFenceClient.GEOFENCE_IN | GeoFenceClient.GEOFENCE_OUT);

        //数据更新类
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==loadAffairList){
                    Bundle bundle = msg.getData();
                    SerialData serialData2 = (SerialData)bundle.getSerializable("affairList");
                    List<AffairForm> affairForms =(List<AffairForm>)serialData2.getData();
                    System.out.println("地理服务列表：");
                    for (AffairForm affair : affairForms
                         ) {
                        System.out.println(affair.toString());
                    }
                    for (AffairForm affairForm: affairForms) {
                        centerPoint = new DPoint();
                        centerPoint.setLatitude(affairForm.getLatitude());
                        centerPoint.setLongitude(affairForm.getLongitude());
                        mGeoFenceClient.addGeoFence (centerPoint, 1000, affairForm.getTtitle());
                    }
                }


                GeoFenceListener geoFenceListener = new GeoFenceListener () {
                    @Override
                    public void onGeoFenceCreateFinished(List<GeoFence> list, int i, String s) {
                        if (i == GeoFence.ADDGEOFENCE_SUCCESS) {
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

                super.handleMessage(msg);
            }
        };

        //数据库查询线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<AffairForm> affairList = Repository.INSTANCE.getAffairListByDate(formatter.format(date));
                Bundle bundle = new Bundle();
                SerialData serialData = new SerialData();
                serialData.setData(affairList);
                bundle.putSerializable("affairList",serialData);
                Message message = new Message();
                message.what = 1;
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }).start();


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
                Intent intent1 = new Intent(GeoFenceService.this,NotificationService.class);
                String customId = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                intent1.putExtra("title",customId);
                intent1.putExtra("content","您进入该事务区域："+customId);
                intent1.putExtra("notificationCode",flags++);
                startService(intent1);
                Log.e ( "onReceive: ","进入" );
            }
            else {
                Log.e ( "onReceive: ","离开" );
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy ();
        System.out.println("地理服务停止");
        //停止地理服务
        LocalNowLocation.INSTANCE.stopLocation();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


