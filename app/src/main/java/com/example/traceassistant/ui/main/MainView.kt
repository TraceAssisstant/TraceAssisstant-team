package com.example.traceassistant.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.traceassistant.R
import com.example.traceassistant.databinding.ActivityMainViewBinding
import com.example.traceassistant.logic.Entity.SignNature
import com.example.traceassistant.ui.affairsCollection.CollectionView
import java.util.*
import kotlin.concurrent.timer


class MainView : AppCompatActivity() {

    private lateinit var binding: ActivityMainViewBinding
    val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    val timer = Timer(true)
    var id = 0
    var lastId=4
    val date = Date(System.currentTimeMillis())
    val timerTask: TimerTask = object : TimerTask() {
        override fun run() {
            if (id<lastId){
                id++
                viewModel.getSN(id)
                Log.d("idtime",id.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.loadRepository()

        viewModel.mainViewLiveData.observe(this, Observer { result ->
            val signature = result.getOrNull()
            if (signature != null) {
                val (str, id) = signature as SignNature
                binding.daySentence.text = str
                binding.mainImage.setImageResource(id)

            }
        }
        )


        //        以下代码皆仅是测试
        //        向“添加事务”按钮添加跳转到事务收集页面的方法
        binding.addButton.setOnClickListener() {
            val intent = Intent(this, CollectionView::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        timer.schedule(timerTask, date, 1000)
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }
}