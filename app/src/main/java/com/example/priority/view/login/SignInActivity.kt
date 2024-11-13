package com.example.priority.view.login

import User
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.priority.databinding.ActivitySignInBinding
import android.content.Intent
import android.text.InputType
import android.util.Log
import android.widget.Toast
import com.example.priority.R
import com.example.priority.view.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
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
        setupPasswordVisibilityToggle()

        binding.signInButton.setOnClickListener {
            signInGoogle()
        }
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
            startMainActivity()
        }
    }

    private fun initListeners() {
        binding.btnSignIn.setOnClickListener { signInUser() }
        binding.txtSignup.setOnClickListener { startSignUpActivity() }
    }

    private fun signInUser() {
        val email = binding.etEmail.editText?.text.toString().trim()
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

    private fun signInGoogle() {
        val signInButton = findViewById<SignInButton>(R.id.signInButton)
        signInButton.setSize(SignInButton.SIZE_ICON_ONLY) // Menghilangkan teks dan hanya menampilkan ikon
        signInButton.setPadding(0, 0, 0, 0)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("55984212820-qir4qmee5mgd8icp8or7s7kapgns67qo.apps.googleusercontent.com")
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("SignInActivity", "Google sign-in failed", e)
                Toast.makeText(this, "Google sign-in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userResponse = auth.currentUser
                    Log.d(TAG, "signInWithCredential:success")
                    updateUI(userResponse)
                    saveUserData(userResponse)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun saveUserData(user: FirebaseUser?) {
        if (user != null) {
            val database = FirebaseDatabase.getInstance().reference
            val email = user.email ?: ""
            val userData = User(user.uid, user.displayName, email)

            database.child("users").child(user.uid).setValue(userData)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(TAG, "User data saved successfully")
                    } else {
                        Log.e(TAG, "Error saving user data: ${it.exception?.message}")
                    }
                }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.e("SignInActivity", "User is null, login failed")
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    companion object {
        private const val TAG = "SignInActivity"
        private const val RC_SIGN_IN = 9001
    }

    private fun startSignUpActivity() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
