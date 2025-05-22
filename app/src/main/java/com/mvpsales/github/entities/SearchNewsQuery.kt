package com.mvpsales.github.entities

data class SearchNewsQuery(
    val searchTerm: String = "",
    val sources: List<String> = emptyList(),
    val page: Int = 1
)