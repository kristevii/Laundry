package com.kristevi.laundry.pegawai

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kristevi.laundry.R

class TambahPegawaiActivity : AppCompatActivity() {
    lateinit var tvjuduladdpegawai : TextView
    lateinit var tvnamaaddpegawai : TextView
    lateinit var etNameaddpegawai : EditText
    lateinit var tvalamataddpegawai : TextView
    lateinit var etAlamataddpegawai : EditText
    lateinit var tvNoHpaddpegawai : TextView
    lateinit var etNoHpaddpegawai : EditText
    lateinit var tvCabangaddpegawai : TextView
    lateinit var etCabangaddpegawai : EditText
    lateinit var buttonaddpegawai : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pegawai)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init() {
        tvjuduladdpegawai = findViewById(R.id.tvjuduladdpegawai)
        tvnamaaddpegawai = findViewById(R.id.tvnamaaddpegawai)
        etNameaddpegawai = findViewById(R.id.etNameaddpegawai)
        tvalamataddpegawai = findViewById(R.id.tvalamataddpegawai)
        etAlamataddpegawai = findViewById(R.id.etAlamataddpegawai)
        tvNoHpaddpegawai = findViewById(R.id.tvNoHpaddpegawai)
        etNoHpaddpegawai = findViewById(R.id.etNoHpaddpegawai)
        tvCabangaddpegawai = findViewById(R.id.tvCabangaddpegawai)
        etCabangaddpegawai = findViewById(R.id.etCabangaddpegawai)
        buttonaddpegawai = findViewById(R.id.buttonaddpegawai)
    }
}