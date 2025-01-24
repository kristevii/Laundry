package com.kristevi.laundry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cabang.DataCabangActivity
import pegawai.DataPegawaiActivity
import pelanggan.DataPelangganActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    // langkah 1, inisialisasi
    lateinit var card1 : CardView
    lateinit var transaksi : LinearLayout
    lateinit var pelanggan : LinearLayout
    lateinit var laporan : LinearLayout
    lateinit var card2 : CardView
    lateinit var card3 : CardView
    lateinit var card4 : CardView
    lateinit var card5 : CardView
    lateinit var card6 : CardView
    lateinit var card7 : CardView
    lateinit var tvsapa: TextView
    lateinit var tvtanggal : TextView
    lateinit var tvestimasi: TextView
    lateinit var tvjmlestimasi : TextView
    lateinit var garis : View
    lateinit var tvimg1 : TextView
    lateinit var tvimg2: TextView
    lateinit var tvimg3 : TextView
    lateinit var tvdesc : TextView
    lateinit var tvpesan : TextView
    lateinit var img1 : ImageView
    lateinit var img2 : ImageView
    lateinit var img3 : ImageView
    lateinit var img4 : ImageView
    lateinit var img5 : ImageView
    lateinit var img6 : ImageView
    lateinit var img7 : ImageView
    lateinit var img8 : ImageView
    lateinit var img9 : ImageView
    lateinit var tvcard2 : TextView
    lateinit var tvcard3 : TextView
    lateinit var tvcard4 : TextView
    lateinit var tvcard5 : TextView
    lateinit var tvcard6 : TextView
    lateinit var tvcard7 : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        tvtanggal = findViewById(R.id.tvtanggal)
        tvtanggal.text = getCurrentDate()

        tvsapa = findViewById(R.id.tvsapa)
        tvsapa.text = getGreetingMessage()

        val pelanggan : LinearLayout = findViewById(R.id.pelanggan)
        pelanggan.setOnClickListener {
            // membuat  intent untuk memulai activity kedua
            val intent = Intent(this, DataPelangganActivity::class.java)
            startActivity(intent)
        }
        val card2 : CardView = findViewById(R.id.card2)
        card2.setOnClickListener {
            // membuat  intent untuk memulai activity kedua
            val intent = Intent(this, AkunActivity::class.java)
            startActivity(intent)
        }
        val card5 : CardView = findViewById(R.id.card5)
        card5.setOnClickListener {
            // membuat  intent untuk memulai activity kedua
            val intent = Intent(this, DataPegawaiActivity::class.java)
            startActivity(intent)
        }
        val card6 : CardView = findViewById(R.id.card6)
        card6.setOnClickListener {
            // membuat  intent untuk memulai activity kedua
            val intent = Intent(this, DataCabangActivity::class.java)
            startActivity(intent)
        }

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
        tvsapa = findViewById(R.id.img1)
        tvtanggal  = findViewById(R.id.img1)
        tvestimasi = findViewById(R.id.img1)
        tvjmlestimasi = findViewById(R.id.img1)
        garis = findViewById(R.id.garis)
        tvimg1 = findViewById(R.id.tvimg1)
        tvimg2 = findViewById(R.id.tvimg2)
        tvimg3 = findViewById(R.id.tvimg3)
        tvdesc = findViewById(R.id.tvdesc)
        tvpesan = findViewById(R.id.tvpesan)
        img1 = findViewById(R.id.img1)
        img2 = findViewById(R.id.img2)
        img3 = findViewById(R.id.img3)
        img4 = findViewById(R.id.img4)
        img5 = findViewById(R.id.img5)
        img6 = findViewById(R.id.img6)
        img7 = findViewById(R.id.img7)
        img8 = findViewById(R.id.img8)
        img9 = findViewById(R.id.img9)
        tvcard2 = findViewById(R.id.tvcard2)
        tvcard3 = findViewById(R.id.tvcard3)
        tvcard4 = findViewById(R.id.tvcard4)
        tvcard5 = findViewById(R.id.tvcard5)
        tvcard6 = findViewById(R.id.tvcard6)
        tvcard7 = findViewById(R.id.tvcard7)
    }
    private fun getGreetingMessage(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when(hour){
            in 5..11 -> "Selamat Pagi, Kristevi"
            in 12..14 -> "Selamat Siang, Kristevi"
            in 15..17 -> "Selamat Sore, Kristevi"
            else -> "Selamat Malam, Kristevi"
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        return dateFormat.format(Date())
    }
}