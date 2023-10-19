package com.richard.storyapp.core.data.remote.response

import com.google.gson.annotations.SerializedName
import com.richard.storyapp.core.data.local.room.Story

data class GetStoriesResponse(

	@field:SerializedName("listStory")
	val listStory: List<Story>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)