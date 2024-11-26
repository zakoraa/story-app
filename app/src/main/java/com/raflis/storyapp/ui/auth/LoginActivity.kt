package com.raflis.storyapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.raflis.storyapp.R
import com.raflis.storyapp.data.ResultStatus
import com.raflis.storyapp.data.remote.entity.User
import com.raflis.storyapp.databinding.ActivityLoginBinding
import com.raflis.storyapp.ui.home.HomeActivity
import com.raflis.storyapp.viewModel.ViewModelFactory
import com.raflis.storyapp.viewModel.auth.AuthViewModel


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: AuthViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initAction()
    }

    private fun initView() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun initAction() {
        with(binding) {
            edLoginEmail.setInputName(getString(R.string.email))
            edLoginPassword.setInputName(getString(R.string.password))

            tvDirectToSignUp.setOnClickListener {
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
            }

            btnLogin.setOnClickListener {
                login()
            }
        }
    }

    private fun login() {
        with(binding) {
            val email = edLoginEmail.text.toString()
            val password = edLoginPassword.text.toString()

            if (email.isEmpty()) {
                showToast(getString(R.string.error_empty_field, email))
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast(getString(R.string.error_wrong_email_format))
            } else if (password.isEmpty()) {
                showToast(getString(R.string.error_empty_field, password))
            } else {
                val user = User(
                    email = email,
                    password = password
                )

                viewModel.login(user).observe(this@LoginActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is ResultStatus.Loading -> {
                                showLoading(true)
                            }

                            is ResultStatus.Success -> {
                                showLoading(false)
                                showToast(getString(R.string.login_success))
                                val intent =
                                    Intent(this@LoginActivity, HomeActivity::class.java).apply {
                                        flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    }
                                startActivity(intent)
                            }

                            is ResultStatus.Error -> {
                                showLoading(false)
                                showToast(getString(R.string.error_wrong_input))

                            }
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            btnLogin.visibility = if (isLoading) View.GONE else View.VISIBLE
            btnLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}