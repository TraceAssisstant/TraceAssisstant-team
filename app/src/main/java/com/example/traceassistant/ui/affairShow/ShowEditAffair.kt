package com.example.traceassistant.ui.affairShow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.traceassistant.R
import com.example.traceassistant.Tools.toDateTimeObscure
import com.example.traceassistant.databinding.ActivityShowEditAffairBinding
import com.example.traceassistant.logic.Entity.AffairForm

class ShowEditAffair : AppCompatActivity() {

    lateinit var binding: ActivityShowEditAffairBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowEditAffairBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val AFFAIR_DATA: AffairForm = intent.getBundleExtra("affair")?.getSerializable("affair") as AffairForm

        /**
         * 数据绑定
         */
        bindData(AFFAIR_DATA)





    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    private fun bindData(AFFAIR_DATA: AffairForm){
        binding.editTitle.setText(AFFAIR_DATA.ttitle)
        binding.editContent.setText(AFFAIR_DATA.mainContent)
        binding.editDatePick.setText(AFFAIR_DATA.date)
        binding.editTimePick.setText(toDateTimeObscure(AFFAIR_DATA.time))
        binding.editLevel.setText(AFFAIR_DATA.level.toString())
        binding.editRing.isChecked = AFFAIR_DATA.ringMusic
        binding.editVibration.isChecked = AFFAIR_DATA.isShake
        when(AFFAIR_DATA.tag){
            "学习" -> binding.estudy.isChecked = true
            "工作" -> binding.ework.isChecked = true
            "休息" -> binding.erest.isChecked = true
            "娱乐" -> binding.eentertainment.isChecked = true
            "睡觉" -> binding.esleep.isChecked = true
            else -> {}
        }
    }
}