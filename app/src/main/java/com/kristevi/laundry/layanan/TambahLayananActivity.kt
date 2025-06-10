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
import com.kristevi.laundry.ModelData.ModelPegawai
import com.kristevi.laundry.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    var idLayanan : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_layanan)

        init()
        getData()
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

    fun getData() {
        idLayanan = intent.getStringExtra("idLayanan").toString()
        val judul = intent.getStringExtra("judul")
        val nama = intent.getStringExtra("namaLayanan")
        val harga = intent.getStringExtra("hargaLayanan")
        val cabang = intent.getStringExtra("cabangLayanan")
        tvjuduladdlayanan.text = judul
        etaddLayanan.setText(nama)
        ethargaaddlayanan.setText(harga)
        etcabangaddlayanan.setText(cabang)
        if (!tvjuduladdlayanan.text.equals(this.getString(R.string.tvjuduladdlayanan))) {
            if (judul == getString(R.string.tveditlayanan)) {
                mati()
                buttonaddlayanan.text = this.getString(R.string.sunting)
            }
        } else {
            hidup()
            etaddLayanan.requestFocus()
            buttonaddlayanan.text = this.getString(R.string.simpan)
        }
    }

    fun mati() {
        etaddLayanan.isEnabled = false
        ethargaaddlayanan.isEnabled = false
        etcabangaddlayanan.isEnabled = false
    }

    fun hidup() {
        etaddLayanan.isEnabled = true
        ethargaaddlayanan.isEnabled = true
        etcabangaddlayanan.isEnabled = true
    }

    fun update() {
        val layananRef = database.getReference("layanan").child(idLayanan)
        val data = ModelLayanan(
            idLayanan,
            etaddLayanan.text.toString(),
            ethargaaddlayanan.text.toString(),
            etcabangaddlayanan.text.toString())
        // map untuk update data
        val updateData = mutableMapOf<String, Any>()
        updateData["namaLayanan"] = data.namaLayanan.toString()
        updateData["hargaLayanan"] = data.hargaLayanan.toString()
        updateData["cabangLayanan"] = data.cabangLayanan.toString()
        layananRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(this, this.getString(R.string.Data_Berhasil_Diperbarui),Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, this.getString(R.string.Data_Gagal_Diperbarui),Toast.LENGTH_SHORT).show()
        }
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
        if (buttonaddlayanan.text.equals(this.getString(R.string.simpan))) {
            simpan()
        } else if (buttonaddlayanan.text.equals(this.getString(R.string.sunting))) {
            hidup()
            etaddLayanan.requestFocus()
            buttonaddlayanan.text = this.getString(R.string.perbarui)
        } else if (buttonaddlayanan.text.equals(this.getString(R.string.perbarui))) {
            update()
        }
    }
}