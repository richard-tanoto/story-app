package com.richard.storyapp.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.richard.storyapp.core.data.StoryRepository
import com.richard.storyapp.core.data.local.room.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    storyRepository: StoryRepository
) : ViewModel() {

    val stories: LiveData<PagingData<Story>> = storyRepository.getStories().cachedIn(viewModelScope)

}