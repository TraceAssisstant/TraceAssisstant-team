package com.example.traceassistant.ui.habit

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.traceassistant.R
import kotlinx.android.synthetic.main.fragment_habit.*

class HabitFragment : Fragment() {

    val viewModel by lazy { ViewModelProvider(this).get(HabitViewModel::class.java) }
    var btnNum=0//点击开始/暂停按钮的次数，奇数次开始计时，偶数次暂停计时

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habit, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /**
         * 导航栏
         */

        start.setOnClickListener {
            btnNum++
            when{
                btnNum==1->{
                    viewModel.startTime()
                    viewModel.start()
                    start.setImageResource(R.drawable.kaishi)
                    startimg.setImageResource(R.drawable.wenzi6)
                }//第一次开始计时时存入开始时间
                btnNum%2==1->{
                    viewModel.start()
                    viewModel.latterPause()
                    start.setImageResource(R.drawable.kaishi)
                    startimg.setImageResource(R.drawable.wenzi6)
                }
                btnNum%2==0->{
                    viewModel.pause()
                    viewModel.formerPause()
                    start.setImageResource(R.drawable.zanting)
                    startimg.setImageResource(R.drawable.wenzi10)
                }
            }
        }
       stop.setOnClickListener {
            viewModel.stop()
            btnNum=0
        }

        var hour:String="00"
        var min:String="00"
        var sec:String="0"
        viewModel.hour.observe(viewLifecycleOwner, Observer { h ->
            val hourStr = String.format("%02d",h)
            hours.setText(hourStr)
            if (!hourStr.equals("00")){
                hour=hourStr
            }
        })

        viewModel.minute.observe(viewLifecycleOwner, Observer { m ->
            val minStr = String.format("%02d",m)
            minute.setText(minStr)
            if (!minStr.equals("00")){
                min=minStr
            }
        })

        viewModel.second.observe(viewLifecycleOwner, Observer { s ->
            val secStr = String.format("%02d",s)
            Log.d("second",secStr)
            second.setText(secStr)
            if (!secStr.equals("00")){
                sec=secStr
            }
        })

        toDrawingView.setOnClickListener{
            val intent= Intent(requireContext(),ShowDrawing::class.java)
            intent.putExtra("hour",hour)
            intent.putExtra("minute",min)
            intent.putExtra("second",sec)
            Log.d("hour&minute&second","${hour}时${minute}分${second}秒")
            startActivity(intent)
        }
    }
}