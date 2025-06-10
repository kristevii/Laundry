package com.kristevi.laundry.transaksi

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kristevi.laundry.ModelData.ModelTambahan
import com.kristevi.laundry.ModelData.ModelTransaksiTambahan
import com.kristevi.laundry.R
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class NotaTransaksiActivity : AppCompatActivity() {
    // inisaialisasi
    lateinit var tvnamalaundry : TextView
    lateinit var tvnamacabangnota : TextView
    lateinit var tvidtransaksinota : TextView
    lateinit var tvisiidtransaksinota : TextView
    lateinit var tvtanggalnota : TextView
    lateinit var tvisitanggalnota : TextView
    lateinit var tvpelanggannota : TextView
    lateinit var tvisipelanggannota : TextView
    lateinit var tvpegawainota : TextView
    lateinit var tvisipegawainota : TextView
    lateinit var tvlayananutamanota : TextView
    lateinit var tvhargalayananutamanota : TextView
    lateinit var tvrinciantambahannota : TextView
    lateinit var nestedscrollviewnota: NestedScrollView
    lateinit var rvlayananTambahannota : RecyclerView
    lateinit var tvsubtotaltambahannota : TextView
    lateinit var tvisisubtotaltambahannota : TextView
    lateinit var tvtotalbayarnota : TextView
    lateinit var tvisitotalbayarnota : TextView
    lateinit var btnkirimwhatsappnota : Button
    lateinit var btncetaknota : Button

    private val listTambahan = ArrayList<ModelTambahan>()
    private lateinit var adapter : TransaksiTambahanAdapter

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null

    private val printerMAC = "DC:0D:51:A7:FF:7A"
    // Ganti dengan alamat MAC printermu
    private val printerUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nota_transaksi)

        init()
        requestBluetoothPermissionIfNeeded()
        setupPrintButton()
        isBluetoothReady()

        tvnamacabangnota.text = getLoggedInEmployeeBranch()

        val idTransaksi = intent.getStringExtra("idTransaksi") ?: ""
        val namaPelanggan = intent.getStringExtra("namaPelanggan") ?: ""
        val namaLayanan = intent.getStringExtra("namaLayanan") ?: ""
        val hargaLayanan = intent.getStringExtra("hargaLayanan") ?: ""
        val total = intent.getIntExtra("total", 0)
        val listtambahan = intent.getSerializableExtra("listtambahan") as? ArrayList<ModelTransaksiTambahan> ?: arrayListOf()

        // Set tanggal dan jam sekarang
        val tanggalFormat = SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault())
        val tanggalSekarang = tanggalFormat.format(Date())
        tvisitanggalnota.text = tanggalSekarang

        // Hitung subtotal tambahan
        val subtotalTambahan = listtambahan.sumOf { it.hargaTambahan.toIntOrNull() ?: 0 }
        tvisisubtotaltambahannota.text = "Rp.$subtotalTambahan,00"

        tvisiidtransaksinota.text = idTransaksi
        tvisipelanggannota.text = "$namaPelanggan"
        tvlayananutamanota.text = "$namaLayanan"
        tvisipegawainota.text = getLoggedInEmployeeName()
        tvhargalayananutamanota.text = "Rp.$hargaLayanan,00"
        tvisitotalbayarnota.text = "Rp.$total,00"

        // Tampilkan tambahan di RecyclerView
        rvlayananTambahannota.layoutManager = LinearLayoutManager(this)
        rvlayananTambahannota.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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

        btnkirimwhatsappnota.setOnClickListener {
            val noHPPelanggan = intent.getStringExtra("noHPPelanggan") ?: ""
            val nomorTujuan = if (noHPPelanggan.startsWith("0")) {
                "62" + noHPPelanggan.substring(1)
            } else {
                noHPPelanggan
            }
            val pesan = buildString {
                append("${getString(R.string.halonotawa)}\n")
                append("\n${getString(R.string.detailnotawa)}\n")
                append("${getString(R.string.tvidtransaksinota)} : ${idTransaksi}\n")
                append("${getString(R.string.tvtanggalnota)} : ${tanggalSekarang}\n")
                append("${getString(R.string.tvpelanggannota)} : ${namaPelanggan}\n")
                append("${getString(R.string.tvlayananutamanota)} : ${namaLayanan}\n")
                append("${getString(R.string.harganotawa)} : ${hargaLayanan}\n")

                if (listtambahan.isNotEmpty()) {
                    append("\n${getString(R.string.tambahannotawa)}\n")
                    listtambahan.forEachIndexed { index, item ->
                        append("${index + 1}. ${item.namaTambahan} -> Rp.${item.hargaTambahan},00\n")
                    }
                    append("${getString(R.string.subtotalnotawa)} : Rp.${subtotalTambahan},00\n")
                }

                append("\n${getString(R.string.totalnotawa)} : Rp.${total},00\n")
                append("\n${getString(R.string.terimakasihnotawa)}")
            }

            val url = "https://wa.me/$nomorTujuan?text=${Uri.encode(pesan)}"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun getLoggedInEmployeeName(): String {
        val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        return sharedPref.getString("nama", "Pegawai") ?: "Pegawai"
    }
    fun getLoggedInEmployeeBranch(): String {
        val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        return sharedPref.getString("cabang", "Cabang") ?: "Cabang"
    }
    fun init(){
        tvnamalaundry = findViewById(R.id.tvnamalaundry)
        tvnamacabangnota = findViewById(R.id.tvnamacabangnota)
        tvidtransaksinota = findViewById(R.id.tvidtransaksinota)
        tvisiidtransaksinota = findViewById(R.id.tvisiidtransaksinota)
        tvtanggalnota = findViewById(R.id.tvtanggalnota)
        tvisitanggalnota = findViewById(R.id.tvisitanggalnota)
        tvpelanggannota = findViewById(R.id.tvpelanggannota)
        tvisipelanggannota = findViewById(R.id.tvisipelanggannota)
        tvpegawainota = findViewById(R.id.tvpegawainota)
        tvisipegawainota = findViewById(R.id.tvisipegawainota)
        tvlayananutamanota = findViewById(R.id.tvlayananutamanota)
        tvhargalayananutamanota = findViewById(R.id.tvhargalayananutamanota)
        tvrinciantambahannota = findViewById(R.id.tvrinciantambahannota)
        nestedscrollviewnota = findViewById(R.id.nestedscrollviewnota)
        rvlayananTambahannota = findViewById(R.id.rvlayananTambahannota)
        tvsubtotaltambahannota = findViewById(R.id.tvsubtotaltambahannota)
        tvisisubtotaltambahannota = findViewById(R.id.tvisisubtotaltambahannota)
        tvtotalbayarnota = findViewById(R.id.tvtotalbayarnota)
        tvisitotalbayarnota = findViewById(R.id.tvisitotalbayarnota)
        btnkirimwhatsappnota = findViewById(R.id.btnkirimwhatsappnota)
        btncetaknota = findViewById(R.id.btncetaknota)
    }
    fun requestBluetoothPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(android.Manifest.permission.BLUETOOTH_CONNECT), 100)
        }
    }
    fun setupPrintButton() {
        btncetaknota.setOnClickListener {
            val message = StringBuilder().apply {
                append("${tvnamalaundry.text}\n")
                append("${tvnamacabangnota.text}\n")
                append("${getString(R.string.tvidtransaksinota)} : ${tvisiidtransaksinota}\n")
                append("${getString(R.string.tvtanggalnota)} : ${tvisitanggalnota}\n")
                append("${getString(R.string.tvpelanggannota)} : ${tvisipelanggannota}\n")
                append("-------------------------------\n")
                append("${tvlayananutamanota.text.toString().padEnd(10)} : ${tvhargalayananutamanota.text}\n")

                if (listTambahan.isNotEmpty()) {
                    append("${getString(R.string.tvrinciantambahannota)}\n")
                    listTambahan.forEachIndexed { i, item ->
                        append("${i + 1}. ${item.namaTambahan?.padEnd(10)} - Rp${item.hargaTambahan}\n")
                    }
                }

                append("-------------------------------\n")
                append("${getString(R.string.tvsubtotaltambahannota)} : ${tvisisubtotaltambahannota}\n")
                append("${getString(R.string.tvtotalbayarnota)} : ${tvisitotalbayarnota}\n")
                append("\n${getString(R.string.terimakasihnotawa)}\n")
            }.toString()

            printToBluetooth(message)

        }
    }
    fun printToBluetooth(text: String) {
        Thread {
            try {
                if (!isBluetoothReady()) return@Thread

                val device = bluetoothAdapter?.getRemoteDevice(printerMAC)
                if (device != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                        checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
                    ) {
                        runOnUiThread {
                            Toast.makeText(this, getString(R.string.izinbluetoothbelum), Toast.LENGTH_SHORT).show()
                        }
                        return@Thread
                    }

                    bluetoothSocket = device.createRfcommSocketToServiceRecord(printerUUID)
                    bluetoothSocket?.connect()
                    outputStream = bluetoothSocket?.outputStream

                    outputStream?.use {
                        it.write(text.toByteArray())
                        it.flush()
                    }

                    runOnUiThread {
                        Toast.makeText(this, getString(R.string.berhasilcetaknota), Toast.LENGTH_SHORT).show()
                    }

                    bluetoothSocket?.close()
                } else {
                    runOnUiThread {
                        Toast.makeText(this, getString(R.string.devicetidakditemukan), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "${getString(R.string.gagalcetak)} : ${e.message}", Toast.LENGTH_LONG).show()
                }
                e.printStackTrace()
            }
        }.start()
    }
    fun isBluetoothReady(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
        ) {
            runOnUiThread {
                Toast.makeText(this, getString(R.string.perluizin), Toast.LENGTH_SHORT).show()
            }
            return false
        }

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            runOnUiThread {
                Toast.makeText(this, getString(R.string.izinbluetoothbelum), Toast.LENGTH_SHORT).show()
            }
            return false
        }

        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.izindiberikan), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.izinditolak), Toast.LENGTH_SHORT).show()
        }
    }

}