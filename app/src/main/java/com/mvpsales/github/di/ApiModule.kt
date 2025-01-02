package com.mvpsales.github.di

import com.mvpsales.github.api.NewsApi
import com.mvpsales.github.api.NewsApiImpl
import com.mvpsales.github.repository.NewsRepository
import com.mvpsales.github.repository.NewsRepositoryImpl
import com.mvpsales.github.utils.Constants
import com.mvpsales.github.utils.DispatcherHelper
import com.mvpsales.github.utils.DispatcherHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
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
        expectSuccess = true
        install(Logging) {
            level = LogLevel.ALL
        }
        install(DefaultRequest) {
            url(Constants.BASE_URL)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header("X-Api-Key", "13f3afa3f3d145aaa527a46166bd4246")
        }
        install(ContentNegotiation) {
            json()
        }
    }

    @Provides
    fun provideNewsApi(httpClient: HttpClient): NewsApi = NewsApiImpl(httpClient)

    @Provides
    fun provideRepository(api: NewsApi): NewsRepository = NewsRepositoryImpl(api)

    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    fun provideDispatcherHelper(): DispatcherHelper = DispatcherHelperImpl()
}