package com.example.traceassistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.traceassistant.databinding.ActivityMainBinding
import com.example.traceassistant.logic.Database.AppDatabase
import com.example.traceassistant.logic.Entity.SignNature
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sndao = AppDatabase.getDatabase(this).SignNatureDao()

        val signNature1= SignNature("you life",114514)   //测试用例
        val signNature2= SignNature("my life",111111)


        thread {
            sndao.insertSN(signNature1)
            sndao.insertSN(signNature2)    //添加数据测试
        }

        thread{


           Log.d("根据id查找图文信息:",sndao.loadSNById(1).toString())

        }
        thread {
            for (sn in sndao.SNList()){
                Log.d("对sn图文的遍历:",sn.toString())
            }
        }

        thread{
            sndao.deleteSNById(2);

        }



    }
}