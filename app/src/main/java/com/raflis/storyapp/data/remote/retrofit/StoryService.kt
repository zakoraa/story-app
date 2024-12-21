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
import retrofit2.http.Query

interface StoryService {
    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page : Int = 1,
        @Query("size") size : Int = 5
    ): GetAllStoriesResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Header("Authorization") token: String,
        @Query("location") location : Int = 1,
    ): GetAllStoriesResponse

    @Multipart
    @POST("stories")
    suspend fun createStory(
        @Header("Authorization") token: String,
        @Part imageFile: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat : RequestBody? = null,
        @Part("lon") lon : RequestBody? = null
    ): CreateStoryResponse
}