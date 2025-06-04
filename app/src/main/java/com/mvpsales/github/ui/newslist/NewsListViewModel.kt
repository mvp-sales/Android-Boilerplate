package com.mvpsales.github.ui.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvpsales.github.entities.ArticleNews
import com.mvpsales.github.entities.GenericError
import com.mvpsales.github.entities.SearchNewsQuery
import com.mvpsales.github.repository.NewsRepository
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
    private val newsRepository: NewsRepository,
    private val dispatcherHelper: DispatcherHelper
) : ViewModel() {

    private val _uiState: MutableStateFlow<NewsListUiState> = MutableStateFlow(NewsListUiState.Initial)
    val uiState: StateFlow<NewsListUiState> = _uiState.asStateFlow()

    fun fetchNews(newsType: NewsListType) {
        val currentState = _uiState.value
        if (currentState is NewsListUiState.Loading || currentState is NewsListUiState.Loaded && currentState.isLoadingMore) {
            return
        }

        _uiState.update { currentState ->
            if (currentState is NewsListUiState.Loaded &&
                currentState.newsType == newsType && currentState.lastLoadedPage > 0) {

                currentState.copy(isLoadingMore = true)
            } else {
                NewsListUiState.Loading
            }
        }

        val query = SearchNewsQuery(
            searchTerm = searchTerm,
            sources = if (sourceId.isEmpty()) emptyList() else listOf(sourceId),
            page = (_uiState.value as? NewsListUiState.Loaded)?.lastLoadedPage ?: 1
        )

        viewModelScope.launch(dispatcherHelper.ioDispatcher()) {
            val resultFlow = when (newsType) {
                NewsListType.ALL_NEWS -> newsRepository.getEverything(query)
                NewsListType.HEADLINES -> newsRepository.getTopHeadlines(query)
            }

            resultFlow.collectLatest { result ->
                _uiState.update { currentState ->
                    when {
                        result.isOk -> {
                            if (currentState is NewsListUiState.Loaded) {
                                currentState.copy(
                                    data = currentState.data + result.value.articles,
                                    lastLoadedPage = result.value.page,
                                    isLoadingMore = false,
                                    totalResultsCount = result.value.totalResults
                                )
                            } else {
                                NewsListUiState.Loaded(
                                    data = result.value.articles,
                                    lastLoadedPage = 1,
                                    totalResultsCount = result.value.totalResults,
                                    newsType = newsType
                                )
                            }
                        }
                        else -> NewsListUiState.Error(result.error)
                    }
                }
            }
        }
    }

    sealed class NewsListUiState {
        data object Initial : NewsListUiState()
        data object Loading : NewsListUiState()
        data class Loaded(
            val data: List<ArticleNews>,
            val lastLoadedPage: Int = 0,
            val isLoadingMore: Boolean = false,
            val totalResultsCount: Int = 0,
            val newsType: NewsListType = NewsListType.ALL_NEWS
        ): NewsListUiState() {
            val fetchedAllResults: Boolean
                get() = data.size == totalResultsCount
        }
        data class Error(val error: GenericError): NewsListUiState()
    }
}