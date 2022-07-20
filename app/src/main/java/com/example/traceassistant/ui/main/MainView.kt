package com.example.traceassistant.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.traceassistant.databinding.ActivityMainViewBinding
import com.example.traceassistant.logic.Entity.SignNature
import com.example.traceassistant.ui.affairsCollection.CollectionView
import java.util.*

class MainView : AppCompatActivity() {

    private lateinit var binding: ActivityMainViewBinding
    val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    lateinit var timeChangeReceiver: TimeChangeReceiver
    var id = 0 //资源id，随时间变化
    var lastId=5//资源数量，后续可扩展


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.loadRepository()//加载图文资源

        val date = Calendar.getInstance()
        id = date[Calendar.DAY_OF_MONTH]%lastId+1
        viewModel.getSN(id)

        //观察id变化，更换图文
        viewModel.mainViewLiveData.observe(this, Observer { result ->
            val signature = result.getOrNull()
            if (signature != null) {
                val (str, id) = signature as SignNature
                binding.daySentence.text = str
                binding.mainImage.setImageResource(id)
            }
        })


        //        以下代码皆仅是测试
        //        向“添加事务”按钮添加跳转到事务收集页面的方法
        binding.addButton.setOnClickListener() {
            val intent = Intent(this, CollectionView::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        //注册监听器，每隔一分钟/一小时 根据日期赋予图文id
        val intentFilter = IntentFilter();
        intentFilter.addAction("android.intent.action.TIME_TICK")//每分钟通知一次，测试用
        //intentFilter.addAction("android.intent.action.DATE_CHANGE")//日期变化通知
        timeChangeReceiver = TimeChangeReceiver()
        registerReceiver(timeChangeReceiver, intentFilter)
    }


    override fun onStop() {
        super.onStop()
        unregisterReceiver(timeChangeReceiver)
    }

    inner class TimeChangeReceiver: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("start","广播开启")
            val c = Calendar.getInstance()
            var mMinute=c[Calendar.MINUTE]//获取当前分钟  测试用
            var mDay=c[Calendar.DAY_OF_MONTH] //获取当前日期
            //id从1开始所以+1
            id=mDay%lastId+1
            Log.d("id",id.toString())
            viewModel.getSN(id)
        }

    }
}