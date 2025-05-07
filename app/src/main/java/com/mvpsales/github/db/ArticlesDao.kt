package com.mvpsales.github.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.ktor.http.Url

@Dao
interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg articles: ArticleNewsEntity)

    @Query("delete from articlenewsentity where :articleUrl = url")
    suspend fun delete(articleUrl: String)

    @Query("select * from articlenewsentity")
    fun getAll(): List<ArticleNewsEntity>

    @Query("select * from articlenewsentity where :articleUrl = url limit 1")
    fun getArticle(articleUrl: String): ArticleNewsEntity?
}