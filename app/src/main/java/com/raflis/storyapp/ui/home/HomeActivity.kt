package com.raflis.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.raflis.storyapp.R
import com.raflis.storyapp.data.ResultStatus
import com.raflis.storyapp.databinding.ActivityHomeBinding
import com.raflis.storyapp.ui.auth.LoginActivity
import com.raflis.storyapp.viewModel.ViewModelFactory
import com.raflis.storyapp.viewModel.auth.AuthViewModel
import com.raflis.storyapp.viewModel.story.StoryViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val storyViewModel: StoryViewModel by viewModels {
        factory
    }
    private val authViewModel: AuthViewModel by viewModels {
        factory
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getAllStories()
        logout()
    }


    private fun logout() {
        with(binding) {
            ivLogout.setOnClickListener {
                authViewModel.logout()
                val intent =
                    Intent(this@HomeActivity, LoginActivity::class.java).apply {
                        flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                startActivity(intent)
            }
        }
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
                                getString(R.string.error_occurs) + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}