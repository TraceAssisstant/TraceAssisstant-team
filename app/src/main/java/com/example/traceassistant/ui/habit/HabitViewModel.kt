package com.example.traceassistant.ui.habit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class HabitViewModel: ViewModel() {
    val hour = MutableLiveData<Int>()
    val minute = MutableLiveData<Int>()
    val second = MutableLiveData<Int>()

    private var secondTime: Int = 0
    private var minuteTime: Int = 0
    private var hourTime: Int = 0

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    /**
     * 初始化
     */
    fun initial(){
        hour.postValue(hourTime)
        minute.postValue(minuteTime)
        second.postValue(secondTime)
    }

    /**
     * 开启计时
     */
    fun start(){
        initial()
        if (timer != null){
            timer?.cancel()
            timer = null
        }
        if (timerTask != null){
            timerTask = null
        }

        timer = Timer()
        timerTask = object : TimerTask(){
            override fun run() {
                if(second.value == null){
                    return
                }
                secondTime = second.value!!
                minuteTime = minute.value!!
                hourTime = hour.value!!

                secondTime += 1
                if (secondTime >=60){
                    secondTime = 0
                    minuteTime += 1
                    if (minuteTime >= 60){
                        minuteTime = 0
                        hourTime += 1
                    }
                }

                second.postValue(secondTime)
                minute.postValue(minuteTime)
                hour.postValue(hourTime)

//                Log.d("timer","${hour.value}:${minute.value}:${second.value}")

            }
        }

        timer!!.schedule(timerTask,0,1000)
    }

    /**
     * 暂停计时
     */
    fun pause(){
        timer?.cancel()
//        Log.d("timer","cancel: ${hour.value}:${minute.value}:${second.value}")
    }

    /**
     * 停止计时
     */
    fun stop(){
        timer?.cancel()
        secondTime = 0
        minuteTime = 0
        hourTime = 0
        initial()
//        Log.d("timer","stop: ${hour.value}:${minute.value}:${second.value}")
    }
}