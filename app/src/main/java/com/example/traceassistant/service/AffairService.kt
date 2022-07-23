package com.example.traceassistant.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.traceassistant.R
import com.example.traceassistant.ui.main.MainView

class AffairService : Service() {

    override fun onCreate() {
        super.onCreate()
        val notificationIntent = Intent(this, AffairNotification::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //定时通知测试，即在服务开启后4秒左右推送通知
        doNotificate("拿快递","去江苏大学新一区快递超市拿快递",notificationIntent,System.currentTimeMillis()+4000)


        //开启前台服务
        Log.d("MyService", "onCreate executed")
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("my_service", "前台Service通知",
                NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val intent = Intent(this, MainView::class.java)

        //回到生成服务的页面
        val pi = PendingIntent.getActivity(this, 0, intent, 0)

        //前台状态栏信息
        val notification = NotificationCompat.Builder(this, "my_service")
            .setContentTitle("事务助手")
            .setContentText("事务助手正在运行")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setContentIntent(pi)
            .build()
        startForeground(1, notification)


    }

    /**
     * 执行信息通知的方法
     */
    fun doNotificate(title:String ,content:String,intent: Intent,time:Long){
        intent.putExtra("title",title)
        intent.putExtra("contentText",content)
        //闹钟开启
        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            time,
            PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        )

    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("MyService", "onStartCommand executed")
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyService", "onDestroy executed")
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

}