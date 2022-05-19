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


//        测试代码@Noble047
        binding.testBtn.setOnClickListener(){
            Repository.initSndao()
            val signNature1= SignNature("you life",114514)
            Repository.insertSN(signNature1)
            Repository.loadSNById(1).observe(this){data->
                Log.d("RepositoryTest",data.getOrNull().toString())
            }
        }
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