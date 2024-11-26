package com.raflis.storyapp.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raflis.storyapp.data.di.Injection
import com.raflis.storyapp.data.remote.repository.AuthRepository
import com.raflis.storyapp.data.remote.repository.StoryRepository
import com.raflis.storyapp.viewModel.auth.AuthViewModel
import com.raflis.storyapp.viewModel.story.StoryViewModel

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository,
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideAuthRepository(context),
                    Injection.provideStoryRepository()
                )
            }.also { instance = it }
    }
}