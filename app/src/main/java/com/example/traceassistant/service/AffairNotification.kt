package com.example.traceassistant.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat
import com.example.traceassistant.R
import com.example.traceassistant.ui.main.MainView

class AffairNotification : AppCompatActivity() {

    //赋默认值
    var title: String = "未获取到事务标题"
    var contentText: String = "未获取到事务详细信息"

    var notificationCode:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_affair_notification)
    }

    override fun onStart() {
        super.onStart()
        var buttonOK: Button = findViewById(R.id.buttonOK)
        var buttonRE: Button = findViewById(R.id.buttonRe)

        //返回主页面
        buttonOK.setOnClickListener {
            var intent = Intent(this,MainView::class.java)
            startActivity(intent)
        }
        finish()

    }
}