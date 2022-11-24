package com.example.traceassistant.ui.main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.traceassistant.R
import com.example.traceassistant.Tools.GlobalApplication
import com.example.traceassistant.logic.Repository

class MainViewModel : ViewModel() {
    private val snLiveData = MutableLiveData<Int>()

    val mainViewLiveData =
        Transformations.switchMap(snLiveData) { id -> Repository.loadSNById(id.toInt()) }

    fun getSN(id: Int) {
        snLiveData.postValue(id)
    }

    //    加载数据库资源
    fun loadRepository() {
        val lastId=5//图文资源的总数，可扩展
        var flag=false//记录是否插入图文
        val array = GlobalApplication.context.resources.getStringArray(R.array.text)
        Repository.initSndao()
    //     批量插入图片签名资源
        var strList = mutableListOf<String>()
        var imageList = mutableListOf<Int>()
        for (k in R.drawable.background01..R.drawable.background20) {
            imageList.add(k)
        }
        for (i in array){
            strList.add(i)
        }
    //    避免数据重复插入
        if (!flag) {
            Repository.batchInsertSN(strList, imageList)
            flag=true
        }
    }
}