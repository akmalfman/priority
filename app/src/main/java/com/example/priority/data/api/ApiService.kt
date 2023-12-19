package com.example.priority.data.api

import com.example.priority.data.response.FileUploadResponse
import okhttp3.*
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("stories/guest")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): FileUploadResponse
}