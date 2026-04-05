package com.mvpsales.github.repository

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapEither
import com.mvpsales.github.api.NewsApi
import com.mvpsales.github.api.request.GetHeadlinesSourcesRequest
import com.mvpsales.github.api.response.toEntity
import com.mvpsales.github.db.NewsSourcesDao
import com.mvpsales.github.db.toDb
import com.mvpsales.github.db.toDomain
import com.mvpsales.github.domain.GenericError
import com.mvpsales.github.domain.NewsSource
import com.mvpsales.github.domain.SearchSourcesQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SourcesRepository {
    suspend fun getHeadlinesSources(query: SearchSourcesQuery): Flow<Result<List<NewsSource>, GenericError>>
    suspend fun getFavouriteSources(): List<NewsSource>
    suspend fun addFavouriteSource(source: NewsSource)
    suspend fun removeSourceFromFavourite(source: NewsSource)
}

class SourcesRepositoryImpl(
    private val newsSourcesDao: NewsSourcesDao,
    private val api: NewsApi
): SourcesRepository {

    override suspend fun getHeadlinesSources(query: SearchSourcesQuery): Flow<Result<List<NewsSource>, GenericError>> =
        api.getHeadlinesSources(
            GetHeadlinesSourcesRequest(category = query.category)
        ).map { result ->
            result.mapEither(
                success = {
                    val favouriteSources = newsSourcesDao.getAll().map { it.toDomain() }
                    it.sources.map { source ->
                        favouriteSources.find { source.id == it.id } ?: source.toEntity()
                    }
                },
                failure = {
                    GenericError(it.message)
                }
            )
        }

    override suspend fun getFavouriteSources(): List<NewsSource> =
        newsSourcesDao.getAll().map { it.toDomain() }

    override suspend fun addFavouriteSource(source: NewsSource) =
        newsSourcesDao.insertAll(source.toDb())

    override suspend fun removeSourceFromFavourite(source: NewsSource) =
        newsSourcesDao.delete(sourceId = source.id)
}