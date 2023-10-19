package com.richard.storyapp.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.richard.storyapp.core.data.local.room.remotekey.RemoteKey
import com.richard.storyapp.core.data.local.room.remotekey.RemoteKeyDao

@Database(entities = [Story::class, RemoteKey::class], version = 1, exportSchema = false)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}