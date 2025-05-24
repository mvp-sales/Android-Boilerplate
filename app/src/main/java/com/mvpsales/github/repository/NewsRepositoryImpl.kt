package com.mvpsales.github.repository

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.mapEither
import com.mvpsales.github.api.NewsApi
import com.mvpsales.github.api.NewsKtorApi
import com.mvpsales.github.api.request.GetHeadlinesSourcesRequest
import com.mvpsales.github.api.request.GetNewsRequest
import com.mvpsales.github.api.response.GenericErrorApiResponse
import com.mvpsales.github.api.response.toEntity
import com.mvpsales.github.db.ArticlesDao
import com.mvpsales.github.db.toDb
import com.mvpsales.github.db.toEntity
import com.mvpsales.github.entities.ArticleNews
import com.mvpsales.github.entities.GenericError
import com.mvpsales.github.entities.SearchNewsQuery
import com.mvpsales.github.entities.SearchSourcesQuery
import com.mvpsales.github.entities.SearchType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val articlesDao: ArticlesDao,
    private val newsApi: NewsApi,
    private val newsKtorApi: NewsKtorApi
) : NewsRepository {

    //private var allNews: List<ArticleNewsApiResponse> = emptyList()
    //private var topHeadlines: List<ArticleNewsApiResponse> = emptyList()

    override suspend fun getEverything(searchTerm: String, page: Int) = flow {
        val response = newsApi.getEverything(searchTerm, page, "")
        when {
            response.isSuccessful ->
                emit(Ok(response.body()!!.articles.map { it.toEntity() }))
            else -> {
                val errMsg = response.errorBody()?.string()?.let {
                    val errMessage: GenericErrorApiResponse = Json.decodeFromString(it)
                    return@let errMessage
                } ?: GenericErrorApiResponse(status = "Err", code = "500", message = "Internal error")

                emit(Err(errMsg))
            }
        }
    }

    override suspend fun getEverything(searchNewsQuery: SearchNewsQuery) =
        newsKtorApi.getNews(
            GetNewsRequest(
                searchType = SearchType.EVERYTHING,
                query = searchNewsQuery.searchTerm,
                sources = searchNewsQuery.sources,
                page = searchNewsQuery.page
            )
        ).map { result ->
            result.mapEither(
                success = { it.articles.map { it.toEntity() } },
                failure = { GenericError(it.message) }
            )
        }

    override suspend fun getTopHeadlines(searchTerm: String, page: Int) = flow {
        val response = newsApi.getTopHeadlines(searchTerm, page, "")
        if (response.isSuccessful) {
            emit(Ok(response.body()!!.articles.map { it.toEntity() }))
        } else {
            val errMsg = response.errorBody()?.string()?.let {
                val errMessage: GenericErrorApiResponse = Json.decodeFromString(it)
                return@let errMessage
            } ?: GenericErrorApiResponse(status = "Err", code = "500", message = "Internal error")

            emit(Err(errMsg))
        }
    }

    override suspend fun getTopHeadlines(searchNewsQuery: SearchNewsQuery) =
        newsKtorApi.getNews(
            GetNewsRequest(
                searchType = SearchType.HEADLINES,
                query = searchNewsQuery.searchTerm,
                sources = searchNewsQuery.sources,
                page = searchNewsQuery.page
            )
        ).map { result ->
            result.mapEither(
                success = { it.articles.map { it.toEntity() } },
                failure = { GenericError(it.message) }
            )
        }

    override suspend fun getHeadlinesSources() = flow {
        val response = newsApi.getHeadlinesSources()
        if (response.isSuccessful) {
            emit(Ok(response.body()!!))
        } else {
            val errMsg = response.errorBody()?.string()?.let {
                val errMessage: GenericErrorApiResponse = Json.decodeFromString(it)
                return@let errMessage
            } ?: GenericErrorApiResponse(status = "Err", code = "500", message = "Internal error")

            emit(Err(errMsg))
        }
    }

    override suspend fun getHeadlinesSources(query: SearchSourcesQuery) =
        newsKtorApi.getHeadlinesSources(
            GetHeadlinesSourcesRequest(category = query.category)
        ).map { result ->
            result.mapEither(
                success = {
                    it.sources.map { it.toEntity() }
                },
                failure = {
                    GenericError(it.message)
                }
            )
        }

    override suspend fun getSavedArticles(): Flow<List<ArticleNews>> = flow {
        emit(articlesDao.getAll().map { it.toEntity() })
    }

    override suspend fun saveArticle(article: ArticleNews) = articlesDao.insertAll(article.toDb())

    override suspend fun deleteArticle(articleUrl: String) = articlesDao.delete(articleUrl)

    override suspend fun getArticle(articleUrl: String): Flow<ArticleNews?> = flow {
        emit(articlesDao.getArticle(articleUrl)?.toEntity())
    }
}