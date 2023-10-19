package com.richard.storyapp.core.data.local.room.remotekey

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM remoteKeys WHERE id = :id")
    suspend fun getRemoteKeyId(id: String): RemoteKey?

    @Query("DELETE FROM remoteKeys")
    suspend fun deleteRemoteKeys()

}