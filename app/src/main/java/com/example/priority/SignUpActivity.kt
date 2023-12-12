package com.example.priority

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.priority.databinding.ActivitySignUpBinding
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        txtSignInListener()
    }

    private fun txtSignInListener() {
        binding.txtSignin.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}