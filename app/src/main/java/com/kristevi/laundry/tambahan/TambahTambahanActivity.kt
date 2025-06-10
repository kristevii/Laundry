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
import com.kristevi.laundry.ModelData.ModelPegawai
import com.kristevi.laundry.ModelData.ModelTambahan
import com.kristevi.laundry.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TambahTambahanActivity : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("tambahan")

    lateinit var tvjuduladdtambahan : TextView
    lateinit var tvnamaaddtambahan : TextView
    lateinit var etNameaddtambahan : EditText
    lateinit var tvhargaaddtambahan : TextView
    lateinit var ethargaaddtambahan : EditText
    lateinit var buttonaddtambahan : Button

    var idTambahan : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_tambahan)

        init()
        getData()
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

    fun getData() {
        idTambahan = intent.getStringExtra("idTambahan").toString()
        val judul = intent.getStringExtra("judul")
        val nama = intent.getStringExtra("namaTambahan")
        val harga = intent.getStringExtra("hargaTambahan")
        tvjuduladdtambahan.text = judul
        etNameaddtambahan.setText(nama)
        ethargaaddtambahan.setText(harga)
        if (!tvjuduladdtambahan.text.equals(this.getString(R.string.tvjuduladdtambahan))) {
            if (judul == getString(R.string.tvjuduledittambahan)) {
                mati()
                buttonaddtambahan.text = getString(R.string.sunting)
            }
        } else {
            hidup()
            etNameaddtambahan.requestFocus()
            buttonaddtambahan.text = getString(R.string.simpan)
        }
    }

    fun mati() {
        etNameaddtambahan.isEnabled = false
        ethargaaddtambahan.isEnabled = false
    }

    fun hidup() {
        etNameaddtambahan.isEnabled = true
        ethargaaddtambahan.isEnabled = true
    }

    fun update() {
        val tambahanRef = database.getReference("tambahan").child(idTambahan)
        val data = ModelTambahan(
            idTambahan,
            etNameaddtambahan.text.toString(),
            ethargaaddtambahan.text.toString())
        // map untuk update data
        val updateData = mutableMapOf<String, Any>()
        updateData["namaTambahan"] = data.namaTambahan.toString()
        updateData["hargaTambahan"] = data.hargaTambahan.toString()
        tambahanRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(this, this.getString(R.string.Data_Pegawai_Berhasil_Diperbarui),Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, this.getString(R.string.Data_Pegawai_Gagal_Diperbarui),Toast.LENGTH_SHORT).show()
        }
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
        if (buttonaddtambahan.text.equals(getString(R.string.simpan))) {
            simpan()
        } else if (buttonaddtambahan.text.equals(getString(R.string.sunting))) {
            hidup()
            etNameaddtambahan.requestFocus()
            buttonaddtambahan.text = getString(R.string.perbarui)
        } else if (buttonaddtambahan.text.equals(getString(R.string.perbarui))) {
            update()
        }
    }
}