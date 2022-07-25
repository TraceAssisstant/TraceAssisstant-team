package com.example.traceassistant.ui.affairsCollection

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Repository

class CollectionViewModel: ViewModel() {

    fun insertAffair(data: AffairForm){
        Repository.initAFDao()
        Repository.insertAffiar(data)
        Log.d("事务列表:",Repository.getAffairList().toString())
    }
}