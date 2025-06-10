package com.kristevi.laundry

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.kristevi.laundry.LoginActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)

        Handler(Looper.getMainLooper()).postDelayed({
            checkLoginStatus()
        }, 3000) // Waktu splash screen dikurangi menjadi 3 detik
    }

    private fun checkLoginStatus() {
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            // Jika tidak login, arahkan ke LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            // Jika sudah login, arahkan ke MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}