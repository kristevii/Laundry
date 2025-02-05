package com.kristevi.laundry.cabang

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kristevi.laundry.R

class TambahCabangActivity : AppCompatActivity() {
    lateinit var tvjuduladdcabang : TextView
    lateinit var tvaddcabang : TextView
    lateinit var etaddCabang : EditText
    lateinit var tvalamataddcabang : TextView
    lateinit var etAlamataddcabang : EditText
    lateinit var tvTeleponaddcabang : TextView
    lateinit var etTeleponaddcabang : EditText
    lateinit var tvlayananaddcabang : TextView
    lateinit var etLayananaddcabang : EditText
    lateinit var buttonaddcabang : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_cabang)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init() {
        tvjuduladdcabang = findViewById(R.id.tvjuduladdcabang)
        tvaddcabang = findViewById(R.id.tvaddcabang)
        etaddCabang = findViewById(R.id.etaddCabang)
        tvalamataddcabang = findViewById(R.id.tvalamataddcabang)
        etAlamataddcabang = findViewById(R.id.etAlamataddcabang)
        tvTeleponaddcabang = findViewById(R.id.tvTeleponaddcabang)
        etTeleponaddcabang = findViewById(R.id.etTeleponaddcabang)
        tvlayananaddcabang = findViewById(R.id.tvLayananaddcabang)
        etLayananaddcabang = findViewById(R.id.tvLayananaddcabang)
        buttonaddcabang = findViewById(R.id.buttonaddcabang)
    }
}