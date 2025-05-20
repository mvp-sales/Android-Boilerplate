package com.mvpsales.github.repository

import com.github.michaelbull.result.Result
import com.mvpsales.github.api.response.GenericErrorApiResponse
import com.mvpsales.github.api.response.GetHeadlinesSourcesNewsApiResponse
import com.mvpsales.github.api.response.GetNewsApiResponse
import com.mvpsales.github.entities.ArticleNews
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getEverything(searchTerm: String, page: Int): Flow<Result<List<ArticleNews>, GenericErrorApiResponse>>
    suspend fun getTopHeadlines(searchTerm: String, page: Int): Flow<Result<List<ArticleNews>, GenericErrorApiResponse>>
    suspend fun getHeadlinesSources(): Flow<Result<GetHeadlinesSourcesNewsApiResponse, GenericErrorApiResponse>>
    suspend fun getSavedArticles(): Flow<List<ArticleNews>>
    suspend fun saveArticle(article: ArticleNews)
    suspend fun deleteArticle(articleUrl: String)
    suspend fun getArticle(articleUrl: String): Flow<ArticleNews?>
}