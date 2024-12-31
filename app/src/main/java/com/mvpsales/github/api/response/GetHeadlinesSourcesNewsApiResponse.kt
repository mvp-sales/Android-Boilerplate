package com.mvpsales.github.api.response

@kotlinx.serialization.Serializable
data class GetHeadlinesSourcesNewsApiResponse(
    val status: String,
    val sources: List<NewsSourceApiResponse>
)
