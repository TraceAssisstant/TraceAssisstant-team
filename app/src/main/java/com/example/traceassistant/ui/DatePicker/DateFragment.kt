package com.example.traceassistant.ui.DatePicker

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.traceassistant.R
import com.example.traceassistant.logic.Repository
import java.text.SimpleDateFormat
import java.util.*

/**
 * 传入目标周的第一天的calendar实例即可显示目标周的一周日历
 */
class DateFragment(private val calendar: Calendar) : Fragment() {
    private lateinit var sunday: TextView
    private lateinit var monday: TextView
    private lateinit var tuesday: TextView
    private lateinit var wednesday: TextView
    private lateinit var thursday: TextView
    private lateinit var friday: TextView
    private lateinit var saturday: TextView
    private lateinit var sundayPoint: TextView
    private lateinit var mondayPoint: TextView
    private lateinit var tuesdayPoint: TextView
    private lateinit var wednesdayPoint: TextView
    private lateinit var thursdayPoint: TextView
    private lateinit var fridayPoint: TextView
    private lateinit var saturdayPoint: TextView

    private lateinit var todayCalendar: Calendar

    private var clickedDate: Int = -1
    private lateinit var operation: (date: Long) -> Unit
    private var dateWithAffair: Int = -1

    private val thisWeek: MutableList<Long> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_date, container, false)

        sunday = view.findViewById(R.id.sunday)
        monday = view.findViewById(R.id.monday)
        tuesday = view.findViewById(R.id.tuesday)
        wednesday = view.findViewById(R.id.wednesday)
        thursday = view.findViewById(R.id.thursday)
        friday = view.findViewById(R.id.friday)
        saturday = view.findViewById(R.id.saturday)

        sundayPoint = view.findViewById(R.id.sunday_point)
        mondayPoint = view.findViewById(R.id.monday_point)
        tuesdayPoint = view.findViewById(R.id.tuesday_point)
        wednesdayPoint = view.findViewById(R.id.wednesday_point)
        thursdayPoint = view.findViewById(R.id.thursday_point)
        fridayPoint = view.findViewById(R.id.friday_point)
        saturdayPoint = view.findViewById(R.id.saturday_point)

        todayCalendar = Calendar.getInstance()
        todayCalendar.timeInMillis = System.currentTimeMillis()

        setTodayBack()

        setDateForWeek(calendar)

        if (this::operation.isInitialized){
            this.setOnDateClickListener(operation)
        }else{
            this.setOnDateClickListener {  }
        }

        if (clickedDate != -1){
            when(clickedDate){
                0 -> {
                    sunday.setBackgroundResource(R.drawable.bg_bluecircle)
                }
                1 -> {
                    monday.setBackgroundResource(R.drawable.bg_bluecircle)
                }
                2 -> {
                    tuesday.setBackgroundResource(R.drawable.bg_bluecircle)
                }
                3 -> {
                    wednesday.setBackgroundResource(R.drawable.bg_bluecircle)
                }
                4 -> {
                    thursday.setBackgroundResource(R.drawable.bg_bluecircle)
                }
                5 -> {
                    friday.setBackgroundResource(R.drawable.bg_bluecircle)
                }
                6 -> {
                    saturday.setBackgroundResource(R.drawable.bg_bluecircle)
                }
            }
        }

        setAllAffair()

        return view
    }

    fun clickDate(calendar: Calendar){
        clickedDate = calendar.get(Calendar.DAY_OF_WEEK)-1
    }

    fun setOperation(operation: (date:Long)->Unit){
        this.operation = operation
    }

    fun refreshAffair(){
        if (this::sundayPoint.isInitialized &&
            this::mondayPoint.isInitialized &&
            this::tuesdayPoint.isInitialized &&
            this::wednesdayPoint.isInitialized &&
            this::thursdayPoint.isInitialized &&
            this::fridayPoint.isInitialized &&
            this::saturdayPoint.isInitialized){
            sundayPoint.setBackgroundColor(Color.TRANSPARENT)
            mondayPoint.setBackgroundColor(Color.TRANSPARENT)
            tuesdayPoint.setBackgroundColor(Color.TRANSPARENT)
            wednesdayPoint.setBackgroundColor(Color.TRANSPARENT)
            thursdayPoint.setBackgroundColor(Color.TRANSPARENT)
            fridayPoint.setBackgroundColor(Color.TRANSPARENT)
            saturdayPoint.setBackgroundColor(Color.TRANSPARENT)
            setAllAffair()
        }
    }

    private fun setAllAffair(){

        for (i in thisWeek){
            val dateSelected = SimpleDateFormat("yyyy-MM-dd").format(i)
            val listOfAffairForm = Repository.getAffairListByDate(dateSelected)
            if (listOfAffairForm.isNotEmpty()){
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = i
                setDateWithAffair(calendar)
            }
        }
    }

    private fun setDateWithAffair(calendar: Calendar){
        this.dateWithAffair = calendar.get(Calendar.DAY_OF_WEEK)-1
        if (dateWithAffair != -1){
            when(dateWithAffair){
                0 -> {
                    sundayPoint.setBackgroundResource(R.drawable.bg_bluecircle)
                }
                1 -> {
                    mondayPoint.setBackgroundResource(R.drawable.bg_bluecircle)
                }
                2 -> {
                    tuesdayPoint.setBackgroundResource(R.drawable.bg_bluecircle)
                }
                3 -> {
                    wednesdayPoint.setBackgroundResource(R.drawable.bg_bluecircle)
                }
                4 -> {
                    thursdayPoint.setBackgroundResource(R.drawable.bg_bluecircle)
                }
                5 -> {
                    fridayPoint.setBackgroundResource(R.drawable.bg_bluecircle)
                }
                6 -> {
                    saturdayPoint.setBackgroundResource(R.drawable.bg_bluecircle)
                }
            }
        }
    }

    private fun setOnDateClickListener(operation: (date:Long)->Unit){
        sunday.setOnClickListener {
            monday.setBackgroundColor(Color.TRANSPARENT)
            tuesday.setBackgroundColor(Color.TRANSPARENT)
            wednesday.setBackgroundColor(Color.TRANSPARENT)
            thursday.setBackgroundColor(Color.TRANSPARENT)
            friday.setBackgroundColor(Color.TRANSPARENT)
            saturday.setBackgroundColor(Color.TRANSPARENT)
            setTodayBack()
            sunday.setBackgroundResource(R.drawable.bg_bluecircle)
            operation(thisWeek[0])
        }
        monday.setOnClickListener {
            sunday.setBackgroundColor(Color.TRANSPARENT)
            tuesday.setBackgroundColor(Color.TRANSPARENT)
            wednesday.setBackgroundColor(Color.TRANSPARENT)
            thursday.setBackgroundColor(Color.TRANSPARENT)
            friday.setBackgroundColor(Color.TRANSPARENT)
            saturday.setBackgroundColor(Color.TRANSPARENT)
            setTodayBack()
            monday.setBackgroundResource(R.drawable.bg_bluecircle)
            operation(thisWeek[1])
        }
        tuesday.setOnClickListener {
            monday.setBackgroundColor(Color.TRANSPARENT)
            sunday.setBackgroundColor(Color.TRANSPARENT)
            wednesday.setBackgroundColor(Color.TRANSPARENT)
            thursday.setBackgroundColor(Color.TRANSPARENT)
            friday.setBackgroundColor(Color.TRANSPARENT)
            saturday.setBackgroundColor(Color.TRANSPARENT)
            setTodayBack()
            tuesday.setBackgroundResource(R.drawable.bg_bluecircle)
            operation(thisWeek[2])
        }
        wednesday.setOnClickListener {
            monday.setBackgroundColor(Color.TRANSPARENT)
            tuesday.setBackgroundColor(Color.TRANSPARENT)
            sunday.setBackgroundColor(Color.TRANSPARENT)
            thursday.setBackgroundColor(Color.TRANSPARENT)
            friday.setBackgroundColor(Color.TRANSPARENT)
            saturday.setBackgroundColor(Color.TRANSPARENT)
            setTodayBack()
            wednesday.setBackgroundResource(R.drawable.bg_bluecircle)
            operation(thisWeek[3])
        }
        thursday.setOnClickListener {
            monday.setBackgroundColor(Color.TRANSPARENT)
            tuesday.setBackgroundColor(Color.TRANSPARENT)
            wednesday.setBackgroundColor(Color.TRANSPARENT)
            sunday.setBackgroundColor(Color.TRANSPARENT)
            friday.setBackgroundColor(Color.TRANSPARENT)
            saturday.setBackgroundColor(Color.TRANSPARENT)
            setTodayBack()
            thursday.setBackgroundResource(R.drawable.bg_bluecircle)
            operation(thisWeek[4])
        }
        friday.setOnClickListener {
            monday.setBackgroundColor(Color.TRANSPARENT)
            tuesday.setBackgroundColor(Color.TRANSPARENT)
            wednesday.setBackgroundColor(Color.TRANSPARENT)
            thursday.setBackgroundColor(Color.TRANSPARENT)
            sunday.setBackgroundColor(Color.TRANSPARENT)
            saturday.setBackgroundColor(Color.TRANSPARENT)
            setTodayBack()
            friday.setBackgroundResource(R.drawable.bg_bluecircle)
            operation(thisWeek[5])
        }
        saturday.setOnClickListener {
            monday.setBackgroundColor(Color.TRANSPARENT)
            tuesday.setBackgroundColor(Color.TRANSPARENT)
            wednesday.setBackgroundColor(Color.TRANSPARENT)
            thursday.setBackgroundColor(Color.TRANSPARENT)
            friday.setBackgroundColor(Color.TRANSPARENT)
            sunday.setBackgroundColor(Color.TRANSPARENT)
            setTodayBack()
            saturday.setBackgroundResource(R.drawable.bg_bluecircle)
            operation(thisWeek[6])
        }
    }


    private fun setDateForWeek(calendar: Calendar){
        val calendar_ = Calendar.getInstance()
        calendar_.timeInMillis = calendar.timeInMillis
        for (i in 1..7){
            setDate(calendar_)
            thisWeek.add(calendar_.timeInMillis)
            calendar_.timeInMillis += 86400000
        }
    }

    private fun setDate(calendar: Calendar){
        when(calendar.get(Calendar.DAY_OF_WEEK)-1){
            0 -> {
                sunday.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            }
            1 -> {
                monday.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            }
            2 -> {
                tuesday.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            }
            3 -> {
                wednesday.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            }
            4 -> {
                thursday.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            }
            5 -> {
                friday.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            }
            6 -> {
                saturday.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            }
        }
    }

    private fun setTodayBack(){
        todayCalendar = Calendar.getInstance()
        todayCalendar.timeInMillis = System.currentTimeMillis()

        val today = todayCalendar.get(Calendar.DAY_OF_YEAR)
        val minus = today - calendar.get(Calendar.DAY_OF_YEAR)

        if (minus in 0..6){
            when(todayCalendar.get(Calendar.DAY_OF_WEEK)-1){
                0 -> {
                    sunday.setBackgroundResource(R.drawable.bg_lightbluecircle)
                }
                1 -> {
                    monday.setBackgroundResource(R.drawable.bg_lightbluecircle)
                }
                2 -> {
                    tuesday.setBackgroundResource(R.drawable.bg_lightbluecircle)
                }
                3 -> {
                    wednesday.setBackgroundResource(R.drawable.bg_lightbluecircle)
                }
                4 -> {
                    thursday.setBackgroundResource(R.drawable.bg_lightbluecircle)
                }
                5 -> {
                    friday.setBackgroundResource(R.drawable.bg_lightbluecircle)
                }
                6 -> {
                    saturday.setBackgroundResource(R.drawable.bg_lightbluecircle)
                }
            }
        }

    }
}