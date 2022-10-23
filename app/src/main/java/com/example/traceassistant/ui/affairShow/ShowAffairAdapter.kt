package com.example.traceassistant.ui.affairShow

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.traceassistant.R
import com.example.traceassistant.Tools.toDateTimeObscure
import com.example.traceassistant.logic.Entity.AffairForm
import com.example.traceassistant.logic.Repository
import com.google.android.material.divider.MaterialDivider

class ShowAffairAdapter(val activity: Context,val data: List<AffairForm>): RecyclerView.Adapter<ShowAffairAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val atitle: TextView = view.findViewById(R.id.showlistitem_title)
        val atime: TextView = view.findViewById(R.id.showlistitem_time)
        val alevel: TextView = view.findViewById(R.id.showlistitem_level)
        val atag: TextView = view.findViewById(R.id.showlistitem_tag)
        val checkBox: CheckBox = view.findViewById(R.id.showlistitem_checkbox)
        val showViewItemView: ConstraintLayout = view.findViewById(R.id.showViewItemView)
        val divider: MaterialDivider = view.findViewById(R.id.completeDivider)
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

        if (affairForm.state == 1){
            holder.checkBox.isChecked = true
            holder.showViewItemView.setBackgroundColor(Color.parseColor("#74D67E"))
            holder.divider.visibility = View.VISIBLE
        }

        /**
         * 完成事务勾选框
         */
        holder.checkBox.setOnClickListener {
            val checkBox = it as CheckBox
            if (checkBox.isChecked){
                Repository.updateDateAffair(affairForm.id,1)
                holder.showViewItemView.setBackgroundColor(Color.parseColor("#74D67E"))
                holder.divider.visibility = View.VISIBLE
            }else{
                Repository.updateDateAffair(affairForm.id,0)
                holder.showViewItemView.setBackgroundColor(Color.WHITE)
                holder.divider.visibility = View.GONE
            }

        }

        /**
         * 跳转详情
         */

        holder.atime.setOnClickListener {
            val intent = Intent(activity,ShowEditAffair::class.java)
            val bundle = Bundle()
            bundle.putSerializable("affair",affairForm)
            intent.putExtra("affair",bundle)
            activity.startActivity(intent)
        }
        holder.atitle.setOnClickListener {
            val intent = Intent(activity,ShowEditAffair::class.java)
            val bundle = Bundle()
            bundle.putSerializable("affair",affairForm)
            intent.putExtra("affair",bundle)
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = data.size

}