package com.raflis.storyapp.data.local.entity

data class UserLocal (
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)