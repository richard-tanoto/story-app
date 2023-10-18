package com.richard.storyapp.core.data.remote.response

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error<out T>(val message: String?) : ApiResult<T>()
    class Loading<out T> : ApiResult<T>()
}