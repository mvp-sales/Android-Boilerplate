
package com.mvpsales.github.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

@kotlinx.serialization.Serializable
@Parcelize
data class ArticleNews(
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?,
    val source: ArticleSource
): Parcelable

@kotlinx.serialization.Serializable
@Parcelize
data class ArticleSource(
    val id: String?,
    val name: String
): Parcelable

fun ArticleNews.formatPublishedDate(format: String): String {
    val fixedDate = publishedAt.replace("+00:00", "Z")
    val instant = Instant.parse(fixedDate).truncatedTo(ChronoUnit.MILLIS)
    val date = Date.from(instant)
    val dateFormatterTo = SimpleDateFormat(format, Locale.US)
    return dateFormatterTo.format(date)
}