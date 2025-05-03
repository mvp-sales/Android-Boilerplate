package com.mvpsales.github.api

import com.mvpsales.github.api.response.ApiResult
import com.mvpsales.github.api.response.GetHeadlinesSourcesNewsApiResponse
import com.mvpsales.github.api.response.GetNewsApiResponse
import kotlinx.coroutines.flow.Flow

interface NewsApi {

    fun getEverything(searchTerm: String, page: Int): Flow<ApiResult<GetNewsApiResponse>>
    fun getTopHeadlines(searchTerm: String, page: Int): Flow<ApiResult<GetNewsApiResponse>>
    fun getHeadlinesSources(): Flow<ApiResult<GetHeadlinesSourcesNewsApiResponse>>
}