package com.kristevi.laundry.akun

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kristevi.laundry.MainActivity
import com.kristevi.laundry.R

class AkunActivity : AppCompatActivity() {
    // langkah pertama inisialisasi
    lateinit var panah : ImageView
    lateinit var tv : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_akun)

        val panah : ImageView = findViewById(R.id.panah)
        panah.setOnClickListener {
            // membuat  intent untuk kembali ke activity pertama
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init () {
        panah = findViewById(R.id.panah)
        tv = findViewById(R.id.tv)
    }
}