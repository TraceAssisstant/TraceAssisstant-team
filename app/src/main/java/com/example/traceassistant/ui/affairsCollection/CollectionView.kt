package com.example.traceassistant.ui.affairsCollection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.traceassistant.R
import com.example.traceassistant.databinding.ActivityCollectionViewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat

class CollectionView : AppCompatActivity() {
    private lateinit var binding: ActivityCollectionViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * 重复类型复选下拉框
         * 已作废
         */
//        val items = listOf("工作日（周一至周五）","双休日（周六周日）","每天")
//        val adapter = ArrayAdapter(this,R.layout.list_item,items)
//        binding.repeatType.setAdapter(adapter)

        /**
         * 日期选择框
         * 默认选中今日
         */
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("选择预订日期")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener(){
            /**
             * @param dateStmap
             * 此处datePicker.selection 是用户指定日期的24点整对应的时间戳
             */
            val dateStmap = datePicker.selection
            val date = SimpleDateFormat("yyyy-MM-dd-kk-mm-ss").format(dateStmap)
            binding.dateShow.text = date
        }

        binding.datePick.setOnClickListener(){
            datePicker.show(supportFragmentManager,"日期选择")
        }

        /**
         * 时间选择器
         * 默认24小时制
         */
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("选择时间")
            .build()

        picker.addOnPositiveButtonClickListener(){
            val minute = picker.minute
            val hour = picker.hour
            binding.timeShow.text = "${hour} : ${minute}"
        }

        binding.timePick.setOnClickListener(){
            picker.show(supportFragmentManager,"时间选择")
        }
    }
}