package com.raflis.storyapp.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
        playAnimation()
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

            if (username.isEmpty()) {
                showToast(getString(R.string.error_empty_field, getString(R.string.username)))
            } else if (email.isEmpty()) {
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
            tvBackToLogin.isClickable = !isLoading
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

            val appName =
                ObjectAnimator.ofFloat(tvAppName, View.TRANSLATION_Y, 100f, 0f).setDuration(1000)
            val title =
                ObjectAnimator.ofFloat(tvTitle, View.TRANSLATION_Y, 100f, 0f).setDuration(1000)
            val desc =
                ObjectAnimator.ofFloat(tvDesc, View.TRANSLATION_Y, 100f, 0f).setDuration(1000)
            val edtUsername = ObjectAnimator.ofFloat(edRegisterName, View.TRANSLATION_Y, 100f, 0f)
                .setDuration(1000)
            val edtEmail = ObjectAnimator.ofFloat(edRegisterEmail, View.TRANSLATION_Y, 100f, 0f)
                .setDuration(1000)
            val edtPass = ObjectAnimator.ofFloat(edRegisterPassword, View.TRANSLATION_Y, 100f, 0f)
                .setDuration(1000)
            val btnLogin =
                ObjectAnimator.ofFloat(btnSignUp, View.TRANSLATION_Y, 100f, 0f).setDuration(1000)
            val llBellowBtn =
                ObjectAnimator.ofFloat(llBellowBtn, View.TRANSLATION_Y, 100f, 0f).setDuration(1000)

            val together = AnimatorSet().apply {
                playTogether(
                    edtUsername,
                    appName,
                    title,
                    desc,
                    edtEmail,
                    edtPass,
                    btnLogin,
                    llBellowBtn
                )
            }

            AnimatorSet().apply {
                playSequentially(together)
                start()
            }
        }

    }
}