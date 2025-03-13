package com.kristevi.laundry.cabang

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
import com.kristevi.laundry.ModelData.ModelCabang
import com.kristevi.laundry.ModelData.ModelPegawai
import com.kristevi.laundry.R
import com.kristevi.laundry.adapter.DataCabangAdapter
import com.kristevi.laundry.adapter.DataPegawaiAdapter
import com.kristevi.laundry.pegawai.TambahPegawaiActivity

class DataCabangActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("cabang")
    lateinit var rvDataCabang : RecyclerView
    lateinit var fabTambahCabang : FloatingActionButton
    lateinit var listcabang: ArrayList<ModelCabang>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_cabang)

        init()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvDataCabang.layoutManager = layoutManager
        rvDataCabang.setHasFixedSize(true)
        listcabang = arrayListOf<ModelCabang>()

        getData()

        val fabTambahCabang : FloatingActionButton = findViewById(R.id.fabTambahCabang)
        fabTambahCabang.setOnClickListener {
            val intent = Intent(this, TambahCabangActivity::class.java)
            intent.putExtra("judul",  (this.getString(R.string.tvjuduladdcabang)))
            intent.putExtra("idCabang", "")
            intent.putExtra("namaCabang", "")
            intent.putExtra("noHPCabang", "")
            intent.putExtra("alamatCabang", "")
            intent.putExtra("layananCabang", "")
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init(){
        rvDataCabang = findViewById(R.id.rvDataCabang)
        fabTambahCabang = findViewById(R.id.fabTambahCabang)
    }
    fun getData(){
        val query = myRef.orderByChild("idCabang").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    listcabang.clear()
                    for (dataSnapshot in snapshot.children) {
                        val cabang = dataSnapshot.getValue(ModelCabang::class.java)
                        listcabang.add(cabang!!)
                    }
                    val adapter = DataCabangAdapter(listcabang)
                    rvDataCabang.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataCabangActivity, error.message, Toast.LENGTH_LONG)
            }
        })
    }
}