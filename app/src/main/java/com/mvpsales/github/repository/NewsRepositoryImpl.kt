package com.mvpsales.github.repository

import com.github.michaelbull.result.mapEither
import com.mvpsales.github.api.NewsKtorApi
import com.mvpsales.github.api.request.GetHeadlinesSourcesRequest
import com.mvpsales.github.api.request.GetNewsRequest
import com.mvpsales.github.api.response.toEntity
import com.mvpsales.github.db.ArticlesDao
import com.mvpsales.github.db.toDb
import com.mvpsales.github.db.toEntity
import com.mvpsales.github.entities.ArticleNews
import com.mvpsales.github.entities.GenericError
import com.mvpsales.github.entities.NewsPage
import com.mvpsales.github.entities.SearchNewsQuery
import com.mvpsales.github.entities.SearchSourcesQuery
import com.mvpsales.github.entities.SearchType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class NewsRepositoryImpl(
    private val articlesDao: ArticlesDao,
    private val newsKtorApi: NewsKtorApi
) : NewsRepository {

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
                success = {
                    NewsPage(
                        articles = it.articles.map { it.toEntity() },
                        totalResults = it.totalResults,
                        page = searchNewsQuery.page
                    )
                },
                failure = { GenericError(it.message) }
            )
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
                success = {
                    NewsPage(
                        articles = it.articles.map { it.toEntity() },
                        totalResults = it.totalResults,
                        page = searchNewsQuery.page
                    )
                },
                failure = { GenericError(it.message) }
            )
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