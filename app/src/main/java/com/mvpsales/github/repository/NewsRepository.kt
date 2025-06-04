package com.mvpsales.github.repository

import com.github.michaelbull.result.Result
import com.mvpsales.github.api.response.GenericErrorApiResponse
import com.mvpsales.github.api.response.GetHeadlinesSourcesNewsApiResponse
import com.mvpsales.github.api.response.GetNewsApiResponse
import com.mvpsales.github.entities.ArticleNews
import com.mvpsales.github.entities.GenericError
import com.mvpsales.github.entities.NewsPage
import com.mvpsales.github.entities.NewsSource
import com.mvpsales.github.entities.SearchNewsQuery
import com.mvpsales.github.entities.SearchSourcesQuery
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getEverything(searchNewsQuery: SearchNewsQuery): Flow<Result<NewsPage, GenericError>>
    suspend fun getTopHeadlines(searchNewsQuery: SearchNewsQuery): Flow<Result<NewsPage, GenericError>>
    suspend fun getHeadlinesSources(query: SearchSourcesQuery): Flow<Result<List<NewsSource>, GenericError>>
    suspend fun getSavedArticles(): Flow<List<ArticleNews>>
    suspend fun saveArticle(article: ArticleNews)
    suspend fun deleteArticle(articleUrl: String)
    suspend fun getArticle(articleUrl: String): Flow<ArticleNews?>
}