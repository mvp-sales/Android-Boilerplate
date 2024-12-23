package com.mvpsales.github.di

import com.mvpsales.github.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun providesKtorClient() = HttpClient(Android) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(DefaultRequest) {
            url(Constants.BASE_URL)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            //header("X-Api-Key", BuildConfig.API_KEY)
        }
        install(ContentNegotiation) {
            json()
        }
    }

    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.Default
}