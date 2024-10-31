package com.example.priority.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.priority.data.ResultState
import com.example.priority.data.api.ApiConfigg
import com.example.priority.data.response.AqiResponse
import com.google.gson.Gson
import retrofit2.HttpException

class DashboardViewModel : ViewModel() {

    fun getAqi(lat: Double, lon: Double,key: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = ApiConfigg.getApiService().getAqi(lat, lon,key)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AqiResponse::class.java)
            emit(errorResponse.status?.let { ResultState.Error(it) })
        }
    }

}