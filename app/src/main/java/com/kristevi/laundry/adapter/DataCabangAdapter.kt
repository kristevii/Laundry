package com.kristevi.laundry.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.database.DatabaseReference
import com.kristevi.laundry.ModelData.ModelCabang
import com.kristevi.laundry.R
import com.kristevi.laundry.cabang.TambahCabangActivity
import com.kristevi.laundry.pegawai.TambahPegawaiActivity

class DataCabangAdapter(private val listcabang: ArrayList<ModelCabang>) :
    RecyclerView.Adapter<DataCabangAdapter.ViewHolder>() {

    lateinit var appContext : Context
    lateinit var databaseReference: DatabaseReference

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_cabangg, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listcabang[position]

        holder.tvcardidcabang.text = "ID Cabang : ${item.idCabang}"
        holder.tvnamacabang.text = item.namaCabang
        holder.tvalamatcabang.text = "Alamat : ${item.alamatCabang}"
        holder.tvteleponcabang.text = "Telepon : ${item.noHPCabang}"
        holder.tvlayanancabang.text = "Layanan : ${item.layananCabang}"
        holder.cardcabang.setOnClickListener {
            val intent = Intent(appContext, TambahCabangActivity::class.java)
            intent.putExtra("judul",  "Edit Cabang")
            intent.putExtra("idCabang", item.idCabang)
            intent.putExtra("namaCabang", item.namaCabang)
            intent.putExtra("noHPCabang", item.noHPCabang)
            intent.putExtra("alamatCabang", item.alamatCabang)
            intent.putExtra("layananCabang", item.layananCabang)
            appContext.startActivity(intent)
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
        val tvlayanancabang: TextView = itemView.findViewById(R.id.tvlayanancabang)
        val btHubungicabang: Button = itemView.findViewById(R.id.btHubungicabang)
        val btLihatcabang: Button = itemView.findViewById(R.id.btLihatcabang)
    }
}
