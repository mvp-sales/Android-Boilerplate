package com.mvpsales.github.di

import com.mvpsales.github.repository.NewsRepository
import com.mvpsales.github.repository.NewsRepositoryImpl
import org.koin.dsl.module

val commonModule = module {
    factory<NewsRepository> { NewsRepositoryImpl(get(), get()) }
}