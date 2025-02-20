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
import com.kristevi.laundry.ModelData.ModelPelanggan

class DataPelangganAdapter (
    private val listpelanggan: ArrayList<ModelPelanggan>) :
    RecyclerView.Adapter<DataPelangganAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pelanggann, parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder,position:Int){
        val item = listpelanggan[position]
        holder.tvCardPelangganId.text = "Id Pelanggan : ${item.idPelanggan}"
        holder.tvnamapelanggan.text = "Nama     : ${item.namaPelanggan}"
        holder.tvalamatpelanggan.text = "Alamat    : ${item.alamatPelanggan}"
        holder.tvnohppelanggan.text = "Telepon  : ${item.noHPPelanggan}"
        holder.tvterdaftarpelanggan.text = item.terdaftar
        holder.cardpelanggan.setOnClickListener {
        }
        holder.btHubungipelanggan.setOnClickListener {
        }
        holder.btLihatpelanggan.setOnClickListener{
        }
    }

    override fun getItemCount(): Int {
        return listpelanggan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardpelanggan : CardView = itemView.findViewById(R.id.cardpelanggan)
        val tvCardPelangganId : TextView = itemView.findViewById(R.id.tvCardPelangganId)
        val tvnamapelanggan : TextView  = itemView.findViewById(R.id.tvnamapelanggan)
        val tvnohppelanggan : TextView  = itemView.findViewById(R.id.tvnohppelanggan)
        val tvalamatpelanggan : TextView  = itemView.findViewById(R.id.tvalamatpelanggan)
        val tvterdaftarpelanggan : TextView  = itemView.findViewById(R.id.tvterdaftarpelanggan)
        val btHubungipelanggan : Button = itemView.findViewById(R.id.btHubungipelanggan)
        val btLihatpelanggan : Button= itemView.findViewById(R.id.btLihatpelanggan)
    }
}



