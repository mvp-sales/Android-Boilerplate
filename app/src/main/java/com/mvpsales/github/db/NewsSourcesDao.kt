package com.mvpsales.github.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsSourcesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg sources: NewsSourceEntity)

    @Query("delete from newssourceentity where :sourceId = sourceId")
    suspend fun delete(sourceId: String)

    @Query("select * from newssourceentity")
    fun getAll(): List<NewsSourceEntity>
}