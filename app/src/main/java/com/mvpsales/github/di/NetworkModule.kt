package com.mvpsales.github.di

import com.mvpsales.github.BuildConfig
import com.mvpsales.github.api.NewsApi
import com.mvpsales.github.api.NewsKtorApi
import com.mvpsales.github.api.NewsKtorApiImpl
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
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

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
    single { provideHttpClient() }
    single { provideRetrofit(get()) }
    factory<NewsApi> { provideNewsApi(get()) }
    factory<NewsKtorApi> { NewsKtorApiImpl(get()) }
}

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
): Retrofit {
    val network = Json { ignoreUnknownKeys = true }
    return Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(network.asConverterFactory("application/json".toMediaType()))
        .build()
}

private fun provideHttpClient(): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Api-Key", BuildConfig.API_KEY)
                .build()
            chain.proceed(request)
        }
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

private fun provideNewsApi(retrofit: Retrofit): NewsApi =
    retrofit.create(NewsApi::class.java)