package com.example.traceassistant.ui.DatePicker

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.traceassistant.R
import java.util.*

class HorizonSliableDatepicker : ConstraintLayout {

    private var viewPager2: ViewPager2
    private var midCalendar: Calendar = Calendar.getInstance()
    private val MILLIS_24H: Long = 86400000

    private lateinit var activity: AppCompatActivity

    private lateinit var fragments: MutableList<DateFragment>

    constructor(context: Context, attrs: AttributeSet?) : super(context,attrs){
        val view = LayoutInflater.from(context).inflate(R.layout.horizon_slidable_datepicker,this)
        viewPager2 = view.findViewById(R.id.viewPager2)
    }

    /**
     * 初始化datePicker
     */
    public fun initialize(activity: AppCompatActivity){
        this.activity = activity

        setDate(System.currentTimeMillis())
    }

    /**
     * @param dateMillis 目标日期
     * 跳转到目标日期所在周
     */
    public fun setDate(dateMillis: Long){
        midCalendar.timeInMillis = dateMillis
        midCalendar.timeInMillis = midCalendar.timeInMillis - (midCalendar.get(Calendar.DAY_OF_WEEK)-1)*MILLIS_24H
        fragments = mutableListOf(DateFragment(midCalendar))

        for (i in 1..100){
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = midCalendar.timeInMillis - 7*i*MILLIS_24H
            fragments.add(0, DateFragment(calendar))
        }
        for (i in 1..100){
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = midCalendar.timeInMillis + 7*i*MILLIS_24H
            fragments.add( DateFragment(calendar))
        }

        if (this::activity.isInitialized){
            val adapter = MyFragmentAdapter(activity,fragments)
            viewPager2.adapter = adapter
            viewPager2.setCurrentItem(100,false)
        }

        val clickCalendar = Calendar.getInstance()
        clickCalendar.timeInMillis = dateMillis
        fragments[100].clickDate(clickCalendar)
    }

    /**
     * @param operation 自定义监听器
     * 设置选中日期时的监听器回调
     */
    public fun setOnDateClickListener(operation: (date:Long)->Unit){
        for (i in fragments){
            i.setOperation(operation)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        return super.onInterceptTouchEvent(ev)
    }
}