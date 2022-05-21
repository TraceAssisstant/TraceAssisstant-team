package com.example.traceassistant.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.traceassistant.R
import com.example.traceassistant.databinding.ActivityMainView2Binding
import com.example.traceassistant.logic.Entity.SignNature

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainView2Binding
    val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_view2)
        binding = ActivityMainView2Binding.inflate(layoutInflater)
        setContentView(binding.root)

//        测试代码@cx330
        //        测试内容：点击按钮将会切换图片以及对应的签名，
        var i=1
        binding.testBtn.setOnClickListener {
            if (i<5){
                i += 1
            }else {
                i = 1
            }

        }
        viewModel.mainViewLiveData.observe(this, Observer { data->
            val signature = data.getOrNull() as SignNature
            val (str,id) = signature
            binding.Signature.text = str
            binding.ImageTest.setImageResource(id) })

    }
}