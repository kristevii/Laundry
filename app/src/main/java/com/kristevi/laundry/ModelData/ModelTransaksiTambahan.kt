package com.kristevi.laundry.ModelData

import java.io.Serializable

data class ModelTransaksiTambahan (
    val idTambahan: String = "",
    val namaTambahan: String = "",
    val hargaTambahan: String = ""
) : Serializable