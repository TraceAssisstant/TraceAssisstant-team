package com.example.traceassistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.traceassistant.databinding.ActivityMainBinding
//import com.example.traceassistant.databinding.ActivityMainView2Binding
import com.example.traceassistant.logic.Database.AppDatabase
import com.example.traceassistant.logic.Entity.SignNature
import com.example.traceassistant.logic.Repository
import com.example.traceassistant.ui.main.MainViewModel
import kotlin.concurrent.thread
import kotlin.math.sign


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_view2)
        binding = ActivityMainBinding.inflate(layoutInflater)
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

            viewModel.showSN(i)
        }
        viewModel.mainViewLiveData.observe(this, Observer { result->
            val signature = result.getOrNull()
            if (signature!=null){
                val (str,id) = signature as SignNature
                println("ll")
                binding.Signature.text = str
                binding.ImageTest.setImageResource(id) }
            }
           )



//        测试代码@Noble047
//        测试内容：点击按钮将会切换图片以及对应的签名，
//        图片与签名的初始资源需要在软件第一次运行时插入
//        Repository.initSndao()
//            批量插入图片签名资源
//            var strList = mutableListOf<String>()
//            var imageList = mutableListOf<Int>()
//
//            for (k in R.drawable.background01..R.drawable.background05){
//                imageList.add(k)
//                strList.add("签名${k}")
//            }
//        Repository.batchInsertSN(strList,imageList)
//
//        Repository.SNList()
//
//        var i = 1
//        binding.testBtn.setOnClickListener(){
//            if (i<5){
//                i += 1
//            }else {
//                i = 1
//            }
//            Repository.loadSNById(i).observe(this){data->
//                val signature = data.getOrNull() as SignNature
//                val (str,id) = signature
//                binding.Signature.text = str
//                binding.ImageTest.setImageResource(id)
//            }
//
//        }
//        @Noble047



//        val sndao = AppDatabase.getDatabase(this).SignNatureDao()
//
//        val signNature1= SignNature("you life",11)   //测试用例
//        val signNature2= SignNature("my life",1111)
//
//
//        thread {
//            sndao.insertSN(signNature1)
//            sndao.insertSN(signNature2)    //添加数据测试
//        }
//
//        thread{
//
//
//           Log.d("根据id查找图文信息:",sndao.loadSNById(1).toString())
//
//        }
//        thread {
//            for (sn in sndao.SNList()){
//                Log.d("对sn图文的遍历:",sn.toString())
//            }
//        }
//
//        thread{
//            sndao.deleteSNById(2);
//
//        }
    }
}