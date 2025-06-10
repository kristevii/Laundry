package com.kristevi.laundry

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kristevi.laundry.ModelData.ModelUser

class LoginActivity : AppCompatActivity() {
    private lateinit var etnohplogin: EditText
    private lateinit var etpasswordlogin: EditText
    private lateinit var buttonlogin: Button
    private lateinit var tvregisterlogin: TextView

    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_login)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance()
        usersRef = database.getReference("users")

        // Initialize UI components
        etnohplogin = findViewById(R.id.etnohplogin)
        etpasswordlogin = findViewById(R.id.etpasswordlogin)
        buttonlogin = findViewById(R.id.buttonlogin)
        tvregisterlogin = findViewById(R.id.tvregisterlogin)


        // Login button click
        buttonlogin.setOnClickListener {
            val nomorHP = etnohplogin.text.toString().trim()
            val password = etpasswordlogin.text.toString().trim()

            if (nomorHP.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Nomor HP dan password harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authenticateUser(nomorHP, password)
        }
    }

    private fun authenticateUser(nomorHP: String, password: String) {
        // Query database untuk mencari user dengan nomorHP yang sesuai
        usersRef.orderByChild("nomorHP").equalTo(nomorHP).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Iterasi melalui hasil (meski seharusnya hanya 1 user dengan nomorHP unik)
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(ModelUser::class.java)

                        if (user != null && user.password == password) {
                            // Login berhasil
                            Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()

                            // Simpan data user yang login (opsional)
                            val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putString("idUser", user.idUser)
                                putString("nama", user.nama)
                                putString("role", user.role)
                                putString("nomorHP", user.nomorHP)
                                putString("cabang", user.cabang)
                                putString("password", user.password)
                                putBoolean("isLoggedIn", true)
                                apply()
                            }

                            // Pindah ke MainActivity
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                            return
                        }
                    }

                    // Jika password tidak cocok
                    Toast.makeText(this@LoginActivity, "Password salah", Toast.LENGTH_SHORT).show()
                } else {
                    // Jika nomor HP tidak ditemukan
                    Toast.makeText(this@LoginActivity, "Nomor HP tidak terdaftar", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("LoginError", "Database error: ${error.message}")
                Toast.makeText(this@LoginActivity, "Terjadi kesalahan: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}