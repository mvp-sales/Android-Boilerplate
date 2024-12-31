package com.mvpsales.github.api.response

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
