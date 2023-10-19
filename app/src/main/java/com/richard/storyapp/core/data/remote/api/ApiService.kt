package com.richard.storyapp.core.data.remote.api

import com.richard.storyapp.core.data.remote.request.LoginRequest
import com.richard.storyapp.core.data.remote.request.RegisterRequest
import com.richard.storyapp.core.data.remote.response.GetStoriesResponse
import com.richard.storyapp.core.data.remote.response.LoginResponse
import com.richard.storyapp.core.data.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int = 0,
    ): GetStoriesResponse

}