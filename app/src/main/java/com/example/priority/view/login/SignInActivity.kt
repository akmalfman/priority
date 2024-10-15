package com.example.priority.view.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.priority.databinding.ActivitySignInBinding
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.priority.view.main.MainActivity
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val auth by lazy { Firebase.auth }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkIfUserIsLoggedIn()
        initListeners()
    }

    private fun checkIfUserIsLoggedIn() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Jika pengguna sudah login, langsung menuju MainActivity
            startMainActivity()
        }
    }

    private fun initListeners() {
        binding.btnSignIn.setOnClickListener { signInUser() }
        binding.txtSignup.setOnClickListener { startSignUpActivity() }

    }

    private fun signInUser() {
        Log.d("SignUpActivity", "Email before trim: ${binding.etEmail.editText?.text}")
        val email = binding.etEmail.editText?.text.toString().trim()
        Log.d("SignUpActivity", "Email after trim: $email")
        val password = binding.etPassword.editText?.text.toString().trim()

        if (validateInput(email, password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Berhasil masuk", Toast.LENGTH_SHORT).show()
                        startMainActivity()
                    } else {
                        when (val exception = task.exception) {
                            is FirebaseAuthInvalidCredentialsException -> {
                                Log.e("SignInActivity", "Gagal masuk: ${exception.message}")
                                Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show()
                            }

                            else -> {
                                // Handle other exceptions
                                Log.e("SignInActivity", "Gagal masuk: ${exception?.message}")
                                Toast.makeText(this, "Gagal masuk", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(this, "Ups! Email belum diisi.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (email.length > 50) {
            Toast.makeText(this, "Email maksimal 50 karakter.", Toast.LENGTH_LONG).show()
            return false
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Ups! Password belum diisi.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length > 16) {
            Toast.makeText(this, "Password maksimal 16 karakter.", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun startSignUpActivity() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}