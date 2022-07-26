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
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Repository
import com.example.traceassistant.ui.main.MainView

class AffairService : Service() {

    override fun onCreate() {
        super.onCreate()
        val notificationIntent = Intent(this, NotificationService::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //初始化数据库并获取事务信息
        Repository.initAFDao()
        var list :List<AffairForm> = Repository.getAffairList();

        //将事务添加到通知队列
        for( af in  list){
            doNotificate(af.ttitle,af.mainContent,notificationIntent,af.time)
        }

        //开启前台服务
        Log.d("MyService", "onCreate executed")
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


    }

    /**
     * 执行定时信息通知的方法
     */
    fun doNotificate(title:String ,content:String,intent: Intent,time:Long){
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