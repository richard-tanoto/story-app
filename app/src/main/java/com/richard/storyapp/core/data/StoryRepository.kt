package com.richard.storyapp.core.data

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.richard.storyapp.core.data.local.room.Story
import com.richard.storyapp.core.data.local.room.StoryDatabase
import com.richard.storyapp.core.data.remote.api.ApiService
import com.richard.storyapp.core.data.remote.response.AddStoryResponse
import com.richard.storyapp.core.data.remote.response.ApiResult
import com.richard.storyapp.core.data.remote.response.GetStoriesResponse
import com.richard.storyapp.core.data.remote.response.StoryDetailResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val storyRemoteMediator: StoryRemoteMediator,
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
) {

    fun addStory(
        image: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Flow<ApiResult<AddStoryResponse>> = flow {
        try {
            val response = apiService.uploadStory(image, description, lat, lon)
            emit(ApiResult.Success(response))
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message))
        }
    }.onStart {
        emit(ApiResult.Loading())
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getStories(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = storyRemoteMediator,
            pagingSourceFactory = { storyDatabase.storyDao().getStories() }
        ).liveData
    }

    fun getStoriesWithLocation(): Flow<ApiResult<GetStoriesResponse>> = flow {
        try {
            val response = apiService.getStories(location = 1)
            emit(ApiResult.Success(response))
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message))
        }
    }.onStart {
        emit(ApiResult.Loading())
    }

    fun getStory(id: String): Flow<ApiResult<StoryDetailResponse>> = flow {
        try {
            val response = apiService.getStory(id)
            emit(ApiResult.Success(response))
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message))
        }
    }.onStart {
        emit(ApiResult.Loading())
    }

}