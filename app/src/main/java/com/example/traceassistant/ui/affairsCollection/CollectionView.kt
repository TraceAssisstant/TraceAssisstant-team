package com.example.traceassistant.ui.affairsCollection

import android.content.Intent
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.maps.*
import com.amap.api.maps.model.*
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.geocoder.*
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.example.traceassistant.R
import com.example.traceassistant.Tools.LocalNowLocation
import com.example.traceassistant.Tools.Navigation
import com.example.traceassistant.Tools.locationPermission
import com.example.traceassistant.Tools.showToast
import com.example.traceassistant.databinding.ActivityCollectionViewBinding
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Repository
import com.example.traceassistant.ui.main.MainView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat

class CollectionView : AppCompatActivity(),PoiSearch.OnPoiSearchListener,AMap.OnPOIClickListener,AMap.OnMapClickListener{
    private lateinit var binding: ActivityCollectionViewBinding

    private lateinit var mMapView: TextureMapView

    var aMap: AMap? = null

    var latitude: Double = 0.0
    var longitude: Double = 0.0

    private var nowLat: Double = 0.0
    private var nowLong: Double = 0.0

    private lateinit var marker: Marker

    private lateinit var query: PoiSearch.Query

    val viewModel by lazy { ViewModelProvider(this).get(CollectionViewModel::class.java) }

    private fun refresh(){
        finish()
        val intent = Intent(this,CollectionView::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LocalNowLocation.startLocation()

        super.onCreate(savedInstanceState)
        binding = ActivityCollectionViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * 定位权限申请
         */
        locationPermission(this)

        /**
         * 导航栏
         */
//        Navigation.initialize(R.id.addPage,this,binding.bottomNavigation)

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
            try {
                val title: String = binding.affairTitle.text.toString()
                val content: String = binding.affairContent.text.toString()
                val time = SimpleDateFormat("yyyy-MM-dd-HH-mm").parse("${dateSelected}-${timeSelected}",).time

                Log.d("时间:", "${dateSelected}-${timeSelected}")

                val level = binding.level.text.toString().toInt()
                val tag = tagSelected
                val ringMusic: Boolean= isring
                val isshake: Boolean = isvibration

                val data = AffairForm(title,content,time,dateSelected,longitude,latitude,0.0,level,tag,ringMusic,isshake,0)

                Log.d("dataInsert",data.toString())

                viewModel.insertAffair(data)

                "添加成功".showToast(Toast.LENGTH_LONG)
            }catch (e: Exception){
                Log.e("insertError",e.toString())
                Log.d("dataBase",Repository.getAffairList().toString())

                "添加失败".showToast(Toast.LENGTH_LONG)
                return@setOnClickListener
            }

            val intent = Intent(this,MainView::class.java)
            this.startActivity(intent)
        }

        /**
         * 地图模块
         */
        MapsInitializer.updatePrivacyShow(this,true,true)
        MapsInitializer.updatePrivacyAgree(this,true)

        mMapView = binding.collectionMap
        mMapView.onCreate(savedInstanceState)
        if(aMap == null){
            aMap = mMapView.map
        }

        if (LocalNowLocation.getLocation() == null){
            MaterialAlertDialogBuilder(this)
                .setMessage("尚未取得定位，是否刷新页面？")
                .setPositiveButton("刷新"){dialog,which ->
                    refresh()
                }
                .setNegativeButton("取消"){dialog,which ->
                    "请自行刷新页面".showToast(Toast.LENGTH_LONG)
                }
                .show()
        }else{
            nowLat = LocalNowLocation.getLocation()!!.latitude
            nowLong = LocalNowLocation.getLocation()!!.longitude
            Log.d("nowLocation","Succeed")
        }


        /**
         * 地图定位蓝点
         */
        val myLocationStyle: MyLocationStyle = MyLocationStyle()
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER)
        myLocationStyle.showMyLocation(true)
        myLocationStyle.interval(2000)
        myLocationStyle.strokeColor(Color.BLUE)
        myLocationStyle.anchor(0.0f,1.0f)
        aMap?.uiSettings?.isMyLocationButtonEnabled = true
        aMap?.isMyLocationEnabled = true
        aMap?.setOnMapClickListener(this)
        aMap?.myLocationStyle = myLocationStyle

        /**
         * 地理搜索
         */
        binding.searchLocationBtn.setOnClickListener {
            query = PoiSearch.Query(binding.searchLocation.text.toString(),"",LocalNowLocation.getLocation()?.cityCode.toString())
            query.pageSize = 10
            query.pageNum = 1
            query.isDistanceSort = true

            val poiSearch = PoiSearch(this,query)
            poiSearch.setOnPoiSearchListener(this)
            poiSearch.bound = PoiSearch.SearchBound(LatLonPoint(nowLat,nowLong),4000)
            poiSearch.searchPOIAsyn()

        }

    }

    override fun onStop() {
        super.onStop()
        LocalNowLocation.stopLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
        LocalNowLocation.stopLocation()
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
        /**
         * 将视角移动到当前定位点
         */
        if (LocalNowLocation.getLocation()?.latitude != null && LocalNowLocation.getLocation()?.longitude != null){
            nowLat = LocalNowLocation.getLocation()!!.latitude
            nowLong = LocalNowLocation.getLocation()!!.longitude
            val cameraPosition = CameraPosition(LatLng(LocalNowLocation.getLocation()!!.latitude,LocalNowLocation.getLocation()!!.longitude),15f,0f,0f)
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            aMap?.moveCamera(cameraUpdate)
        }
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mMapView.onSaveInstanceState(outState)
    }



    /**
     * 点击地图插入标记点并记录该点的坐标
     */
    override fun onMapClick(p0: LatLng?) {
        if (this::marker.isInitialized){
            marker.remove()
        }
        if (p0 != null) {
            latitude = p0.latitude
            longitude = p0.longitude

            var markerOptions = MarkerOptions()
            markerOptions.position(p0)
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_location_on_24))
            marker = aMap!!?.addMarker(markerOptions)
        }
        Log.d("坐标","$latitude,$longitude")
    }

    /**
     * 处理搜索结果
     */
    override fun onPoiSearched(result: PoiResult?, rCode: Int) {
        if (rCode == 1000){
            Log.d("SearchReq","Succeed")
            if (result != null){
                val listPOI = result.pois
                if (result.pois.size <= 0){
                    Log.d("SearchReq","4KM内已无更多符合条件的地点")
                    "4KM内已无更多符合条件的地点".showToast()
                    query.pageNum -= 1
                    return
                }

                for (i in listPOI){
                    Log.d("SearchReq","${i.title}--${i.snippet}")
                }

                /**
                 * 显示结果
                 */
                binding.locationShowLayout.visibility = View.VISIBLE
                val layoutManager = LinearLayoutManager(this)
                binding.locationList.layoutManager = layoutManager
                val adapter = LocationAdapter(listPOI,LocationCollectionView())
                binding.locationList.adapter = adapter

                binding.nextPage.setOnClickListener {
                    if (this::query.isInitialized){
                        query.pageNum += 1

                        val poiSearch = PoiSearch(this,query)
                        poiSearch.setOnPoiSearchListener(this)
                        poiSearch.bound = PoiSearch.SearchBound(LatLonPoint(nowLat,nowLong),4000)
                        poiSearch.searchPOIAsyn()
                    }
                }
                binding.lastPage.setOnClickListener {
                    if (query.pageNum >= 1){
                        query.pageNum -= 1

                        val poiSearch = PoiSearch(this,query)
                        poiSearch.setOnPoiSearchListener(this)
                        poiSearch.bound = PoiSearch.SearchBound(LatLonPoint(nowLat,nowLong),4000)
                        poiSearch.searchPOIAsyn()
                    }
                }

            }
        }else{
            Log.d("SearchReq","Fail")
        }

    }

    override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun onPOIClick(p0: Poi?) {
        TODO("Not yet implemented")
    }
}