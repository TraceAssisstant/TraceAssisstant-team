package com.example.traceassistant.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.amap.api.location.AMapLocation
import com.example.traceassistant.R
import com.example.traceassistant.Tools.DistanceConversion
import com.example.traceassistant.Tools.LocalNowLocation
import kotlin.concurrent.thread

class NotificationService : Service() {

    //赋默认值
    var title: String = "未获取到事务标题"
    var contentText: String = "未获取到事务详细信息"

    var longitudeA:Double = 0.0;
    var latitudeA:Double=0.0;    //事务地点坐标

    var longitudeB:Double = 0.0;
    var latitudeB:Double=0.0;     //用户当前坐标

    var range:Double=0.0;     //用户当前坐标

    var notificationCode:Int = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //获取消息内容参数
        if (intent != null) {
            title = intent.getStringExtra("title").toString()
            contentText = intent.getStringExtra("contentText").toString()
            longitudeA = intent.getDoubleExtra("longitude",0.0)
            latitudeA = intent.getDoubleExtra("latitude",0.0)
            range = intent.getDoubleExtra("range",0.0)
        }
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

        //版本控制
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("important", "important", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }

        //此处为点击消息后进入的具体页面
        val intent = Intent(this, AffairNotification::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, 0)
        val notification = NotificationCompat.Builder(this, "important")
            .setContentTitle(title)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_launcher_foreground
                )
            )
            .setAutoCancel(true)
            .setContentIntent(pi)
            .build()

        thread {
            var amp : AMapLocation?= LocalNowLocation.getLocation()
            for(i in 1..200){
                Thread.sleep(10*1000)
                if (amp != null) {
                    longitudeB = amp.longitude
                    latitudeB = amp.latitude
                    if(DistanceConversion.getDistance1(longitudeA,latitudeA,longitudeB,latitudeB)<range)
                        break
                }
            }

            manager.notify(notificationCode, notification)
        }



        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}