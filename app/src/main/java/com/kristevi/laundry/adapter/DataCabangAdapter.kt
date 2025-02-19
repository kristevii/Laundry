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
import com.kristevi.laundry.ModelData.ModelCabang
import com.kristevi.laundry.R

class DataCabangAdapter(private val listcabang: ArrayList<ModelCabang>) :
    RecyclerView.Adapter<DataCabangAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_cabangg, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listcabang[position]

        holder.tvcardidcabang.text = item.idCabang
        holder.tvnamacabang.text = item.namaCabang
        holder.tvalamatcabang.text = item.alamatCabang
        holder.tvteleponcabang.text = item.noHPCabang
        holder.cardcabang.setOnClickListener {
        }
        holder.btHubungicabang.setOnClickListener {
        }
        holder.btLihatcabang.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return listcabang.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardcabang: CardView = itemView.findViewById(R.id.cardcabang)
        val tvcardidcabang: TextView = itemView.findViewById(R.id.tvcardidcabang)
        val tvnamacabang: TextView = itemView.findViewById(R.id.tvnamacabang)
        val tvteleponcabang: TextView = itemView.findViewById(R.id.tvteleponcabang)
        val tvalamatcabang: TextView = itemView.findViewById(R.id.tvalamatcabang)
        val btHubungicabang: Button = itemView.findViewById(R.id.btHubungicabang)
        val btLihatcabang: Button = itemView.findViewById(R.id.btLihatcabang)
    }
}
