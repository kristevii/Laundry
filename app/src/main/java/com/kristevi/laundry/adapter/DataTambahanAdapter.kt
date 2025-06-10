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
import com.kristevi.laundry.R
import com.kristevi.laundry.ModelData.ModelTambahan
import com.kristevi.laundry.pegawai.TambahPegawaiActivity
import com.kristevi.laundry.tambahan.TambahTambahanActivity

class DataTambahanAdapter(private val listtambahan: ArrayList<ModelTambahan>) :
    RecyclerView.Adapter<DataTambahanAdapter.ViewHolder>() {

    lateinit var appContext : Context
    lateinit var databaseReference: DatabaseReference

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_tambahann, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listtambahan[position]

        holder.tvCardTambahanId.text = appContext.getString(R.string.idtambahan, item.idTambahan)
        holder.tvnamatambahan.text = item.namaTambahan
        holder.tvhargatambahan.text = appContext.getString(R.string.harga, item.hargaTambahan)
        holder.cardtambahan.setOnClickListener {
            val intent = Intent(appContext, TambahTambahanActivity::class.java)
            intent.putExtra("judul",  appContext.getString(R.string.tvjuduledittambahan))
            intent.putExtra("idTambahan", item.idTambahan)
            intent.putExtra("namaTambahan", item.namaTambahan)
            intent.putExtra("hargaTambahan", item.hargaTambahan)
            appContext.startActivity(intent)
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
