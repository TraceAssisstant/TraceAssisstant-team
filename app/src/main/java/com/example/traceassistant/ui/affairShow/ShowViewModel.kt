package com.example.traceassistant.ui.affairShow

import androidx.lifecycle.ViewModel
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Repository

class ShowViewModel: ViewModel() {

    fun alterAffair(data: AffairForm){
        Repository.initAFDao()
        Repository.updateAffair(data)
    }
}