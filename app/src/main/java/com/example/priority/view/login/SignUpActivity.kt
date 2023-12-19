package com.example.priority.view.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.priority.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        setTxtSignInListener()
    }

    private fun setTxtSignInListener() {
        binding.txtSignin.setOnClickListener {
            startSignInActivity()
        }
    }

    private fun startSignInActivity() {
        startActivity(Intent(this, SignInActivity::class.java))
    }
}
