package com.kristevi.laundry.pelanggan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kristevi.laundry.R

class TambahPelangganActivity : AppCompatActivity() {
    lateinit var tvjuduladdpelanggan : TextView
    lateinit var tvnamaaddpelanggan : TextView
    lateinit var etNameaddpelanggan : EditText
    lateinit var tvalamataddpelanggan : TextView
    lateinit var etAlamataddpelanggan : EditText
    lateinit var tvNoHpaddpelanggan : TextView
    lateinit var etNoHpaddpelanggan : EditText
    lateinit var tvCabangaddpelanggan : TextView
    lateinit var etCabangaddpelanggan : EditText
    lateinit var buttonaddpelanggan : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pelanggan)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init() {
        tvjuduladdpelanggan = findViewById(R.id.tvjuduladdpelanggan)
        tvnamaaddpelanggan = findViewById(R.id.tvnamaaddpelanggan)
        etNameaddpelanggan = findViewById(R.id.etNameaddpelanggan)
        tvNoHpaddpelanggan = findViewById(R.id.tvNoHpaddpelanggan)
        etNoHpaddpelanggan = findViewById(R.id.etNoHpaddpelanggan)
        tvalamataddpelanggan = findViewById(R.id.tvalamataddpelanggan)
        etAlamataddpelanggan = findViewById(R.id.etAlamataddpelanggan)
        tvCabangaddpelanggan = findViewById(R.id.tvCabangaddpelanggan)
        etCabangaddpelanggan = findViewById(R.id.etCabangaddpelanggan)
        buttonaddpelanggan = findViewById(R.id.buttonaddpelanggan)
    }
}