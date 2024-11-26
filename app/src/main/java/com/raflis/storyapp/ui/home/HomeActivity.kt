package com.raflis.storyapp.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.raflis.storyapp.R
import com.raflis.storyapp.data.ResultStatus
import com.raflis.storyapp.databinding.ActivityHomeBinding
import com.raflis.storyapp.viewModel.ViewModelFactory
import com.raflis.storyapp.viewModel.story.StoryViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: StoryViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAllStories()

    }

    private fun getAllStories() {
        viewModel.getAllStories().observe(this) { result ->
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