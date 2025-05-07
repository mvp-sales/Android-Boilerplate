package com.mvpsales.github.repository

import com.mvpsales.github.api.response.ApiResult
import com.mvpsales.github.api.response.GetHeadlinesSourcesNewsApiResponse
import com.mvpsales.github.api.response.GetNewsApiResponse
import com.mvpsales.github.db.ArticleNewsEntity
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getEverything(searchTerm: String, page: Int): Flow<ApiResult<GetNewsApiResponse>>
    suspend fun getTopHeadlines(searchTerm: String, page: Int): Flow<ApiResult<GetNewsApiResponse>>
    suspend fun getHeadlinesSources(): Flow<ApiResult<GetHeadlinesSourcesNewsApiResponse>>
    suspend fun getSavedArticles(): Flow<List<ArticleNewsEntity>>
    suspend fun saveArticle(article: ArticleNewsEntity)
    suspend fun deleteArticle(articleUrl: String)
    suspend fun getArticle(articleUrl: String): Flow<ArticleNewsEntity?>
}