package com.raflis.storyapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.raflis.storyapp.data.local.database.AuthPreferences
import com.raflis.storyapp.data.local.database.dataStore
import com.raflis.storyapp.ui.auth.LoginActivity
import com.raflis.storyapp.ui.home.HomeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var authPreferences: AuthPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authPreferences = AuthPreferences.getInstance(dataStore)

        lifecycleScope.launch {
            val userSession = authPreferences.getUserSession().first()
            if (userSession.isLogin) {
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            } else {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
            finish()
        }
    }
}