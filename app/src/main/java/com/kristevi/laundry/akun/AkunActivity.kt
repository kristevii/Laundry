package com.kristevi.laundry.akun

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.kristevi.laundry.LoginActivity
import com.kristevi.laundry.MainActivity
import com.kristevi.laundry.ModelData.ModelPelanggan
import com.kristevi.laundry.ModelData.ModelUser
import com.kristevi.laundry.R
import com.kristevi.laundry.adapter.DataPelangganAdapter.ViewHolder

class AkunActivity : AppCompatActivity() {

    lateinit var tvnamauser: TextView
    lateinit var tvrole: TextView
    lateinit var card1 : CardView
    lateinit var card2 : CardView
    lateinit var card3 : CardView

    private lateinit var sharedPref: SharedPreferences
    private var isPasswordVisible = false
    private lateinit var appContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_akun)

        sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        init()
        loadUserData()

        card1.setOnClickListener {
            val dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_mod_informasi_pengguna, null)

            val alertDialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()

            val nama = sharedPref.getString("nama", "nama")
            val role = sharedPref.getString("role", "role")
            val cabang = sharedPref.getString("cabang", "cabang")
            val telepon = sharedPref.getString("nomorHP", "nomorHP")
            val password = sharedPref.getString("password", "password")

            val tvisinamaakun = dialogView.findViewById<TextView>(R.id.tvisinamaakun)
            val tvisiroleakun = dialogView.findViewById<TextView>(R.id.tvisiroleakun)
            val tvisinamacabang = dialogView.findViewById<TextView>(R.id.tvisinamacabang)
            val tvisinamatelepon = dialogView.findViewById<TextView>(R.id.tvisinamatelepon)
            val tvisipasswordakun = dialogView.findViewById<TextView>(R.id.tvisipasswordakun)

            tvisinamaakun.text = nama
            tvisiroleakun.text = role
            tvisinamacabang.text = cabang
            tvisinamatelepon.text = telepon
            tvisipasswordakun.text = if (isPasswordVisible) password else "••••••••"
            tvisipasswordakun.setOnClickListener {
                isPasswordVisible = !isPasswordVisible
                tvisipasswordakun.text = if (isPasswordVisible) password else "••••••••"
                Toast.makeText(
                    this,
                    if (isPasswordVisible) getString(R.string.passwordtampil) else getString(R.string.passwordtidaktampil),
                    Toast.LENGTH_SHORT
                ).show()
            }

            alertDialog.show()
        }
        card2.setOnClickListener {
            val editIntent = Intent(this, EditDataUserActivity::class.java).apply {
                putExtra("userId", sharedPref.getString("idUser", ""))
                putExtra("nama", sharedPref.getString("nama", ""))
                putExtra("role", sharedPref.getString("role", ""))
                putExtra("cabang", sharedPref.getString("cabang", ""))
                putExtra("nomorHP", sharedPref.getString("nomorHP", ""))
                putExtra("password", sharedPref.getString("password", ""))
            }
            startActivity(editIntent)
        }
        card3.setOnClickListener {
            showLogoutConfirmation()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun init() {
        tvnamauser = findViewById(R.id.tvnamauser)
        tvrole = findViewById(R.id.tvrole)
        card1 = findViewById(R.id.card1)
        card2 = findViewById(R.id.card2)
        card3 = findViewById(R.id.card3)
    }

    private fun loadUserData() {
        val nama = sharedPref.getString("nama", "nama")
        val role = sharedPref.getString("role", "role")

        tvnamauser.text = nama
        tvrole.text = role
    }

    private fun logout() {
        // Clear all user session data
        with(sharedPref.edit()) {
            clear() // Menghapus semua data shared preferences
            apply()
        }

        // Redirect to LoginActivity and clear back stack
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()

        Toast.makeText(this, getString(R.string.telahlogout), Toast.LENGTH_SHORT).show()
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.konfirmasilogout))
            .setMessage(getString(R.string.yakinlogout))
            .setPositiveButton(getString(R.string.buttonlogoutuser)) { dialog, _ ->
                logout()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.btnbatal)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }
}