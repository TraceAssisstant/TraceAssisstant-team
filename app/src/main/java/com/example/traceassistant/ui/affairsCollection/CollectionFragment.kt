package com.example.traceassistant.ui.affairsCollection

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amap.api.maps.AMap
import com.amap.api.maps.model.Marker
import com.amap.api.services.poisearch.PoiSearch
import com.example.traceassistant.R
import com.example.traceassistant.Tools.LocalNowLocation
import com.example.traceassistant.Tools.showToast
import com.example.traceassistant.databinding.FragmentCollectionBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat

class CollectionFragment : Fragment() {

    private lateinit var binding: FragmentCollectionBinding

    private val fragmentViewModel by lazy { activity?.let { ViewModelProvider(it).get(CollectionViewModel::class.java) } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCollectionBinding.inflate(layoutInflater)


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
        binding.datePick.text = SimpleDateFormat("yyyy-MM-dd").format(datePicker.selection)
        dateSelected = SimpleDateFormat("yyyy-MM-dd").format(datePicker.selection)

        datePicker.addOnPositiveButtonClickListener(){
            /**
             * @param dateStmap
             * 此处datePicker.selection 是用户指定日期的24点整对应的时间戳
             */
            val dateStmap = datePicker.selection
            val date = SimpleDateFormat("yyyy-MM-dd").format(dateStmap)
            binding.datePick.text = date
            dateSelected = date
        }

        binding.datePick.setOnClickListener(){
            datePicker.show(parentFragmentManager,"选择日期")
        }

        /**
         * 时间选择器
         * 默认24小时制
         * @param timeSelected 当前选择的时间字符串 格式："hour : minute"
         */
        var timeSelected: String = ""
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("选择时间")
            .build()
        binding.timePick.text = "12:00"
        timeSelected = "12-00"

        picker.addOnPositiveButtonClickListener(){
            val minute = picker.minute
            val hour = picker.hour
            binding.timePick.text = "%02d:%02d".format(hour,minute)
            timeSelected = "${hour}-${minute}"
        }

        binding.timePick.setOnClickListener(){
            picker.show(parentFragmentManager,"时间选择")
        }

        /**
         * 重要级别复选下拉框
         */
        val items = listOf("1","2","3","4","5")
        val adapter = activity?.let { ArrayAdapter(it,R.layout.list_item,items) }
        binding.level.setAdapter(adapter)

        /**
         * tag信息获取
         * tag标签默认单选
         * 当前代码为选中tag后用Toast显示出对应的tag名称
         * @param tagSelected 当前选中的标签
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
         * @param isring 是否开启响铃
         * @param isvibration 是否开启振动
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
         * 下一页
         * 前往地理位置设置页面
         */
        binding.toLocationPage.setOnClickListener {
            if (binding.affairTitle.text.toString().isEmpty() || binding.affairContent.text.toString().isEmpty()){
                "标题或内容不能为空:-)".showToast()
                return@setOnClickListener
            }else if (dateSelected.isEmpty() || timeSelected.isEmpty()){
                "请选择日期时间:-)".showToast()
                return@setOnClickListener
            }else if (binding.level.text.toString().isEmpty()){
                "请选择重要级别:-)".showToast()
                return@setOnClickListener
            }else if (tagSelected.isEmpty()){
                "请选择标签（或者您可以自定义标签）".showToast()
                return@setOnClickListener
            }
            /**
             * 于viewModel中存储已收集的数据
             * 最终在viewModel中进行数据插入操作
             * 最终在viewModel中进行数据插入操作
             */
            TempInsertData.let {
                it.atitle = binding.affairTitle.text.toString()
                it.amainContent = binding.affairContent.text.toString()
                it.adate = dateSelected
                it.atime = SimpleDateFormat("yyyy-MM-dd-HH-mm").parse("${dateSelected}-${timeSelected}",).time
                it.alevel = binding.level.text.toString().toInt()
                it.atag = tagSelected
                it.isRing = isring
                it.isVibration = isvibration
            }

            /**
             * 开启定位
             */
            LocalNowLocation.startLocation()

            val intent = Intent(activity,LocationCollectionView::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}