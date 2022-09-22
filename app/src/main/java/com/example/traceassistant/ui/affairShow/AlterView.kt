package com.example.traceassistant.ui.affairShow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.traceassistant.R
import com.example.traceassistant.Tools.Navigation
import com.example.traceassistant.Tools.showToast
import com.example.traceassistant.databinding.ActivityAlterViewBinding
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Repository
import com.example.traceassistant.ui.main.MainView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat

class AlterView : AppCompatActivity() {
    lateinit var binding: ActivityAlterViewBinding
    val viewModel by lazy { ViewModelProvider(this).get(ShowViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlterViewBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /**
         * 导航栏
         */
//        Navigation.initialize(R.id.showPage,this,binding.bottomNavigation)

        /**
         * 设定初始数据
         */
        binding.alterTitle.setText(intent.getStringExtra("title"))
        binding.alterContent.setText(intent.getStringExtra("content"))
        binding.alterDateShow.text = intent.getStringExtra("date")
        binding.alterTimeShow.text = intent.getStringExtra("time")
        binding.alterLevel.setText(intent.getIntExtra("level",0).toString())
        binding.alterRing.isChecked = intent.getBooleanExtra("ring",false)
        binding.alterVibration.isChecked = intent.getBooleanExtra("vibration",false)

        var tagSelected: String? = ""
        when(intent.getStringExtra("tag")){
            "工作" -> {
                binding.alterWork.isChecked = true
                tagSelected = "工作"
            }
            "学习" -> {
                binding.alterStudy.isChecked = true
                tagSelected = "学习"
            }
            "休息" -> {
                binding.alterRest.isChecked = true
                tagSelected = "休息"
            }
            "娱乐" -> {
                binding.alterEntertainment.isChecked = true
                tagSelected = "娱乐"
            }
            "睡觉" -> {
                binding.alterSleep.isChecked = true
                tagSelected = "睡觉"
            }
        }

        /**
         * 日期选择框
         * 默认选中今日
         */
        var dateSelected: String = intent.getStringExtra("date").toString()
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
            val date = SimpleDateFormat("yyyy-MM-dd").format(dateStmap)
            binding.alterDateShow.text = date
            dateSelected = date
        }

        binding.alterDatePick.setOnClickListener(){
            datePicker.show(supportFragmentManager,"日期选择")
        }

        /**
         * 时间选择器
         * 默认24小时制
         */
        var timeSelected: String = intent.getStringExtra("time").toString()
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("选择时间")
            .build()

        picker.addOnPositiveButtonClickListener(){
            val minute = picker.minute
            val hour = picker.hour
            binding.alterTimeShow.text = "${hour} : ${minute}"
            timeSelected = "${hour}-${minute}"
        }

        binding.alterTimePick.setOnClickListener(){
            picker.show(supportFragmentManager,"时间选择")
        }

        /**
         * 重要级别复选下拉框
         */
        val items = listOf("1","2","3","4","5")
        val adapter = ArrayAdapter(this,R.layout.list_item,items)
        binding.alterLevel.setAdapter(adapter)

        /**
         * tag信息获取
         * tag标签默认单选
         * 当前代码为选中tag后用Toast显示出对应的tag名称
         */
        binding.alterTagGroup.setOnCheckedStateChangeListener { group, checkedId ->
            val id =  group.checkedChipId
            when(id){
                R.id.study -> tagSelected = "学习"
                R.id.work -> tagSelected = "工作"
                R.id.rest -> tagSelected = "休息"
                R.id.entertainment -> tagSelected = "娱乐"
                R.id.sleep -> tagSelected = "睡觉"
            }
        }

        /**
         * 响铃与振动开关
         * 响铃的铃声选择功能待定
         */
        var isring = intent.getBooleanExtra("ring",false)
        var isvibration = intent.getBooleanExtra("vibration",false)
        binding.alterRing.setOnCheckedChangeListener { button, ischecked ->
            isring = ischecked
            if (ischecked){
                "开启响铃".showToast()
            }else{
                "禁用响铃".showToast()
            }
        }

        binding.alterVibration.setOnCheckedChangeListener { button, ischecked ->
            isvibration = ischecked
            if (ischecked){
                "开启振动".showToast()
            }else{
                "禁用振动".showToast()
            }
        }

        /**
         * 表单提交按钮
         */
        binding.alterSubmitBtn.setOnClickListener(){
            try {
                val title: String = binding.alterTitle.text.toString()
                val content: String = binding.alterContent.text.toString()
                val time = SimpleDateFormat("yyyy-MM-dd-HH-mm").parse("${dateSelected}-${timeSelected}",).time

                Log.d("时间:", "${dateSelected}-${timeSelected}")

                val level = binding.alterLevel.text.toString().toInt()
                val tag = tagSelected.toString()
                val ringMusic: Boolean= isring
                val isshake: Boolean = isvibration

                val data = AffairForm(title,content,time, dateSelected,0.0,0.0,0.0,level,tag,ringMusic,isshake,1)
                data.id = intent.getIntExtra("id",0)

                Log.d("dataId",data.id.toString())
                Log.d("dataAlter",data.toString())

                viewModel.alterAffair(data)

            }catch (e: Exception){
                Log.e("insertError",e.toString())
                Log.d("dataBase", Repository.getAffairList().toString())
                return@setOnClickListener
            }

            val intent = Intent(this, ShowView::class.java)
            this.startActivity(intent)
        }


    }
}