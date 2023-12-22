package com.example.priority.data.response

import com.google.gson.annotations.SerializedName

data class EmisiResponse(

	@field:SerializedName("Bahan Bakar")
	val bahanBakar: String? = null,

	@field:SerializedName("Kendaraan")
	val kendaraan: String? = null,

	@field:SerializedName("Hasil Emisi")
	val hasilEmisi: Any? = null
)
