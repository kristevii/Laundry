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

        // Pastikan idPegawai tidak null atau kosong, jika null tampilkan "Tidak ada ID"
        holder.tvCardPegawaiId.text = item.idPegawai ?: "Tidak ada ID"
        holder.tvnamapegawai.text = item.namaPegawai ?: "Nama tidak tersedia"
        holder.tvalamatpegawai.text = item.alamatPegawai ?: "Alamat tidak tersedia"
        holder.tvnohppegawai.text = item.noHPPegawai ?: "No HP tidak tersedia"
        holder.tvterdaftarpegawai.text = item.terdaftar ?: "Status tidak tersedia"

        holder.cardpegawai.setOnClickListener {
            // Implementasi untuk card pegawai, misalnya navigasi ke detail pegawai
        }

        holder.btHubungipegawai.setOnClickListener {
            // Implementasi untuk tombol Hubungi
        }

        holder.btLihatpegawai.setOnClickListener {
            // Implementasi untuk tombol Lihat
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
        val tvterdaftarpegawai: TextView = itemView.findViewById(R.id.tvterdaftarpegawai)
        val btHubungipegawai: Button = itemView.findViewById(R.id.btHubungipegawai)
        val btLihatpegawai: Button = itemView.findViewById(R.id.btLihatpegawai)
    }
}
