package com.example.traceassistant.ui.affairsCollection

import androidx.lifecycle.ViewModel
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Repository

class CollectionViewModel: ViewModel() {

    inner class affairTempData(){
        var atitle: String = ""
        var amainContent: String = ""
        var atime: Long = 0
        var adate: String = ""
        var alongitude: Double = 0.0
        var alatitude: Double = 0.0
        var alocRange: Double = 0.0
        var alevel: Int = 0
        var atag: String = ""
        var isRing: Boolean = false
        var isVibration: Boolean = false
        var satate: Int = 0

    }

    var affairForm: affairTempData = affairTempData()

    fun insertAffair(data: AffairForm){
        Repository.initAFDao()
        Repository.insertAffiar(data)
    }
}