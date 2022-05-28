package com.example.traceassistant.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.traceassistant.R
import com.example.traceassistant.databinding.ActivityMainViewBinding
import com.example.traceassistant.ui.affairsCollection.CollectionView
import com.example.traceassistant.ui.affairsCollection.addElementsView

class MainView : AppCompatActivity() {

    private lateinit var binding: ActivityMainViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)




//        以下代码皆仅是测试
//        向“添加事务”按钮添加跳转到事务收集页面的方法
        binding.addButton.setOnClickListener(){
            val intent = Intent(this,CollectionView::class.java)
            startActivity(intent)
        }
    }
}