package com.mvpsales.github.api.request

import com.mvpsales.github.domain.SearchType

data class GetNewsRequest(
    val searchType: SearchType,
    val query: String = "",
    val sources: List<String> = emptyList(),
    val page: Int = 1,
    val pageSize: Int = 25
)

internal fun SearchType.endpoint() = when(this) {
    SearchType.EVERYTHING -> "everything"
    SearchType.HEADLINES -> "top-headlines"
}