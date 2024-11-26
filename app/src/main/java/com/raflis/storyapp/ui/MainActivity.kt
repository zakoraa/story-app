package com.raflis.storyapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.raflis.storyapp.ui.auth.LoginActivity
import com.raflis.storyapp.ui.home.HomeActivity
import com.raflis.storyapp.viewModel.ViewModelFactory
import com.raflis.storyapp.viewModel.auth.AuthViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var factory: ViewModelFactory;
    private val viewModel: AuthViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        factory = ViewModelFactory.getInstance(this)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                return@observe
            } else {
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            }
            finish()
        }
    }
}