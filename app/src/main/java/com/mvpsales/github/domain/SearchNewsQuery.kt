package com.mvpsales.github.domain

enum class SearchType {
    EVERYTHING, HEADLINES
}

data class SearchNewsQuery(
    val searchType: SearchType = SearchType.EVERYTHING,
    val searchTerm: String = "",
    val sources: List<String> = emptyList(),
    val page: Int = 1
)