package com.mvpsales.github.entities

enum class SearchType {
    EVERYTHING, HEADLINES
}

data class SearchNewsQuery(
    val searchTerm: String = "",
    val sources: List<String> = emptyList(),
    val page: Int = 1
)