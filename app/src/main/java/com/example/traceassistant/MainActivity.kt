package com.example.traceassistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.traceassistant.databinding.ActivityMainBinding
import com.example.traceassistant.logic.Database.AppDatabase
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Entity.SignNature
import com.example.traceassistant.logic.Repository
import java.lang.Exception
import kotlin.concurrent.thread
import kotlin.math.sign


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        测试代码@Noble047
//        测试内容：点击按钮将会切换图片以及对应的签名，
//        图片与签名的初始资源需要在软件第一次运行时插入

//        Repository.initSndao()
////            批量插入图片签名资源
//            var strList = mutableListOf<String>()
//            var imageList = mutableListOf<Int>()
//
//            for (k in R.drawable.background01..R.drawable.background05){
//                try{
//                imageList.add(k)
//                strList.add("签名${k}")
//                }catch (e:Exception){
//                    Log.d("warning",e.toString())
//                }
//            }
//
//        try {
//            Repository.batchInsertSN(strList, imageList)
//        }catch (e :Exception){
//            Log.d("warning",e.toString())
//        }
//        Repository.SNList()
//
//        var i = 1
//        binding.testBtn.setOnClickListener(){
//            if (i<5){
//                i += 1
//            }else {
//                i = 1
//            }
//            if(i!=2) {
//                Repository.loadSNById(i).observe(this) { data ->
//                    val signature = data.getOrNull() as SignNature
//                    val (str, id) = signature
//                    binding.Signature.text = str
//                    binding.ImageTest.setImageResource(id)
//                }
//            }
//        }

    // @Noble047

//        val sndao = AppDatabase.getDatabase(this).signNatureDao()
//
//        val signNature1= SignNature("you life",11)   //测试用例
//        val signNature2= SignNature("my life",1111)
//
//
//        thread {
//            try{
//            sndao.insertSN(signNature1)
//            sndao.insertSN(signNature2)    //添加数据测试
//            }catch (e : Exception){
    //
    //           }
//              for (sn in sndao.SNList()){
////                Log.d("对sn图文的遍历:",sn.toString())
////            }
//
//
//        }


        val afDao = AppDatabase.getDatabase(this).affairFormDao()


        thread {
            val af1 = AffairForm(
                "取快递", "去新一区菜鸟驿站取快递", 1234567,
                123.45, 123.34, 100.0, 2, 3, "取快递", "user/music01.mp3", true
            )
            val af2 = AffairForm(
                "取快递2", "去c区菜鸟驿站取快递", 1234599,
                121.45, 126.34, 100.0, 1, 5, "取快递", "user/music01.mp3", true
            )

            afDao.affairInsert(af1)
            afDao.affairInsert(af2)

            for( af in afDao.affairQueryByTime()){
                Log.d("对事务列表的遍历:(时间顺序): ",af.toString())
            }
        }

    }
}