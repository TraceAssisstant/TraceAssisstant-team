package com.example.traceassistant.ui.habit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.traceassistant.R
import com.example.traceassistant.Tools.Navigation
import com.example.traceassistant.databinding.ActivityHabitViewBinding

class HabitView : AppCompatActivity() {

    lateinit var binding: ActivityHabitViewBinding
    val viewModel by lazy { ViewModelProvider(this).get(HabitViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHabitViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * 导航栏
         */
        Navigation.initialize(R.id.habitPage,this,binding.bottomNavigation)

        binding.start.setOnClickListener {
            viewModel.start()
        }
        binding.pause.setOnClickListener {
            viewModel.pause()
        }
        binding.stop.setOnClickListener {
            viewModel.stop()
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
    }
}