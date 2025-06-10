package com.kristevi.laundry.transaksi

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.kristevi.laundry.ModelData.ModelTransaksiTambahan
import com.kristevi.laundry.R

class TransaksiiActivity : AppCompatActivity() {

    lateinit var card1transaksi: CardView
    lateinit var tvdatapelanggantransaksi: TextView
    lateinit var tvnamapelanggantransaksi: TextView
    lateinit var tvnohppelanggantransaksi: TextView
    lateinit var btnpilihpelanggantransaksi: Button
    lateinit var tvdatalayanantransaksi: TextView
    lateinit var tvnamalayanantransaksi: TextView
    lateinit var tvhargalayanantransaksi: TextView
    lateinit var btnpilihlayanantransaksi: Button
    lateinit var tvlayanantambahantransaksi: TextView
    lateinit var nestedscrollview: NestedScrollView
    lateinit var rvLayananTambahan: RecyclerView
    lateinit var card2transaksi: CardView
    lateinit var btntambahantransaksi: Button
    lateinit var btnprosestransaksi: Button

    private val pilihPelanggan = 1
    private val pilihLayanan = 2
    private val pilihTambahan = 3

    private val dataList = mutableListOf<ModelTransaksiTambahan>()
    private lateinit var tambahanAdapter: TransaksiTambahanAdapter

    private var idPelanggan: String = ""
    private var cabangPelanggan: String = ""
    private var namaPelanggan: String = ""
    private var noHPPelanggan: String = ""
    private var idLayanan: String = ""
    private var namaLayanan: String = ""
    private var hargaLayanan: String = ""
    private var idPegawai: String = ""

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_transaksii)

        sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)
        cabangPelanggan = sharedPref.getString("cabangPelanggan", "") ?: ""
        idPegawai = sharedPref.getString("idPegawai", "") ?: ""

        init()

        FirebaseApp.initializeApp(this)

        tambahanAdapter = TransaksiTambahanAdapter(dataList)
        tambahanAdapter.appContext = this
        rvLayananTambahan.adapter = tambahanAdapter
        rvLayananTambahan.layoutManager = LinearLayoutManager(this)
        rvLayananTambahan.setHasFixedSize(true)

        btnpilihpelanggantransaksi.setOnClickListener {
            val intent = Intent(this, PilihPelangganActivity::class.java)
            startActivityForResult(intent, pilihPelanggan)
        }

        btnpilihlayanantransaksi.setOnClickListener {
            val intent = Intent(this, PilihLayananActivity::class.java)
            startActivityForResult(intent, pilihLayanan)
        }

        btntambahantransaksi.setOnClickListener {
            val intent = Intent(this, PilihTambahanActivity::class.java)
            startActivityForResult(intent, pilihTambahan)
        }

        btnprosestransaksi.setOnClickListener {
            if (namaPelanggan.isNotEmpty() && namaLayanan.isNotEmpty()) {
                val intent = Intent(this, KonfirmasiTransaksiActivity::class.java)
                intent.putExtra("namaPelanggan", namaPelanggan)
                intent.putExtra("noHPPelanggan", noHPPelanggan)
                intent.putExtra("namaLayanan", namaLayanan)
                intent.putExtra("hargaLayanan", hargaLayanan)
                intent.putExtra("listtambahan", ArrayList(dataList))
                startActivity(intent)
            } else {
                Toast.makeText(this, getString(R.string.databelumlengkap), Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pilihPelanggan && resultCode == RESULT_OK && data != null) {
            idPelanggan = data.getStringExtra("idPelanggan").toString()
            namaPelanggan = data.getStringExtra("namaPelanggan") ?: ""
            noHPPelanggan = data.getStringExtra("noHPPelanggan") ?: ""

            tvnamapelanggantransaksi.text = "${getString(R.string.tvnamapelanggantransaksi)} ${namaPelanggan}"
            tvnohppelanggantransaksi.text = "${getString(R.string.tvnohppelanggantransaksi)}${noHPPelanggan}"
        }

        if (requestCode == pilihLayanan && resultCode == RESULT_OK && data != null) {
            idLayanan = data.getStringExtra("idLayanan").toString()
            namaLayanan = data.getStringExtra("namaLayanan") ?: ""
            hargaLayanan = data.getStringExtra("hargaLayanan") ?: ""

            tvnamalayanantransaksi.text = "${getString(R.string.tvnamalayanantransaksi)}${namaLayanan}"
            tvhargalayanantransaksi.text = "${getString(R.string.tvhargalayanantransaksi)}Rp.${hargaLayanan},00"
        }

        if (requestCode == pilihTambahan && resultCode == RESULT_OK && data != null) {
            val id = data.getStringExtra("idTambahan") ?: return
            val nama = data.getStringExtra("namaTambahan") ?: ""
            val harga = data.getStringExtra("hargaTambahan") ?: ""

            if (dataList.any { it.idTambahan == id }) {
                Toast.makeText(this, getString(R.string.tambahansudahdipilih), Toast.LENGTH_SHORT).show()
                return
            }

            val model = ModelTransaksiTambahan(id, nama, harga)
            dataList.add(model)
            tambahanAdapter.notifyDataSetChanged()
        }
    }

    fun init() {
        card1transaksi = findViewById(R.id.card1transaksi)
        tvdatapelanggantransaksi = findViewById(R.id.tvdatapelanggantransaksi)
        tvnamapelanggantransaksi = findViewById(R.id.tvnamapelanggantransaksi)
        tvnohppelanggantransaksi = findViewById(R.id.tvnohppelanggantransaksi)
        btnpilihpelanggantransaksi = findViewById(R.id.btnpilihpelanggantransaksi)
        tvdatalayanantransaksi = findViewById(R.id.tvdatalayanantransaksi)
        tvnamalayanantransaksi = findViewById(R.id.tvnamalayanantransaksi)
        tvhargalayanantransaksi = findViewById(R.id.tvhargalayanantransaksi)
        btnpilihlayanantransaksi = findViewById(R.id.btnpilihlayanantransaksi)
        tvlayanantambahantransaksi = findViewById(R.id.tvlayanantambahantransaksi)
        nestedscrollview = findViewById(R.id.nestedscrollview)
        rvLayananTambahan = findViewById(R.id.rvlayananTambahan)
        card2transaksi = findViewById(R.id.card2transaksi)
        btntambahantransaksi = findViewById(R.id.btntambahantransaksi)
        btnprosestransaksi = findViewById(R.id.btnprosestransaksi)
    }
}
