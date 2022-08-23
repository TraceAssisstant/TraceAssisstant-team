package com.example.traceassistant.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.amap.api.fence.GeoFenceClient
import com.amap.api.fence.GeoFenceClient.GEOFENCE_IN
import com.example.traceassistant.R
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Repository
import com.example.traceassistant.ui.main.MainView

class AffairService : Service() {

    override fun onCreate() {
        super.onCreate()

        //创建通知service实例
        val notificationIntent = Intent(this, NotificationService::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        doNotificate("欢迎来到智醒事务助手","您的事务已经安排妥当！",notificationIntent,System.currentTimeMillis()+1000)

        //初始化数据库并获取事务信息
        Repository.initAFDao()
        var list :List<AffairForm> = Repository.getAffairList();


        try{
            for( af in  list){
                Log.d("事务已添加到队列！", af.toString())
                doNotificate(af.ttitle,af.mainContent,notificationIntent,af.time)
            }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as
                   NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel("AffairService", "前台Service通知",
                    NotificationManager.IMPORTANCE_DEFAULT)
                manager.createNotificationChannel(channel)
            }
            val intent = Intent(this, MainView::class.java)

            //回到生成服务的页面
            val pi = PendingIntent.getActivity(this, 0, intent, 0)

            //前台状态栏信息
            val notification = NotificationCompat.Builder(this, "AffairService")
               .setContentTitle("智醒事务助手")
               .setContentText("事务助手正在运行...")
               .setSmallIcon(R.mipmap.ic_launcher)
               .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
               .setContentIntent(pi)
               .build()
            startForeground(1, notification)
        }catch (e:Exception){
           e.printStackTrace()
        }



    }

    /**
     * 执行定时信息通知的方法
     */
    fun doNotificate(title:String,content:String, intent: Intent,time:Long){
        intent.putExtra("title",title)
        intent.putExtra("contentText",content)
        //闹钟开启
        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            time,
            PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyService", "onDestroy executed")
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

}