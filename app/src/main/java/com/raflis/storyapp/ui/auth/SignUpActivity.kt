package com.raflis.storyapp.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.raflis.storyapp.R
import com.raflis.storyapp.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            edtUsername.setInputName(getString(R.string.username))
            edtEmail.setInputName(getString(R.string.email))
            edtPass.setInputName(getString(R.string.password))

            tvBackToLogin.setOnClickListener{
                finish()
            }
        }
    }
}