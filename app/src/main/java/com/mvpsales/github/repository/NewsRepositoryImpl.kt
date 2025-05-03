package com.mvpsales.github.repository

import com.mvpsales.github.api.NewsApi
import com.mvpsales.github.api.response.ApiResult
import com.mvpsales.github.api.response.ArticleNewsApiResponse
import com.mvpsales.github.api.response.GetNewsApiResponse
import com.mvpsales.github.ui.newslist.NewsListViewModel.NewsListUiState
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi
) : NewsRepository {

    //private var allNews: List<ArticleNewsApiResponse> = emptyList()
    //private var topHeadlines: List<ArticleNewsApiResponse> = emptyList()

    override suspend fun getEverything(searchTerm: String, page: Int) = newsApi.getEverything(searchTerm, page)/*.map { result ->
        when (result) {
            is ApiResult.Success ->  {
                allNews += result.data.articles
                ApiResult.Success(
                    GetNewsApiResponse(
                        result.data.status,
                        result.data.totalResults,
                        allNews
                    )
                )
            }
            else -> result
        }
    }*/

    override suspend fun getTopHeadlines(searchTerm: String, page: Int) = newsApi.getTopHeadlines(searchTerm, page)/*.map { result ->
        when (result) {
            is ApiResult.Success -> {
                topHeadlines += result.data.articles
                ApiResult.Success(
                    GetNewsApiResponse(
                        result.data.status,
                        result.data.totalResults,
                        topHeadlines
                    )
                )
            }
            else -> result
        }
    }*/

    override suspend fun getHeadlinesSources() = newsApi.getHeadlinesSources()
}