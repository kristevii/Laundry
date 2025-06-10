package com.kristevi.laundry.transaksi

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kristevi.laundry.ModelData.ModelPelanggan
import com.kristevi.laundry.R

class PilihPelangganActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pelanggan")
    private lateinit var rvPilihDataPelanggan: RecyclerView
    private lateinit var listpelanggan: ArrayList<ModelPelanggan>
    private lateinit var tvpilihpelanggankosong: TextView
    private lateinit var searchviewpilihpelanggan: androidx.appcompat.widget.SearchView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var cabangPelanggan: String
    private lateinit var fullList: ArrayList<ModelPelanggan>
    private lateinit var adapter: PilihPelangganAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pilih_pelanggan)

        init()
        setupRecyclerView()
        setupSearch()
        getData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)
        cabangPelanggan = sharedPref.getString("cabangPelanggan", "").orEmpty()

        rvPilihDataPelanggan = findViewById(R.id.rvPilihDataPelanggan)
        tvpilihpelanggankosong = findViewById(R.id.tvpilihpelanggankosong)
        searchviewpilihpelanggan = findViewById(R.id.searchviewpilihpelanggan)

        listpelanggan = ArrayList()
        fullList = ArrayList()
    }

    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvPilihDataPelanggan.layoutManager = layoutManager
        rvPilihDataPelanggan.setHasFixedSize(true)

        adapter = PilihPelangganAdapter(listpelanggan)
        rvPilihDataPelanggan.adapter = adapter
    }

    fun setupSearch() {
        searchviewpilihpelanggan.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    fun getData() {
        val query = if (cabangPelanggan.isNotEmpty()) {
            myRef.orderByChild("cabangPelanggan").equalTo(cabangPelanggan).limitToLast(100)
        } else {
            myRef.limitToLast(100)
        }

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    listpelanggan.clear()
                    fullList.clear()

                    for (dataSnapshot in snapshot.children) {
                        val pelanggan = dataSnapshot.getValue(ModelPelanggan::class.java)
                        pelanggan?.let {
                            listpelanggan.add(it)
                            fullList.add(it)
                        }
                    }
                    adapter.notifyDataSetChanged()
                    tvpilihpelanggankosong.visibility = View.GONE
                } else {
                    tvpilihpelanggankosong.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@PilihPelangganActivity,
                    "Gagal mengambil data: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun filterList(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            ArrayList(fullList)
        } else {
            val searchQuery = query.lowercase()
            fullList.filter {
                it.namaPelanggan?.lowercase()?.contains(searchQuery) == true ||
                        it.alamatPelanggan?.lowercase()?.contains(searchQuery) == true ||
                        it.noHPPelanggan?.lowercase()?.contains(searchQuery) == true
            } as ArrayList<ModelPelanggan>
        }

        listpelanggan.clear()
        listpelanggan.addAll(filteredList)
        adapter.notifyDataSetChanged()

        if (!query.isNullOrEmpty() && listpelanggan.isEmpty()) {
            showEmptyView(getString(R.string.datacaritidakada))
        } else {
            checkDataEmpty()
        }
    }
    fun checkDataEmpty() {
        if (listpelanggan.isEmpty()) {
            showEmptyView(getString(R.string.datatidakada))
        } else {
            hideEmptyView()
        }
    }

    fun showEmptyView(message: String) {
        tvpilihpelanggankosong.text = message
        tvpilihpelanggankosong.visibility = View.VISIBLE
        rvPilihDataPelanggan.visibility = View.GONE
    }

    fun hideEmptyView() {
        tvpilihpelanggankosong.visibility = View.GONE
        rvPilihDataPelanggan.visibility = View.VISIBLE
    }
}