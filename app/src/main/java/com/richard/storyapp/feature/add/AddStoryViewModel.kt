package com.richard.storyapp.feature.add

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richard.storyapp.core.data.StoryRepository
import com.richard.storyapp.core.data.remote.response.AddStoryResponse
import com.richard.storyapp.core.data.remote.response.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _uri = MutableLiveData<Uri?>()
    val uri: LiveData<Uri?> get() = _uri

    private val _addStoryResponse = MutableLiveData<ApiResult<AddStoryResponse>>()
    val addStoryResponse: LiveData<ApiResult<AddStoryResponse>> get() = _addStoryResponse

    fun uploadStory(
        image: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ) = viewModelScope.launch {
        storyRepository.addStory(image, description, lat, lon).collect {
            _addStoryResponse.value = it
        }
    }

    fun cancelRequest() {
        viewModelScope.coroutineContext.cancelChildren()
        _addStoryResponse.value = ApiResult.Error("Cancelled")
    }

    fun setUri(uri: Uri) {
        _uri.value = uri
    }

    fun clearUri() {
        _uri.value = null
    }

}