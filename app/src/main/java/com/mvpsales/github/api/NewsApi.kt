package com.mvpsales.github.api

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.mvpsales.github.api.request.GetHeadlinesSourcesRequest
import com.mvpsales.github.api.request.GetNewsRequest
import com.mvpsales.github.api.request.endpoint
import com.mvpsales.github.api.response.GenericErrorApiResponse
import com.mvpsales.github.api.response.GetHeadlinesSourcesNewsApiResponse
import com.mvpsales.github.api.response.GetNewsApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface NewsApi {
    fun getNews(request: GetNewsRequest): Flow<Result<GetNewsApiResponse, GenericErrorApiResponse>>
    fun getHeadlinesSources(request: GetHeadlinesSourcesRequest): Flow<Result<GetHeadlinesSourcesNewsApiResponse, GenericErrorApiResponse>>
}

class NewsApiImpl(
    private val httpClient: HttpClient
): NewsApi {
    override fun getNews(request: GetNewsRequest): Flow<Result<GetNewsApiResponse, GenericErrorApiResponse>> = flow {
        try {
            val url = "/v2/${request.searchType.endpoint()}?q=${request.query}&pageSize=${request.pageSize}&page=${request.page}&sources=${request.sources.joinToString(",")}"
            emit(
                Ok(
                    httpClient.get(url).body()
                )
            )
        } catch (e: ClientRequestException) {
            emit(Err(e.response.body()))
        } catch (e: ServerResponseException) {
            emit(Err(e.response.body()))
        } catch (e: Exception) {
            emit(
                Err(
                    GenericErrorApiResponse(
                        status = "error",
                        code = "genericError",
                        message = e.message ?: "Something went wrong"
                    )
                )
            )
        }
    }

    override fun getHeadlinesSources(request: GetHeadlinesSourcesRequest): Flow<Result<GetHeadlinesSourcesNewsApiResponse, GenericErrorApiResponse>> = flow {
        try {
            emit(
                Ok(
                    httpClient.get("/v2/top-headlines/sources?category=${request.category}").body()
                )
            )
        } catch (e: ClientRequestException) {
            emit(Err(e.response.body()))
        } catch (e: ServerResponseException) {
            emit(Err(e.response.body()))
        } catch (e: Exception) {
            emit(
                Err(
                    GenericErrorApiResponse(
                        status = "error",
                        code = "genericError",
                        message = e.message ?: "Something went wrong"
                    )
                )
            )
        }
    }
}