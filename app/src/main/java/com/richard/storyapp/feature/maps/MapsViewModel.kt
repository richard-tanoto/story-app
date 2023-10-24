package com.richard.storyapp.feature.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.richard.storyapp.core.data.StoryRepository
import com.richard.storyapp.core.data.remote.response.ApiResult
import com.richard.storyapp.core.data.remote.response.GetStoriesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    storyRepository: StoryRepository
) : ViewModel() {

    val stories: LiveData<ApiResult<GetStoriesResponse>> = storyRepository.getStoriesWithLocation().asLiveData()

}