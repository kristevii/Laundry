package com.kristevi.laundry.pelanggan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kristevi.laundry.ModelData.ModelPelanggan
import com.kristevi.laundry.R
import com.kristevi.laundry.adapter.DataPelangganAdapter

class DataPelangganActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")
    lateinit var rvDataPelanggan : RecyclerView
    lateinit var fabTambahPelanggan : FloatingActionButton
    lateinit var listpelanggan: ArrayList<ModelPelanggan>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pelanggan)

        init()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvDataPelanggan.layoutManager = layoutManager
        rvDataPelanggan.setHasFixedSize(true)
        listpelanggan = arrayListOf<ModelPelanggan>()

        getData()

        val fabTambahPelanggan : FloatingActionButton = findViewById(R.id.fabTambahPelanggan)
        fabTambahPelanggan.setOnClickListener {
            val intent = Intent(this, TambahPelangganActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init(){
        rvDataPelanggan = findViewById(R.id.rvDataPelanggan)
        fabTambahPelanggan = findViewById(R.id.fabTambahPelanggan)
    }
    fun getData(){
        val query = myRef.orderByChild("idPelanggan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    listpelanggan.clear()
                    for (dataSnapshot in snapshot.children) {
                        val pegawai = dataSnapshot.getValue(ModelPelanggan::class.java)
                        listpelanggan.add(pegawai!!)
                    }
                    val adapter = DataPelangganAdapter(listpelanggan)
                    rvDataPelanggan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataPelangganActivity, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}