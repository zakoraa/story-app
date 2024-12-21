package com.raflis.storyapp.viewModel.maps

import androidx.lifecycle.ViewModel
import com.raflis.storyapp.data.remote.repository.StoryRepository

class MapsViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {
    fun getStoriesWithLocation() = storyRepository.getStoriesWithLocation(location = 1)
}