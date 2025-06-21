package com.mvpsales.github.repository

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapEither
import com.mvpsales.github.api.NewsKtorApi
import com.mvpsales.github.api.request.GetHeadlinesSourcesRequest
import com.mvpsales.github.api.request.GetNewsRequest
import com.mvpsales.github.api.response.toEntity
import com.mvpsales.github.db.ArticlesDao
import com.mvpsales.github.db.toDb
import com.mvpsales.github.db.toEntity
import com.mvpsales.github.domain.ArticleNews
import com.mvpsales.github.domain.GenericError
import com.mvpsales.github.domain.NewsPage
import com.mvpsales.github.domain.NewsSource
import com.mvpsales.github.domain.SearchNewsQuery
import com.mvpsales.github.domain.SearchSourcesQuery
import com.mvpsales.github.domain.SearchType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

interface NewsRepository {

    suspend fun getNews(searchNewsQuery: SearchNewsQuery): Flow<Result<NewsPage, GenericError>>
    suspend fun getSavedArticles(): Flow<List<ArticleNews>>
    suspend fun saveArticle(article: ArticleNews)
    suspend fun deleteArticle(articleUrl: String)
    suspend fun getArticle(articleUrl: String): Flow<ArticleNews?>
}

class NewsRepositoryImpl(
    private val articlesDao: ArticlesDao,
    private val newsKtorApi: NewsKtorApi
) : NewsRepository {

    override suspend fun getNews(searchNewsQuery: SearchNewsQuery) =
        newsKtorApi.getNews(
            GetNewsRequest(
                searchType = searchNewsQuery.searchType,
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

    override suspend fun getSavedArticles(): Flow<List<ArticleNews>> = flow {
        emit(articlesDao.getAll().map { it.toEntity() })
    }

    override suspend fun saveArticle(article: ArticleNews) = articlesDao.insertAll(article.toDb())

    override suspend fun deleteArticle(articleUrl: String) = articlesDao.delete(articleUrl)

    override suspend fun getArticle(articleUrl: String): Flow<ArticleNews?> = flow {
        emit(articlesDao.getArticle(articleUrl)?.toEntity())
    }
}