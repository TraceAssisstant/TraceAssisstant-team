package com.example.traceassistant.Tools

import android.util.Log
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener

object LocalNowLocation: AMapLocationListener {

    val mLocationClient: AMapLocationClient = AMapLocationClient(GlobalApplication.context)
    val mLocationOption: AMapLocationClientOption = AMapLocationClientOption()
    private lateinit var MyLocation: AMapLocation

    fun initialize(){
        mLocationClient.setLocationListener(this)
        /**
         * 设定高精度模式
         * 同时需要网络+GPS
         */
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
        /**
         * 设置默认需要返回地址信息
         */
        mLocationOption.setNeedAddress(true)

        mLocationOption.setInterval(1000)

//        mLocationOption.setOnceLocation(true)
//        mLocationOption.setOnceLocationLatest(true)

        mLocationClient.setLocationOption(mLocationOption)
    }

    /**
     * 调用一次该方法就会进行一次定位
     */
    fun startLocation(){
        mLocationClient.startLocation()
        Log.d("Location","start")
    }
    fun stopLocation(){
        mLocationClient.stopLocation()
        Log.d("Location","stop")
    }

    override fun onLocationChanged(amapLocation: AMapLocation?) {
        if (amapLocation != null){
            if (amapLocation.errorCode == 0){
                /**
                 * 解析AMapLocation
                 * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                 * amapLocation.getLatitude();//获取纬度
                 * amapLocation.getLongitude();//获取经度
                 * amapLocation.getAccuracy();//获取精度信息
                 * amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                 * amapLocation.getCountry();//国家信息
                 * amapLocation.getProvince();//省信息
                 * amapLocation.getCity();//城市信息
                 * amapLocation.getDistrict();//城区信息
                 * amapLocation.getStreet();//街道信息
                 * amapLocation.getStreetNum();//街道门牌号信息
                 * amapLocation.getCityCode();//城市编码
                 * amapLocation.getAdCode();//地区编码
                 * amapLocation.getAoiName();//获取当前定位点的AOI信息
                 * amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                 * amapLocation.getFloor();//获取当前室内定位的楼层
                 * amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
                 */

                MyLocation = amapLocation
//                Log.d("Location","succeed!  city:${MyLocation.city} latitude:${MyLocation.latitude}  longtitude:${MyLocation.longitude}")
            }else{
                Log.d("LocationError","errorCode:${amapLocation.errorCode}//errorInfo:${amapLocation.errorInfo}")
            }
        }
    }

    fun getLocation(): AMapLocation? {
        if (this::MyLocation.isInitialized){
            return MyLocation
        }else{
            Log.d("LocationErrpr","no Location")
            return null
        }
    }

}