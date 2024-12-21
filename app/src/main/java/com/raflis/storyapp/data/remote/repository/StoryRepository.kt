package com.raflis.storyapp.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.raflis.storyapp.data.ResultStatus
import com.raflis.storyapp.data.local.database.StoryDatabase
import com.raflis.storyapp.data.local.pref.AuthPreferences
import com.raflis.storyapp.data.remote.entity.Story
import com.raflis.storyapp.data.remote.response.CreateStoryResponse
import com.raflis.storyapp.data.remote.response.GetAllStoriesResponse
import com.raflis.storyapp.data.remote.retrofit.StoryService
import com.raflis.storyapp.data.remote_mediator.StoryRemoteMediator
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val storyService: StoryService,
    private val authPreferences: AuthPreferences,
    private val storyDatabase: StoryDatabase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getAllStories(): LiveData<ResultStatus<PagingData<Story>>> = liveData {
        emit(ResultStatus.Loading)
        try {
            val pager = Pager(
                config = PagingConfig(
                    pageSize = 5,
                ),
                remoteMediator = StoryRemoteMediator(authPreferences, storyDatabase, storyService),
                pagingSourceFactory = {
                    storyDatabase.storyDao().getAllStories()
                }
            )

            pager.flow.collect { pagingData ->
                emit(ResultStatus.Success(pagingData))
            }
        } catch (e: Exception) {
            emit(ResultStatus.Error(e.message.toString()))
        }
    }

    fun createStory(
        imageFile: File,
        description: String,
        lat: Double? = null,
        lon: Double? = null
    ) = liveData {
        emit(ResultStatus.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val latData = lat?.toString()?.toRequestBody("text/plain".toMediaType())
        val lonData = lon?.toString()?.toRequestBody("text/plain".toMediaType())

        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val token = authPreferences.getUserSession().firstOrNull()?.token
            if (token.isNullOrEmpty()) {
                Result.failure<Throwable>(Exception("Token not found"))
            }
            val successResponse =
                storyService.createStory(
                    "Bearer $token",
                    multipartBody,
                    requestBody,
                    latData,
                    lonData
                )
            emit(ResultStatus.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, CreateStoryResponse::class.java)
            emit(ResultStatus.Error(errorResponse.message))
        }
    }

    fun getStoriesWithLocation(location: Int = 1) = liveData {
        emit(ResultStatus.Loading)
        try {
            val token = authPreferences.getUserSession().firstOrNull()?.token
            if (token.isNullOrEmpty()) {
                Result.failure<Throwable>(Exception("Token not found"))
            }

            val res = storyService.getStoriesWithLocation("Bearer $token", location)

            emit(ResultStatus.Success(res))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, GetAllStoriesResponse::class.java)
            emit(ResultStatus.Error(errorResponse.message))
        }
    }


    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            storyService: StoryService,
            authPreferences: AuthPreferences,
            storyDatabase: StoryDatabase
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(storyService, authPreferences, storyDatabase)
            }.also { instance = it }
    }
}