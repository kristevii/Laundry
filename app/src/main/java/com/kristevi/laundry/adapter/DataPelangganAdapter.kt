package com.kristevi.laundry.adapter

import android.content.Context
import android.content.Intent
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
import com.kristevi.laundry.R
import com.kristevi.laundry.ModelData.ModelPelanggan
import com.kristevi.laundry.pegawai.TambahPegawaiActivity
import com.kristevi.laundry.pelanggan.TambahPelangganActivity

class DataPelangganAdapter (
    private val listpelanggan: ArrayList<ModelPelanggan>) :
    RecyclerView.Adapter<DataPelangganAdapter.ViewHolder>(){

    lateinit var appContext : Context
    lateinit var databaseReference: DatabaseReference

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pelanggann, parent,false)
        appContext = parent.context
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder,position:Int){
        val item = listpelanggan[position]
        holder.tvCardPelangganId.text = "Id Pelanggan : ${item.idPelanggan}"
        holder.tvnamapelanggan.text = item.namaPelanggan
        holder.tvalamatpelanggan.text = "Alamat    : ${item.alamatPelanggan}"
        holder.tvnohppelanggan.text = "Telepon  : ${item.noHPPelanggan}"
        holder.tvcabangpelanggan.text = "Cabang  : ${item.cabangPelanggan}"
        holder.tvterdaftarpelanggan.text = item.terdaftar
        holder.cardpelanggan.setOnClickListener {
            val intent = Intent(appContext, TambahPelangganActivity::class.java)
            intent.putExtra("judul",  "Edit Pelanggan")
            intent.putExtra("idPelanggan", item.idPelanggan)
            intent.putExtra("namaPelanggan", item.namaPelanggan)
            intent.putExtra("alamatPelanggan", item.alamatPelanggan)
            intent.putExtra("noHPPelanggan", item.noHPPelanggan)
            intent.putExtra("cabangPelanggan", item.cabangPelanggan)
            appContext.startActivity(intent)
        }
        holder.btHubungipelanggan.setOnClickListener {
        }
        holder.btLihatpelanggan.setOnClickListener{
            val dialogView = LayoutInflater.from(appContext).inflate(R.layout.dialog_mod_pelanggan, null)

            val dialogBuilder = AlertDialog.Builder(appContext).setView(dialogView)

            val alertDialog = dialogBuilder.create()
            alertDialog.show()

            // Ambil elemen dari layout dialog
            val tvisiidpelanggan = dialogView.findViewById<TextView>(R.id.tvisiidpelanggan)
            val tvisinamapelanggan = dialogView.findViewById<TextView>(R.id.tvisinamapelanggan)
            val tvisialamatpelanggan = dialogView.findViewById<TextView>(R.id.tvisialamatpelanggan)
            val tvisinohppelanggan = dialogView.findViewById<TextView>(R.id.tvisinohppelanggan)
            val tvisicabangpelanggan = dialogView.findViewById<TextView>(R.id.tvisicabangpelanggan)
            val tvisiterdaftarpelanggan = dialogView.findViewById<TextView>(R.id.tvisiterdaftarpelanggan)
            val buttonsuntingpelanggan = dialogView.findViewById<Button>(R.id.buttonsuntingpelanggan)
            val buttonhapuspelanggan = dialogView.findViewById<Button>(R.id.buttonhapuspelanggan)

            // Set data ke dalam dialog
            tvisiidpelanggan.text = item.idPelanggan
            tvisinamapelanggan.text = item.namaPelanggan
            tvisialamatpelanggan.text = item.alamatPelanggan
            tvisinohppelanggan.text = item.noHPPelanggan
            tvisicabangpelanggan.text = item.cabangPelanggan
            tvisiterdaftarpelanggan.text = item.terdaftar

            // Tombol "Sunting" membuka halaman Edit Pegawai
            buttonsuntingpelanggan.setOnClickListener {
                val intent = Intent(appContext, TambahPegawaiActivity::class.java)
                intent.putExtra("judul", "Edit Pelanggan")
                intent.putExtra("idPegawai", item.idPelanggan)
                intent.putExtra("namaPegawai", item.namaPelanggan)
                intent.putExtra("noHPPegawai", item.noHPPelanggan)
                intent.putExtra("alamatPegawai", item.alamatPelanggan)
                intent.putExtra("cabangPegawai", item.cabangPelanggan)
                appContext.startActivity(intent)
                alertDialog.dismiss() // Tutup dialog setelah klik
            }

            // Tombol "Hapus" untuk menghapus pegawai (bisa ditambahkan logika Firebase)
            buttonhapuspelanggan.setOnClickListener {
                // Contoh: Konfirmasi sebelum menghapus
                AlertDialog.Builder(holder.itemView.context)
                    .setTitle("Konfirmasi")
                    .setMessage("Apakah Anda yakin ingin menghapus pegawai ini?")
                    .setPositiveButton("Hapus") { _, _ ->
                        val idPelanggan = item.idPelanggan

                        // Pastikan ID Pelanggan tidak null atau kosong
                        if (idPelanggan.isNullOrEmpty()) {
                            Toast.makeText(
                                holder.itemView.context,
                                "ID Pelanggan tidak valid!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setPositiveButton
                        }

                        // Inisialisasi database jika belum dilakukan
                        databaseReference = FirebaseDatabase.getInstance().getReference("pelanggan")

                        // Hapus data dari Firebase berdasarkan ID
                        databaseReference.child(idPelanggan).removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(
                                    holder.itemView.context,
                                    "Data berhasil dihapus",
                                    Toast.LENGTH_SHORT
                                ).show()
                                listpelanggan.removeAt(position) // Hapus dari list lokal
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
                    .setNegativeButton("Batal", null)
                    .show()
            }
        }
    }

    override fun getItemCount(): Int {
        return listpelanggan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardpelanggan : CardView = itemView.findViewById(R.id.cardpelanggan)
        val tvCardPelangganId : TextView = itemView.findViewById(R.id.tvCardPelangganId)
        val tvnamapelanggan : TextView  = itemView.findViewById(R.id.tvnamapelanggan)
        val tvnohppelanggan : TextView  = itemView.findViewById(R.id.tvnohppelanggan)
        val tvalamatpelanggan : TextView  = itemView.findViewById(R.id.tvalamatpelanggan)
        val tvcabangpelanggan : TextView  = itemView.findViewById(R.id.tvcabangpelanggan)
        val tvterdaftarpelanggan : TextView  = itemView.findViewById(R.id.tvterdaftarpelanggan)
        val btHubungipelanggan : Button = itemView.findViewById(R.id.btHubungipelanggan)
        val btLihatpelanggan : Button= itemView.findViewById(R.id.btLihatpelanggan)
    }
}



