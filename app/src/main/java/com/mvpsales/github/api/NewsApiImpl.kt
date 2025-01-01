package com.mvpsales.github.api

import com.mvpsales.github.api.response.ApiResult
import com.mvpsales.github.api.response.ArticleNewsApiResponse
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
import javax.inject.Inject

class NewsApiImpl @Inject constructor(
    private val httpClient: HttpClient
): NewsApi {

    override fun getEverything(page: Int): Flow<ApiResult<GetNewsApiResponse>> = flow {
        emit(ApiResult.Loading())
        try {
            emit(
                ApiResult.Success(
                    httpClient.get("/v2/everything").body()
                )
            )
        } catch (e: ClientRequestException) {
            emit(ApiResult.Error(e.response.body()))
        } catch (e: ClientRequestException) {
            emit(ApiResult.Error(e.response.body()))
        } catch (e: Exception) {
            emit(
                ApiResult.Error(
                    GenericErrorApiResponse(
                        status = "error",
                        code = "genericError",
                        message = e.message ?: "Something went wrong"
                    )
                )
            )
        }
    }

    override fun getTopHeadlines(page: Int): Flow<ApiResult<GetNewsApiResponse>> = flow {
        emit(ApiResult.Loading())
        try {
            emit(
                ApiResult.Success(
                    httpClient.get("/v2/top-headlines").body()
                )
            )
        } catch (e: ClientRequestException) {
            emit(ApiResult.Error(e.response.body()))
        } catch (e: ServerResponseException) {
            emit(ApiResult.Error(e.response.body()))
        } catch (e: Exception) {
            emit(
                ApiResult.Error(
                    GenericErrorApiResponse(
                        status = "error",
                        code = "genericError",
                        message = e.message ?: "Something went wrong"
                    )
                )
            )
        }
    }

    override fun getHeadlinesSources(): Flow<ApiResult<GetHeadlinesSourcesNewsApiResponse>> = flow {
        emit(ApiResult.Loading())
        try {
            emit(
                ApiResult.Success(
                    httpClient.get("/v2/top-headlines/sources").body()
                )
            )
        } catch (e: ClientRequestException) {
            emit(ApiResult.Error(e.response.body()))
        } catch (e: ServerResponseException) {
            emit(ApiResult.Error(e.response.body()))
        } catch (e: Exception) {
            emit(
                ApiResult.Error(
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