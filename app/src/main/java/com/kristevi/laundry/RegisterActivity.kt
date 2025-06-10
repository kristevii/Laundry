package com.kristevi.laundry

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.kristevi.laundry.ModelData.ModelUser

class RegisterActivity : AppCompatActivity() {

    private lateinit var etnamaregister: EditText
    private lateinit var etnohpregister: EditText
    private lateinit var etpasswordregister: EditText
    private lateinit var etcabangregister: EditText
    private lateinit var sproleregister: Spinner
    private lateinit var buttonregister: Button
    private lateinit var tvloginregister: TextView

    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_register)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance()
        usersRef = database.getReference("users")

        // Initialize UI components
        etnamaregister = findViewById(R.id.etnamaregister)
        etnohpregister = findViewById(R.id.etnohpregister)
        etpasswordregister = findViewById(R.id.etpasswordregister)
        etcabangregister = findViewById(R.id.etcabangregister)
        sproleregister = findViewById(R.id.sproleregister) // Make sure you have this Spinner in your layout
        buttonregister = findViewById(R.id.buttonregister)
        tvloginregister = findViewById(R.id.tvloginregister)

        // Go to login page
        tvloginregister.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Register button click
        buttonregister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val nama = etnamaregister.text.toString().trim()
        val nomorHP = etnohpregister.text.toString().trim()
        val password = etpasswordregister.text.toString().trim()
        val cabang = etcabangregister.text.toString().trim()
        val role = sproleregister.selectedItem.toString() // Get selected role from Spinner

        // Validate inputs
        if (nama.isEmpty() || nomorHP.isEmpty() || password.isEmpty() || cabang.isEmpty()) {
            Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show()
            return
        }

        if (!nomorHP.matches(Regex("^[0-9]{10,15}$"))) {
            Toast.makeText(this, "Nomor HP harus 10-15 digit angka", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password harus minimal 6 karakter", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if phone number already exists
        usersRef.orderByChild("nomorHP").equalTo(nomorHP).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(this@RegisterActivity, "Nomor HP sudah terdaftar", Toast.LENGTH_SHORT).show()
                } else {
                    // Create new user
                    val userId = usersRef.push().key!!
                    val user = ModelUser(
                        idUser = userId,
                        nama = nama,
                        nomorHP = nomorHP,
                        password = password,
                        cabang = cabang,
                        role = role // Add role to user data
                    )

                    // Save user to database
                    usersRef.child(userId).setValue(user)
                        .addOnSuccessListener {
                            Toast.makeText(this@RegisterActivity, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Log.e("RegisterError", "Gagal menyimpan user", e)
                            Toast.makeText(
                                this@RegisterActivity,
                                "Gagal registrasi: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("RegisterError", "Database error: ${error.message}")
                Toast.makeText(
                    this@RegisterActivity,
                    "Terjadi kesalahan database: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}