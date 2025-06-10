package com.kristevi.laundry.pegawai

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.kristevi.laundry.R
import com.kristevi.laundry.ModelData.ModelPegawai
import com.kristevi.laundry.ModelData.ModelUser
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TambahPegawaiActivity : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pegawai")
    val usersRef: DatabaseReference = database.getReference("users")

    lateinit var tvjuduladdpegawai : TextView
    lateinit var tvnamaaddpegawai : TextView
    lateinit var etNameaddpegawai : EditText
    lateinit var tvalamataddpegawai : TextView
    lateinit var etAlamataddpegawai : EditText
    lateinit var tvNoHpaddpegawai : TextView
    lateinit var etNoHpaddpegawai : EditText
    lateinit var tvCabangaddpegawai : TextView
    lateinit var etCabangaddpegawai : EditText
    lateinit var tvroleaddpegawai : TextView
    lateinit var sproleaddpegawai : Spinner
    lateinit var tvpasswordaddpegawai : TextView
    lateinit var sppasswordaddpegawai : Spinner
    lateinit var buttonaddpegawai : Button

    var idPegawai : String = ""
    var isEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pegawai)

        init()
        setupSpinners()
        getData()
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
        tvroleaddpegawai = findViewById(R.id.tvroleaddpegawai)
        sproleaddpegawai = findViewById(R.id.sproleaddpegawai)
        tvpasswordaddpegawai = findViewById(R.id.tvpasswordaddpegawai)
        sppasswordaddpegawai = findViewById(R.id.sppasswordaddpegawai)
        buttonaddpegawai = findViewById(R.id.buttonaddpegawai)
    }

    private fun setupSpinners() {
        // Setup Spinner for Role
        ArrayAdapter.createFromResource(
            this,
            R.array.role, // Pastikan ini sesuai dengan nama array di strings.xml Anda
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sproleaddpegawai.adapter = adapter
        }

        // Setup Spinner for Password
        ArrayAdapter.createFromResource(
            this,
            R.array.password, // Pastikan ini sesuai dengan nama array di strings.xml Anda
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sppasswordaddpegawai.adapter = adapter
        }
    }

    fun getData() {
        idPegawai = intent.getStringExtra("idPegawai").toString()
        val judul = intent.getStringExtra("judul")
        val nama = intent.getStringExtra("namaPegawai")
        val alamat = intent.getStringExtra("alamatPegawai")
        val nohp = intent.getStringExtra("noHPPegawai")
        val cabang = intent.getStringExtra("cabangPegawai")
        tvjuduladdpegawai.text = judul
        etNameaddpegawai.setText(nama)
        etAlamataddpegawai.setText(alamat)
        etNoHpaddpegawai.setText(nohp)
        etCabangaddpegawai.setText(cabang)

        // Default: semua elemen VISIBLE dan ENABLED
        etNameaddpegawai.isEnabled = true
        etAlamataddpegawai.isEnabled = true
        etNoHpaddpegawai.isEnabled = true
        etCabangaddpegawai.isEnabled = true
        tvpasswordaddpegawai.visibility = TextView.VISIBLE
        sppasswordaddpegawai.visibility = Spinner.VISIBLE
        tvroleaddpegawai.visibility = TextView.VISIBLE
        sproleaddpegawai.visibility = Spinner.VISIBLE
        // Set sproleaddpegawai dan sppasswordaddpegawai tidak dapat diedit secara default
        sproleaddpegawai.isEnabled = false
        sppasswordaddpegawai.isEnabled = false


        if (judul == getString(R.string.tvjuduleditpegawai)) {
            isEditMode = true
            buttonaddpegawai.text = this.getString(R.string.sunting)
            // Di mode "Sunting" awal, semua EditText dan Spinner tetap non-aktif
            etNameaddpegawai.isEnabled = false
            etAlamataddpegawai.isEnabled = false
            etNoHpaddpegawai.isEnabled = false
            etCabangaddpegawai.isEnabled = false
            sproleaddpegawai.isEnabled = false // Pastikan ini juga non-aktif
            sppasswordaddpegawai.isEnabled = false // Pastikan ini juga non-aktif

        } else {
            isEditMode = false
            etNameaddpegawai.requestFocus()
            buttonaddpegawai.text = this.getString(R.string.simpan)
            // Di mode "Simpan" (tambah data baru), semua elemen aktif
            sproleaddpegawai.isEnabled = true
            sppasswordaddpegawai.isEnabled = true
        }
    }

    fun update() {
        val pegawaiRef = database.getReference("pegawai").child(idPegawai)
        val currentTime = SimpleDateFormat("dd MMMM yy HH:mm:ss", Locale.getDefault()).format(
            Date()
        )
        val data = ModelPegawai(
            idPegawai,
            etNameaddpegawai.text.toString(),
            etAlamataddpegawai.text.toString(),
            etNoHpaddpegawai.text.toString(),
            etCabangaddpegawai.text.toString(),
            currentTime)
        // map untuk update data
        val updateData = mutableMapOf<String, Any>()
        updateData["namaPegawai"] = data.namaPegawai.toString()
        updateData["alamatPegawai"] = data.alamatPegawai.toString()
        updateData["noHPPegawai"] = data.noHPPegawai.toString()
        updateData["cabangPegawai"] = data.cabangPegawai.toString()
        pegawaiRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(this, this.getString(R.string.Data_Berhasil_Diperbarui),Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, this.getString(R.string.Data_Gagal_Diperbarui),Toast.LENGTH_SHORT).show()
        }

        val passwordUser = sppasswordaddpegawai.selectedItem.toString()
        val roleUser = sproleaddpegawai.selectedItem.toString()
        val namaPegawai = etNameaddpegawai.text.toString()
        val noHPPegawai = etNoHpaddpegawai.text.toString()
        val cabangPegawai = etCabangaddpegawai.text.toString()

        usersRef.orderByChild("nomorHP").equalTo(noHPPegawai)
            .addListenerForSingleValueEvent(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val userId = userSnapshot.key
                            if (userId != null) {
                                val userUpdates = mutableMapOf<String, Any>()
                                userUpdates["password"] = passwordUser
                                userUpdates["role"] = roleUser
                                userUpdates["nama"] = namaPegawai
                                userUpdates["cabang"] = cabangPegawai

                                usersRef.child(userId).updateChildren(userUpdates)
                                    .addOnSuccessListener {
                                        Toast.makeText(this@TambahPegawaiActivity, R.string.databerhasildiperbarui, Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this@TambahPegawaiActivity, "${getString(R.string.gagalperbaruidata)}${e.message}", Toast.LENGTH_LONG).show()
                                    }
                            }
                        }
                    } else {
                        Toast.makeText(this@TambahPegawaiActivity, R.string.gagalperbarui, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    Toast.makeText(this@TambahPegawaiActivity, "${getString(R.string.errormencari)}${error.message}", Toast.LENGTH_LONG).show()
                }
            })
    }


    fun simpan(){
        val namaPegawai = etNameaddpegawai.text.toString()
        val alamatPegawai = etAlamataddpegawai.text.toString()
        val noHPPegawai = etNoHpaddpegawai.text.toString()
        val cabangPegawai = etCabangaddpegawai.text.toString()
        val passwordUser = sppasswordaddpegawai.selectedItem.toString()
        val roleUser = sproleaddpegawai.selectedItem.toString()

        val pegawaiBaru = myRef.push()
        val idPegawai = pegawaiBaru.key

        val currentTime = SimpleDateFormat("dd MMMM yy HH:mm:ss", Locale.getDefault()).format(
            Date()
        )
        val dataPegawai = ModelPegawai(
            idPegawai.toString(),
            namaPegawai,
            alamatPegawai,
            noHPPegawai,
            cabangPegawai,
            currentTime
        )

        pegawaiBaru.setValue(dataPegawai)
            .addOnSuccessListener {
                Toast.makeText(this, this.getString(R.string.sukses_simpan_pegawai), Toast.LENGTH_SHORT).show()

                // Kemudian, buat data user
                val userId = usersRef.push().key
                val dataUser = ModelUser(
                    idUser = userId.toString(),
                    nama = namaPegawai,
                    nomorHP = noHPPegawai,
                    password = passwordUser,
                    cabang = cabangPegawai,
                    role = roleUser
                )

                // Simpan data user
                usersRef.child(userId.toString()).setValue(dataUser)
                    .addOnSuccessListener {
                        Toast.makeText(this, R.string.sukses_simpan_pegawai, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "${getString(R.string.gagalmenambah)}${e.message}", Toast.LENGTH_LONG).show()
                        // Opsional: Pertimbangkan untuk menghapus data pegawai jika pembuatan user gagal
                        // pegawaiBaru.removeValue()
                    }
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
        val password = sppasswordaddpegawai.selectedItem.toString()
        val role = sproleaddpegawai.selectedItem.toString()

        // Validasi data pegawai
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

        // Validasi password dan role hanya dilakukan jika mode bukan "Sunting" atau sudah "Perbarui"
        if (!isEditMode || buttonaddpegawai.text.equals(this.getString(R.string.perbarui))) {
            if (password == "Pilih Password") { // Sesuaikan dengan teks default Anda di strings.xml
                Toast.makeText(this, R.string.pilihpassword, Toast.LENGTH_SHORT).show()
                sppasswordaddpegawai.requestFocus()
                return
            }
            if (role == "Pilih Role") { // Sesuaikan dengan teks default Anda di strings.xml
                Toast.makeText(this, R.string.pilihrole, Toast.LENGTH_SHORT).show()
                sproleaddpegawai.requestFocus()
                return
            }
        }


        if (buttonaddpegawai.text.equals(getString(R.string.simpan))) {
            simpan()
        } else if (buttonaddpegawai.text.equals(this.getString(R.string.sunting))) {
            // Saat di mode "Sunting", aktifkan semua EditText dan Spinner untuk di-edit
            etNameaddpegawai.isEnabled = true
            etAlamataddpegawai.isEnabled = true
            etNoHpaddpegawai.isEnabled = true
            etCabangaddpegawai.isEnabled = true
            sproleaddpegawai.isEnabled = true // Aktifkan spinner role
            sppasswordaddpegawai.isEnabled = true // Aktifkan spinner password
            etNameaddpegawai.requestFocus()
            buttonaddpegawai.text = this.getString(R.string.perbarui)
        } else if (buttonaddpegawai.text.equals(this.getString(R.string.perbarui))) {
            update()
        }
    }
}