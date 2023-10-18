package com.richard.storyapp.core.data.remote.api

import com.richard.storyapp.core.data.remote.request.LoginRequest
import com.richard.storyapp.core.data.remote.request.RegisterRequest
import com.richard.storyapp.core.data.remote.response.LoginResponse
import com.richard.storyapp.core.data.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

}