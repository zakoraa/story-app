package com.raflis.storyapp.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.raflis.storyapp.R
import com.raflis.storyapp.data.ResultStatus
import com.raflis.storyapp.data.remote.entity.User
import com.raflis.storyapp.databinding.ActivitySignUpBinding
import com.raflis.storyapp.viewModel.ViewModelFactory
import com.raflis.storyapp.viewModel.auth.AuthViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: AuthViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initAction()
    }

    private fun initView() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun initAction() {
        with(binding) {
            edRegisterName.setInputName(getString(R.string.username))
            edRegisterEmail.setInputName(getString(R.string.email))
            edRegisterPassword.setInputName(getString(R.string.password))

            tvBackToLogin.setOnClickListener {
                finish()
            }
            btnSignUp.setOnClickListener {
                signUp()
            }
        }
    }


    private fun signUp() {
        with(binding) {
            val username = edRegisterName.text.toString()
            val email = edRegisterEmail.text.toString()
            val password = edRegisterPassword.text.toString()

            if (email.isEmpty()) {
                showToast(getString(R.string.error_empty_field, username))
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast(getString(R.string.error_wrong_email_format))
            } else if (email.isEmpty()) {
                showToast(getString(R.string.error_empty_field, email))
            } else if (password.isEmpty()) {
                showToast(getString(R.string.error_empty_field, password))
            } else {
                val user = User(
                    name = username,
                    email = email,
                    password = password
                )

                viewModel.signUp(user).observe(this@SignUpActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is ResultStatus.Loading -> {
                                showLoading(true)
                            }

                            is ResultStatus.Success -> {
                                showLoading(false)
                                showToast(getString(R.string.sign_up_success))
                                finish()
                            }

                            is ResultStatus.Error -> {
                                showLoading(false)
                                showToast(getString(R.string.error_something_wrong))

                            }
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            btnSignUp.visibility = if (isLoading) View.GONE else View.VISIBLE
            btnLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}