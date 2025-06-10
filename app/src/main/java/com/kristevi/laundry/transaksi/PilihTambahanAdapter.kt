package com.kristevi.laundry.transaksi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kristevi.laundry.ModelData.ModelLayanan
import com.kristevi.laundry.ModelData.ModelTambahan
import com.kristevi.laundry.R

class PilihTambahanAdapter (
    private val listtambahan: ArrayList<ModelTambahan>):
    RecyclerView.Adapter<PilihTambahanAdapter.ViewHolder>() {

    lateinit var appContext : Context
    lateinit var databaseReference: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_pilih_tambahan, parent,false)

        appContext = parent.context
        databaseReference = FirebaseDatabase.getInstance().getReference("pelanggan")
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nomor = position + 1
        val item = listtambahan[position]
        holder.tvCardPilihTambahanId.text = "[$nomor]"
        holder.tvnamapilihtambahan.text = item.namaTambahan ?: ""
        holder.tvhargapilihtambahan.text = appContext.getString(R.string.harga, item.hargaTambahan)
        holder.cardpilihtambahan.setOnClickListener {
            val intent = Intent().apply {
                putExtra("idTambahan", item.idTambahan ?: "")
                putExtra("namaTambahan", item.namaTambahan ?: "")
                putExtra("hargaTambahan", item.hargaTambahan ?: "")
            }

            if (appContext is Activity) {
                (appContext as Activity).setResult(Activity.RESULT_OK, intent)
                (appContext as Activity).finish()
            } else {
                Toast.makeText(appContext, appContext.getString(R.string.gagalkirimdata), Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun getItemCount(): Int = listtambahan.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val cardpilihtambahan : CardView = itemView.findViewById(R.id.cardpilihtambahan)
        val tvCardPilihTambahanId : TextView = itemView.findViewById(R.id.tvCardPilihTambahanId)
        val tvnamapilihtambahan : TextView = itemView.findViewById(R.id.tvnamapilihtambahan)
        val tvhargapilihtambahan : TextView = itemView.findViewById(R.id.tvhargapilihtambahan)
    }
}