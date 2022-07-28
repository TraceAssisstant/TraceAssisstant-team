package com.example.traceassistant.ui.affairShow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traceassistant.R
import com.example.traceassistant.Tools.Navigation
import com.example.traceassistant.databinding.ActivityShowViewBinding
import com.example.traceassistant.logic.Repository
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class ShowView : AppCompatActivity() {
    lateinit var binding: ActivityShowViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * 需要显示的事务数据集
         */
        Repository.initAFDao()
        val c = Calendar.getInstance()
        val dateStr: String = String.format("%4d-%02d-%02d",c[Calendar.YEAR],c[Calendar.MONTH]+1,c[Calendar.DAY_OF_MONTH])
        var affairList = Repository.getAffairListByDate(dateStr)

        Log.d("all",Repository.getAffairList().toString())

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

            affairList = Repository.getAffairListByDate(dateSelected)

            val layoutManager = LinearLayoutManager(this)
            binding.affairList.layoutManager = layoutManager
            val adapter = AffairAdapter(affairList)
            binding.affairList.adapter = adapter
        }

        binding.datePick.setOnClickListener(){
            datePicker.show(supportFragmentManager,"日期选择")
        }

        /**
         * 数据显示
         */
        val layoutManager = LinearLayoutManager(this)
        binding.affairList.layoutManager = layoutManager
        val adapter = AffairAdapter(affairList)
        binding.affairList.adapter = adapter

    }
}