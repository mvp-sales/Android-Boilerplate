package com.mvpsales.github.api

import com.mvpsales.github.api.response.GetHeadlinesSourcesNewsApiResponse
import com.mvpsales.github.api.response.GetNewsApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/everything?pageSize=20")
    suspend fun getEverything(@Query("q") searchTerm: String, @Query("page") page: Int): Response<GetNewsApiResponse>
    @GET("/v2/top-headlines?pageSize=20")
    suspend fun getTopHeadlines(@Query("q") searchTerm: String, @Query("page") page: Int): Response<GetNewsApiResponse>
    @GET("/v2/top-headlines/sources")
    suspend fun getHeadlinesSources(): Response<GetHeadlinesSourcesNewsApiResponse>
}