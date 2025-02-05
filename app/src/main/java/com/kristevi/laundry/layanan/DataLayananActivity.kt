package com.kristevi.laundry.layanan

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.kristevi.laundry.cabang.TambahCabangActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kristevi.laundry.R

class DataLayananActivity : AppCompatActivity() {
    lateinit var rvDataLayanan : RecyclerView
    lateinit var fabTambahLayanan : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_layanan)

        val fabTambahLayanan : FloatingActionButton = findViewById(R.id.fabTambahLayanan)
        fabTambahLayanan.setOnClickListener {
            val intent = Intent(this, TambahCabangActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init(){
        rvDataLayanan = findViewById(R.id.rvDataLayanan)
        fabTambahLayanan = findViewById(R.id.fabTambahLayanan)
    }
}