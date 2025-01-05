package com.mvpsales.github.api.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@kotlinx.serialization.Serializable
@Parcelize
data class ArticleNewsApiResponse(
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String,
    val source: ArticleSourceNewsApiResponse
) : Parcelable

@kotlinx.serialization.Serializable
@Parcelize
data class ArticleSourceNewsApiResponse(
    val id: String?,
    val name: String
) : Parcelable