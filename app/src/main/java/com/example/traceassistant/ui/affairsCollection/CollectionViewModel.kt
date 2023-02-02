package com.example.traceassistant.ui.affairsCollection

import androidx.lifecycle.ViewModel
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Repository

class CollectionViewModel: ViewModel() {

    fun insertAffair(data: AffairForm){
        Repository.insertAffiar(data)
    }
}