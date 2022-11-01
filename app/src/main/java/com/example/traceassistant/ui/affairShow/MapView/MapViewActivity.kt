package com.example.traceassistant.ui.affairShow.MapView

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView
import com.amap.api.maps.MapsInitializer
import com.amap.api.maps.model.MyLocationStyle
import com.example.traceassistant.R
import com.example.traceassistant.databinding.ActivityMapViewBinding
import com.google.android.material.datepicker.MaterialDatePicker

class MapViewActivity : AppCompatActivity(),AMap.OnMyLocationChangeListener {

    lateinit var mMapView: MapView

    lateinit var binding: ActivityMapViewBinding

    lateinit var mAMap: AMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /**
         * 标题栏
         */
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

        /**
         * 隐私政策
         */
        MapsInitializer.updatePrivacyShow(this, true, true)
        MapsInitializer.updatePrivacyAgree(this, true)

        mMapView = binding.MapviewMap
        mMapView.onCreate(savedInstanceState)

        /**
         * 初始化aMap对象
         */
        if (!this::mAMap.isInitialized){
            mAMap = mMapView.map
        }
        /**
         * 显示定位按钮
         */
        mAMap.uiSettings.isMyLocationButtonEnabled = true

        /**
         * 显示定位蓝点
         */
        showLocationBluepoint()
        mAMap.setOnMyLocationChangeListener(this)
    }

    private fun datePicker()=MaterialDatePicker.Builder.datePicker()
        .setTitleText("选择日期")
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build()

    private fun showLocationBluepoint(){
        val mLocationStyle = MyLocationStyle()
        mLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW)
        mLocationStyle.interval(2000)
        mAMap.myLocationStyle = mLocationStyle
        mAMap.isMyLocationEnabled = true
    }

    /**
     * 定位信息回调
     */
    override fun onMyLocationChange(p0: Location?) {
        Log.d("LocationNow","经度：${p0?.latitude}  纬度：${p0?.longitude}")
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
}