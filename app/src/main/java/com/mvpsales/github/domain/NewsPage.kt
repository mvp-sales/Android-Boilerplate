package com.mvpsales.github.domain

data class NewsPage(
    val articles: List<ArticleNews>,
    val totalResults: Int,
    val page: Int
)
