package com.example.traceassistant.ui.affairShow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traceassistant.R
import com.example.traceassistant.databinding.FragmentShowBinding
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Repository
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat

class ShowFragment : Fragment() {

    private lateinit var binding: FragmentShowBinding

    /**
     * Adapter的数据
     */
    private lateinit var listOfAffairForm: List<AffairForm>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentShowBinding.inflate(layoutInflater)
        Repository.initAFDao()

        /**
         * 日期选择
         * @param dateSelected 当前选择的日期
         */
        var dateSelected:String = ""
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("选择预订日期")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        dateSelected = SimpleDateFormat("yyyy-MM-dd").format(datePicker.selection)
        binding.topAppBar.title = dateSelected
        listOfAffairForm = Repository.getAffairListByDate(dateSelected)
        Log.d("Today",listOfAffairForm.toString())
        val adapter = ShowAffairAdapter(listOfAffairForm)
        val layoutManager = LinearLayoutManager(context)
        binding.affairsShowView.layoutManager = layoutManager
        binding.affairsShowView.adapter = adapter

        datePicker.addOnPositiveButtonClickListener(){
            /**
             * @param dateStmap
             * 此处datePicker.selection 是用户指定日期的24点整对应的时间戳
             * 切换日期时对应切换数据
             */
            val dateStmap = datePicker.selection
            val date = SimpleDateFormat("yyyy-MM-dd").format(dateStmap)
            dateSelected = date
            binding.topAppBar.title = dateSelected
            listOfAffairForm = Repository.getAffairListByDate(dateSelected)

            val adapter = ShowAffairAdapter(listOfAffairForm)
            val layoutManager = LinearLayoutManager(context)
            binding.affairsShowView.layoutManager = layoutManager
            binding.affairsShowView.adapter = adapter
        }

        binding.topAppBar.setNavigationOnClickListener {
            datePicker.show(parentFragmentManager,"日期")
        }

        /**
         * 切换地图视角
         * 通过地图罗列当日所有事务地点
         */
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            true
        }


        return binding.root
    }
}