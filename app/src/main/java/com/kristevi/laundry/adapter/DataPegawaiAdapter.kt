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
import com.kristevi.laundry.ModelData.ModelPegawai
import com.kristevi.laundry.pegawai.TambahPegawaiActivity

class DataPegawaiAdapter(private val listpegawai: ArrayList<ModelPegawai>) :
    RecyclerView.Adapter<DataPegawaiAdapter.ViewHolder>() {

    private lateinit var appContext: Context
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pegawaii, parent, false)
        appContext = parent.context
        databaseReference = FirebaseDatabase.getInstance().getReference("pegawai")
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listpegawai[position]

        holder.tvCardPegawaiId.text = appContext.getString(R.string.idpegawai, item.idPegawai)
        holder.tvnamapegawai.text = item.namaPegawai
        holder.tvalamatpegawai.text = appContext.getString(R.string.alamat, item.alamatPegawai)
        holder.tvnohppegawai.text = appContext.getString(R.string.telepon, item.noHPPegawai)
        holder.tvcabangpegawai.text = appContext.getString(R.string.cabang, item.cabangPegawai)
        holder.tvterdaftarpegawai.text = item.terdaftar
        holder.btHubungipegawai.setOnClickListener {
            val rawNumber = item.noHPPegawai?.trim() ?: ""
            // Format nomor HP ke format internasional (62)
            val nomorHP = when {
                rawNumber.startsWith("0") -> rawNumber.replaceFirst("0", "62")
                rawNumber.startsWith("+62") -> rawNumber.replace("+62", "62")
                else -> rawNumber
            }
            val pesan = appContext.getString(R.string.whatsapp_message, item.namaPegawai)
            val url = "https://wa.me/$nomorHP?text=${Uri.encode(pesan)}"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                setPackage("com.whatsapp")
            }
            val isWhatsappInstalled = try {
                appContext.packageManager.getPackageInfo("com.whatsapp", 0)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }

            if (isWhatsappInstalled) {
                appContext.startActivity(intent)
            } else {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                appContext.startActivity(browserIntent)
                Toast.makeText(appContext, R.string.whatsapp_not_installed, Toast.LENGTH_SHORT).show()
            }
        }



        holder.btLihatpegawai.setOnClickListener {
            val dialogView = LayoutInflater.from(appContext)
                .inflate(R.layout.dialog_mod_pegawai, null)

            val alertDialog = AlertDialog.Builder(appContext)
                .setView(dialogView)
                .create()

            alertDialog.show()

            val tvisiidpegawai = dialogView.findViewById<TextView>(R.id.tvisiidpegawai)
            val tvisinamapegawai = dialogView.findViewById<TextView>(R.id.tvisinamapegawai)
            val tvisialamatpegawai = dialogView.findViewById<TextView>(R.id.tvisialamatpegawai)
            val tvisinohppegawai = dialogView.findViewById<TextView>(R.id.tvisinohppegawai)
            val tvisicabangpegawai = dialogView.findViewById<TextView>(R.id.tvisicabangpegawai)
            val tvisiterdaftarpegawai = dialogView.findViewById<TextView>(R.id.tvisiterdaftarpegawai)
            val buttonsuntingpegawai = dialogView.findViewById<Button>(R.id.buttonsuntingpegawai)
            val buttonhapuspegawai = dialogView.findViewById<Button>(R.id.buttonhapuspegawai)

            // Set data
            tvisiidpegawai.text = item.idPegawai
            tvisinamapegawai.text = item.namaPegawai
            tvisialamatpegawai.text = item.alamatPegawai
            tvisinohppegawai.text = item.noHPPegawai
            tvisicabangpegawai.text = item.cabangPegawai
            tvisiterdaftarpegawai.text = item.terdaftar

            buttonsuntingpegawai.setOnClickListener {
                val intent = Intent(appContext, TambahPegawaiActivity::class.java).apply {
                    putExtra("judul", appContext.getString(R.string.tvjuduleditpegawai))
                    putExtra("idPegawai", item.idPegawai)
                    putExtra("namaPegawai", item.namaPegawai)
                    putExtra("noHPPegawai", item.noHPPegawai)
                    putExtra("alamatPegawai", item.alamatPegawai)
                    putExtra("cabangPegawai", item.cabangPegawai)
                }
                appContext.startActivity(intent)
                alertDialog.dismiss()
            }

            buttonhapuspegawai.setOnClickListener {
                AlertDialog.Builder(holder.itemView.context)
                    .setTitle(R.string.title_confirm_delete)
                    .setMessage(R.string.message_confirm_delete)
                    .setPositiveButton(R.string.button_delete) { _, _ ->
                        val idPegawai = item.idPegawai
                        if (idPegawai.isNullOrEmpty()) {
                            Toast.makeText(holder.itemView.context, R.string.invalid_id, Toast.LENGTH_SHORT).show()
                            return@setPositiveButton
                        }

                        databaseReference.child(idPegawai).removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(holder.itemView.context, R.string.delete_success, Toast.LENGTH_SHORT).show()
                                if (position != RecyclerView.NO_POSITION && position < listpegawai.size) {
                                    listpegawai.removeAt(position)
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
        }
    }

    override fun getItemCount(): Int = listpegawai.size

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