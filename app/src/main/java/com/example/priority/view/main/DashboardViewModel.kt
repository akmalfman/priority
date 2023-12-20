package com.example.priority.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.priority.data.ResultState
import com.example.priority.data.api.ApiConfigg
import com.example.priority.data.response.AqiResponse
import com.example.priority.data.response.FileUploadResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File

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