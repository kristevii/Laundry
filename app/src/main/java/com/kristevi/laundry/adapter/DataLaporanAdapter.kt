package com.kristevi.laundry.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.kristevi.laundry.R
import com.kristevi.laundry.ModelData.ModelLaporan
import com.kristevi.laundry.ModelData.ModelTransaksiTambahan
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DataLaporanAdapter(
    private val listlaporan: ArrayList<ModelLaporan>,
    private val listtambahan: ArrayList<ModelTransaksiTambahan>,
    private val context: Context
) : RecyclerView.Adapter<DataLaporanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_laporan, parent, false)
        return ViewHolder(view)
    }

    fun addNewLaporan(newLaporan: ModelLaporan) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val newDate = dateFormat.parse(newLaporan.waktuTransaksi)
        var insertPosition = listlaporan.size
        for (i in listlaporan.indices) {
            val currentDate = dateFormat.parse(listlaporan[i].waktuTransaksi)
            if (newDate.before(currentDate)) {
                insertPosition = i
                break
            }
        }
        listlaporan.add(insertPosition, newLaporan)
        notifyItemInserted(insertPosition)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listlaporan[position]
        holder.tvidlaporan.text = "[${position + 1}]"
        holder.tvnamapelangganlaporan.text = item.namaPelanggan
        holder.tvjenislayananlaporan.text = item.jenisLayanan
        holder.tvjumlahtambahanlaporan.text = item.jumlahTambahan
        holder.tvjmlbayarlaporan.text = item.jumlahTotalBayar
        holder.tvwaktutanggallaporan.text = item.waktuTransaksi

        val ambilSekarang = context.getString(R.string.ambilsekarang)
        val bayarSekarang = context.getString(R.string.bayarsekarang)
        val selesai = context.getString(R.string.selesai)
        val lunas = context.getString(R.string.lunas)
        val belumDibayar = context.getString(R.string.belumbayar)
        when (item.statusPembayaran) {
            "Lunas" -> {
                holder.tvbelumbayarlaporan.setBackgroundResource(R.drawable.bg_status_hijau)
                holder.tvbelumbayarlaporan.setTextColor(ContextCompat.getColor(context, R.color.green))
                holder.tvbelumbayarlaporan.text = lunas
            }
            else -> {
                holder.tvbelumbayarlaporan.setBackgroundResource(R.drawable.bg_status_merah)
                holder.tvbelumbayarlaporan.setTextColor(ContextCompat.getColor(context, R.color.red))
                holder.tvbelumbayarlaporan.text = belumDibayar
            }
        }

        if (item.statusPembayaran == "Lunas") {
            holder.buttonbayarsekaranglaporan.text = ambilSekarang
            holder.buttonbayarsekaranglaporan.visibility = View.VISIBLE
            holder.buttonbayarsekaranglaporan.backgroundTintList = ContextCompat.getColorStateList(context, R.color.blue)
            holder.buttonbayarsekaranglaporan.setTextColor(ContextCompat.getColor(context, R.color.white))

            if (item.statusPengambilan == "Diambil") {
                holder.buttonbayarsekaranglaporan.visibility = View.GONE
                holder.tvpengambilanlaporan.visibility = View.VISIBLE
                holder.tvpengambilanlaporan.text = context.getString(R.string.diambilpada, item.waktuPengambilan)
                holder.tvbelumbayarlaporan.text = selesai
                holder.tvbelumbayarlaporan.setBackgroundResource(R.drawable.bg_status_kuning)
                holder.tvbelumbayarlaporan.setTextColor(ContextCompat.getColor(context, R.color.orange))
            }
        } else {
            holder.buttonbayarsekaranglaporan.text = bayarSekarang
            holder.buttonbayarsekaranglaporan.visibility = View.VISIBLE
            holder.buttonbayarsekaranglaporan.backgroundTintList = ContextCompat.getColorStateList(context, R.color.red)
            holder.buttonbayarsekaranglaporan.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.tvpengambilanlaporan.visibility = View.GONE
        }

        holder.buttonbayarsekaranglaporan.setOnClickListener {
            when (holder.buttonbayarsekaranglaporan.text) {
                bayarSekarang -> showPaymentDialog(holder, position, item)
                ambilSekarang -> confirmItemTaken(holder, position, item)
            }
        }

        holder.cardlaporan.setOnClickListener {
            showDetailDialog(item)
        }
    }

    private fun confirmItemTaken(holder: ViewHolder, position: Int, item: ModelLaporan) {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val tanggalSekarang = formatter.format(Date())

        item.statusPengambilan = "Diambil"
        item.waktuPengambilan = tanggalSekarang
        notifyItemChanged(position)

        holder.buttonbayarsekaranglaporan.visibility = View.GONE
        holder.tvpengambilanlaporan.visibility = View.VISIBLE
        holder.tvpengambilanlaporan.text = context.getString(R.string.diambilpada, tanggalSekarang)
        holder.tvbelumbayarlaporan.text = context.getString(R.string.selesai)

        item.idLaporan?.let { id ->
            val laporanRef = FirebaseDatabase.getInstance().getReference("laporan").child(id)
            laporanRef.child("statusPengambilan").setValue("Diambil")
            laporanRef.child("waktuPengambilan").setValue(tanggalSekarang)
        }

        Toast.makeText(context, context.getString(R.string.barangtelahdiambil), Toast.LENGTH_SHORT).show()
    }

    private fun showPaymentDialog(holder: ViewHolder, position: Int, item: ModelLaporan) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_mod_pembayaran_laporan, null)
        val dialog = AlertDialog.Builder(context).setView(dialogView).setCancelable(true).create()

        val listmetode = listOf(
            R.id.lineartunai,
            R.id.linearqris,
            R.id.lineardana,
            R.id.lineargopay,
            R.id.linearovo
        )

        for (metodeId in listmetode) {
            dialogView.findViewById<LinearLayout>(metodeId)?.setOnClickListener {
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val tanggalSekarang = formatter.format(Date())
                val metodePembayaran = when (metodeId) {
                    R.id.lineartunai -> "Tunai"
                    R.id.linearqris -> "QRIS"
                    R.id.lineardana -> "DANA"
                    R.id.lineargopay -> "GoPay"
                    R.id.linearovo -> "OVO"
                    else -> "Tunai"
                }

                val statusPembayaran = "Lunas"
                item.statusPembayaran = statusPembayaran
                item.waktuTransaksi = tanggalSekarang
                item.metodePembayaran = metodePembayaran
                notifyItemChanged(position)

                holder.tvbelumbayarlaporan.text = statusPembayaran
                holder.tvwaktutanggallaporan.text = tanggalSekarang
                holder.buttonbayarsekaranglaporan.text = context.getString(R.string.ambilsekarang)

                item.idLaporan?.let { id ->
                    val laporanRef = FirebaseDatabase.getInstance().getReference("laporan").child(id)
                    laporanRef.child("statusPembayaran").setValue(statusPembayaran)
                    laporanRef.child("waktuTransaksi").setValue(tanggalSekarang)
                    laporanRef.child("metodePembayaran").setValue(metodePembayaran)
                }

                Toast.makeText(
                    context,
                    context.getString(R.string.pembayaranberhasil, metodePembayaran),
                    Toast.LENGTH_SHORT
                ).show()

                dialog.dismiss()
            }
        }

        dialogView.findViewById<TextView>(R.id.btnbatal)?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun showDetailDialog(item: ModelLaporan) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_mod_detail_laporan, null)
        val dialog = AlertDialog.Builder(context).setView(dialogView).setCancelable(true).create()

        dialogView.findViewById<TextView>(R.id.tvisiidlaporandetail)?.text = item.idLaporan
        dialogView.findViewById<TextView>(R.id.tvisitanggaldetail)?.text = item.waktuTransaksi
        dialogView.findViewById<TextView>(R.id.tvisipelanggandetail)?.text = item.namaPelanggan
        dialogView.findViewById<TextView>(R.id.tvlayananutamadetail)?.text = item.jenisLayanan
        dialogView.findViewById<TextView>(R.id.tvhargalayananutamadetail)?.text = item.hargaLayananUtama
        dialogView.findViewById<TextView>(R.id.tvisisubtotaltambahandetail)?.text = item.jumlahTotalTambahan
        dialogView.findViewById<TextView>(R.id.tvisitotalbayardetail)?.text = item.jumlahTotalBayar
        dialogView.findViewById<TextView>(R.id.tvisimetodepaydetail)?.text = item.metodePembayaran ?: "-"
        dialogView.findViewById<TextView>(R.id.tvisitglwaktuambildetail)?.text = item.waktuPengambilan ?: "-"

        dialog.show()
    }

    override fun getItemCount(): Int {
        return listlaporan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardlaporan: CardView = itemView.findViewById(R.id.cardlaporan)
        val tvidlaporan: TextView = itemView.findViewById(R.id.tvidlaporan)
        val tvnamapelangganlaporan: TextView = itemView.findViewById(R.id.tvnamapelangganlaporan)
        val tvjenislayananlaporan: TextView = itemView.findViewById(R.id.tvjenislayananlaporan)
        val tvjumlahtambahanlaporan: TextView = itemView.findViewById(R.id.tvjumlahtambahanlaporan)
        val tvjmlbayarlaporan: TextView = itemView.findViewById(R.id.tvjmlbayarlaporan)
        val tvwaktutanggallaporan: TextView = itemView.findViewById(R.id.tvwaktutanggallaporan)
        val tvbelumbayarlaporan: TextView = itemView.findViewById(R.id.tvbelumbayarlaporan)
        val buttonbayarsekaranglaporan: TextView = itemView.findViewById(R.id.buttonbayarsekaranglaporan)
        val tvpengambilanlaporan: TextView = itemView.findViewById(R.id.tvpengambilanlaporan)
    }
}