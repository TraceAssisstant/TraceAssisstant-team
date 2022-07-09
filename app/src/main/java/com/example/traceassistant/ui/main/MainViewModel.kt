package com.example.traceassistant.ui.main

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.traceassistant.R
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
        Repository.initSndao()
    //     批量插入图片签名资源
        var strList = mutableListOf<String>()
        var imageList = mutableListOf<Int>()
        for (k in R.drawable.background01..R.drawable.background05) {
            imageList.add(k)
            strList.add("签名${k}")
            Log.d("k", k.toString())
        }
        //Log.d("lastid", lastId.toString())
        Log.d("imgSize1",imageList.size.toString())
        Log.d("strSize1",strList.size.toString())
    //    避免数据重复插入
        if (!flag) {
            Repository.batchInsertSN(strList, imageList)
            flag=true
        }
        Repository.SNList()
    }
}