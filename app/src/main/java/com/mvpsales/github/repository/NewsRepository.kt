package com.mvpsales.github.repository

import com.mvpsales.github.api.response.ApiResult
import com.mvpsales.github.api.response.GetHeadlinesSourcesNewsApiResponse
import com.mvpsales.github.api.response.GetNewsApiResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getEverything(page: Int): Flow<ApiResult<GetNewsApiResponse>>
    suspend fun getTopHeadlines(page: Int): Flow<ApiResult<GetNewsApiResponse>>
    suspend fun getHeadlinesSources(): Flow<ApiResult<GetHeadlinesSourcesNewsApiResponse>>
}