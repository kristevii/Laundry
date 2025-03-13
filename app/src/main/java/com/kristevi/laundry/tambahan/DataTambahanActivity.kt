package com.kristevi.laundry.tambahan

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
import com.kristevi.laundry.ModelData.ModelTambahan
import com.kristevi.laundry.R
import com.kristevi.laundry.adapter.DataTambahanAdapter

class DataTambahanActivity : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("tambahan")

    lateinit var rvDataTambahan : RecyclerView
    lateinit var fabTambahTambahan : FloatingActionButton
    lateinit var listtambahan: ArrayList<ModelTambahan>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_tambahan)

        init()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvDataTambahan.layoutManager = layoutManager
        rvDataTambahan.setHasFixedSize(true)
        listtambahan = arrayListOf<ModelTambahan>()

        getData()

        val fabTambahTambahan : FloatingActionButton = findViewById(R.id.fabTambahTambahan)
        fabTambahTambahan.setOnClickListener {
            val intent = Intent(this, TambahTambahanActivity::class.java)
            intent.putExtra("judul",  (this.getString(R.string.tvjuduladdtambahan)))
            intent.putExtra("idTambahan", "")
            intent.putExtra("namaTambahan", "")
            intent.putExtra("hargaTambahan", "")
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init(){
        rvDataTambahan = findViewById(R.id.rvDataTambahan)
        fabTambahTambahan = findViewById(R.id.fabTambahTambahan)
    }
    fun getData(){
        val query = myRef.orderByChild("idTambahan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    listtambahan.clear()
                    for (dataSnapshot in snapshot.children) {
                        val tambahan = dataSnapshot.getValue(ModelTambahan::class.java)
                        listtambahan.add(tambahan!!)
                    }
                    val adapter = DataTambahanAdapter(listtambahan)
                    rvDataTambahan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataTambahanActivity, error.message, Toast.LENGTH_LONG)
            }
        })
    }
}