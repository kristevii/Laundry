package com.kristevi.laundry.layanan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kristevi.laundry.R

class TambahLayananActivity : AppCompatActivity() {
    lateinit var tvjuduladdlayanan : TextView
    lateinit var tvaddLayanan : TextView
    lateinit var etaddLayanan : EditText
    lateinit var tvHargaaddlayanan : TextView
    lateinit var ethargaaddlayanan : EditText
    lateinit var tvcabangaddlayanan : TextView
    lateinit var etcabangaddlayanan : EditText
    lateinit var buttonaddlayanan : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_layanan)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init() {
        tvjuduladdlayanan = findViewById(R.id.tvjuduladdlayanan)
        tvaddLayanan = findViewById(R.id.tvaddLayanan)
        etaddLayanan = findViewById(R.id.etaddLayanan)
        tvHargaaddlayanan = findViewById(R.id.tvHargaaddlayanan)
        ethargaaddlayanan = findViewById(R.id.ethargaaddlayanan)
        tvcabangaddlayanan = findViewById(R.id.tvcabangaddlayanan)
        etcabangaddlayanan = findViewById(R.id.etcabangaddlayanan)
        buttonaddlayanan = findViewById(R.id.buttonaddlayanan)
    }
}