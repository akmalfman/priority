package com.example.priority.data.response

import com.google.gson.annotations.SerializedName

data class AqiResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("current")
	val current: Current? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("location")
	val location: Location? = null,

	@field:SerializedName("state")
	val state: String? = null
)

data class Location(

	@field:SerializedName("coordinates")
	val coordinates: List<Any?>? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class Current(
	@field:SerializedName("pollution")
	val pollution: Pollution? = null
)


data class Pollution(

	@field:SerializedName("aqius")
	val aqius: Int? = null,

	@field:SerializedName("maincn")
	val maincn: String? = null,

	@field:SerializedName("ts")
	val ts: String? = null,

	@field:SerializedName("mainus")
	val mainus: String? = null,

	@field:SerializedName("aqicn")
	val aqicn: Int? = null
)