package com.kristevi.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kristevi.laundry.R
import com.kristevi.laundry.ModelData.ModelLayanan

class DataLayananAdapter(private val listlayanan: ArrayList<ModelLayanan>) :
    RecyclerView.Adapter<DataLayananAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_layanann, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listlayanan[position]

        holder.tvcardidlayanan.text = item.idLayanan
        holder.tvnamalayanan.text = item.namaLayanan
        holder.tvhargalayanan.text = item.hargaLayanan
        holder.tvcabanglayanan.text = item.cabangLayanan
        holder.cardlayanan.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return listlayanan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardlayanan: CardView = itemView.findViewById(R.id.cardlayanan)
        val tvcardidlayanan: TextView = itemView.findViewById(R.id.tvcardidlayanan)
        val tvnamalayanan: TextView = itemView.findViewById(R.id.tvnamalayanan)
        val tvhargalayanan: TextView = itemView.findViewById(R.id.tvhargalayanan)
        val tvcabanglayanan: TextView = itemView.findViewById(R.id.tvcabanglayanan)
    }
}
