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
import com.kristevi.laundry.ModelData.ModelLayanan
import com.kristevi.laundry.R

class PilihLayananActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("layanan")
    private lateinit var rvPilihDataLayanan: RecyclerView
    private lateinit var listlayanan: ArrayList<ModelLayanan>
    private lateinit var tvpilihlayanankosong: TextView
    private lateinit var searchviewpilihlayanan: androidx.appcompat.widget.SearchView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var cabangPelanggan: String
    private lateinit var fullList: ArrayList<ModelLayanan>
    private lateinit var adapter: PilihLayananAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pilih_layanan)

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

    private fun init() {
        sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)
        cabangPelanggan = sharedPref.getString("cabangPelanggan", "").orEmpty()
        rvPilihDataLayanan = findViewById(R.id.rvPilihDataLayanan)
        tvpilihlayanankosong = findViewById(R.id.tvpilihlayanankosong)
        searchviewpilihlayanan = findViewById(R.id.searchviewpilihlayanan)
        listlayanan = ArrayList()
        fullList = ArrayList()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvPilihDataLayanan.layoutManager = layoutManager
        rvPilihDataLayanan.setHasFixedSize(true)

        adapter = PilihLayananAdapter(listlayanan)
        rvPilihDataLayanan.adapter = adapter
    }

    private fun setupSearch() {
        searchviewpilihlayanan.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun getData() {
        val query = if (cabangPelanggan.isNotEmpty()) {
            myRef.orderByChild("cabangPelanggan").equalTo(cabangPelanggan).limitToLast(100)
        } else {
            myRef.limitToLast(100)
        }

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    listlayanan.clear()
                    fullList.clear()

                    for (dataSnapshot in snapshot.children) {
                        val layanan = dataSnapshot.getValue(ModelLayanan::class.java)
                        layanan?.let {
                            listlayanan.add(it)
                            fullList.add(it)
                        }
                    }

                    adapter.notifyDataSetChanged()
                    checkDataEmpty()
                } else {
                    showEmptyView("Tidak ada data layanan")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@PilihLayananActivity,
                    "Gagal mengambil data: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
                showEmptyView("Gagal memuat data")
            }
        })
    }

    private fun filterList(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            ArrayList(fullList)
        } else {
            val searchQuery = query.lowercase()
            fullList.filter {
                it.namaLayanan?.lowercase()?.contains(searchQuery) == true ||
                        it.hargaLayanan?.lowercase()?.contains(searchQuery) == true
            } as ArrayList<ModelLayanan>
        }

        listlayanan.clear()
        listlayanan.addAll(filteredList)
        adapter.notifyDataSetChanged()

        if (!query.isNullOrEmpty() && listlayanan.isEmpty()) {
            showEmptyView(getString(R.string.datacaritidakada))
        } else {
            checkDataEmpty()
        }
    }

    fun checkDataEmpty() {
        if (listlayanan.isEmpty()) {
            showEmptyView(getString(R.string.datatidakada))
        } else {
            hideEmptyView()
        }
    }

    fun showEmptyView(message: String) {
        tvpilihlayanankosong.text = message
        tvpilihlayanankosong.visibility = View.VISIBLE
        rvPilihDataLayanan.visibility = View.GONE
    }

    fun hideEmptyView() {
        tvpilihlayanankosong.visibility = View.GONE
        rvPilihDataLayanan.visibility = View.VISIBLE
    }
}