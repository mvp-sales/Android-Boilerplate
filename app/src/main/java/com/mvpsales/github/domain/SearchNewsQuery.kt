package com.mvpsales.github.domain

enum class SearchType {
    EVERYTHING, HEADLINES
}

data class SearchNewsQuery(
    val searchTerm: String = "",
    val sources: List<String> = emptyList(),
    val page: Int = 1
)