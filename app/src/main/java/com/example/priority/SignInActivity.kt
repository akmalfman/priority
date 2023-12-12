package com.example.priority

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.priority.databinding.ActivitySignInBinding
import android.content.Intent

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        txtSignUpListener()
    }

    private fun txtSignUpListener() {
        binding.txtSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}