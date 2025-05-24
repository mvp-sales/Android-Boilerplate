package com.mvpsales.github.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@kotlinx.serialization.Serializable
@Parcelize
data class NewsSource(
    val id: String,
    val name: String,
    val description: String,
    val url: String,
    val category: String,
    val language: String,
    val country: String
): Parcelable
