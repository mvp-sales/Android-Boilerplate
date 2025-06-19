package com.mvpsales.github.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mvpsales.github.domain.NewsSource

@Entity(indices = [Index(value = ["sourceId"], unique = true)])
data class NewsSourceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sourceId: String,
    val name: String,
    val description: String,
    val url: String,
    val category: String,
    val language: String,
    val country: String
)

fun NewsSourceEntity.toDomain(): NewsSource =
    NewsSource(
        id = sourceId,
        name = name,
        description = description,
        url = url,
        category = category,
        language = language,
        country = country,
        favourite = true
    )

fun NewsSource.toDb(): NewsSourceEntity =
    NewsSourceEntity(
        sourceId = id,
        name = name,
        description = description,
        url = url,
        category = category,
        language = language,
        country = country
    )