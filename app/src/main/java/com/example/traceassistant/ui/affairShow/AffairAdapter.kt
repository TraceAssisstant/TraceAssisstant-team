package com.example.traceassistant.ui.affairShow

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.traceassistant.R
import com.example.traceassistant.Tools.GlobalApplication
import com.example.traceassistant.logic.Entity.AffairForm
import java.text.SimpleDateFormat

class AffairAdapter(val data: List<AffairForm>): RecyclerView.Adapter<AffairAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.show_title)
        val time: TextView = view.findViewById(R.id.show_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.affair_item,parent,false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val affair = data[position]
        holder.title.text = affair.ttitle
        holder.time.text = SimpleDateFormat("HH:mm").format(affair.time)

        val dataP: AffairForm = data[position]

        holder.title.setOnClickListener(){
            val intent = Intent(it.context,AlterView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            /**
             * @return String
             */
            intent.putExtra("title",dataP.ttitle)

            /**
             * @return String
             */
            intent.putExtra("content",dataP.mainContent)

            /**
             * @return String (yyyy-MM-dd)
             */
            intent.putExtra("date",dataP.date)

            /**
             * @return String (HH:mm)
             */
            intent.putExtra("time",SimpleDateFormat("HH-mm").format(dataP.time))

            /**
             * @return Int
             */
            intent.putExtra("level",dataP.level)

            /**
             * @return String
             */
            intent.putExtra("tag",dataP.tag)

            /**
             * @return Boolean
             */
            intent.putExtra("ring",dataP.ringMusic)

            /**
             * @return Boolean
             */
            intent.putExtra("vibration",dataP.isShake)

            it.context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return data.size
    }
}