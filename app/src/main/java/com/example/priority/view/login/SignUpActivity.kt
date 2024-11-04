package com.example.priority.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.priority.databinding.ActivitySignUpBinding
import com.example.priority.view.main.DashboardFragment
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val auth by lazy { Firebase.auth }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        binding.btnSignUp.setOnClickListener { signUpUser() }
        binding.txtSignin.setOnClickListener { startSignInActivity() }
    }

    private fun signUpUser() {
        val fullname = binding.etFullname.editText?.text.toString().trim()
        Log.d("SignUpActivity", "Email before trim: ${binding.etEmail.editText?.text}")
        val email = binding.etEmail.editText?.text.toString().trim()
        Log.d("SignUpActivity", "Email after trim: $email")
        val password = binding.etPassword.editText?.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.editText?.text.toString().trim()

        if (validateInput(fullname, email, password, confirmPassword)) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.let {
                            // Simpan data tambahan di Realtime Database
                            saveUserDataToDatabase(it.uid, fullname, email)
                        }

//                        Toast.makeText(this, "Berhasil membuat akun", Toast.LENGTH_SHORT).show()
//                        startSignInActivity()
                    } else {
//                        Log.e("SignUpActivity", "Gagal membuat akun: ${task.exception}")
//                        Toast.makeText(this, "Gagal membuat akun", Toast.LENGTH_SHORT).show()
                        val exception = task.exception
                        when (exception) {
                            is FirebaseAuthInvalidCredentialsException -> {
                                // Handle the case where the email address is badly formatted
                                Log.e("SignUpActivity", "Gagal membuat akun: ${exception.message}")
                                Toast.makeText(this, "Email harus valid", Toast.LENGTH_SHORT).show()
                            }

                            is FirebaseAuthUserCollisionException -> {
                                // Handle the case where the email address already exists
                                Log.e("SignUpActivity", "Akun sudah terdaftar")
                                Toast.makeText(this, "Akun sudah terdaftar", Toast.LENGTH_SHORT).show()
                            }

                            else -> {
                                // Handle other exceptions
                                Log.e("SignUpActivity", "Gagal membuat akun: ${exception?.message}")
                                Toast.makeText(this, "Gagal membuat akun", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
        }
    }

    private fun validateInput(fullname: String, email: String, password: String, confirmPassword: String): Boolean {
        if (fullname.isEmpty()) {
            Toast.makeText(this, "Ups! Nama Lengkap belum diisi.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (fullname.length > 100) {
            Toast.makeText(this, "Nama Lengkap maksimal 100 karakter.", Toast.LENGTH_LONG).show()
            return false
        }

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

        if (confirmPassword.isEmpty()) {
            Toast.makeText(this, "Konfirmasi dulu password kamu.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Password tidak sama dengan konfirmasi password", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun saveUserDataToDatabase(uid: String, fullname: String, email: String) {
        // Set URL khusus untuk database ini
        val database = Firebase.database("https://priority-2e229-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        val userMap = mapOf(
            "fullname" to fullname,
            "email" to email
        )

        database.child("users").child(uid).setValue(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Akun berhasil dibuat dan disimpan", Toast.LENGTH_SHORT).show()
                startSignInActivity()
            }
            .addOnFailureListener { e ->
                Log.e("SignUpActivity", "Gagal menyimpan data pengguna: ${e.message}")
                Toast.makeText(this, "Gagal menyimpan data pengguna", Toast.LENGTH_SHORT).show()
            }
    }

    private fun startSignInActivity() {
        startActivity(Intent(this, SignInActivity::class.java))
    }

    private fun startDashboardFragment() {
        startActivity(Intent(this,DashboardFragment::class.java))
        finish()
    }
}
