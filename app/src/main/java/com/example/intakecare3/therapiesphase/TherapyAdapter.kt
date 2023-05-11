package com.example.intakecare3.therapiesphase

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.connectingtomdb.R
import com.example.intakecare3.menuaccess.TherapyLogsActivity

class TherapyAdapter(val context: Context, var therapyList: List<TherapyDetails>, clickListener: ClickedListener ) : RecyclerView.Adapter<TherapyAdapter.ViewHolder>() {

    private var clickListener: ClickedListener = clickListener

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var infoTherapy: TextView
        var theLogo: ImageView
        var imageMore: ImageView

        init {
            infoTherapy = itemView.findViewById(R.id.infoTherapy)
            theLogo = itemView.findViewById(R.id.theLogo)
            imageMore = itemView.findViewById(R.id.imageMore)
        }

    }

    public fun setData(therapyModel: List<TherapyDetails>) {
        this.therapyList = therapyModel
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_items, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val therapyModel = therapyList[position]

        holder.infoTherapy.text = therapyList[position].drug.toString()
        holder.itemView.setOnClickListener {
            clickListener.clickedItem(therapyModel)
        }
    }

    override fun getItemCount(): Int {
        //return therapy list declared in the adapter
        return therapyList.size
    }

    interface ClickedListener {
        fun clickedItem(therapyModel: TherapyDetails)
    }
}