package com.mvpsales.github.repository

import com.mvpsales.github.api.NewsApi
import com.mvpsales.github.api.response.ApiResult
import com.mvpsales.github.api.response.ArticleNewsApiResponse
import com.mvpsales.github.api.response.GetNewsApiResponse
import com.mvpsales.github.api.response.toEntity
import com.mvpsales.github.db.ArticleNewsEntity
import com.mvpsales.github.db.ArticlesDao
import com.mvpsales.github.db.toDb
import com.mvpsales.github.db.toEntity
import com.mvpsales.github.entities.ArticleNews
import com.mvpsales.github.ui.newslist.NewsListViewModel.NewsListUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val articlesDao: ArticlesDao,
    private val newsApi: NewsApi
) : NewsRepository {

    //private var allNews: List<ArticleNewsApiResponse> = emptyList()
    //private var topHeadlines: List<ArticleNewsApiResponse> = emptyList()

    override suspend fun getEverything(searchTerm: String, page: Int): Flow<ApiResult<List<ArticleNews>>> =
        newsApi.getEverything(searchTerm, page).map { result ->
            when (result) {
                is ApiResult.Success -> {
                    ApiResult.Success(
                        result.data.articles.map { it.toEntity() }
                    )
                }
                is ApiResult.Error -> ApiResult.Error(result.error)
                is ApiResult.Loading -> ApiResult.Loading()
            }
        }

    override suspend fun getTopHeadlines(searchTerm: String, page: Int): Flow<ApiResult<List<ArticleNews>>> =
        newsApi.getTopHeadlines(searchTerm, page).map { result ->
            when (result) {
                is ApiResult.Success -> {
                    ApiResult.Success(
                        result.data.articles.map { it.toEntity() }
                    )
                }
                is ApiResult.Error -> ApiResult.Error(result.error)
                is ApiResult.Loading -> ApiResult.Loading()
            }
        }

    override suspend fun getHeadlinesSources() = newsApi.getHeadlinesSources()

    override suspend fun getSavedArticles(): Flow<List<ArticleNews>> = flow {
        emit(articlesDao.getAll().map { it.toEntity() })
    }

    override suspend fun saveArticle(article: ArticleNews) = articlesDao.insertAll(article.toDb())

    override suspend fun deleteArticle(articleUrl: String) = articlesDao.delete(articleUrl)

    override suspend fun getArticle(articleUrl: String): Flow<ArticleNews?> = flow {
        emit(articlesDao.getArticle(articleUrl)?.toEntity())
    }
}