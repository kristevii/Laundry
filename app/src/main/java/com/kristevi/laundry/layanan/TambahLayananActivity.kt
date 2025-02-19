package com.kristevi.laundry.layanan

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
import com.kristevi.laundry.ModelData.ModelLayanan
import com.kristevi.laundry.R

class TambahLayananActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("layanan")
    lateinit var tvjuduladdlayanan : TextView
    lateinit var tvaddlayanan : TextView
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

        init()
        // getData()
        buttonaddlayanan.setOnClickListener{
            cekValidasi()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init() {
        tvjuduladdlayanan = findViewById(R.id.tvjuduladdlayanan)
        tvaddlayanan = findViewById(R.id.tvaddLayanan)
        etaddLayanan = findViewById(R.id.etaddLayanan)
        tvHargaaddlayanan = findViewById(R.id.tvHargaaddlayanan)
        ethargaaddlayanan = findViewById(R.id.ethargaaddlayanan)
        tvcabangaddlayanan = findViewById(R.id.tvcabangaddlayanan)
        etcabangaddlayanan = findViewById(R.id.etcabangaddlayanan)
        buttonaddlayanan = findViewById(R.id.buttonaddlayanan)
    }
    fun simpan(){
        val layananBaru = myRef.push()
        val idLayanan = layananBaru.key
        val data = ModelLayanan(
            idLayanan.toString(),
            etaddLayanan.text.toString(),
            ethargaaddlayanan.text.toString(),
            etcabangaddlayanan.text.toString()
        )

        layananBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, this.getString(R.string.sukses_simpan_layanan), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener() {
                Toast.makeText(this, this.getString(R.string.gagal_simpan_layanan), Toast.LENGTH_SHORT).show()
            }
    }

    fun cekValidasi() {
        val nama = etaddLayanan.text.toString()
        val harga = ethargaaddlayanan.text.toString()
        val cabang = etcabangaddlayanan.text.toString()
        // validasi data
        if (nama.isEmpty()) {
            etaddLayanan.error = this.getString(R.string.validasi_nama_layanan)
            Toast.makeText(this, this.getString(R.string.validasi_nama_layanan), Toast.LENGTH_SHORT).show()
            etaddLayanan.requestFocus()
            return
        }
        if (harga.isEmpty()) {
            ethargaaddlayanan.error = this.getString(R.string.validasi_harga_layanan)
            Toast.makeText(this, this.getString(R.string.validasi_harga_layanan), Toast.LENGTH_SHORT).show()
            ethargaaddlayanan.requestFocus()
            return
        }
        if (cabang.isEmpty()) {
            etcabangaddlayanan.error = this.getString(R.string.validasi_cabang_layanan)
            Toast.makeText(this, this.getString(R.string.validasi_cabang_layanan), Toast.LENGTH_SHORT).show()
            etcabangaddlayanan.requestFocus()
            return
        }
        simpan()
    }
}