package com.raflis.storyapp.data.remote.retrofit

import com.raflis.storyapp.data.remote.response.LoginResponse
import com.raflis.storyapp.data.remote.response.SignUpResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @FormUrlEncoded
    @POST("/register")
    suspend fun signUp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): SignUpResponse

    @FormUrlEncoded
    @POST("/login")
    suspend fun signUp(
        @Field("name") name: String,
        @Field("email") email: String,
    ): LoginResponse
}