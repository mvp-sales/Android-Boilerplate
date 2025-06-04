package com.mvpsales.github.entities

data class NewsPage(
    val articles: List<ArticleNews>,
    val totalResults: Int,
    val page: Int
)
