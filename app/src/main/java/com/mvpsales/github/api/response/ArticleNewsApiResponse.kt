package com.mvpsales.github.api.response

@kotlinx.serialization.Serializable
data class ArticleNewsApiResponse(
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String,
    val source: ArticleSourceNewsApiResponse
)

@kotlinx.serialization.Serializable
data class ArticleSourceNewsApiResponse(
    val id: String?,
    val name: String
)