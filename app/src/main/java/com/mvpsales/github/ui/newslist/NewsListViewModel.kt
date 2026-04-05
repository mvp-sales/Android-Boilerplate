package com.mvpsales.github.ui.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.annotation.UnsafeResultErrorAccess
import com.github.michaelbull.result.annotation.UnsafeResultValueAccess
import com.mvpsales.github.domain.ArticleNews
import com.mvpsales.github.domain.GenericError
import com.mvpsales.github.domain.SearchNewsQuery
import com.mvpsales.github.domain.SearchType
import com.mvpsales.github.repository.NewsRepository
import com.mvpsales.github.repository.SourcesRepository
import com.mvpsales.github.utils.DispatcherHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewsListViewModel(
    private val searchTerm: String,
    private val sourceId: String,
    private val searchOnlyFromFavouriteSources: Boolean,
    private val newsRepository: NewsRepository,
    private val sourcesRepository: SourcesRepository,
    private val dispatcherHelper: DispatcherHelper
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val title: String
        get() = searchTerm.ifEmpty {
            sourceId
        }

    @OptIn(UnsafeResultValueAccess::class, UnsafeResultErrorAccess::class)
    fun fetchNews(newsType: NewsListType) {
        val currentState = _uiState.value
        if (currentState is UiState.Loading || currentState is UiState.Loaded && currentState.isLoadingMore) {
            return
        }

        _uiState.update { currentState ->
            if (currentState is UiState.Loaded &&
                currentState.newsType == newsType && currentState.lastLoadedPage > 0) {

                currentState.copy(isLoadingMore = true)
            } else {
                UiState.Loading
            }
        }

        viewModelScope.launch(dispatcherHelper.ioDispatcher()) {
            val sources = when {
                sourceId.isNotEmpty() -> listOf(sourceId)
                searchOnlyFromFavouriteSources -> {
                    val favouritesSources = sourcesRepository.getFavouriteSources()
                    favouritesSources.map { it.id }
                }
                else -> emptyList<String>()
            }

            val lastLoadedPage = (_uiState.value as? UiState.Loaded)?.lastLoadedPage ?: 0
            val query = SearchNewsQuery(
                searchType = when (newsType) {
                    NewsListType.ALL_NEWS -> SearchType.EVERYTHING
                    NewsListType.HEADLINES -> SearchType.HEADLINES
                },
                searchTerm = searchTerm,
                sources = sources,
                page = lastLoadedPage + 1
            )

            newsRepository.getNews(query).collectLatest { result ->
                _uiState.update { currentState ->
                    when {
                        result.isOk -> {
                            if (currentState is UiState.Loaded) {
                                currentState.copy(
                                    data = currentState.data + result.value.articles,
                                    lastLoadedPage = result.value.page,
                                    isLoadingMore = false,
                                    totalResultsCount = result.value.totalResults
                                )
                            } else {
                                UiState.Loaded(
                                    data = result.value.articles,
                                    lastLoadedPage = 1,
                                    totalResultsCount = result.value.totalResults,
                                    newsType = newsType
                                )
                            }
                        }
                        else -> UiState.Error(result.error)
                    }
                }
            }
        }
    }

    sealed class UiState {
        data object Initial : UiState()
        data object Loading : UiState()
        data class Loaded(
            val data: List<ArticleNews>,
            val lastLoadedPage: Int = 0,
            val isLoadingMore: Boolean = false,
            val totalResultsCount: Int = 0,
            val newsType: NewsListType = NewsListType.ALL_NEWS
        ): UiState() {
            val fetchedAllResults: Boolean
                get() = data.size == totalResultsCount
        }
        data class Error(val error: GenericError): UiState()
    }
}