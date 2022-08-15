package com.example.traceassistant.ui.affairsCollection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.services.core.PoiItem
import com.example.traceassistant.R

class LocationAdapter(val data: ArrayList<PoiItem>):RecyclerView.Adapter<LocationAdapter.ViewHolder>() {
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
        holder.distance.text = locationItem.distance.toString()
        /**
         * 设置点击事件
         * 点击后移动画面中心至指定地点并标记
         */
        holder.tTitle.setOnClickListener {

        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}