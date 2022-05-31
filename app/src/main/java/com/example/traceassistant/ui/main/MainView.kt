package com.example.traceassistant.ui.main

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.traceassistant.R
import com.example.traceassistant.Tools.showToast
import com.example.traceassistant.databinding.ActivityMainViewBinding
import com.example.traceassistant.logic.Entity.SignNature
import com.example.traceassistant.ui.affairsCollection.CollectionView


class MainView : AppCompatActivity() {

    private lateinit var binding: ActivityMainViewBinding
    val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    var id = 0

    override fun onResume() {
        super.onResume()
        var lastId = viewModel.loadRepository()

        if (id < lastId) {
            id++
            viewModel.showSN(id)
        }else{
            id = 0
        }

        viewModel.mainViewLiveData.observe(this, Observer { result ->
            val signature = result.getOrNull()
            if (signature != null) {
                val (str, id) = signature as SignNature
                binding.daySentence.text = str
                binding.mainImage.setImageResource(id)

            } else {
                "No Data ".showToast()
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        以下代码皆仅是测试
//        向“添加事务”按钮添加跳转到事务收集页面的方法
        binding.addButton.setOnClickListener() {
            val intent = Intent(this, CollectionView::class.java)
            startActivity(intent)
        }
    }

}