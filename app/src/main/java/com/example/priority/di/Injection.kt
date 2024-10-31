package com.example.priority.di

import android.content.Context
import com.example.priority.data.UserRepository
import com.example.priority.data.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.apiService // Ubah dari getApiService() ke apiService
        return UserRepository.getInstance(apiService)
    }
}
