package com.example.priority.view.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.priority.databinding.ActivitySignInBinding
import android.content.Intent
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.priority.view.main.MainActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val auth by lazy { Firebase.auth }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkIfUserIsLoggedIn()
        initListeners()
        setupPasswordVisibilityToggle()

        binding.signInButton.setOnClickListener {
            signInGoogle()
        }
    }

    private fun signInGoogle() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .build()

        val credentialManager = CredentialManager.create(this)
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    //import from androidx.CredentialManager
                    request = request,
                    context = this@SignInActivity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) { //import from androidx.CredentialManager
                Log.d("Error", e.message.toString())
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    // Process Login dengan Firebase Auth
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }
            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user: FirebaseUser? = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this@SignInActivity, MainActivity::class.java))
            finish()
        }
    }
    companion object {
        private const val TAG = "SignInActivity"
    }

    private fun setupPasswordVisibilityToggle() {
        var isPasswordVisible = false

        binding.etPassword.setEndIconOnClickListener {
            isPasswordVisible = !isPasswordVisible
            binding.tvPassText.inputType = if (isPasswordVisible) {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.tvPassText.setSelection(binding.tvPassText.text?.length ?: 0)
        }
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