package com.example.traceassistant.ui.affairShow.MapView

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.maps.AMap
import com.amap.api.maps.model.Marker
import com.example.traceassistant.R
import com.example.traceassistant.Tools.toDateTimeObscure
import com.example.traceassistant.logic.Entity.AffairForm

/**
 * 用于自定义地图标记点InfoWindow
 */
class InfoWindowAdapter(val activity: AppCompatActivity):AMap.InfoWindowAdapter {
    var infoWindow: View? = null

    override fun getInfoWindow(p0: Marker?): View? {
        infoWindow = LayoutInflater.from(activity).inflate(R.layout.info_window,null)
        render(p0,infoWindow)
        return infoWindow
    }

    override fun getInfoContents(p0: Marker?): View? {
        return null
    }

    private fun render(marker: Marker?,view: View?){
        if (marker != null && view != null){
            val infoTitle: TextView = view.findViewById(R.id.infoTitle)
            val infoContent: TextView = view.findViewById(R.id.infoContent)
            val infoTime: TextView = view.findViewById(R.id.infoTime)

            val data: AffairForm = marker.`object` as AffairForm
            infoTitle.text = marker.title
            infoContent.text = marker.snippet
            infoTime.text = toDateTimeObscure(data.time)
        }
    }

}