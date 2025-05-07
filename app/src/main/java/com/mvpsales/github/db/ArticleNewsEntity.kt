package com.mvpsales.github.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["url"], unique = true)])
data class ArticleNewsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?,
    @Embedded val source: ArticleSourceNews
)

data class ArticleSourceNews(
    @ColumnInfo(name = "source_id") val id: String?,
    @ColumnInfo(name = "source_name") val name: String
)