package com.example.traceassistant.ui.affairShow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traceassistant.R
import com.example.traceassistant.Tools.Navigation
import com.example.traceassistant.databinding.ActivityShowViewBinding
import com.example.traceassistant.logic.Repository
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat

class ShowView : AppCompatActivity() {
    lateinit var binding: ActivityShowViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * 导航栏
         */
        Navigation.initialize(R.id.homePage,this,binding.bottomNavigation)

        /**
         * 日期选择框
         * 默认选中今日
         */
        var dateSelected:String = ""
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("选择日期")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener(){
            /**
             * @param dateStmap
             * 此处datePicker.selection 是用户指定日期的24点整对应的时间戳
             */
            val dateStmap = datePicker.selection
            val date = SimpleDateFormat("yyyy-MM-dd").format(dateStmap)
            binding.dateShow.text = date
            dateSelected = date
        }

        binding.datePick.setOnClickListener(){
            datePicker.show(supportFragmentManager,"日期选择")
        }

        /**
         * 数据显示
         */
        Repository.initAFDao()
        val affairList = Repository.getAffairList()
        val layoutManager = LinearLayoutManager(this)
        binding.affairList.layoutManager = layoutManager
        val adapter = AffairAdapter(affairList)
        binding.affairList.adapter = adapter

    }
}