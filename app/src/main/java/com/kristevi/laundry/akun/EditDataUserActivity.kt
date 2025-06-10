package com.kristevi.laundry.akun

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kristevi.laundry.R

class EditDataUserActivity : AppCompatActivity() {
    private lateinit var editnamauser: EditText
    private lateinit var editnohpuser: EditText
    private lateinit var editcabanguser: EditText
    private lateinit var btnSimpanedituser: Button

    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference
    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_data_user)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance()
        usersRef = database.getReference("users")

        init()
        loadDataFromIntent()
        setupSaveButton()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        editnamauser = findViewById(R.id.editnamauser)
        editnohpuser = findViewById(R.id.editnohpuser)
        editcabanguser = findViewById(R.id.editcabanguser)
        btnSimpanedituser = findViewById(R.id.btnSimpanedituser)
    }

    fun loadDataFromIntent() {
        userId = intent.getStringExtra("userId") ?: ""
        val nama = intent.getStringExtra("nama") ?: ""
        val nomorHP = intent.getStringExtra("nomorHP") ?: ""
        val cabang = intent.getStringExtra("cabang") ?: ""

        editnamauser.setText(nama)
        editnohpuser.setText(nomorHP)
        editcabanguser.setText(cabang)
    }

    fun setupSaveButton() {
        btnSimpanedituser.setOnClickListener {
            if (validateInputs()) {
                updateUserData()
            }
        }
    }

    fun validateInputs(): Boolean {
        if (editnamauser.text.toString().trim().isEmpty()) {
            editnamauser.error = "Nama tidak boleh kosong"
            return false
        }
        if (editnohpuser.text.toString().trim().isEmpty()) {
            editnohpuser.error = "Nomor HP tidak boleh kosong"
            return false
        }
        if (editcabanguser.text.toString().trim().isEmpty()) {
            editcabanguser.error = "Cabang tidak boleh kosong"
            return false
        }
        return true
    }

    fun updateUserData() {
        if (userId.isEmpty()) {
            Toast.makeText(this, "Error: User ID tidak valid", Toast.LENGTH_SHORT).show()
            return
        }
        val updatedUser = HashMap<String, Any>()
        updatedUser["nama"] = editnamauser.text.toString().trim()
        updatedUser["nomorHP"] = editnohpuser.text.toString().trim()
        updatedUser["cabang"] = editcabanguser.text.toString().trim()

        usersRef.child(userId).updateChildren(updatedUser)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("nama", updatedUser["nama"].toString())
                        putString("nomorHP", updatedUser["nomorHP"].toString())
                        putString("cabang", updatedUser["cabang"].toString())
                        apply()
                    }

                    Toast.makeText(this, "Data user berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Gagal memperbarui data: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

}