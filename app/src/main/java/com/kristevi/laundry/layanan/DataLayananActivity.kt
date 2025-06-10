package com.kristevi.laundry.layanan

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
import com.kristevi.laundry.ModelData.ModelLayanan
import com.kristevi.laundry.R
import com.kristevi.laundry.adapter.DataLayananAdapter
import com.kristevi.laundry.layanan.TambahLayananActivity
import com.kristevi.laundry.pegawai.TambahPegawaiActivity

class DataLayananActivity : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("layanan")

    lateinit var rvDataLayanan : RecyclerView
    lateinit var fabTambahLayanan : FloatingActionButton
    lateinit var listlayanan: ArrayList<ModelLayanan>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_layanan)

        init()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvDataLayanan.layoutManager = layoutManager
        rvDataLayanan.setHasFixedSize(true)
        listlayanan = arrayListOf<ModelLayanan>()

        getData()

        val fabTambahLayanan : FloatingActionButton = findViewById(R.id.fabTambahLayanan)
        fabTambahLayanan.setOnClickListener {
            val intent = Intent(this, TambahLayananActivity::class.java)
            intent.putExtra("judul",  (this.getString(R.string.tvjuduladdlayanan)))
            intent.putExtra("idLayanan", "")
            intent.putExtra("namaLayanan", "")
            intent.putExtra("hargaLayanan", "")
            intent.putExtra("cabangLayanan", "")
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
    fun getData(){
        val query = myRef.orderByChild("idLayanan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    listlayanan.clear()
                    for (dataSnapshot in snapshot.children) {
                        val layanan = dataSnapshot.getValue(ModelLayanan::class.java)
                        listlayanan.add(layanan!!)
                    }
                    val adapter = DataLayananAdapter(listlayanan)
                    rvDataLayanan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataLayananActivity, error.message, Toast.LENGTH_LONG)
            }
        })
    }
}