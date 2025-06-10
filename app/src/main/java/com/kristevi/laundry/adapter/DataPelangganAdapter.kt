package com.kristevi.laundry.adapter

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kristevi.laundry.R
import com.kristevi.laundry.ModelData.ModelPelanggan
import com.kristevi.laundry.pelanggan.TambahPelangganActivity

class DataPelangganAdapter(
    private val listpelanggan: ArrayList<ModelPelanggan>
) : RecyclerView.Adapter<DataPelangganAdapter.ViewHolder>() {

    private lateinit var appContext: Context
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pelanggann, parent, false)
        appContext = parent.context
        databaseReference = FirebaseDatabase.getInstance().getReference("pelanggan")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listpelanggan[position]
        holder.tvCardPelangganId.text = appContext.getString(R.string.idpelanggan, item.idPelanggan)
        holder.tvnamapelanggan.text = item.namaPelanggan
        holder.tvalamatpelanggan.text = appContext.getString(R.string.alamat, item.alamatPelanggan)
        holder.tvnohppelanggan.text = appContext.getString(R.string.telepon, item.noHPPelanggan)
        holder.tvcabangpelanggan.text = appContext.getString(R.string.cabang, item.cabangPelanggan)
        holder.tvterdaftarpelanggan.text = item.terdaftar

        holder.btHubungipelanggan.setOnClickListener {
            val rawNumber = item.noHPPelanggan?.trim() ?: ""
            val nomorHP = when {
                rawNumber.startsWith("0") -> rawNumber.replaceFirst("0", "62")
                rawNumber.startsWith("+62") -> rawNumber.replace("+62", "62")
                else -> rawNumber
            }

            val pesan = appContext.getString(R.string.whatsapp_message, item.namaPelanggan)
            val url = "https://wa.me/$nomorHP?text=${Uri.encode(pesan)}"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                setPackage("com.whatsapp")
            }

            try {
                appContext.packageManager.getPackageInfo("com.whatsapp", 0)
                appContext.startActivity(intent)
            } catch (e: PackageManager.NameNotFoundException) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                appContext.startActivity(browserIntent)
                Toast.makeText(appContext, R.string.whatsapp_not_installed, Toast.LENGTH_SHORT).show()
            }
        }

        holder.btLihatpelanggan.setOnClickListener {
            showDetailDialog(item, position, holder)
        }
    }

    private fun showDetailDialog(item: ModelPelanggan, position: Int, holder: ViewHolder) {
        val dialogView = LayoutInflater.from(appContext).inflate(R.layout.dialog_mod_pelanggan, null)
        val alertDialog = AlertDialog.Builder(appContext).setView(dialogView).create()

        dialogView.findViewById<TextView>(R.id.tvisiidpelanggan).text = item.idPelanggan
        dialogView.findViewById<TextView>(R.id.tvisinamapelanggan).text = item.namaPelanggan
        dialogView.findViewById<TextView>(R.id.tvisialamatpelanggan).text = item.alamatPelanggan
        dialogView.findViewById<TextView>(R.id.tvisinohppelanggan).text = item.noHPPelanggan
        dialogView.findViewById<TextView>(R.id.tvisicabangpelanggan).text = item.cabangPelanggan
        dialogView.findViewById<TextView>(R.id.tvisiterdaftarpelanggan).text = item.terdaftar

        dialogView.findViewById<Button>(R.id.buttonsuntingpelanggan).setOnClickListener {
            val intent = Intent(appContext, TambahPelangganActivity::class.java).apply {
                putExtra("judul", appContext.getString(R.string.tvjuduleditpelanggan))
                putExtra("idPelanggan", item.idPelanggan)
                putExtra("namaPelanggan", item.namaPelanggan)
                putExtra("noHPPelanggan", item.noHPPelanggan)
                putExtra("alamatPelanggan", item.alamatPelanggan)
                putExtra("cabangPelanggan", item.cabangPelanggan)
            }
            appContext.startActivity(intent)
            alertDialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.buttonhapuspelanggan).setOnClickListener {
            showDeleteConfirmation(item, position, alertDialog, holder)
        }

        alertDialog.show()
    }

    private fun showDeleteConfirmation(
        item: ModelPelanggan,
        position: Int,
        alertDialog: AlertDialog,
        holder: ViewHolder
    ) {
        AlertDialog.Builder(holder.itemView.context)
            .setTitle(R.string.title_confirm_delete)
            .setMessage(R.string.message_confirm_delete)
            .setPositiveButton(R.string.button_delete) { _, _ ->
                val idPelanggan = item.idPelanggan
                if (idPelanggan.isNullOrEmpty()) {
                    Toast.makeText(holder.itemView.context, R.string.invalid_id, Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                databaseReference.child(idPelanggan).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(holder.itemView.context, R.string.delete_success, Toast.LENGTH_SHORT).show()
                        if (position != RecyclerView.NO_POSITION && position < listpelanggan.size) {
                            listpelanggan.removeAt(position)
                            notifyItemRemoved(position)
                        }
                        alertDialog.dismiss()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(holder.itemView.context, "Gagal menghapus: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton(R.string.button_cancel, null)
            .show()
    }

    override fun getItemCount(): Int = listpelanggan.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardpelanggan: CardView = itemView.findViewById(R.id.cardpelanggan)
        val tvCardPelangganId: TextView = itemView.findViewById(R.id.tvCardPelangganId)
        val tvnamapelanggan: TextView = itemView.findViewById(R.id.tvnamapelanggan)
        val tvnohppelanggan: TextView = itemView.findViewById(R.id.tvnohppelanggan)
        val tvalamatpelanggan: TextView = itemView.findViewById(R.id.tvalamatpelanggan)
        val tvcabangpelanggan: TextView = itemView.findViewById(R.id.tvcabangpelanggan)
        val tvterdaftarpelanggan: TextView = itemView.findViewById(R.id.tvterdaftarpelanggan)
        val btHubungipelanggan: Button = itemView.findViewById(R.id.btHubungipelanggan)
        val btLihatpelanggan: Button = itemView.findViewById(R.id.btLihatpelanggan)
    }
}