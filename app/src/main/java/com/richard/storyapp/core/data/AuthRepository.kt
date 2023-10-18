package com.richard.storyapp.core.data

import com.richard.storyapp.core.data.remote.api.ApiService
import com.richard.storyapp.core.data.remote.request.LoginRequest
import com.richard.storyapp.core.data.remote.request.RegisterRequest
import com.richard.storyapp.core.data.remote.response.ApiResult
import com.richard.storyapp.core.data.remote.response.LoginResponse
import com.richard.storyapp.core.data.remote.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun login(request: LoginRequest): Flow<ApiResult<LoginResponse>> = flow {
        try {
            val response = apiService.login(request)
            emit(ApiResult.Success(response))
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message))
        }
    }.onStart {
        emit(ApiResult.Loading())
    }.flowOn(Dispatchers.IO)

    fun register(name: String, email: String, password: String): Flow<ApiResult<RegisterResponse>> =
        flow {
            try {
                val response = apiService.register(RegisterRequest(name, email, password))
                emit(ApiResult.Success(response))
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message))
            }
        }.onStart {
            emit(ApiResult.Loading())
        }.flowOn(Dispatchers.IO)

}