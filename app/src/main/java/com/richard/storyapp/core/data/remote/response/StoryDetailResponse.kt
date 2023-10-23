package com.richard.storyapp.core.data.remote.response

data class StoryDetailResponse(
	val error: Boolean,
	val message: String,
	val story: StoryDetail
)

data class StoryDetail(
	val photoUrl: String,
	val createdAt: String,
	val name: String,
	val description: String,
	val lon: Any,
	val id: String,
	val lat: Any
)

