package com.mvpsales.github.api.response

import com.mvpsales.github.entities.NewsSource

@kotlinx.serialization.Serializable
data class NewsSourceApiResponse(
    val id: String,
    val name: String,
    val description: String,
    val url: String,
    val category: String,
    val language: String,
    val country: String
)

fun NewsSourceApiResponse.toEntity() = NewsSource(
    id, name, description, url, category, language, country
)
