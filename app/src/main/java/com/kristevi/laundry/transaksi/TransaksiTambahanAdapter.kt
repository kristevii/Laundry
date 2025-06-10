package com.kristevi.laundry.transaksi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kristevi.laundry.ModelData.ModelTransaksiTambahan
import com.kristevi.laundry.R

class TransaksiTambahanAdapter(
    private val listtambahan: MutableList<ModelTransaksiTambahan>
) : RecyclerView.Adapter<TransaksiTambahanAdapter.ViewHolder>() {

    lateinit var appContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_tambahan_transaksi, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nomor = position + 1
        val item = listtambahan[position]

        holder.tvCardPilihTambahanId.text = "[$nomor]"
        holder.tvnamapilihtambahan.text = item.namaTambahan
        holder.tvhargapilihtambahan.text = appContext.getString(R.string.harga, item.hargaTambahan)

        // Aksi hapus
        holder.btnhapuspilihtambahan.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                listtambahan.removeAt(pos)
                notifyItemRemoved(pos)
                notifyItemRangeChanged(pos, listtambahan.size)
            }
        }

        // Aksi klik item untuk kembali ke activity sebelumnya
        holder.cardpilihtambahan.setOnClickListener {
            val intent = Intent()
            intent.putExtra("idTambahan", item.idTambahan)
            intent.putExtra("namaTambahan", item.namaTambahan)
            intent.putExtra("hargaTambahan", item.hargaTambahan)
            (appContext as Activity).setResult(Activity.RESULT_OK, intent)
            (appContext as Activity).finish()
        }
    }

    override fun getItemCount(): Int = listtambahan.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCardPilihTambahanId: TextView = itemView.findViewById(R.id.tvCardPilihTambahanId)
        val tvnamapilihtambahan: TextView = itemView.findViewById(R.id.tvnamapilihtambahan)
        val tvhargapilihtambahan: TextView = itemView.findViewById(R.id.tvhargapilihtambahan)
        val btnhapuspilihtambahan: ImageView = itemView.findViewById(R.id.btnhapuspilihtambahan)
        val cardpilihtambahan: CardView = itemView.findViewById(R.id.cardpilihtambahan)
    }
}
