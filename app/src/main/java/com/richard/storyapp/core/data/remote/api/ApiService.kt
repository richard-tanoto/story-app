package com.richard.storyapp.core.data.remote.api

import com.richard.storyapp.core.data.remote.request.LoginRequest
import com.richard.storyapp.core.data.remote.request.RegisterRequest
import com.richard.storyapp.core.data.remote.response.AddStoryResponse
import com.richard.storyapp.core.data.remote.response.GetStoriesResponse
import com.richard.storyapp.core.data.remote.response.LoginResponse
import com.richard.storyapp.core.data.remote.response.RegisterResponse
import com.richard.storyapp.core.data.remote.response.StoryDetailResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
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

    @GET("stories/{id}")
    suspend fun getStory(
        @Path("id") id: String
    ): StoryDetailResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?,
    ): AddStoryResponse

}