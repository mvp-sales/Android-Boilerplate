package com.mvpsales.github.api

import com.mvpsales.github.api.response.ApiResult
import com.mvpsales.github.api.response.ArticleNewsApiResponse
import com.mvpsales.github.api.response.GetHeadlinesSourcesNewsApiResponse
import kotlinx.coroutines.flow.Flow

interface NewsApi {

    fun getEverything(page: Int): Flow<ApiResult<ArticleNewsApiResponse>>
    fun getTopHeadlines(page: Int): Flow<ApiResult<ArticleNewsApiResponse>>
    fun getHeadlinesSources(): Flow<ApiResult<GetHeadlinesSourcesNewsApiResponse>>
}