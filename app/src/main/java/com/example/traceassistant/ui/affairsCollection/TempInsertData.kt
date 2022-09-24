package com.example.traceassistant.ui.affairsCollection

import com.example.traceassistant.logic.Entity.AffairForm

object TempInsertData {
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
    var state: Int = 0

    fun getAffair(): AffairForm {
        return AffairForm(atitle, amainContent, atime, adate, alongitude, alatitude, alocRange,
            alevel, atag, isRing, isVibration, state)
    }

    override fun toString(): String {
        return "$atitle, $amainContent, $atime, $adate, $alongitude, $alatitude, $alocRange, $alevel, $atag, $isRing, $isVibration, $state"
    }
}