package com.raflis.storyapp.ui.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raflis.storyapp.R
import com.raflis.storyapp.data.remote.entity.Story
import com.raflis.storyapp.databinding.StoryCardItemBinding
import com.raflis.storyapp.ui.story_detail.StoryDetailActivity
import com.raflis.storyapp.utils.DateUtil

class StoryAdapter :
    PagingDataAdapter<Story, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = StoryCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val storyData = getItem(position)
        if (storyData != null) {
            holder.bind(storyData)
        }
    }

    inner class StoryViewHolder(private val binding: StoryCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: Story) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(ivItemPhoto)

                tvItemName.text = story.name
                tvStoryDesc.text = story.description
                tvStoryTime.text = DateUtil.formatToLocalizedDate(story.createdAt)

                itemView.setOnClickListener {
                    navigateToDetail(story)
                }
            }
        }

        private fun navigateToDetail(story: Story) {
            val context = itemView.context
            val intent = Intent(context, StoryDetailActivity::class.java).apply {
                putExtra(EXTRA_STORY, story)
            }

            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context as Activity,
                Pair(binding.ivItemPhoto, "image"),
                Pair(binding.tvItemName, "name"),
                Pair(binding.tvStoryDesc, "description"),
                Pair(binding.tvStoryTime, "time"),
            )
            context.startActivity(intent, optionsCompat.toBundle())
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Story,
                newItem: Story
            ): Boolean {
                return oldItem == newItem
            }

        }

    }
}
