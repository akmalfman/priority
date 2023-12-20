package com.example.priority.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.priority.data.UserRepository
import java.io.File

class CameraViewModel(private val repository: UserRepository) : ViewModel(){
    fun uploadImage( file: File, description: String) = repository.uploadImage(file, description)
}