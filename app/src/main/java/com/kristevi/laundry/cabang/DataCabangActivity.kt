package com.kristevi.laundry.cabang

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kristevi.laundry.R

class DataCabangActivity : AppCompatActivity() {
    lateinit var rvDataCabang : RecyclerView
    lateinit var fabTambahCabang : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_cabang)

        val fabTambahCabang : FloatingActionButton = findViewById(R.id.fabTambahCabang)
        fabTambahCabang.setOnClickListener {
            val intent = Intent(this, TambahCabangActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fun init() {
            rvDataCabang = findViewById(R.id.rvDataCabang)
        }
    }
}