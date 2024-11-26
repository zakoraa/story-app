package com.raflis.storyapp.viewModel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raflis.storyapp.data.remote.entity.User
import com.raflis.storyapp.data.remote.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun login(user: User) {
        viewModelScope.launch {
            authRepository.login(user)
        }
    }

    fun signUp(user: User) {
        viewModelScope.launch {
            authRepository.signUp(user)
        }
    }

}