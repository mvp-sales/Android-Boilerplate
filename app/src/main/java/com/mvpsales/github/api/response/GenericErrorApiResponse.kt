package com.mvpsales.github.api.response

@kotlinx.serialization.Serializable
data class GenericErrorApiResponse(
    val status: String,
    val code: String,
    val message: String
)
