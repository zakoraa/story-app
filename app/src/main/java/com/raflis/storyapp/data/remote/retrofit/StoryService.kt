package com.raflis.storyapp.data.remote.retrofit

import com.raflis.storyapp.data.remote.response.CreateStoryResponse
import com.raflis.storyapp.data.remote.response.GetAllStoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface StoryService {
    @GET("stories")
    suspend fun getAllStories(@Header("Authorization") token: String): GetAllStoriesResponse

    @Multipart
    @POST("stories")
    suspend fun createStory(
        @Header("Authorization") token: String,
        @Part imageFile: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): CreateStoryResponse
}