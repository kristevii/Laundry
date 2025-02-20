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
import com.kristevi.laundry.ModelData.ModelTambahan

class DataTambahanAdapter(private val listtambahan: ArrayList<ModelTambahan>) :
    RecyclerView.Adapter<DataTambahanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_tambahann, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listtambahan[position]

        holder.tvCardTambahanId.text = "ID Tambahan : ${item.idTambahan}"
        holder.tvnamatambahan.text = item.namaTambahan
        holder.tvhargatambahan.text = "Harga : Rp. ${item.hargaTambahan}"
        holder.cardtambahan.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return listtambahan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardtambahan: CardView = itemView.findViewById(R.id.cardtambahan)
        val tvCardTambahanId: TextView = itemView.findViewById(R.id.tvCardTambahanId)
        val tvnamatambahan: TextView = itemView.findViewById(R.id.tvnamatambahan)
        val tvhargatambahan: TextView = itemView.findViewById(R.id.tvhargatambahan)
    }
}
