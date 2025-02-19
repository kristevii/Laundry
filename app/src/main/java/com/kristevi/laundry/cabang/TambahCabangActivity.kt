package com.kristevi.laundry.cabang

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
import com.kristevi.laundry.ModelData.ModelCabang
import com.kristevi.laundry.R

class TambahCabangActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("cabang")
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

        init()
        // getData()
        buttonaddcabang.setOnClickListener{
            cekValidasi()
        }

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
        etLayananaddcabang = findViewById(R.id.etLayananaddcabang)
        buttonaddcabang = findViewById(R.id.buttonaddcabang)
    }
    fun simpan(){
        val cabangBaru = myRef.push()
        val idCabang = cabangBaru.key
        val data = ModelCabang(
            idCabang.toString(),
            etaddCabang.text.toString(),
            etAlamataddcabang.text.toString(),
            etTeleponaddcabang.text.toString(),
            etLayananaddcabang.text.toString()
        )
        cabangBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, this.getString(R.string.sukses_simpan_cabang), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener() {
                Toast.makeText(this, this.getString(R.string.gagal_simpan_cabang), Toast.LENGTH_SHORT).show()
            }
    }

    fun cekValidasi() {
        val nama = etaddCabang.text.toString()
        val alamat = etAlamataddcabang.text.toString()
        val noHP = etTeleponaddcabang.text.toString()
        val layanan = etLayananaddcabang.text.toString()
        // validasi data
        if (nama.isEmpty()) {
            etaddCabang.error = this.getString(R.string.validasi_nama_cabang)
            Toast.makeText(this, this.getString(R.string.validasi_nama_cabang), Toast.LENGTH_SHORT).show()
            etaddCabang.requestFocus()
            return
        }
        if (alamat.isEmpty()) {
            etAlamataddcabang.error = this.getString(R.string.validasi_alamat_cabang)
            Toast.makeText(this, this.getString(R.string.validasi_alamat_cabang), Toast.LENGTH_SHORT).show()
            etAlamataddcabang.requestFocus()
            return
        }
        if (noHP.isEmpty()) {
            etTeleponaddcabang.error = this.getString(R.string.validasi_noHP_cabang)
            Toast.makeText(this, this.getString(R.string.validasi_noHP_cabang), Toast.LENGTH_SHORT).show()
            etTeleponaddcabang.requestFocus()
            return
        }
        if (layanan.isEmpty()) {
            etLayananaddcabang.error = this.getString(R.string.validasi_layanan_cabang)
            Toast.makeText(this, this.getString(R.string.validasi_layanan_cabang), Toast.LENGTH_SHORT).show()
            etLayananaddcabang.requestFocus()
            return
        }
        simpan()
    }
}