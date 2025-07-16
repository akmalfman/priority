package com.example.priority.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.priority.data.ResultState
import com.example.priority.data.api.ApiConfigg
import com.example.priority.data.response.AqiResponse
import com.example.priority.data.response.Current
import com.example.priority.data.response.Data
import com.example.priority.data.response.Pollution
import com.google.gson.Gson
import retrofit2.HttpException

class DashboardViewModel : ViewModel() {

//    fun getAqiTest(lat: Double, lon: Double, apiKey: String): LiveData<ResultState<AqiResponse>> {
//        val result = MutableLiveData<ResultState<AqiResponse>>()
//        result.value = ResultState.Success(
//            AqiResponse(
//                data = Data(
//                    city = "Jakarta",
//                    state = "DKI Jakarta",
//                    country = "Indonesia",
//                    current = Current(
//                        pollution = Pollution(
//                            aqius = 85,
//                            ts = "2025-02-11T12:00:00Z"
//                        )
//                    )
//                )
//            )
//        )
//        return result
//    }

    fun getAqiTest(lat: Double, lon: Double, apiKey: String): LiveData<ResultState<AqiResponse>> {
        val result = MutableLiveData<ResultState<AqiResponse>>()
        result.value = ResultState.Loading

        try {
            val response = AqiResponse(
                data = Data(
                    city = "Jakarta",
                    state = "DKI Jakarta",
                    country = "Indonesia",
                    current = Current(
                        pollution = Pollution(
                            aqius = 85,
                            ts = "2025-02-11T12:00:00Z"
                        )
                    )
                )
            )
            result.value = ResultState.Success(response)
        } catch (e: Exception) {
            result.value = ResultState.Error("Terjadi kesalahan")
        }

        return result
    }


}