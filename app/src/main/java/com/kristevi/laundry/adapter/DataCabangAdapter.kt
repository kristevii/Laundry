package com.kristevi.laundry.adapter

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kristevi.laundry.ModelData.ModelCabang
import com.kristevi.laundry.R
import com.kristevi.laundry.cabang.TambahCabangActivity
import com.kristevi.laundry.pegawai.TambahPegawaiActivity
import com.kristevi.laundry.pelanggan.TambahPelangganActivity

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

        holder.tvcardidcabang.text = appContext.getString(R.string.idcabang, item.idCabang)
        holder.tvnamacabang.text = item.namaCabang
        holder.tvalamatcabang.text = appContext.getString(R.string.alamat, item.alamatCabang)
        holder.tvteleponcabang.text = appContext.getString(R.string.telepon, item.noHPCabang)
        holder.tvlayanancabang.text = appContext.getString(R.string.layanan, item.layananCabang)
        holder.btHubungicabang.setOnClickListener {
            val rawNumber = item.noHPCabang?.trim() ?: ""
            // Format nomor HP ke format internasional (62)
            val nomorHP = when {
                rawNumber.startsWith("0") -> rawNumber.replaceFirst("0", "62")
                rawNumber.startsWith("+62") -> rawNumber.replace("+62", "62")
                else -> rawNumber
            }
            val pesan = "Halo, saya ingin menghubungi Anda terkait Laundry."
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
                // Jika WhatsApp tidak terpasang, buka via browser
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                appContext.startActivity(browserIntent)
                Toast.makeText(appContext, "WhatsApp", Toast.LENGTH_SHORT).show()
            }
        }
        holder.btLihatcabang.setOnClickListener {
            val dialogView = LayoutInflater.from(appContext).inflate(R.layout.dialog_mod_cabang, null)

            val dialogBuilder = AlertDialog.Builder(appContext).setView(dialogView)

            val alertDialog = dialogBuilder.create()
            alertDialog.show()

            // Ambil elemen dari layout dialog
            val tvisinamacabang = dialogView.findViewById<TextView>(R.id.tvisinamacabang)
            val tvisialamatcabang = dialogView.findViewById<TextView>(R.id.tvisialamatcabang)
            val tvisinohpcabang = dialogView.findViewById<TextView>(R.id.tvisinohpcabang)
            val buttonsuntingpelanggan = dialogView.findViewById<Button>(R.id.buttonsuntingcabang)
            val buttonhapuscabang = dialogView.findViewById<Button>(R.id.buttonhapuscabang)

            // Set data ke dalam dialog
            tvisinamacabang.text = item.namaCabang
            tvisialamatcabang.text = item.alamatCabang
            tvisinohpcabang.text = item.noHPCabang

            // Tombol "Sunting" membuka halaman Edit Pegawai
            buttonsuntingpelanggan.setOnClickListener {
                val intent = Intent(appContext, TambahCabangActivity::class.java)
                intent.putExtra("judul", appContext.getString(R.string.tveditcabang))
                intent.putExtra("namaCabang", item.namaCabang)
                intent.putExtra("noHPCabang", item.noHPCabang)
                intent.putExtra("alamatCabang", item.alamatCabang)
                intent.putExtra("layananCabang", item.layananCabang)

                appContext.startActivity(intent)
                alertDialog.dismiss() // Tutup dialog setelah klik
            }

            // Tombol "Hapus" untuk menghapus pegawai (bisa ditambahkan logika Firebase)
            buttonhapuscabang.setOnClickListener {
                // Contoh: Konfirmasi sebelum menghapus
                AlertDialog.Builder(holder.itemView.context)
                    .setTitle(R.string.title_confirm_delete)
                    .setMessage(R.string.message_confirm_delete)
                    .setPositiveButton(R.string.button_delete) { _, _ ->
                        val idCabang = item.idCabang

                        // Pastikan ID Pelanggan tidak null atau kosong
                        if (idCabang.isNullOrEmpty()) {
                            Toast.makeText(
                                holder.itemView.context,
                                R.string.invalid_id,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setPositiveButton
                        }

                        // Inisialisasi database jika belum dilakukan
                        databaseReference = FirebaseDatabase.getInstance().getReference("cabang")

                        // Hapus data dari Firebase berdasarkan ID
                        databaseReference.child(idCabang).removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(
                                    holder.itemView.context,
                                    R.string.delete_success,
                                    Toast.LENGTH_SHORT
                                ).show()
                                listcabang.removeAt(position) // Hapus dari list lokal
                                notifyItemRemoved(position) // Perbarui tampilan RecyclerView
                                alertDialog.dismiss() // Tutup dialog
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    holder.itemView.context,
                                    "Gagal menghapus: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                    .setNegativeButton(R.string.button_cancel, null)
                    .show()
            }
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
