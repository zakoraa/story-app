package com.raflis.storyapp.data.remote.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class Story(

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("photo")
    val photo: File? = null,

    @field:SerializedName("password")
    val password: String? = null
) : Parcelable
