package com.raflis.storyapp.viewModel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raflis.storyapp.data.remote.entity.User
import com.raflis.storyapp.data.remote.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun login(user: User) = authRepository.login(user)

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun signUp(user: User) = authRepository.signUp(user)

}