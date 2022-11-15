package com.example.traceassistant.ui.affairShow.MapView

import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.MapsInitializer
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import com.example.traceassistant.R
import com.example.traceassistant.Tools.toDate
import com.example.traceassistant.databinding.ActivityMapViewBinding
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Repository
import com.google.android.material.datepicker.MaterialDatePicker

class MapViewActivity : AppCompatActivity(),AMap.OnMyLocationChangeListener {

    lateinit var mMapView: MapView

    lateinit var binding: ActivityMapViewBinding

    lateinit var mAMap: AMap

//    当前位置
    var latlng: LatLng = LatLng(0.0,0.0)

//    当前选择的日期
    var date: String = ""
//    当天的事务集
    var affairsToday: List<AffairForm>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

//      标题栏
        val datePicker = datePicker()
        binding.MapviewTopAppBar.setNavigationOnClickListener {
            this.finish()
        }
        binding.MapviewTopAppBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.chooseDate -> {
                    datePicker.show(supportFragmentManager,"日期")
                    true
                }
                else -> false
            }
        }

//      隐私政策
        MapsInitializer.updatePrivacyShow(this, true, true)
        MapsInitializer.updatePrivacyAgree(this, true)

        mMapView = binding.MapviewMap
        mMapView.onCreate(savedInstanceState)

//      初始化aMap对象
        if (!this::mAMap.isInitialized){
            mAMap = mMapView.map
        }


//      设置地图缩放
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(16f))

//      显示定位蓝点
        showLocationBluepoint()
        mAMap.setOnMyLocationChangeListener(this)

//      当前选择的日期赋值
        date = datePicker.selection?.let { toDate(it/1000) }.toString()

//      获取当天事务集
        affairsToday = getAffairListByDate(date)

//      标记今日地点
        markerUpdate()

//      日期选择器监听器
        datePicker.addOnPositiveButtonClickListener {
            date = datePicker.selection?.let { toDate(it/1000) }.toString()
            affairsToday = getAffairListByDate(date)
            markerUpdate()
            Log.d("DateSelectionChange","$date :## $affairsToday")
        }

//      标记点击监听器
        mAMap.setOnMarkerClickListener {
            Log.d("Marker",it.`object`.toString())
            mAMap.animateCamera(CameraUpdateFactory.newLatLng(it.position))
            it.showInfoWindow()
            true
        }
        mAMap.setInfoWindowAdapter(InfoWindowAdapter(this))
    }

    /**
     * 定位信息回调
     * @param p0 当前地理位置信息
     */
    override fun onMyLocationChange(p0: Location?) {
//        Log.d("LocationNow","经度：${p0?.latitude}  纬度：${p0?.longitude}")
        if (p0 != null) {
            latlng = LatLng(p0.latitude,p0.longitude)
        }
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mMapView.onSaveInstanceState(outState)
    }

    private fun datePicker()=MaterialDatePicker.Builder.datePicker()
        .setTitleText("选择日期")
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build()

    private fun showLocationBluepoint(){
        val mLocationStyle = MyLocationStyle()
        mLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        mLocationStyle.strokeColor(Color.TRANSPARENT)
        mLocationStyle.radiusFillColor(Color.TRANSPARENT)
        mAMap.myLocationStyle = mLocationStyle
        mAMap.uiSettings.isMyLocationButtonEnabled = true
        mAMap.isMyLocationEnabled = true
    }

    private fun getAffairListByDate(date: String): List<AffairForm>{
        Repository.initAFDao()
        return Repository.getAffairListByDate(date)
    }

    private fun drawMarker(latlng: LatLng,data: AffairForm){
        val markerOptions = MarkerOptions()
        markerOptions.apply {
            position(latlng)
            title(data.ttitle)
            snippet(data.mainContent)
            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_location_on_24))
            setFlat(false)
        }
        mAMap.addMarker(markerOptions).`object` = data
    }

    private fun markerUpdate(){
        mAMap.clear()
        if (affairsToday != null){
            for (data in affairsToday!!){
                drawMarker(LatLng(data.latitude,data.longitude),data)
            }
        }
    }

}