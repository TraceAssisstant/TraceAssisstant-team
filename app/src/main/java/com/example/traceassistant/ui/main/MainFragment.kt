package com.example.traceassistant.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.traceassistant.Tools.GlobalApplication
import com.example.traceassistant.databinding.FragmentMainBinding
import com.example.traceassistant.logic.Entity.SignNature
import com.example.traceassistant.service.GeoFenceService
import java.util.*

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    lateinit var timeChangeReceiver: TimeChangeReceiver
    var rsid = 0 //资源id，随时间变化
    var lastId=5//资源数量，后续可扩展

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)

        /**
         * 日期
         */
        binding
        val dateToday = Calendar.getInstance()
        binding.dayToday.text = dateToday[Calendar.DAY_OF_MONTH].toString()
        binding.monthToday.text = "${dateToday[Calendar.YEAR].toString()}年 ${dateToday[Calendar.MONTH]+1}月"

        viewModel.loadRepository()//加载图文资源

        val date = Calendar.getInstance()
        rsid = date[Calendar.DAY_OF_MONTH]%lastId+1
        viewModel.getSN(rsid)

        //观察rsid变化，更换图文
        viewModel.mainViewLiveData.observe(viewLifecycleOwner,Observer{result ->
            val signature = result.getOrNull()
            if (signature != null) {
                val (str, imageid) = signature as SignNature
                binding.daySentence.text = str
                binding.mainImage.setImageResource(imageid)
            }
        })

        binding.buttonTest.setOnClickListener {
            /**
             * 开启后台地理围栏&事务提醒服务
             */
            var intent : Intent = Intent()
            intent.setClass(GlobalApplication.context, GeoFenceService::class.java)
            activity?.startService(intent)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //注册监听器，每隔一分钟/一小时 根据日期赋予图文id
        val intentFilter = IntentFilter();
        intentFilter.addAction("android.intent.action.TIME_TICK")//每分钟通知一次，测试用
        //intentFilter.addAction("android.intent.action.DATE_CHANGE")//日期变化通知
        timeChangeReceiver = TimeChangeReceiver()
        activity?.applicationContext?.registerReceiver(timeChangeReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.applicationContext?.unregisterReceiver(timeChangeReceiver)
    }


    inner class TimeChangeReceiver: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("start","广播开启")
            val c = Calendar.getInstance()
            var mMinute=c[Calendar.MINUTE]//获取当前分钟  测试用
            var mDay=c[Calendar.DAY_OF_MONTH] //获取当前日期
            //id从1开始所以+1
            rsid=mDay%lastId+1
            Log.d("id",rsid.toString())
            viewModel.getSN(rsid)
        }

    }
}