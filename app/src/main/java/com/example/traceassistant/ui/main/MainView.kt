package com.example.traceassistant.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.traceassistant.R
import com.example.traceassistant.databinding.ActivityMainViewBinding
import com.example.traceassistant.logic.Entity.SignNature
import com.example.traceassistant.logic.Repository
import com.example.traceassistant.ui.affairsCollection.CollectionView
import com.example.traceassistant.ui.affairsCollection.addElementsView

class MainView : AppCompatActivity() {

    private lateinit var binding: ActivityMainViewBinding
    val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewBinding.inflate(layoutInflater)

        setContentView(binding.root)

//        @cx330测试
        viewModel.loadRepository()


//        测试代码@cx330
//        点击图片切换背景，签名
        var id=0
        binding.mainImage.setOnClickListener {
//            实现任意切换
//            val id = (1..4).random()

//            实现递增切换
            if (id<5){
                id++
                viewModel.showSN(id)

            }
        }


        viewModel.mainViewLiveData.observe(this, Observer { result->
            val signature = result.getOrNull()
            if (signature!=null){
                val (str,id) = signature as SignNature
                binding.daySentence.text = str
                binding.mainImage.setImageResource(id)
            }else{
                Toast.makeText(this, "tt", Toast.LENGTH_SHORT).show()
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

}