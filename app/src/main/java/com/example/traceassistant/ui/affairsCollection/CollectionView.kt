package com.example.traceassistant.ui.affairsCollection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.traceassistant.R
import com.example.traceassistant.Tools.Navigation
import com.example.traceassistant.Tools.showToast
import com.example.traceassistant.databinding.ActivityCollectionViewBinding
import com.example.traceassistant.ui.affairShow.ShowView
import com.example.traceassistant.ui.habit.HabitView
import com.example.traceassistant.ui.main.MainView
import com.example.traceassistant.ui.setting.SettingView
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
         * 导航栏
         */
        Navigation.initialize(R.id.addPage,this,binding.bottomNavigation)

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
        binding.tagGroup.setOnCheckedStateChangeListener { group, checkedId ->
            val id =  group.checkedChipId
            when(id){
                R.id.study -> "学习".showToast()
                R.id.work -> "工作".showToast()
                R.id.rest -> "休息".showToast()
                R.id.entertainment -> "娱乐".showToast()
                R.id.sleep -> "睡觉".showToast()
            }
        }

        /**
         * 响铃与振动开关
         * 响铃的铃声选择功能待定
         */
        binding.ring.setOnCheckedChangeListener { button, ischecked ->
            if (ischecked){
                "开启响铃".showToast()
            }else{
                "禁用响铃".showToast()
            }
        }

        binding.vibration.setOnCheckedChangeListener { button, ischecked ->
            if (ischecked){
                "开启振动".showToast()
            }else{
                "禁用振动".showToast()
            }
        }
    }
}