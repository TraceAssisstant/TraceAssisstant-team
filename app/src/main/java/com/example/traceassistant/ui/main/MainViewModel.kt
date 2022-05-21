package com.example.traceassistant.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.traceassistant.logic.Repository

class MainViewModel:ViewModel() {
    private val snLiveData=MutableLiveData<String>()

    val mainViewLiveData=Transformations.switchMap(snLiveData){id->Repository.loadSNById(id.toInt())}

    fun showSN(id:Int){
    snLiveData.value= id.toString()
    }
}