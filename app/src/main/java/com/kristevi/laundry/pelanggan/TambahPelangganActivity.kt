package com.kristevi.laundry.pelanggan

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
import com.kristevi.laundry.R
import com.kristevi.laundry.ModelData.ModelPelanggan

class TambahPelangganActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")
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

        init()
        // getData()
        buttonaddpelanggan.setOnClickListener{
            cekValidasi()
        }

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

    fun simpan(){
        val pelangganBaru = myRef.push()
        val idPelanggan = pelangganBaru.key
        val data = ModelPelanggan(
            idPelanggan.toString(),
            etNameaddpelanggan.text.toString(),
            etAlamataddpelanggan.text.toString(),
            etNoHpaddpelanggan.text.toString(),
            etCabangaddpelanggan.text.toString(),
            "timestamp"
        )
        pelangganBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, this.getString(R.string.sukses_simpan_pelanggan), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener() {
                Toast.makeText(this, this.getString(R.string.gagal_simpan_pelanggan), Toast.LENGTH_SHORT).show()
            }
    }

    fun cekValidasi() {
        val nama = etNameaddpelanggan.text.toString()
        val alamat = etAlamataddpelanggan.text.toString()
        val noHP = etNoHpaddpelanggan.text.toString()
        val cabang = etCabangaddpelanggan.text.toString()
        // validasi data
        if (nama.isEmpty()) {
            etNameaddpelanggan.error = this.getString(R.string.validasi_nama_pelanggan)
            Toast.makeText(this, this.getString(R.string.validasi_nama_pelanggan), Toast.LENGTH_SHORT).show()
            etNameaddpelanggan.requestFocus()
            return
        }
        if (alamat.isEmpty()) {
            etAlamataddpelanggan.error = this.getString(R.string.validasi_alamat_pelanggan)
            Toast.makeText(this, this.getString(R.string.validasi_alamat_pelanggan), Toast.LENGTH_SHORT).show()
            etAlamataddpelanggan.requestFocus()
            return
        }
        if (noHP.isEmpty()) {
            etNoHpaddpelanggan.error = this.getString(R.string.validasi_noHP_pelanggan)
            Toast.makeText(this, this.getString(R.string.validasi_noHP_pelanggan), Toast.LENGTH_SHORT).show()
            etNoHpaddpelanggan.requestFocus()
            return
        }
        if (cabang.isEmpty()) {
            etCabangaddpelanggan.error = this.getString(R.string.validasi_cabang_pelanggan)
            Toast.makeText(this, this.getString(R.string.validasi_cabang_pelanggan), Toast.LENGTH_SHORT).show()
            etCabangaddpelanggan.requestFocus()
            return
        }
        simpan()
    }
}