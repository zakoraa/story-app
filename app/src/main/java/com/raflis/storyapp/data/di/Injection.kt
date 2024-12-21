package com.raflis.storyapp.data.di

import android.content.Context
import com.raflis.storyapp.data.local.database.StoryDatabase
import com.raflis.storyapp.data.local.pref.AuthPreferences
import com.raflis.storyapp.data.local.pref.dataStore
import com.raflis.storyapp.data.remote.repository.AuthRepository
import com.raflis.storyapp.data.remote.repository.StoryRepository
import com.raflis.storyapp.data.remote.retrofit.AuthConfig
import com.raflis.storyapp.data.remote.retrofit.StoryConfig

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val authPreferences = AuthPreferences.getInstance(context.dataStore)
        val apiService = AuthConfig.getAuthService()
        return AuthRepository.getInstance(apiService, authPreferences)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val authPreferences = AuthPreferences.getInstance(context.dataStore)
        val apiService = StoryConfig.getStoryService()
        val storyDatabase = StoryDatabase.getInstance(context)
        return StoryRepository.getInstance(apiService, authPreferences, storyDatabase)
    }
}