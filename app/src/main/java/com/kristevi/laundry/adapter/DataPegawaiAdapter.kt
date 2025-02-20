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
import com.kristevi.laundry.ModelData.ModelPegawai

class DataPegawaiAdapter(private val listpegawai: ArrayList<ModelPegawai>) :
    RecyclerView.Adapter<DataPegawaiAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pegawaii, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listpegawai[position]

        holder.tvCardPegawaiId.text = "ID Pegawai  : ${item.idPegawai}"
        holder.tvnamapegawai.text = "Nama  : ${item.namaPegawai}"
        holder.tvalamatpegawai.text = "Alamat  : ${item.alamatPegawai}"
        holder.tvnohppegawai.text = "Telepon  : ${item.noHPPegawai}"
        holder.tvcabangpegawai.text = "Cabang  : ${item.cabangPegawai}"
        holder.tvterdaftarpegawai.text = item.terdaftar
        holder.cardpegawai.setOnClickListener {
        }
        holder.btHubungipegawai.setOnClickListener {
        }
        holder.btLihatpegawai.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return listpegawai.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardpegawai: CardView = itemView.findViewById(R.id.cardpegawai)
        val tvCardPegawaiId: TextView = itemView.findViewById(R.id.tvCardPegawaiId)
        val tvnamapegawai: TextView = itemView.findViewById(R.id.tvnamapegawai)
        val tvnohppegawai: TextView = itemView.findViewById(R.id.tvnohppegawai)
        val tvalamatpegawai: TextView = itemView.findViewById(R.id.tvalamatpegawai)
        val tvcabangpegawai: TextView = itemView.findViewById(R.id.tvcabangpegawai)
        val tvterdaftarpegawai: TextView = itemView.findViewById(R.id.tvterdaftarpegawai)
        val btHubungipegawai: Button = itemView.findViewById(R.id.btHubungipegawai)
        val btLihatpegawai: Button = itemView.findViewById(R.id.btLihatpegawai)
    }
}
