package com.raflis.storyapp.data.remote.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(

    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
) : Parcelable