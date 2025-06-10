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
import com.kristevi.laundry.ModelData.ModelPelanggan
import com.kristevi.laundry.R

class PilihPelangganAdapter (
    private val listpelanggan: ArrayList<ModelPelanggan>):
    RecyclerView.Adapter<PilihPelangganAdapter.ViewHolder>() {

    lateinit var appContext : Context
    lateinit var databaseReference: DatabaseReference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_pilih_pelanggan, parent,false)
        appContext = parent.context
        databaseReference = FirebaseDatabase.getInstance().getReference("pelanggan")
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nomor = position + 1
        val item = listpelanggan[position]
        holder.tvpilihCardPelangganId.text = "[$nomor]"
        holder.tvnamapilihpelanggan.text = item.namaPelanggan ?: ""
        holder.tvalamatpilihpelanggan.text = appContext.getString(R.string.alamat, item.alamatPelanggan)
        holder.tvnohppilihpelanggan.text = appContext.getString(R.string.telepon, item.noHPPelanggan)
        holder.cardpilihpelanggan.setOnClickListener {
            val intent = Intent(appContext, TransaksiiActivity::class.java)
            intent.putExtra("idPelanggan", item.idPelanggan ?: "")
            intent.putExtra("namaPelanggan", item.namaPelanggan ?: "")
            intent.putExtra("alamatPelanggan", item.alamatPelanggan ?: "")
            intent.putExtra("noHPPelanggan", item.noHPPelanggan ?: "")
            (appContext as Activity).setResult(Activity.RESULT_OK, intent)
            (appContext as Activity).finish()
        }
    }

    override fun getItemCount(): Int = listpelanggan.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val cardpilihpelanggan : CardView = itemView.findViewById(R.id.cardpilihpelanggan)
        val tvpilihCardPelangganId : TextView = itemView.findViewById(R.id.tvpilihCardPelangganId)
        val tvnamapilihpelanggan : TextView = itemView.findViewById(R.id.tvnamapilihpelanggan)
        val tvalamatpilihpelanggan : TextView = itemView.findViewById(R.id.tvalamatpilihpelanggan)
        val tvnohppilihpelanggan : TextView = itemView.findViewById(R.id.tvnohppilihpelanggan)
    }
}