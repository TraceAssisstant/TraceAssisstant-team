package com.example.traceassistant.ui.affairsCollection

import android.content.Intent
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapsInitializer
import com.amap.api.maps.TextureMapView
import com.amap.api.maps.model.*
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.example.traceassistant.R
import com.example.traceassistant.Tools.LocalNowLocation
import com.example.traceassistant.Tools.showToast
import com.example.traceassistant.databinding.ActivityLocationCollectionViewBinding
import com.example.traceassistant.ui.main.MainView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LocationCollectionView : AppCompatActivity(), PoiSearch.OnPoiSearchListener,
    AMap.OnPOIClickListener, AMap.OnMapClickListener,AMap.OnMyLocationChangeListener {
    lateinit var binding: ActivityLocationCollectionViewBinding

    private lateinit var mMapView: TextureMapView

    var aMap: AMap? = null

    var latitude: Double = 0.0
    var longitude: Double = 0.0

    private var nowLat: Double = 0.0
    private var nowLong: Double = 0.0

    private lateinit var marker: Marker

    private lateinit var query: PoiSearch.Query

    val viewModel by lazy { ViewModelProvider(this).get(CollectionViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationCollectionViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("viewmodel", TempInsertData.toString())

        /**
         * 地图模块
         */
        MapsInitializer.updatePrivacyShow(this, true, true)
        MapsInitializer.updatePrivacyAgree(this, true)

        mMapView = binding.collectionMap
        mMapView.onCreate(savedInstanceState)
        if (aMap == null) {
            aMap = mMapView.map
        }


        /**
         * 地图定位蓝点
         */
        val myLocationStyle: MyLocationStyle = MyLocationStyle()
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        myLocationStyle.interval(2000)
        aMap?.uiSettings?.isMyLocationButtonEnabled = true
        aMap?.myLocationStyle = myLocationStyle
        aMap?.isMyLocationEnabled = true
        aMap?.setOnMapClickListener(this)
        aMap?.setOnMyLocationChangeListener(this)
        aMap?.showIndoorMap(true)

        /**
         * 地理搜索
         */
        binding.searchLocationBtn.setOnClickListener {
            query = PoiSearch.Query(
                binding.searchLocation.text.toString(),
                "",
                LocalNowLocation.getLocation()?.cityCode.toString()
            )
            query.pageSize = 10
            query.pageNum = 1
            query.isDistanceSort = true

            val poiSearch = PoiSearch(this, query)
            poiSearch.setOnPoiSearchListener(this)
            poiSearch.bound = PoiSearch.SearchBound(LatLonPoint(nowLat, nowLong), 4000)
            poiSearch.searchPOIAsyn()

        }

        /**
         * 数据提交
         */
        binding.submitAll.setOnClickListener {
            TempInsertData.alongitude = longitude
            TempInsertData.alatitude = latitude

            Log.d("insert", TempInsertData.getAffair().toString())

            TempInsertData.atime /= 1000

            viewModel.insertAffair(TempInsertData.getAffair())

            val intent = Intent(this, MainView::class.java)
            startActivity(intent)
            MainView.MyActivityNow.activity?.finish()
        }

        /**
         * 返回Fragment
         */
        binding.backToCollection.setOnClickListener {
            finish()
        }
    }


    /**
     * 重启activity
     */
    private fun refresh() {
        finish()
        val intent = Intent(this, LocationCollectionView::class.java)
        LocalNowLocation.startLocation()
        startActivity(intent)
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
        val cameraPosition = CameraPosition(LatLng(nowLat, nowLong), 15f, 0f, 0f)
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        aMap?.moveCamera(cameraUpdate)
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
        if (this::marker.isInitialized) {
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
        Log.d("坐标", "$latitude,$longitude")
    }

    /**
     * 处理搜索结果
     */
    override fun onPoiSearched(result: PoiResult?, rCode: Int) {
        if (rCode == 1000) {
            Log.d("SearchReq", "Succeed")
            if (result != null) {
                val listPOI = result.pois
                if (result.pois.size <= 0) {
                    Log.d("SearchReq", "4KM内已无更多符合条件的地点")
                    "4KM内已无更多符合条件的地点".showToast()
                    query.pageNum -= 1
                    return
                }

                for (i in listPOI) {
                    Log.d("SearchReq", "${i.title}--${i.snippet}")
                }

                /**
                 * 显示结果
                 */
                binding.locationList.visibility = View.VISIBLE
                binding.nextPage.visibility = View.VISIBLE
                binding.lastPage.visibility = View.VISIBLE
                val layoutManager = LinearLayoutManager(this)
                binding.locationList.layoutManager = layoutManager
                val adapter = LocationAdapter(listPOI, this)
                binding.locationList.adapter = adapter

                binding.nextPage.setOnClickListener {
                    if (this::query.isInitialized) {
                        query.pageNum += 1

                        val poiSearch = PoiSearch(this, query)
                        poiSearch.setOnPoiSearchListener(this)
                        poiSearch.bound = PoiSearch.SearchBound(LatLonPoint(nowLat, nowLong), 4000)
                        poiSearch.searchPOIAsyn()
                    }
                }
                binding.lastPage.setOnClickListener {
                    if (query.pageNum >= 1) {
                        query.pageNum -= 1

                        val poiSearch = PoiSearch(this, query)
                        poiSearch.setOnPoiSearchListener(this)
                        poiSearch.bound = PoiSearch.SearchBound(LatLonPoint(nowLat, nowLong), 4000)
                        poiSearch.searchPOIAsyn()
                    }
                }

            }
        } else {
            Log.d("SearchReq", "Fail")
        }

    }

    /**
     * 地理信息回调
     */
    override fun onMyLocationChange(p0: Location?) {
        if (p0 != null) {
            nowLat = p0.latitude
            nowLong = p0.longitude
            Log.d("LocationNow","经度：${p0.latitude}  纬度：${p0.longitude}")
        }
    }

    override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun onPOIClick(p0: Poi?) {
        TODO("Not yet implemented")
    }

}