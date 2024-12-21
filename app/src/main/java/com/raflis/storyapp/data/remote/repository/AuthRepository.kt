package com.raflis.storyapp.data.remote.repository

import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.raflis.storyapp.data.ResultStatus
import com.raflis.storyapp.data.local.entity.UserLocal
import com.raflis.storyapp.data.local.pref.AuthPreferences
import com.raflis.storyapp.data.remote.entity.User
import com.raflis.storyapp.data.remote.response.LoginResponse
import com.raflis.storyapp.data.remote.retrofit.AuthService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class AuthRepository private constructor(
    private val authService: AuthService,
    private val authPreferences: AuthPreferences
) {

    fun login(user: User) = liveData {
        emit(ResultStatus.Loading)
        try {
            val successResponse =
                authService.login(email = user.email, password = user.password)
            authPreferences.saveSession(
                UserLocal(
                    email = user.email,
                    token = successResponse.loginResult.token,
                    isLogin = true
                )
            )
            emit(ResultStatus.Success(successResponse))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            emit(ResultStatus.Error(errorBody.message))
        }
    }

    fun signUp(user: User) = liveData {
        emit(ResultStatus.Loading)
        try {
            val successResponse =
                authService.signUp(
                    name = user.name,
                    email = user.email,
                    password = user.password
                )
            emit(ResultStatus.Success(successResponse))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            emit(ResultStatus.Error(errorBody.message))
        }
    }

    suspend fun logout() {
        authPreferences.logout()
    }

    fun getSession(): Flow<UserLocal> {
        return authPreferences.getUserSession()
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            authService: AuthService, authPreferences: AuthPreferences
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(authService, authPreferences)
            }.also { instance = it }
    }

}