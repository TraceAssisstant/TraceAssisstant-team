package com.example.traceassistant.logic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.traceassistant.Tools.GlobalApplication
import com.example.traceassistant.Tools.showToast
import com.example.traceassistant.logic.Dao.SignNatureDao
import com.example.traceassistant.logic.Database.AppDatabase
import com.example.traceassistant.logic.Entity.SignNature
import kotlinx.coroutines.Dispatchers
import kotlin.concurrent.thread

object Repository {

    lateinit var sndao: SignNatureDao

//    初始化数据库dao sndao
    fun initSndao(){
        sndao = AppDatabase.getDatabase(GlobalApplication.context).SignNatureDao()
    }

//    插入图文
    fun insertSN(signNature: SignNature){
        thread {
            sndao.insertSN(signNature)
        }
    }

//    批量插入图文，以初始化
//    参数1：签名组成的列表，参数2：图片id组成的列表
    fun batchInsertSN(strList: MutableList<String>,imageIdList: MutableList<Int>){
        val i = strList.size
        for (k in 1..i){
            val str = strList[k-1]
            val image = imageIdList[k-1]
            insertSN(SignNature(str,image))
        }
    }

//    根据id获取图文类
//    返回一个用Result<T>封装的实例(此处封装的实例是signNature实体类的实例)，请利用getOrNull()将封装的数据取出
    fun loadSNById(id:Int) = liveData(Dispatchers.IO){
        val result = try {
            Result.success(sndao.loadSNById(id))
        }catch (e: Exception){
            Result.failure<String>(e)
        }
        emit(result)
    }



    //    遍历数据库
    fun SNList(){
        thread {
            for (sn in sndao.SNList()){
                Log.d("对sn图文的遍历:",sn.toString())
            }
        }
    }

    //返回数据库中图文记录条数
    fun getSnNumber():Int{
        var num = 0;

        var list: List<SignNature> = sndao.SNList()
            for (sn in list) {
                Log.d("对sn图文的:", sn.toString())
                num = num + 1
            }

        return num;
    }


}