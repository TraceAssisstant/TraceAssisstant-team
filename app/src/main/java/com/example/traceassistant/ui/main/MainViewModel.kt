package com.example.traceassistant.ui.main

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.traceassistant.R
import com.example.traceassistant.logic.Repository

class MainViewModel : ViewModel() {
    private val snLiveData = MutableLiveData<String>()

    val mainViewLiveData =
        Transformations.switchMap(snLiveData) { id -> Repository.loadSNById(id.toInt()) }

    fun showSN(id: Int) {
        snLiveData.value = id.toString()
    }


//    加载数据库资源
    fun loadRepository(): Int {
        Repository.initSndao()
//     批量插入图片签名资源
        var strList = mutableListOf<String>()
        var imageList = mutableListOf<Int>()
        var lastId = 0

        for (k in R.drawable.background01..R.drawable.background05) {
            imageList.add(k)
            strList.add("签名${k}")
            lastId ++
            Log.d("k", k.toString())
        }
        Log.d("lastid", lastId.toString())


//    避免数据重复插入
        if (Repository.loadSNById(lastId) == null) {
            Repository.batchInsertSN(strList, imageList)

        }

        Repository.SNList()
        return lastId
    }
}