package com.lamz.storyapp.di

import android.content.Context
import com.example.priority.data.UserRepository
import com.example.priority.data.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}