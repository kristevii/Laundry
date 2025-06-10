package com.kristevi.laundry.transaksi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kristevi.laundry.ModelData.ModelLayanan
import com.kristevi.laundry.R

class PilihLayananAdapter (
    private val listlayanan: ArrayList<ModelLayanan>):
    RecyclerView.Adapter<PilihLayananAdapter.ViewHolder>() {

    lateinit var appContext : Context
    lateinit var databaseReference: DatabaseReference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_pilih_layanan, parent,false)
        appContext = parent.context
        databaseReference = FirebaseDatabase.getInstance().getReference("pelanggan")
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nomor = position + 1
        val item = listlayanan[position]
        holder.tvCardPilihLayananId.text = "[$nomor]"
        holder.tvnamapilihlayanan.text = item.namaLayanan ?: ""
        holder.tvhargapilihlayanan.text = appContext.getString(R.string.harga, item.hargaLayanan)
        holder.cardpilihlayanan.setOnClickListener {
            val intent = Intent(appContext, TransaksiiActivity::class.java)
            intent.putExtra("idLayanan", item.idLayanan ?: "")
            intent.putExtra("namaLayanan", item.namaLayanan ?: "")
            intent.putExtra("hargaLayanan", item.hargaLayanan ?: "")
            (appContext as Activity).setResult(Activity.RESULT_OK, intent)
            (appContext as Activity).finish()
        }
    }

    override fun getItemCount(): Int = listlayanan.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val cardpilihlayanan : CardView = itemView.findViewById(R.id.cardpilihlayanan)
        val tvCardPilihLayananId : TextView = itemView.findViewById(R.id.tvCardPilihLayananId)
        val tvnamapilihlayanan : TextView = itemView.findViewById(R.id.tvnamapilihlayanan)
        val tvhargapilihlayanan : TextView = itemView.findViewById(R.id.tvhargapilihlayanan)
    }
}