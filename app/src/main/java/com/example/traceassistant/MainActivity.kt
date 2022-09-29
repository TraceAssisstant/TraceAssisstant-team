package com.example.traceassistant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.amap.api.fence.GeoFenceClient
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClientOption
import com.example.traceassistant.Tools.*
import com.example.traceassistant.databinding.ActivityMainBinding
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Entity.Habit
import com.example.traceassistant.logic.Entity.SignNature
import com.example.traceassistant.logic.Repository
import com.example.traceassistant.service.AffairService

import com.example.traceassistant.service.GeoFenceService
import com.example.traceassistant.service.NotificationService

import kotlin.concurrent.thread
import kotlin.math.sign


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Repository.initHabitDao()
//        var habit1:Habit = Habit("背单词",1659872212,1659874000,400,"2022-08-07")
//        var habit2:Habit = Habit("敲代码",1659877212,1659879000,300,"2022-08-07")
//        var habit3:Habit = Habit("看书",1659872212,1659875000,1000,"2022-08-06")
//        var habit4:Habit = Habit("看书2",1629872212,1629875000,2000,"2022-01-06")
//        var habit5:Habit = Habit("看书3",1629872212,1629875000,2000,"2021-01-06")
//
////        Repository.habitInsert(habit1)
////        Repository.habitInsert(habit2)
////        Repository.habitInsert(habit3)
////        Repository.habitInsert(habit4)
////        Repository.habitInsert(habit5)
//
//
//        var habits: List<Habit> = Repository.habitQuery()
//        for(habit in habits){
//            Log.d("habit",habit.toString())
//        }
//
//        Repository.focusArrayQuery("2022-08")
//        Repository.pauseArrayQuery("2022-08")
//        Repository.initAFDao()
//        Repository.deleteAffairAll()
//        var affairForm1:AffairForm = AffairForm("拿快递","去新一区快递超市拿快递",798798794,"2022-8-29",
//            -112.0004,41.222825,10000.0,1,"取快递",true,true,1)
//        var affairForm2:AffairForm = AffairForm("交材料","去计算机学院交材料",798798123,"2022-8-30",
//            -112.0003,41.222824,10000.0,1,"交材料",true,true,1)
//        Repository.insertAffiar(affairForm1)
//        Repository.insertAffiar(affairForm2)


    }

    override fun onStart() {
        super.onStart()
        println("test???"+ toStamp("2022-9-29 21:14:00"))

    }
}