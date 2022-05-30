package com.example.traceassistant.ui.main

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.traceassistant.R
import com.example.traceassistant.databinding.ActivityMainViewBinding
import com.example.traceassistant.logic.Entity.SignNature
import com.example.traceassistant.ui.affairsCollection.CollectionView


class MainView : AppCompatActivity() {

    private lateinit var binding: ActivityMainViewBinding
    val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun onResume() {
        var lastId = viewModel.loadRepository()
        super.onResume()
        var id = 0
//            实现任意切换
//            val id = (1..4).random()

//            实现递增切换
        if (id < lastId) {
            id++
            viewModel.showSN(id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        @cx330测试
        var lastId = viewModel.loadRepository()

        fun getImageRes(imgIndex: Int): Int {
            return resources.getIdentifier("background$imgIndex", "drawable", packageName)
        }
        Log.d("img", getImageRes(1).toString())


//        测试代码@ cx330
//        点击图片切换背景，签名

        var id = 0
        binding.mainImage.setOnClickListener {
//            实现任意切换
//            val id = (1..4).random()

//            实现递增切换
            if (id < lastId) {
                id++
                viewModel.showSN(id)

            }
        }

        viewModel.mainViewLiveData.observe(this, Observer { result ->
            val signature = result.getOrNull()
//            Toast.makeText(this, "kkk", Toast.LENGTH_SHORT).show()

            if (signature != null) {
                val (str, id) = signature as SignNature
//                Toast.makeText(this, "qqq", Toast.LENGTH_SHORT).show()

                binding.daySentence.text = str
                binding.mainImage.setImageResource(id)

            } else {
//                Toast.makeText(this, "tt", Toast.LENGTH_SHORT).show()

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