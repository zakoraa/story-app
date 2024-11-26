package com.raflis.storyapp.data.remote.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.raflis.storyapp.data.ResultStatus
import com.raflis.storyapp.data.remote.entity.Story
import com.raflis.storyapp.data.remote.response.CreateStoryResponse
import com.raflis.storyapp.data.remote.retrofit.StoryService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val storyService: StoryService,
) {

    fun getAllStories(): LiveData<ResultStatus<List<Story>>> = liveData {
        emit(ResultStatus.Loading)
        try {
            val response = storyService.getAllStories()
            val stories = response.listStory
            val storyList = stories.map { story ->
                Story(
                    id = story.id,
                    name = story.name,
                    description = story.description,
                    photoUrl = story.photoUrl,
                    createdAt = story.createdAt,
                )
            }
            emit(ResultStatus.Success(storyList))
        } catch (e: Exception) {
            Log.d("StoryRepository", "getAllStories: ${e.message.toString()} ")
            emit(ResultStatus.Error(e.message.toString()))
        }
    }

    fun createStory(imageFile: File, description: String) = liveData {
        emit(ResultStatus.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = storyService.createStory(multipartBody, requestBody)
            emit(ResultStatus.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, CreateStoryResponse::class.java)
            emit(ResultStatus.Error(errorResponse.message))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            storyService: StoryService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(storyService)
            }.also { instance = it }
    }
}