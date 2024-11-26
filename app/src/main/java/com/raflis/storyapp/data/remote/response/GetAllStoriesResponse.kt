package com.raflis.storyapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.raflis.storyapp.data.remote.entity.Story

data class GetAllStoriesResponse(

	@field:SerializedName("listStory")
	val listStory: List<Story>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
