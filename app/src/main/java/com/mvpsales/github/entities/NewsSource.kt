package com.mvpsales.github.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Locale

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

fun NewsSource.getLanguageCountryNames(): Pair<String, String> {
    val locale = Locale(language, country)
    val displayLanguage = locale.getDisplayLanguage(Locale.ENGLISH)  // Localized name
    val displayCountry = locale.getDisplayCountry(Locale.ENGLISH)    // Localized name
    return displayLanguage to displayCountry
}