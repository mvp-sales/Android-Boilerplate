package com.mvpsales.github.api.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

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

fun ArticleNewsApiResponse.formatPublishedDate(format: String): String? {
    val dateFormatterFrom = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.US)
    val date = dateFormatterFrom.parse(this.publishedAt)
    date?.let { datenn ->
        val dateFormatterTo = SimpleDateFormat("dd MMM yyyy", Locale.US)
        return dateFormatterTo.format(datenn)
    } ?: return null
}