package com.richard.storyapp.di

import android.content.Context
import androidx.room.Room
import com.richard.storyapp.core.data.StoryRemoteMediator
import com.richard.storyapp.core.data.StoryRepository
import com.richard.storyapp.core.data.local.room.StoryDatabase
import com.richard.storyapp.core.data.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StoryDatabase =
        Room.databaseBuilder(context, StoryDatabase::class.java, "stories.db").build()

    @Provides
    @Singleton
    fun provideStoryRemoteMediator(
        apiService: ApiService,
        database: StoryDatabase
    ): StoryRemoteMediator = StoryRemoteMediator(apiService, database)

    @Provides
    @Singleton
    fun provideStoryRepository(
        remoteMediator: StoryRemoteMediator,
        database: StoryDatabase,
        apiService: ApiService
    ): StoryRepository = StoryRepository(remoteMediator, database, apiService)

}