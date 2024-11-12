package com.example.priority.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.priority.R
import com.example.priority.view.login.SignInActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Handler().postDelayed({
                val splashIntent = Intent(this, MainActivity::class.java)
                startActivity(splashIntent)
            }, 3000)
        } else {
            Handler().postDelayed({
                val splashIntent = Intent(this, SignInActivity::class.java)
                startActivity(splashIntent)
            }, 3000)
        }

    }
}