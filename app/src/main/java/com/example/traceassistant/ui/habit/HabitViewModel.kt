package com.example.traceassistant.ui.habit

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.traceassistant.logic.Entity.Habit
import com.example.traceassistant.logic.Repository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

    private var beginTime:Long=0
    private var endTime:Long=0
    private var pauseTime:Long=0
    private var date:String="404"
    private var title:String="工作"
    private var former:Long=0//按暂停计时时 开始获取的暂停时间
    private var latter:Long=0//按继续时时 结束获取的时间

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
    @RequiresApi(Build.VERSION_CODES.O)
    fun startTime(){
        var date1:Date= Date()
        beginTime=date1.time
        val myDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        date=myDateTimeFormatter.format(LocalDateTime.now())
        Log.d("beginTime,date","begintime:${date1.time},date:${date}")
    }
    /**
     * 存入计时结束时的时间
     */
    fun stopTime(){
        var date:Date= Date()
        endTime=date.time
        Log.d("endTime","${date.time}")
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

                Log.d("timer","${hour.value}:${minute.value}:${second.value}")

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
    @RequiresApi(Build.VERSION_CODES.O)
    fun stop(){
        timer?.cancel()
        //如果有按下暂停，但没有按继续计时直接停止计时 计算暂停时间
        if (former!=0L&&former>latter){
            latterPause()
        }
        stopTime()//停止时存入结束时间
        //插入habit记录
        Repository.initHabitDao()
        val habit:Habit= Habit(title, beginTime, endTime, pauseTime, date)
        Repository.habitInsert(habit)
        //测试
        val strTest=Repository.habitQuery().joinToString(",")
        Log.d("habitList","${strTest}")
        val myDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        Log.d("日期","${myDateTimeFormatter.format(LocalDateTime.now())}")
        Log.d("当日专注/暂停时间","${Repository.habitQueryByDate(myDateTimeFormatter.format(LocalDateTime.now()).toString())}")
        secondTime = 0
        minuteTime = 0
        hourTime = 0
        initial()
        pauseTime=0
        latter=0
        former=0
//        Log.d("timer","stop: ${hour.value}:${minute.value}:${second.value}")
    }
}