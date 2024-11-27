package com.raflis.storyapp.data.di

import android.content.Context
import com.raflis.storyapp.data.local.database.AuthPreferences
import com.raflis.storyapp.data.local.database.dataStore
import com.raflis.storyapp.data.remote.repository.AuthRepository
import com.raflis.storyapp.data.remote.repository.StoryRepository
import com.raflis.storyapp.data.remote.retrofit.AuthConfig
import com.raflis.storyapp.data.remote.retrofit.StoryConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val authPreferences = AuthPreferences.getInstance(context.dataStore)
        val user = runBlocking { authPreferences.getUserSession().first() }
        val apiService = AuthConfig.getAuthService(user.token)
        return AuthRepository.getInstance(apiService, authPreferences)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = AuthPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserSession().first() }
        val apiService = StoryConfig.getStoryService(user.token)
        return StoryRepository.getInstance(apiService)
    }
}