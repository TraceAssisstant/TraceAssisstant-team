package com.example.traceassistant.ui.habit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.traceassistant.R
import com.example.traceassistant.databinding.ActivityShowDrawingBinding

class ShowDrawing : AppCompatActivity() {

    lateinit var binding:ActivityShowDrawingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_drawing)
        binding= ActivityShowDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //viewPager2
        val fragments = listOf(DailyChartFragment(),MonthChartFragment())
        val viewpager2Adapter = MyFragmentAdapter(this,fragments)
        binding.viewPager2.adapter = viewpager2Adapter
    }
}