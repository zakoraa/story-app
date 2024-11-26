package com.raflis.storyapp.ui.create_story

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.raflis.storyapp.databinding.ActivityCreateStoryBinding
import com.raflis.storyapp.viewModel.ViewModelFactory
import com.raflis.storyapp.viewModel.story.StoryViewModel

class CreateStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateStoryBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: StoryViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}