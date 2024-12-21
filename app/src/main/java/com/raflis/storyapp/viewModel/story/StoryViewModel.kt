package com.raflis.storyapp.viewModel.story

import androidx.lifecycle.ViewModel
import com.raflis.storyapp.data.remote.repository.StoryRepository
import java.io.File

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getAllStories() = storyRepository.getAllStories()

    fun createStory(imageFile: File, description: String, lat: Double?, lon: Double?) =
        storyRepository.createStory(imageFile, description, lat, lon)

}