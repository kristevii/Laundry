package com.kristevi.laundry.pegawai

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
import com.kristevi.laundry.ModelData.ModelPegawai
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TambahPegawaiActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pegawai")
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

        init()
        // getData()
        buttonaddpegawai.setOnClickListener{
            cekValidasi()
        }

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
        tvNoHpaddpegawai = findViewById(R.id.tvNoHpaddpegawai)
        etNoHpaddpegawai = findViewById(R.id.etNoHpaddpegawai)
        tvalamataddpegawai = findViewById(R.id.tvalamataddpegawai)
        etAlamataddpegawai = findViewById(R.id.etAlamataddpegawai)
        tvCabangaddpegawai = findViewById(R.id.tvCabangaddpegawai)
        etCabangaddpegawai = findViewById(R.id.etCabangaddpegawai)
        buttonaddpegawai = findViewById(R.id.buttonaddpegawai)
    }

    fun simpan(){
        val pegawaiBaru = myRef.push()
        val idPegawai = pegawaiBaru.key
        val currrentTime = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault()).format(
            Date()
        )
        val data = ModelPegawai(
            idPegawai.toString(),
            etNameaddpegawai.text.toString(),
            etAlamataddpegawai.text.toString(),
            etNoHpaddpegawai.text.toString(),
            etCabangaddpegawai.text.toString(),
            currrentTime
        )
        pegawaiBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, this.getString(R.string.sukses_simpan_pegawai), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener() {
                Toast.makeText(this, this.getString(R.string.gagal_simpan_pegawai), Toast.LENGTH_SHORT).show()
            }
    }

    fun cekValidasi() {
        val nama = etNameaddpegawai.text.toString()
        val alamat = etAlamataddpegawai.text.toString()
        val noHP = etNoHpaddpegawai.text.toString()
        val cabang = etCabangaddpegawai.text.toString()
        // validasi data
        if (nama.isEmpty()) {
            etNameaddpegawai.error = this.getString(R.string.validasi_nama_pegawai)
            Toast.makeText(this, this.getString(R.string.validasi_nama_pegawai), Toast.LENGTH_SHORT).show()
            etNameaddpegawai.requestFocus()
            return
        }
        if (alamat.isEmpty()) {
            etAlamataddpegawai.error = this.getString(R.string.validasi_alamat_pegawai)
            Toast.makeText(this, this.getString(R.string.validasi_alamat_pegawai), Toast.LENGTH_SHORT).show()
            etAlamataddpegawai.requestFocus()
            return
        }
        if (noHP.isEmpty()) {
            etNoHpaddpegawai.error = this.getString(R.string.validasi_noHP_pegawai)
            Toast.makeText(this, this.getString(R.string.validasi_noHP_pegawai), Toast.LENGTH_SHORT).show()
            etNoHpaddpegawai.requestFocus()
            return
        }
        if (cabang.isEmpty()) {
            etCabangaddpegawai.error = this.getString(R.string.validasi_cabang_pegawai)
            Toast.makeText(this, this.getString(R.string.validasi_cabang_pegawai), Toast.LENGTH_SHORT).show()
            etCabangaddpegawai.requestFocus()
            return
        }
        simpan()
    }
}