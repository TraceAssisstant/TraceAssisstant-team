package com.example.traceassistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.traceassistant.Tools.locationPermission
import com.example.traceassistant.databinding.ActivityMainBinding
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Entity.Habit
import com.example.traceassistant.logic.Entity.SignNature
import com.example.traceassistant.logic.Repository
import kotlin.concurrent.thread
import kotlin.math.sign


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Repository.initHabitDao()
        var habit1:Habit = Habit("背单词",1659872212,1659874000,400,"2022-8-7")
        var habit2:Habit = Habit("敲代码",1659877212,1659879000,300,"2022-8-7")
        var habit3:Habit = Habit("看书",1659872212,1659875000,1000,"2022-8-6")
        var habit4:Habit = Habit("看书2",1629872212,1629875000,2000,"2022-1-6")
        var habit5:Habit = Habit("看书3",1629872212,1629875000,2000,"2021-1-6")

//        Repository.habitInsert(habit1)
//        Repository.habitInsert(habit2)
//        Repository.habitInsert(habit3)
//        Repository.habitInsert(habit4)
//        Repository.habitInsert(habit5)

        val(focusTime,pauseTime) = Repository.habitQueryByYear("2022")

        Log.d("focusTime",focusTime.toString())
        Log.d("pauseTime",pauseTime.toString())


        var habits: List<Habit> = Repository.habitQuery()
        for(habit in habits){
            Log.d("habit",habit.toString())
        }


    }
}