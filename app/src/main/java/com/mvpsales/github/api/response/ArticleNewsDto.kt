package com.mvpsales.github.api.response

import android.os.Parcelable
import com.mvpsales.github.domain.ArticleNews
import com.mvpsales.github.domain.ArticleSource
import kotlinx.parcelize.Parcelize

@kotlinx.serialization.Serializable
@Parcelize
data class ArticleNewsDto(
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?,
    val source: ArticleSourceNewsDto
) : Parcelable

@kotlinx.serialization.Serializable
@Parcelize
data class ArticleSourceNewsDto(
    val id: String?,
    val name: String
) : Parcelable

fun ArticleNewsDto.toEntity(): ArticleNews = ArticleNews(
    this.author,
    this.title,
    this.description,
    this.url,
    this.urlToImage,
    this.publishedAt,
    this.content,
    ArticleSource(
        this.source.id,
        this.source.name
    )
)