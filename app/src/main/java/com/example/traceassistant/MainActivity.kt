package com.example.traceassistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.traceassistant.databinding.ActivityMainBinding
import com.example.traceassistant.logic.Database.AppDatabase
import com.example.traceassistant.logic.Entity.SignNature
import com.example.traceassistant.logic.Repository
import kotlin.concurrent.thread
import kotlin.math.sign


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        测试内容：点击按钮将会切换图片以及对应的签名，
//        图片与签名的初始资源需要在软件第一次运行时插入
        Repository.initSndao()
//        批量插入图片签名资源
        var strList = mutableListOf<String>()
        var imageList = mutableListOf<Int>()

        for (k in R.drawable.background01..R.drawable.background05){
            imageList.add(k)
            strList.add("签名${k}")
        }
        Repository.SNList()
        //资源数量测试
        Log.d("资源数量",Repository.getSnNumber().toString())
        try {
            Repository.batchInsertSN(strList,imageList)

        }catch (e:Exception){
            Log.w("插入错误",e)
        }



        var i = 1
        binding.testBtn.setOnClickListener(){
            if (i<5){
                i += 1
            }else {
                i = 1
            }
            Repository.loadSNById(i).observe(this){data->
                val signature = data.getOrNull() as SignNature
                val (str,id) = signature
                binding.Signature.text = str
                binding.ImageTest.setImageResource(id)
            }

        }

    }
}