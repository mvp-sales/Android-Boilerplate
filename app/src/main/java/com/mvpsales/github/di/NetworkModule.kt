package com.mvpsales.github.di

import com.mvpsales.github.BuildConfig
import com.mvpsales.github.api.NewsApi
import com.mvpsales.github.api.NewsApiImpl
import com.mvpsales.github.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient(Android) {
            expectSuccess = true
            install(Logging) {
                level = LogLevel.ALL
            }
            install(DefaultRequest) {
                url(Constants.BASE_URL)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header("X-Api-Key", BuildConfig.API_KEY)
            }
            install(ContentNegotiation) {
                json()
            }
        }
    }
    factory<NewsApi> { NewsApiImpl(get()) }
}