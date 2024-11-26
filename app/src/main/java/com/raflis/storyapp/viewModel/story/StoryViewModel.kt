package com.raflis.storyapp.viewModel.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raflis.storyapp.data.remote.repository.StoryRepository
import kotlinx.coroutines.launch
import java.io.File

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getAllStories() = storyRepository.getAllStories()

    fun createStory(imageFile: File, description: String) {
        viewModelScope.launch {
            storyRepository.createStory(imageFile, description)
        }
    }

}