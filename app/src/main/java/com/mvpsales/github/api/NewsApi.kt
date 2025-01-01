package com.mvpsales.github.api

import com.mvpsales.github.api.response.ApiResult
import com.mvpsales.github.api.response.GetHeadlinesSourcesNewsApiResponse
import com.mvpsales.github.api.response.GetNewsApiResponse
import kotlinx.coroutines.flow.Flow

interface NewsApi {

    fun getEverything(page: Int): Flow<ApiResult<GetNewsApiResponse>>
    fun getTopHeadlines(page: Int): Flow<ApiResult<GetNewsApiResponse>>
    fun getHeadlinesSources(): Flow<ApiResult<GetHeadlinesSourcesNewsApiResponse>>
}