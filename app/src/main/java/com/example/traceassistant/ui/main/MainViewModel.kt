package com.example.traceassistant.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.traceassistant.R
import com.example.traceassistant.logic.Repository

class MainViewModel:ViewModel() {
    private val snLiveData=MutableLiveData<String>()

    val mainViewLiveData=Transformations.switchMap(snLiveData){id->Repository.loadSNById(id.toInt())}

    fun showSN(id:Int){
    snLiveData.value= id.toString()
    }

    fun loadRepository(){
        Repository.initSndao()
//        批量插入图片签名资源
        var strList = mutableListOf<String>()
        var imageList = mutableListOf<Int>()

        for (k in R.drawable.background01..R.drawable.background02){
            imageList.add(k)
            strList.add("签名${k}")
        }



        Repository.batchInsertSN(strList,imageList)

        Repository.SNList()
    }
}