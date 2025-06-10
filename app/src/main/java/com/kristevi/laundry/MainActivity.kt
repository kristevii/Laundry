package com.kristevi.laundry

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kristevi.laundry.akun.AkunActivity
import com.kristevi.laundry.cabang.DataCabangActivity
import com.kristevi.laundry.laporan.DataLaporanActivity
import com.kristevi.laundry.layanan.DataLayananActivity
import com.kristevi.laundry.pegawai.DataPegawaiActivity
import com.kristevi.laundry.pelanggan.DataPelangganActivity
import com.kristevi.laundry.printer.PrinterActivity
import com.kristevi.laundry.tambahan.DataTambahanActivity
import com.kristevi.laundry.transaksi.TransaksiiActivity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var card1: CardView
    private lateinit var transaksi: LinearLayout
    private lateinit var pelanggan: LinearLayout
    private lateinit var laporan: LinearLayout
    private lateinit var card2: CardView
    private lateinit var card3: CardView
    private lateinit var card4: CardView
    private lateinit var card5: CardView
    private lateinit var card6: CardView
    private lateinit var card7: CardView
    private lateinit var tvsapa: TextView
    private lateinit var tvtanggal: TextView
    private lateinit var tvestimasi: TextView
    private lateinit var tvjmlestimasi: TextView
    private lateinit var garis: View
    private lateinit var tvimg1dash: TextView
    private lateinit var tvimg2dash: TextView
    private lateinit var tvimg3dash: TextView
    private lateinit var tvdesc: TextView
    private lateinit var tvpesan: TextView
    private lateinit var img1dash: ImageView
    private lateinit var img2dash: ImageView
    private lateinit var img3dash: ImageView
    private lateinit var img4dash: ImageView
    private lateinit var img5dash: ImageView
    private lateinit var img6dash: ImageView
    private lateinit var img7dash: ImageView
    private lateinit var img8dash: ImageView
    private lateinit var img9dash: ImageView
    private lateinit var tvcard2dash: TextView
    private lateinit var tvcard3dash: TextView
    private lateinit var tvcard4dash: TextView
    private lateinit var tvcard5dash: TextView
    private lateinit var tvcard6dash: TextView
    private lateinit var tvcard7dash: TextView

    private lateinit var sharedPref: SharedPreferences
    private val database = FirebaseDatabase.getInstance()
    private val laporanRef = database.getReference("laporan")
    private var incomeListener: ValueEventListener? = null
    private val incomeFormat = NumberFormat.getNumberInstance(Locale("id", "ID"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)

        init()
        setupRealTimeIncomeListener()
        setupViewBasedOnRole()
        setupClickListeners()

        tvtanggal.text = getCurrentDate()
        tvsapa.text = getGreetingMessage()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        card1 = findViewById(R.id.card1)
        transaksi = findViewById(R.id.transaksi)
        pelanggan = findViewById(R.id.pelanggan)
        laporan = findViewById(R.id.laporan)
        card2 = findViewById(R.id.card2)
        card3 = findViewById(R.id.card3)
        card4 = findViewById(R.id.card4)
        card5 = findViewById(R.id.card5)
        card6 = findViewById(R.id.card6)
        card7 = findViewById(R.id.card7)
        tvsapa = findViewById(R.id.tvsapa)
        tvtanggal = findViewById(R.id.tvtanggal)
        tvestimasi = findViewById(R.id.tvestimasi)
        tvjmlestimasi = findViewById(R.id.tvjmlestimasi)
        garis = findViewById(R.id.garis)
        tvimg1dash = findViewById(R.id.tvimg1dash)
        tvimg2dash = findViewById(R.id.tvimg2dash)
        tvimg3dash = findViewById(R.id.tvimg3dash)
        tvdesc = findViewById(R.id.tvdesc)
        tvpesan = findViewById(R.id.tvpesan)
        img1dash = findViewById(R.id.img1dash)
        img2dash = findViewById(R.id.img2dash)
        img3dash = findViewById(R.id.img3dash)
        img4dash = findViewById(R.id.img4dash)
        img5dash = findViewById(R.id.img5dash)
        img6dash = findViewById(R.id.img6dash)
        img7dash = findViewById(R.id.img7dash)
        img8dash = findViewById(R.id.img8dash)
        img9dash = findViewById(R.id.img9dash)
        tvcard2dash = findViewById(R.id.tvcard2dash)
        tvcard3dash = findViewById(R.id.tvcard3dash)
        tvcard4dash = findViewById(R.id.tvcard4dash)
        tvcard5dash = findViewById(R.id.tvcard5dash)
        tvcard6dash = findViewById(R.id.tvcard6dash)
        tvcard7dash = findViewById(R.id.tvcard7dash)
    }

    fun setupRealTimeIncomeListener() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFormat.format(Date())

        // Hapus listener sebelumnya jika ada
        incomeListener?.let { laporanRef.removeEventListener(it) }

        incomeListener = laporanRef.orderByChild("waktuTransaksi")
            .startAt(today)
            .endAt(today + "\uf8ff") // Untuk mencocokkan semua transaksi hari ini
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var dailyIncome = 0.0
                    var transactionCount = 0

                    if (snapshot.exists()) {
                        for (data in snapshot.children) {
                            try {
                                val status = data.child("statusPembayaran").getValue(String::class.java) ?: ""
                                val jumlahBayar = data.child("jumlahTotalBayar").getValue(String::class.java) ?: ""
                                val waktuTransaksi = data.child("waktuTransaksi").getValue(String::class.java) ?: ""

                                if (status.equals("Lunas", ignoreCase = true) &&
                                    waktuTransaksi.startsWith(today)) {

                                    val cleanAmount = cleanCurrencyFormat(jumlahBayar)
                                    dailyIncome += cleanAmount
                                    transactionCount++

                                    Log.d("IncomeUpdate", "Added transaction: $cleanAmount | Total: $dailyIncome")
                                }
                            } catch (e: Exception) {
                                Log.e("IncomeError", "Error processing transaction", e)
                            }
                        }
                    }

                    updateIncomeDisplay(dailyIncome)
                    Log.d("IncomeSummary", "Total transactions today: $transactionCount | Total income: $dailyIncome")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("IncomeError", "Database error: ${error.message}")
                    runOnUiThread {
                        tvjmlestimasi.text = "Rp 0"
                        Toast.makeText(
                            this@MainActivity,
                            "Gagal memuat data pendapatan: ${error.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    fun cleanCurrencyFormat(amount: String): Double {
        return try {
            amount.replace("Rp", "")
                .replace(".", "")
                .replace(",", ".")
                .replace("[^0-9.]".toRegex(), "")
                .trim()
                .toDouble()
        } catch (e: Exception) {
            Log.e("FormatError", "Failed to parse amount: $amount", e)
            0.0
        }
    }

    fun updateIncomeDisplay(income: Double) {
        runOnUiThread {
            val formattedIncome = if (income > 0) {
                incomeFormat.format(income)
            } else {
                "0"
            }
            tvjmlestimasi.text = "Rp $formattedIncome"
        }
    }

    fun getLoggedInEmployeeName(): String {
        return sharedPref.getString("nama", "Pegawai") ?: "Pegawai"
    }

    fun setupViewBasedOnRole() {
        val isAdmin = sharedPref.getString("role", "") == "Admin"
        card5.visibility = if (isAdmin) View.VISIBLE else View.GONE
        card6.visibility = if (isAdmin) View.VISIBLE else View.GONE
    }

    fun setupClickListeners() {
        transaksi.setOnClickListener {
            startActivity(Intent(this, TransaksiiActivity::class.java))
        }
        pelanggan.setOnClickListener {
            startActivity(Intent(this, DataPelangganActivity::class.java))
        }
        laporan.setOnClickListener {
            startActivity(Intent(this, DataLaporanActivity::class.java))
        }
        card2.setOnClickListener {
            startActivity(Intent(this, AkunActivity::class.java))
        }
        card3.setOnClickListener {
            startActivity(Intent(this, DataLayananActivity::class.java))
        }
        card4.setOnClickListener {
            startActivity(Intent(this, DataTambahanActivity::class.java))
        }
        card5.setOnClickListener {
            if (isAdmin()) {
                startActivity(Intent(this, DataPegawaiActivity::class.java))
            }
        }
        card6.setOnClickListener {
            if (isAdmin()) {
                startActivity(Intent(this, DataCabangActivity::class.java))
            }
        }
        card7.setOnClickListener {
            startActivity(Intent(this, PrinterActivity::class.java))
        }
    }

    fun isAdmin(): Boolean {
        return sharedPref.getString("role", "") == "Admin"
    }

    fun getGreetingMessage(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val user = getLoggedInEmployeeName()
        return when (hour) {
            in 5..11 -> getString(R.string.selamatPagi, user)
            in 12..14 -> getString(R.string.selamatSiang, user)
            in 15..17 -> getString(R.string.selamatSore, user)
            else -> getString(R.string.selamatMalam, user)
        }
    }

    fun getCurrentDate(): String {
        val date = Date()
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    override fun onResume() {
        super.onResume()
        setupViewBasedOnRole()
        setupRealTimeIncomeListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        incomeListener?.let { laporanRef.removeEventListener(it) }
    }
}