package com.example.traceassistant.ui.habit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.traceassistant.logic.Entity.Habit
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

    private var former:Long=0//按暂停计时时 开始获取的暂停时间
    private var latter:Long=0//按继续时时 结束获取的时间
    private var pauseTime:Long=0

    //需要提供一个空参的默认构造
    private var habit:Habit?=Habit("工作",1,1,1,"1")
    /**
     * 初始化
     */
    fun initial(){
        hour.postValue(hourTime)
        minute.postValue(minuteTime)
        second.postValue(secondTime)
    }
    /**
     * 存入第一次开启计时的时间
     */
    fun startTime(){
        var date:Date= Date()
        Log.d("beginTime","${date.time}")
        //habit!!.beginTime=date.time
    }
    /**
     * 存入计时结束时的时间
     */
    fun stopTime(){
        var date:Date= Date()
        Log.d("endTime","${date.time}")
        //habit!!.endTime=date.time
    }
    /**
     * 获取按下暂停时的时间
     */
    fun formerPause(){
        val date:Date= Date()
        former=date.time
    }
    /**
     * 获取结束暂停的时间，并存入暂停时间
     */
    fun latterPause(){
        val date:Date=Date()
        latter=date.time
        pauseTime+=latter-former
        Log.d("pauseTime","latter:${latter},former:${former},pausetime:${pauseTime}")
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
        stopTime()//停止时存入结束时间

        //如果有按下暂停，但没有按继续计时直接停止计时 计算暂停时间
        if (former!=0L&&former>latter){
            latterPause()
        }
        pauseTime=0
        latter=0
        former=0
//        Log.d("timer","stop: ${hour.value}:${minute.value}:${second.value}")
    }
}