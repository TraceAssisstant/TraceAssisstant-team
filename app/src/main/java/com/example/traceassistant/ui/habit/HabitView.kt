package com.example.traceassistant.ui.habit

import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.traceassistant.R
import com.example.traceassistant.Tools.Navigation
import com.example.traceassistant.databinding.ActivityHabitViewBinding
import com.example.traceassistant.logic.Repository

class HabitView : AppCompatActivity() {

    lateinit var binding: ActivityHabitViewBinding
    val viewModel by lazy { ViewModelProvider(this).get(HabitViewModel::class.java) }
    var btnNum=0//点击开始/暂停按钮的次数，奇数次开始计时，偶数次暂停计时

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHabitViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * 导航栏
         */
        Navigation.initialize(R.id.habitPage,this,binding.bottomNavigation)

        binding.start.setOnClickListener {
            btnNum++
            when{
                btnNum==1->{
                    viewModel.startTime()
                    viewModel.start()
                    binding.start.setImageResource(R.drawable.ic_baseline_pause_24)
                }//第一次开始计时时存入开始时间
                btnNum%2==1->{
                    viewModel.start()
                    viewModel.latterPause()
                    binding.start.setImageResource(R.drawable.ic_baseline_pause_24)
                }
                btnNum%2==0->{
                    viewModel.pause()
                    viewModel.formerPause()
                    binding.start.setImageResource(R.drawable.ic_baseline_arrow_right_24)
                }
            }
        }
        binding.stop.setOnClickListener {
            viewModel.stop()
            btnNum=0
        }

        viewModel.hour.observe(this, Observer { h ->
            val hourStr = String.format("%02d",h)
            binding.hours.text = hourStr
        })
        viewModel.minute.observe(this, Observer { m ->
            val minStr = String.format("%02d",m)
            binding.minute.text = minStr
        })
        viewModel.second.observe(this, Observer { s ->
            val secStr = String.format("%02d",s)
            binding.second.text = secStr
        })

        binding.toDrawingView.setOnClickListener{
            val intent=Intent("DrawingView")
            startActivity(intent)
        }
    }
}