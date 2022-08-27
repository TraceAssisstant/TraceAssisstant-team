package com.example.traceassistant.ui.affairsCollection

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.services.core.PoiItem
import com.example.traceassistant.R
import com.example.traceassistant.Tools.LocalNowLocation

class LocationAdapter(val data: ArrayList<PoiItem>,val activity: CollectionView):RecyclerView.Adapter<LocationAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val tTitle: TextView = view.findViewById(R.id.locationTitle)
        val snippet: TextView = view.findViewById(R.id.locationSite)
        val distance: TextView = view.findViewById(R.id.locationDistance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.location_item,parent,false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: LocationAdapter.ViewHolder, position: Int) {
        val locationItem = data[position]
        holder.tTitle.text = locationItem.title
        holder.snippet.text = locationItem.snippet
        holder.distance.text = "${locationItem.distance}米"
        /**
         * 设置点击事件
         * 点击后移动画面中心至指定地点并标记
         */
        holder.tTitle.setOnClickListener {
            activity.latitude = data[position].latLonPoint.latitude
            activity.longitude = data[position].latLonPoint.longitude
            Log.d("SearchReq","修改坐标为${activity.latitude},${activity.longitude}")

            activity.aMap?.clear(true)

            /**
             * 插入标记点
             */
            var markerOptions = MarkerOptions()
            markerOptions.position(LatLng(data[position].latLonPoint.latitude,data[position].latLonPoint.longitude))
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_add_location_24))
            activity.aMap?.addMarker(markerOptions)

            /**
             * 视角移动
             */
            val cameraPosition = CameraPosition(LatLng(data[position].latLonPoint.latitude,data[position].latLonPoint.longitude),15f,0f,0f)
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            activity.aMap?.moveCamera(cameraUpdate)
        }
        holder.snippet.setOnClickListener {
            activity.latitude = data[position].latLonPoint.latitude
            activity.longitude = data[position].latLonPoint.longitude
            Log.d("SearchReq","修改坐标为${activity.latitude},${activity.longitude}")

            activity.aMap?.clear(true)

            var markerOptions = MarkerOptions()
            markerOptions.position(LatLng(data[position].latLonPoint.latitude,data[position].latLonPoint.longitude))
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_add_location_24))
            activity.aMap?.addMarker(markerOptions)

            val cameraPosition = CameraPosition(LatLng(data[position].latLonPoint.latitude,data[position].latLonPoint.longitude),15f,0f,0f)
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            activity.aMap?.moveCamera(cameraUpdate)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}