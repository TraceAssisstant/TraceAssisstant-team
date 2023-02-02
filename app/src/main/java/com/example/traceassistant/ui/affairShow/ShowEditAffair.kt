package com.example.traceassistant.ui.affairShow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.example.traceassistant.R
import com.example.traceassistant.Tools.showToast
import com.example.traceassistant.Tools.toDateTimeObscure
import com.example.traceassistant.Tools.toStamp
import com.example.traceassistant.databinding.ActivityShowEditAffairBinding
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Repository
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat

class ShowEditAffair : AppCompatActivity() {

    lateinit var binding: ActivityShowEditAffairBinding
    var tagSelected: String = ""
    var timeSelected: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowEditAffairBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val AFFAIR_DATA: AffairForm = intent.getBundleExtra("affair")?.getSerializable("affair") as AffairForm

        /**
         * 日期选择框
         * 默认选中今日
         * @param dateSelected 当前选中的日期字符串
         */
        var dateSelected:String = ""
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("选择预订日期")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        binding.editDatePick.text = SimpleDateFormat("yyyy-MM-dd").format(datePicker.selection)
        dateSelected = SimpleDateFormat("yyyy-MM-dd").format(datePicker.selection)

        datePicker.addOnPositiveButtonClickListener(){
            /**
             * @param dateStmap
             * 此处datePicker.selection 是用户指定日期的24点整对应的时间戳
             */
            val dateStmap = datePicker.selection
            val date = SimpleDateFormat("yyyy-MM-dd").format(dateStmap)
            binding.editDatePick.text = date
            dateSelected = date
        }

        binding.editDatePick.setOnClickListener(){
            datePicker.show(supportFragmentManager,"选择日期")
        }

        /**
         * 时间选择器
         * 默认24小时制
         * @param timeSelected 当前选择的时间字符串 格式："hour : minute"
         */
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("选择时间")
            .build()
        binding.editTimePick.text = "12:00"
        timeSelected = "12:00"

        picker.addOnPositiveButtonClickListener(){
            val minute = picker.minute
            val hour = picker.hour
            binding.editTimePick.text = "%02d:%02d".format(hour,minute)
            timeSelected = "%02d:%02d".format(hour,minute)
        }

        binding.editTimePick.setOnClickListener(){
            picker.show(supportFragmentManager,"时间选择")
        }

        /**
         * 重要级别复选下拉框
         */
        val items = listOf("1","2","3","4","5")
        val adapter = ArrayAdapter(this,R.layout.list_item,items)
        binding.editLevel.setAdapter(adapter)

        /**
         * tag信息获取
         * tag标签默认单选
         * 当前代码为选中tag后用Toast显示出对应的tag名称
         * @param tagSelected 当前选中的标签
         */
        binding.editTagGroup.setOnCheckedStateChangeListener { group, checkedId ->
            val id =  group.checkedChipId
            when(id){
                R.id.estudy -> tagSelected = "学习"
                R.id.ework -> tagSelected = "工作"
                R.id.erest -> tagSelected = "休息"
                R.id.eentertainment -> tagSelected = "娱乐"
                R.id.esleep -> tagSelected = "睡觉"
            }
        }

        /**
         * 响铃与振动开关
         * 响铃的铃声选择功能待定
         * @param isring 是否开启响铃
         * @param isvibration 是否开启振动
         */
        var isring = false
        var isvibration = false
        binding.editRing.setOnCheckedChangeListener { button, ischecked ->
            isring = ischecked
            if (ischecked){
                "开启响铃".showToast()
            }else{
                "禁用响铃".showToast()
            }
        }

        binding.editVibration.setOnCheckedChangeListener { button, ischecked ->
            isvibration = ischecked
            if (ischecked){
                "开启振动".showToast()
            }else{
                "禁用振动".showToast()
            }
        }

        /**
         * 确认修改按钮
         */
        binding.confirmEdit.setOnClickListener {
            val editTitle = binding.editTitle.text.toString()
            val editDatePick = binding.editDatePick.text.toString()
            val editTime = toStamp("${editDatePick}-${timeSelected}")
            val editContent = binding.editContent.text.toString()
            val editLevel = binding.editLevel.text.toString().toInt()
            val id = AFFAIR_DATA.id
            val editAffair = AffairForm(editTitle,editContent,editTime,editDatePick,AFFAIR_DATA.longitude,AFFAIR_DATA.latitude,
            AFFAIR_DATA.locRange,editLevel,tagSelected,binding.editRing.isChecked,binding.editVibration.isChecked,AFFAIR_DATA.state)
            editAffair.id = id
            Log.d("EDIT AFFAIR",editAffair.toString())

            Repository.updateAffair(editAffair)
            finish()
        }

        /**
         * 删除按钮
         */
        binding.confirmDelete.setOnClickListener {

        }

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
        timeSelected = toDateTimeObscure(AFFAIR_DATA.time)
        binding.editLevel.setText(AFFAIR_DATA.level.toString())
        binding.editRing.isChecked = AFFAIR_DATA.ringMusic
        binding.editVibration.isChecked = AFFAIR_DATA.isShake
        when(AFFAIR_DATA.tag){
            "学习" -> binding.editTagGroup.check(R.id.estudy)
            "工作" -> binding.editTagGroup.check(R.id.ework)
            "休息" -> binding.editTagGroup.check(R.id.erest)
            "娱乐" -> binding.editTagGroup.check(R.id.eentertainment)
            "睡觉" -> binding.editTagGroup.check(R.id.esleep)
            else -> {}
        }
        tagSelected = AFFAIR_DATA.tag
    }

}