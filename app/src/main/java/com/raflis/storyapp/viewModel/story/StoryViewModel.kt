package com.raflis.storyapp.viewModel.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.raflis.storyapp.data.ResultStatus
import com.raflis.storyapp.data.remote.entity.Story
import com.raflis.storyapp.data.remote.repository.StoryRepository
import java.io.File

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getAllStories(): LiveData<ResultStatus<PagingData<Story>>> = storyRepository.getAllStories()

    fun createStory(imageFile: File, description: String) =
        storyRepository.createStory(imageFile, description)

}