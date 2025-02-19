package com.kristevi.laundry.tambahan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import com.kristevi.laundry.ModelData.ModelTambahan
import com.kristevi.laundry.R

class TambahTambahanActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("tambahan")
    lateinit var tvjuduladdtambahan : TextView
    lateinit var tvnamaaddtambahan : TextView
    lateinit var etNameaddtambahan : EditText
    lateinit var tvhargaaddtambahan : TextView
    lateinit var ethargaaddtambahan : EditText
    lateinit var buttonaddtambahan : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_tambahan)

        init()
        // getData()
        buttonaddtambahan.setOnClickListener{
            cekValidasi()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init() {
        tvjuduladdtambahan = findViewById(R.id.tvjuduladdtambahan)
        tvnamaaddtambahan = findViewById(R.id.tvnamaaddtambahan)
        etNameaddtambahan = findViewById(R.id.etNameaddtambahan)
        tvhargaaddtambahan = findViewById(R.id.tvhargaaddtambahan)
        ethargaaddtambahan = findViewById(R.id.ethargaddtambahan)
        buttonaddtambahan = findViewById(R.id.buttonaddtambahan)
    }
    fun simpan(){
        val layananBaru = myRef.push()
        val idLayanan = layananBaru.key
        val data = ModelTambahan(
            idLayanan.toString(),
            etNameaddtambahan.text.toString(),
            ethargaaddtambahan.text.toString()
        )

        layananBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, this.getString(R.string.sukses_simpan_tambahan), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener() {
                Toast.makeText(this, this.getString(R.string.gagal_simpan_tambahan), Toast.LENGTH_SHORT).show()
            }
    }

    fun cekValidasi() {
        val nama = etNameaddtambahan.text.toString()
        val harga = ethargaaddtambahan.text.toString()
        // validasi data
        if (nama.isEmpty()) {
            etNameaddtambahan.error = this.getString(R.string.validasi_nama_tambahan)
            Toast.makeText(this, this.getString(R.string.validasi_nama_tambahan), Toast.LENGTH_SHORT).show()
            etNameaddtambahan.requestFocus()
            return
        }
        if (harga.isEmpty()) {
            ethargaaddtambahan.error = this.getString(R.string.validasi_harga_tambahan)
            Toast.makeText(this, this.getString(R.string.validasi_harga_tambahan), Toast.LENGTH_SHORT).show()
            ethargaaddtambahan.requestFocus()
            return
        }
        simpan()
    }
}