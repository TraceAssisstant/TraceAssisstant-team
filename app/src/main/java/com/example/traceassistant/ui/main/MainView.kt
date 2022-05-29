package com.example.traceassistant.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.*
import com.example.traceassistant.R
import com.example.traceassistant.databinding.ActivityMainViewBinding
import com.example.traceassistant.logic.Entity.SignNature
import com.example.traceassistant.ui.affairsCollection.CollectionView
import com.example.traceassistant.ui.affairsCollection.addElementsView

class MainView : AppCompatActivity() {

    private lateinit var binding: ActivityMainViewBinding
    lateinit var timeChangeReceiver: TimeChangeReceiver
    val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewBinding.inflate(layoutInflater)

        setContentView(binding.root)

//        @cx330测试
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.TIME_TICK")
        timeChangeReceiver = TimeChangeReceiver()
        registerReceiver(timeChangeReceiver, intentFilter)


//        以下代码皆仅是测试
//        向“添加事务”按钮添加跳转到事务收集页面的方法
        binding.addButton.setOnClickListener() {
            val intent = Intent(this, CollectionView::class.java)
            startActivity(intent)
        }
    }

    //@cx330测试
//    注销广播
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(timeChangeReceiver)
    }

    inner class TimeChangeReceiver : BroadcastReceiver(), LifecycleOwner {
        private lateinit var lifecycleRegistry: LifecycleRegistry
        override fun onReceive(p0: Context?, p1: Intent?) {
            Toast.makeText(p0, "kkk", Toast.LENGTH_SHORT).show()
//            使该类成为 LifecycleOwner类
            lifecycleRegistry = LifecycleRegistry(this)
            lifecycleRegistry.markState(Lifecycle.State.CREATED)
            val id = (1..4).random()
            viewModel.showSN(id)
            viewModel.mainViewLiveData.observe(this, Observer { result ->
                val sn = result.getOrNull() as SignNature
                if (sn != null) {
                    val (str, id) = sn
                    binding.mainImage.setImageResource(id)
                    binding.daySentence.text = str
                }
            })
        }


        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }


    }
}