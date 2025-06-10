package com.kristevi.laundry.transaksi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kristevi.laundry.ModelData.ModelTransaksiTambahan
import com.kristevi.laundry.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.tasks.Task

class KonfirmasiTransaksiActivity : AppCompatActivity() {

    // Inisialisasi UI
    lateinit var card1konfirmasitransaksi: CardView
    lateinit var tvJudulKonfirmasiTransaksi: TextView
    lateinit var tvNamaPelangganKonfirmasi: TextView
    lateinit var tvNoHPPelangganKonfirmasi: TextView
    lateinit var tvNamaLayananKonfirmasi: TextView
    lateinit var tvHargaLayananKonfirmasi: TextView
    lateinit var tvJudulLayananTambahanKonfirmasi: TextView
    lateinit var nestedscrollview: NestedScrollView
    lateinit var rvlayananTambahan: RecyclerView
    lateinit var card2konfirmasitransaksi: CardView
    lateinit var tvjudultotalbayar: TextView
    lateinit var tvisitotalbayar: TextView
    lateinit var btnbataltransaksikonfirmasi: Button
    lateinit var btnpembayarantransaksikonfirmasi: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_konfirmasi_transaksi)

        init()

        // Ambil data dari intent
        val namaPelanggan = intent.getStringExtra("namaPelanggan") ?: ""
        val noHPPelanggan = intent.getStringExtra("noHPPelanggan") ?: ""
        val namaLayanan = intent.getStringExtra("namaLayanan") ?: ""
        val hargaLayanan = intent.getStringExtra("hargaLayanan") ?: ""
        val listtambahan = intent.getSerializableExtra("listtambahan") as? ArrayList<ModelTransaksiTambahan> ?: arrayListOf()

        // Tampilkan data
        tvNamaPelangganKonfirmasi.text = "$namaPelanggan"
        tvNoHPPelangganKonfirmasi.text = "$noHPPelanggan"
        tvNamaLayananKonfirmasi.text = "$namaLayanan"
        tvHargaLayananKonfirmasi.text = "Rp.$hargaLayanan,00"

        // Adapter anonim untuk menampilkan card tambahan
        rvlayananTambahan.layoutManager = LinearLayoutManager(this)
        rvlayananTambahan.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_tambahan_konfirmasi_data, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val nomor = position + 1
                val tambahan = listtambahan[position]
                val tvCardPilihTambahanId = holder.itemView.findViewById<TextView>(R.id.tvCardPilihTambahanId)
                val tvnamapilihtambahan = holder.itemView.findViewById<TextView>(R.id.tvnamapilihtambahan)
                val tvhargapilihtambahan = holder.itemView.findViewById<TextView>(R.id.tvhargapilihtambahan)

                tvCardPilihTambahanId.text = "[$nomor]"
                tvnamapilihtambahan.text = tambahan.namaTambahan
                tvhargapilihtambahan.text = "Rp.${tambahan.hargaTambahan},00"
            }

            override fun getItemCount(): Int = listtambahan.size
        }

        // Hitung total bayar
        val totalTambahan = listtambahan.sumOf { it.hargaTambahan.toIntOrNull() ?: 0 }
        val totalLayanan = hargaLayanan.toIntOrNull() ?: 0
        val total = totalLayanan + totalTambahan
        tvisitotalbayar.text = "Rp.$total,00"

        btnbataltransaksikonfirmasi.setOnClickListener {
            finish() // kembali ke TransaksiActivity
        }

        btnpembayarantransaksikonfirmasi.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_mod_transaksi, null)
            val dialog = AlertDialog.Builder(this).setView(dialogView).setCancelable(true).create()

            val listmetode = listOf(
                R.id.linearbayarnanti,
                R.id.lineartunai,
                R.id.linearqris,
                R.id.lineardana,
                R.id.lineargopay,
                R.id.linearovo
            )

            for (metodeId in listmetode) {
                dialogView.findViewById<LinearLayout>(metodeId).setOnClickListener {
                    val idTransaksi = UUID.randomUUID().toString()
                    val waktuTransaksi = System.currentTimeMillis()
                    val jumlahTambahan = listtambahan.size

                    // Simpan ke Firebase
                    val laporanRef = FirebaseDatabase.getInstance().getReference("laporan")
                    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val tanggalSekarang = formatter.format(Date())

                    val metodePembayaran = when (metodeId) {
                        R.id.linearbayarnanti -> "Bayar Nanti"
                        R.id.lineartunai -> "Tunai"
                        R.id.linearqris -> "QRIS"
                        R.id.lineardana -> "DANA"
                        R.id.lineargopay -> "GoPay"
                        R.id.linearovo -> "OVO"
                        else -> "Bayar Nanti"
                    }

                    val statusPembayaran = if (metodePembayaran == "Bayar Nanti") "Belum Dibayar" else "Lunas"

                    val dataMap = hashMapOf(
                        "idLaporan" to idTransaksi,
                        "namaPelanggan" to namaPelanggan,
                        "jenisLayanan" to namaLayanan,
                        "hargaLayananUtama" to "Rp.$hargaLayanan,00",
                        "jumlahTotalTambahan" to "Rp.$totalTambahan,00",
                        "jumlahTambahan" to getString(R.string.jmllayanantambahan, "$jumlahTambahan"),
                        "jumlahTotalBayar" to "Rp.$total,00",
                        "waktuTransaksi" to tanggalSekarang,
                        "statusPembayaran" to statusPembayaran,
                        "metodePembayaran" to metodePembayaran
                    )

                    laporanRef.child(idTransaksi).setValue(dataMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val detailTambahanRef = laporanRef.child(idTransaksi).child("detailTambahan")
                            listtambahan.forEach { tambahan ->
                                val tambahanMap = hashMapOf(
                                    "namaTambahan" to tambahan.namaTambahan,
                                    "hargaTambahan" to "Rp.${tambahan.hargaTambahan},00"
                                )
                                detailTambahanRef.push().setValue(tambahanMap)
                            }
                            Toast.makeText(this, getString(R.string.sukses_simpan_transaksi), Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, NotaTransaksiActivity::class.java).apply {
                                putExtra("idTransaksi", idTransaksi)
                                putExtra("namaPelanggan", namaPelanggan)
                                putExtra("noHPPelanggan", noHPPelanggan)
                                putExtra("namaLayanan", namaLayanan)
                                putExtra("hargaLayanan", hargaLayanan)
                                putExtra("total", total)
                                putExtra("listtambahan", listtambahan)
                                putExtra("metodePembayaran", metodePembayaran)

                            }
                            startActivity(intent)
                            dialog.dismiss()
                        } else {
                            Toast.makeText(this, getString(R.string.gagal_simpan_transaksi), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }


            dialogView.findViewById<TextView>(R.id.btnbatal)?.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        card1konfirmasitransaksi = findViewById(R.id.card1konfirmasitransaksi)
        tvJudulKonfirmasiTransaksi = findViewById(R.id.tvJudulKonfirmasiTransaksi)
        tvNamaPelangganKonfirmasi = findViewById(R.id.tvNamaPelangganKonfirmasi)
        tvNoHPPelangganKonfirmasi = findViewById(R.id.tvNoHPPelangganKonfirmasi)
        tvNamaLayananKonfirmasi = findViewById(R.id.tvNamaLayananKonfirmasi)
        tvHargaLayananKonfirmasi = findViewById(R.id.tvHargaLayananKonfirmasi)
        tvJudulLayananTambahanKonfirmasi = findViewById(R.id.tvJudulLayananTambahanKonfirmasi)
        nestedscrollview = findViewById(R.id.nestedscrollview)
        rvlayananTambahan = findViewById(R.id.rvlayananTambahan)
        card2konfirmasitransaksi = findViewById(R.id.card2konfirmasitransaksi)
        tvjudultotalbayar = findViewById(R.id.tvjudultotalbayar)
        tvisitotalbayar = findViewById(R.id.tvisitotalbayar)
        btnbataltransaksikonfirmasi = findViewById(R.id.btnbataltransaksikonfirmasi)
        btnpembayarantransaksikonfirmasi = findViewById(R.id.btnpembayarantransaksikonfirmasi)
    }
}
