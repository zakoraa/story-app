package com.raflis.storyapp.ui.story_detail

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.raflis.storyapp.R
import com.raflis.storyapp.data.remote.entity.Story
import com.raflis.storyapp.databinding.ActivityStoryDetailBinding
import com.raflis.storyapp.ui.home.StoryAdapter
import com.raflis.storyapp.utils.DateUtil

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()

        binding.ibBack.setOnClickListener {
            finish()
        }
    }

    private fun initView() {
        val story = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(StoryAdapter.EXTRA_STORY, Story::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(StoryAdapter.EXTRA_STORY)
        }

        story?.let {
            Glide.with(this)
                .load(it.photoUrl)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.ivDetailPhoto)
            with(binding) {
                tvDetailName.text = it.name
                tvStoryTime.text = DateUtil.formatToLocalizedDate(it.createdAt)
                tvDetailDescription.text = it.description
            }
        } ?: run {
            Toast.makeText(this, "Story data is missing", Toast.LENGTH_SHORT).show()
        }
    }
}