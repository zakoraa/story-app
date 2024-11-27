package com.raflis.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.raflis.storyapp.R
import com.raflis.storyapp.data.ResultStatus
import com.raflis.storyapp.databinding.ActivityHomeBinding
import com.raflis.storyapp.ui.auth.LoginActivity
import com.raflis.storyapp.ui.create_story.CreateStoryActivity
import com.raflis.storyapp.viewModel.ViewModelFactory
import com.raflis.storyapp.viewModel.auth.AuthViewModel
import com.raflis.storyapp.viewModel.story.StoryViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var factory: ViewModelFactory
    private val storyViewModel: StoryViewModel by viewModels {
        factory
    }
    private val authViewModel: AuthViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        factory = ViewModelFactory.getInstance(this)
        initView()
    }

    override fun onResume() {
        super.onResume()
        authViewModel.getSession().observe(this) { user ->
            Log.d("USERRR: ", user.toString())
            if (user.isLogin) {
                getAllStories()
            }
        }
    }

    private fun initView() {
        authViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, LoginActivity::class.java).apply {
                    flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish()
                return@observe
            } else {
                enableEdgeToEdge()
                setContentView(binding.root)
                ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    v.setPadding(
                        systemBars.left,
                        systemBars.top,
                        systemBars.right,
                        systemBars.bottom
                    )
                    insets
                }

                getAllStories()
                handleSettings()

                binding.floatBtnCreateStory.setOnClickListener {
                    val intent = Intent(this@HomeActivity, CreateStoryActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun handleSettings() {
        with(binding) {
            ivSettings.setOnClickListener {
                val options =
                    arrayOf(getString(R.string.select_language), getString(R.string.log_out_desc))
                val builder = AlertDialog.Builder(this@HomeActivity)
                builder.setItems(options) { _, which ->
                    when (which) {
                        0 -> {
                            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                        }

                        1 -> {
                            logout()
                        }
                    }
                }
                builder.show()
            }
        }
    }

    private fun logout() {
        authViewModel.logout()
        val intent =
            Intent(this@HomeActivity, LoginActivity::class.java).apply {
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        startActivity(intent)
    }


    private fun getAllStories() {
        storyViewModel.getAllStories().observe(this) { result ->
            with(binding) {
                if (result != null) {
                    when (result) {
                        is ResultStatus.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }

                        is ResultStatus.Success -> {
                            progressBar.visibility = View.GONE
                            if (result.data.isEmpty()) {
                                tvNoData.visibility = View.VISIBLE
                            }
                            val storyAdapter = StoryAdapter(result.data)
                            with(binding) {
                                recyclerView.adapter = storyAdapter
                                recyclerView.setHasFixedSize(true)
                                recyclerView.layoutManager =
                                    LinearLayoutManager(this@HomeActivity)
                            }
                        }

                        is ResultStatus.Error -> {
                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                this@HomeActivity,
                                getString(R.string.error_expired_session),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}