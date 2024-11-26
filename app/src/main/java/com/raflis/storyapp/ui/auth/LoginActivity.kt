package com.raflis.storyapp.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
        playAnimation()
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
                showToast(getString(R.string.error_empty_field, getString(R.string.email)))
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast(getString(R.string.error_wrong_email_format))
            } else if (password.isEmpty()) {
                showToast(getString(R.string.error_empty_field, getString(R.string.password)))
            } else if (password.length < 8) {
                showToast(
                    getString(
                        R.string.error_min_length_field,
                        getString(R.string.password),
                        8
                    )
                )
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
            tvDirectToSignUp.isClickable = !isLoading
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun playAnimation() {
        with(binding) {
            ObjectAnimator.ofFloat(decoration, View.TRANSLATION_X, 100f, 0f).apply {
                duration = 2000
            }.start()

            val appName = ObjectAnimator.ofFloat(tvAppName,View.TRANSLATION_Y, 100f,0f).setDuration(1000)
            val title = ObjectAnimator.ofFloat(tvTitle,View.TRANSLATION_Y, 100f,0f).setDuration(1000)
            val desc = ObjectAnimator.ofFloat(tvDesc,View.TRANSLATION_Y, 100f,0f).setDuration(1000)
            val edtEmail = ObjectAnimator.ofFloat(edLoginEmail,View.TRANSLATION_Y, 100f,0f).setDuration(1000)
            val edtPass = ObjectAnimator.ofFloat(edLoginPassword,View.TRANSLATION_Y, 100f,0f).setDuration(1000)
            val btnLogin = ObjectAnimator.ofFloat(btnLogin,View.TRANSLATION_Y, 100f,0f).setDuration(1000)
            val llBellowBtn = ObjectAnimator.ofFloat(llBellowBtn,View.TRANSLATION_Y, 100f,0f).setDuration(1000)

            val together = AnimatorSet().apply {
                playTogether(appName, title, desc,edtEmail, edtPass, btnLogin, llBellowBtn)
            }

            AnimatorSet().apply {
                playSequentially(together)
                start()
            }
        }

    }

}