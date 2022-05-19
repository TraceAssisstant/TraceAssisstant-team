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

//    向签名图片数据库插入图文
    fun insertSN(signNature: SignNature){
        thread {
            sndao.insertSN(signNature)
        }
    }

//    从签名图片数据库根据id获取图文类
//    返回一个用Result<T>封装的实例(此处封装的实例是signNature实体类的实例)，请利用getOrNull()将封装的数据取出
    fun loadSNById(id:Int) = liveData(Dispatchers.IO){
        val result = try {
            Result.success(sndao.loadSNById(id))
        }catch (e: Exception){
            Result.failure<String>(e)
        }
        emit(result)
    }



    //    测试GlobalApplication类全局获取context功能运作正常
    fun test(){
        GlobalApplication.context.toString().showToast();
    }


}