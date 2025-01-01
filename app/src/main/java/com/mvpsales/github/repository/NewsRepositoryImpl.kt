package com.mvpsales.github.repository

import com.mvpsales.github.api.NewsApi
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi
) : NewsRepository {

    override suspend fun getEverything(page: Int) = newsApi.getEverything(page)

    override suspend fun getTopHeadlines(page: Int) = newsApi.getTopHeadlines(page)

    override suspend fun getHeadlinesSources() = newsApi.getHeadlinesSources()
}