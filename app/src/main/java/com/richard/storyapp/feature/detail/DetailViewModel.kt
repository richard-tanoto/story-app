package com.richard.storyapp.feature.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.richard.storyapp.core.data.StoryRepository
import com.richard.storyapp.core.data.remote.response.ApiResult
import com.richard.storyapp.core.data.remote.response.StoryDetailResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    storyRepository: StoryRepository
) : ViewModel() {

    private val id = MutableLiveData<String>()

    val storyDetail: LiveData<ApiResult<StoryDetailResponse>> = id.switchMap {
        storyRepository.getStory(it).asLiveData()
    }

    fun setId(id: String) {
        this.id.value = id
    }

    fun getId(): String? = id.value

}