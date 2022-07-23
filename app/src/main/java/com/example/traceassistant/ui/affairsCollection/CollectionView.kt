package com.example.traceassistant.ui.affairsCollection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.traceassistant.R
import com.example.traceassistant.Tools.GlobalApplication
import com.example.traceassistant.Tools.Navigation
import com.example.traceassistant.Tools.showToast
import com.example.traceassistant.databinding.ActivityCollectionViewBinding
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Repository
import com.example.traceassistant.ui.main.MainView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.ParsePosition
import java.text.SimpleDateFormat

class CollectionView : AppCompatActivity() {
    private lateinit var binding: ActivityCollectionViewBinding

    val viewModel by lazy { ViewModelProvider(this).get(CollectionViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * 导航栏
         */
        Navigation.initialize(R.id.addPage,this,binding.bottomNavigation)

        /**
         * 日期选择框
         * 默认选中今日
         */
        var dateSelected:String = ""
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
            binding.dateShow.text = date
            dateSelected = date
        }

        binding.datePick.setOnClickListener(){
            datePicker.show(supportFragmentManager,"日期选择")
        }

        /**
         * 时间选择器
         * 默认24小时制
         */
        var timeSelected: String = ""
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
            timeSelected = "${hour}-${minute}"
        }

        binding.timePick.setOnClickListener(){
            picker.show(supportFragmentManager,"时间选择")
        }

        /**
         * 重要级别复选下拉框
         */
        val items = listOf("1","2","3","4","5")
        val adapter = ArrayAdapter(this,R.layout.list_item,items)
        binding.level.setAdapter(adapter)

        /**
         * tag信息获取
         * tag标签默认单选
         * 当前代码为选中tag后用Toast显示出对应的tag名称
         */
        var tagSelected: String = ""
        binding.tagGroup.setOnCheckedStateChangeListener { group, checkedId ->
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
        var isring = false
        var isvibration = false
        binding.ring.setOnCheckedChangeListener { button, ischecked ->
            isring = ischecked
            if (ischecked){
                "开启响铃".showToast()
            }else{
                "禁用响铃".showToast()
            }
        }

        binding.vibration.setOnCheckedChangeListener { button, ischecked ->
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
        binding.submitBtn.setOnClickListener(){
            val title: String = binding.affairTitle.text.toString()
            val content: String = binding.affairContent.text.toString()
            val time = SimpleDateFormat("yyyy-MM-dd-HH-mm").parse("${dateSelected}-${timeSelected}",).time

            Log.d("时间:", "${dateSelected}-${timeSelected}")

            val level = binding.level.text.toString().toInt()
            val tag = tagSelected
            val ringMusic: Boolean= isring
            val isshake: Boolean = isvibration

            val data = AffairForm(title,content,time,0.0,0.0,0.0,0,level,tag,ringMusic,isshake)

            viewModel.insertAffair(data)

            val intent = Intent(this,MainView::class.java)
            this.startActivity(intent)


        }
    }
}