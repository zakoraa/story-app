package com.raflis.storyapp.viewModel.auth

import androidx.lifecycle.ViewModel
import com.raflis.storyapp.data.remote.entity.User
import com.raflis.storyapp.data.remote.repository.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun login(user: User) = authRepository.login(user)

    fun signUp(user: User) = authRepository.signUp(user)

}