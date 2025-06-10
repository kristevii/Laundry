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
import com.kristevi.laundry.ModelData.ModelPegawai
import com.kristevi.laundry.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    var idCabang : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_cabang)

        init()
        getData()
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

    fun getData() {
        idCabang = intent.getStringExtra("idCabang").toString()
        val judul = intent.getStringExtra("judul")
        val nama = intent.getStringExtra("namaCabang")
        val alamat = intent.getStringExtra("alamatCabang")
        val nohp = intent.getStringExtra("noHPCabang")
        val layanan = intent.getStringExtra("layananCabang")
        tvjuduladdcabang.text = judul
        etaddCabang.setText(nama)
        etAlamataddcabang.setText(alamat)
        etTeleponaddcabang.setText(nohp)
        etLayananaddcabang.setText(layanan)
        if (!tvjuduladdcabang.text.equals(this.getString(R.string.tvjuduladdcabang))) {
            if (judul == getString(R.string.tveditcabang)) {
                mati()
                buttonaddcabang.text = getString(R.string.sunting)
            }
        } else {
            hidup()
            etaddCabang.requestFocus()
            buttonaddcabang.text = getString(R.string.simpan)
        }
    }

    fun mati() {
        etaddCabang.isEnabled = false
        etAlamataddcabang.isEnabled = false
        etTeleponaddcabang.isEnabled = false
        etLayananaddcabang.isEnabled = false
    }

    fun hidup() {
        etaddCabang.isEnabled = true
        etAlamataddcabang.isEnabled = true
        etTeleponaddcabang.isEnabled = true
        etLayananaddcabang.isEnabled = true
    }

    fun update() {
        val cabangRef = database.getReference("cabang").child(idCabang)
        val data = ModelCabang(
            idCabang,
            etaddCabang.text.toString(),
            etAlamataddcabang.text.toString(),
            etTeleponaddcabang.text.toString(),
            etLayananaddcabang.text.toString())
        // map untuk update data
        val updateData = mutableMapOf<String, Any>()
        updateData["namaCabang"] = data.namaCabang.toString()
        updateData["alamatCabang"] = data.alamatCabang.toString()
        updateData["noHPCabang"] = data.noHPCabang.toString()
        updateData["layananCabang"] = data.layananCabang.toString()
        cabangRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(this, this.getString(R.string.Data_Berhasil_Diperbarui),Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, this.getString(R.string.Data_Gagal_Diperbarui),Toast.LENGTH_SHORT).show()
        }
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
        if (buttonaddcabang.text.equals(getString(R.string.simpan))) {
            simpan()
        } else if (buttonaddcabang.text.equals(getString(R.string.sunting))) {
            hidup()
            etaddCabang.requestFocus()
            buttonaddcabang.text = getString(R.string.perbarui)
        } else if (buttonaddcabang.text.equals(getString(R.string.perbarui))) {
            update()
        }
    }
}