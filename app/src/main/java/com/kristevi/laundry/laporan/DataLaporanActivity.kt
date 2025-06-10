package com.kristevi.laundry.laporan

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.kristevi.laundry.ModelData.ModelLaporan
import com.kristevi.laundry.ModelData.ModelTransaksiTambahan
import com.kristevi.laundry.R
import com.kristevi.laundry.adapter.DataLaporanAdapter
import java.text.SimpleDateFormat
import java.util.*

class DataLaporanActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("laporan")
    lateinit var rvDataLaporan: RecyclerView
    lateinit var listlaporan: ArrayList<ModelLaporan>
    lateinit var adapter: DataLaporanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_laporan)

        init()

        rvDataLaporan.layoutManager = LinearLayoutManager(this)
        rvDataLaporan.setHasFixedSize(true)
        listlaporan = arrayListOf()

        val listtambahan = intent.getSerializableExtra("listtambahan") as? ArrayList<ModelTransaksiTambahan> ?: arrayListOf()
        adapter = DataLaporanAdapter(listlaporan, listtambahan, this)
        rvDataLaporan.adapter = adapter

        getData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        rvDataLaporan = findViewById(R.id.rvDataLaporan)
    }

    fun getData() {
        val query = myRef.orderByChild("waktuTransaksi")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val tempList = mutableListOf<ModelLaporan>()
                    for (dataSnapshot in snapshot.children) {
                        val laporan = dataSnapshot.getValue(ModelLaporan::class.java)
                        laporan?.let { tempList.add(it) }
                    }

                    // Ubah dari sortedByDescending ke sortedBy (ascending)
                    listlaporan.clear()
                    listlaporan.addAll(tempList.sortedBy {
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            .parse(it.waktuTransaksi)
                    })
                    adapter.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataLaporanActivity, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}