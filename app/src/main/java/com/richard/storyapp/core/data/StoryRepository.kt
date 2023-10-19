package com.richard.storyapp.core.data

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.richard.storyapp.core.data.local.room.Story
import com.richard.storyapp.core.data.local.room.StoryDatabase
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val storyRemoteMediator: StoryRemoteMediator,
    private val storyDatabase: StoryDatabase,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getStories(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = storyRemoteMediator,
            pagingSourceFactory = { storyDatabase.storyDao().getStories() }
        ).liveData
    }

}