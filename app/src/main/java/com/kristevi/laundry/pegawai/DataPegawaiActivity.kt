package com.kristevi.laundry.pegawai

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
import com.kristevi.laundry.ModelData.ModelPegawai
import com.kristevi.laundry.R
import com.kristevi.laundry.adapter.DataPegawaiAdapter

class DataPegawaiActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pegawai")
    lateinit var rvDataPegawai : RecyclerView
    lateinit var fabTambahPegawai : FloatingActionButton
    lateinit var listpegawai: ArrayList<ModelPegawai>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pegawai)

        init()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvDataPegawai.layoutManager = layoutManager
        rvDataPegawai.setHasFixedSize(true)
        listpegawai = arrayListOf<ModelPegawai>()

        getData()

        val fabTambahPegawai : FloatingActionButton = findViewById(R.id.fabTambahPegawai)
        fabTambahPegawai.setOnClickListener {
            val intent = Intent(this, TambahPegawaiActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init(){
        rvDataPegawai = findViewById(R.id.rvDataPegawai)
        fabTambahPegawai = findViewById(R.id.fabTambahPegawai)
    }
    fun getData(){
        val query = myRef.orderByChild("idPegawai").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    listpegawai.clear()
                    for (dataSnapshot in snapshot.children) {
                        val pegawai = dataSnapshot.getValue(ModelPegawai::class.java)
                        listpegawai.add(pegawai!!)
                    }
                    val adapter = DataPegawaiAdapter(listpegawai)
                    rvDataPegawai.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataPegawaiActivity, error.message, Toast.LENGTH_LONG)
            }
        })
    }
}