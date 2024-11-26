package com.raflis.storyapp.ui.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raflis.storyapp.R
import com.raflis.storyapp.data.remote.entity.Story
import com.raflis.storyapp.databinding.StoryCardItemBinding
import com.raflis.storyapp.ui.story_detail.StoryDetailActivity
import com.raflis.storyapp.utils.DateUtil


class StoryAdapter(private val listStories: List<Story>) :
    RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {

    companion object {
        const val EXTRA_STORY = "extra_story"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            StoryCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = listStories[position]
        story.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = listStories.size

    inner class ListViewHolder(private val binding: StoryCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(ivStoryImage)
                tvStoryName.text = story.name
                tvStoryDesc.text = story.description
                tvStoryTime.text = DateUtil.formatToLocalizedDate(story.createdAt)

                itemView.setOnClickListener {
                    val context = itemView.context
                    val intent = Intent(context, StoryDetailActivity::class.java)
                    intent.putExtra(EXTRA_STORY, story)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            Pair(ivStoryImage, "image"),
                            Pair(tvStoryName, "name"),
                            Pair(tvStoryDesc, "description"),
                            Pair(tvStoryTime, "time"),
                        )
                    context.startActivity(intent, optionsCompat.toBundle())
                }
            }

        }
    }
}
