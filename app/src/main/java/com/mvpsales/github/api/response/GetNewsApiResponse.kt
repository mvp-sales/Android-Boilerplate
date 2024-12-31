package com.mvpsales.github.api.response

@kotlinx.serialization.Serializable
data class GetNewsApiResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleNewsApiResponse>
)
