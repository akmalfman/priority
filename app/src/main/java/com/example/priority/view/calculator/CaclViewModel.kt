package com.example.priority.view.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.priority.data.ResultState
import com.example.priority.data.api.ApiConfig
import com.example.priority.data.api.ApiConfigg
import com.example.priority.data.response.AqiResponse
import com.example.priority.data.response.EmisiResponse
import com.google.gson.Gson
import retrofit2.HttpException

class CaclViewModel : ViewModel() {
    fun postEmisi(model_name : String, bbm : String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = ApiConfig.getApiService().postEmisi(model_name, bbm)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, EmisiResponse::class.java)
            emit(errorResponse.let { ResultState.Error(it.toString()) })
        }
    }
}