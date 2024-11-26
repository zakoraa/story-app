package com.raflis.storyapp.data.di

import android.content.Context
import com.raflis.storyapp.data.local.database.AuthPreferences
import com.raflis.storyapp.data.local.database.dataStore
import com.raflis.storyapp.data.remote.repository.AuthRepository
import com.raflis.storyapp.data.remote.repository.StoryRepository
import com.raflis.storyapp.data.remote.retrofit.AuthConfig
import com.raflis.storyapp.data.remote.retrofit.StoryConfig

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = AuthConfig.getAuthService()
        val authPreferences = AuthPreferences.getInstance(context.dataStore)
        return AuthRepository.getInstance(apiService, authPreferences)
    }

    fun provideStoryRepository(): StoryRepository {
        val apiService = StoryConfig.getStoryService()
        return StoryRepository.getInstance(apiService)
    }
}