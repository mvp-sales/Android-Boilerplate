package com.mvpsales.github.api.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Date
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
    val fixedDate = publishedAt.replace("+00:00", "Z");
    val instant = Instant.parse(fixedDate).truncatedTo(ChronoUnit.MILLIS)
    val date = Date.from(instant)
    val dateFormatterTo = SimpleDateFormat("dd MMM yyyy", Locale.US)
    return dateFormatterTo.format(date)
}