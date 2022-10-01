package com.example.traceassistant.ui.affairShow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.traceassistant.R
import com.example.traceassistant.Tools.toDateTimeObscure
import com.example.traceassistant.logic.Entity.AffairForm

class ShowAffairAdapter(val data: List<AffairForm>): RecyclerView.Adapter<ShowAffairAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val atitle: TextView = view.findViewById(R.id.showlistitem_title)
        val atime: TextView = view.findViewById(R.id.showlistitem_time)
        val alevel: TextView = view.findViewById(R.id.showlistitem_level)
        val atag: TextView = view.findViewById(R.id.showlistitem_tag)
        val checkBox: CheckBox = view.findViewById(R.id.showlistitem_checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.showview_listitem,parent,false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val affairForm = data[position]
        holder.atitle.text = affairForm.ttitle
        holder.atime.text = toDateTimeObscure(affairForm.time)
        holder.alevel.text = affairForm.level.toString()
        holder.atag.text = affairForm.tag
    }

    override fun getItemCount(): Int = data.size

}