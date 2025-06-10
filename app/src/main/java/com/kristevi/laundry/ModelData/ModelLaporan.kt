package com.kristevi.laundry.ModelData

data class ModelLaporan(
    var idLaporan: String? = "",
    var namaPelanggan: String? = "",
    var jenisLayanan: String? = "",
    var hargaLayananUtama: String? = "",
    var namaTambahan: String? = "",
    var hargaTambahan: String? = "",
    var jumlahTambahan: String? = "",
    var jumlahTotalTambahan: String? = "",
    var jumlahTotalBayar: String? = "",
    var waktuTransaksi: String? = "",
    var statusPembayaran: String? = "",
    var statusPengambilan: String? = "",
    var waktuPengambilan: String? = "",
    var metodePembayaran: String? = "",
)
