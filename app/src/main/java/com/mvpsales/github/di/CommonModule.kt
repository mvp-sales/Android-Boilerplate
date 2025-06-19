package com.mvpsales.github.di

import com.mvpsales.github.repository.NewsRepository
import com.mvpsales.github.repository.NewsRepositoryImpl
import com.mvpsales.github.repository.SourcesRepository
import com.mvpsales.github.repository.SourcesRepositoryImpl
import org.koin.dsl.module

val commonModule = module {
    factory<NewsRepository> { NewsRepositoryImpl(get(), get()) }
    factory<SourcesRepository> { SourcesRepositoryImpl(get(), get()) }
}