package com.example.traceassistant.ui.affairShow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.traceassistant.R
import com.example.traceassistant.logic.Entity.AffairForm
import java.text.SimpleDateFormat

class AffairAdapter(val data: List<AffairForm>): RecyclerView.Adapter<AffairAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.show_title)
        val time: TextView = view.findViewById(R.id.show_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.affair_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val affair = data[position]
        holder.title.text = affair.ttitle
        holder.time.text = SimpleDateFormat("HH:mm").format(affair.time)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}