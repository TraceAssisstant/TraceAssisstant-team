package com.example.traceassistant.Tools

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context


//默认的自定义Application，可全局获取Context
class GlobalApplication: Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}