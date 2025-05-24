package com.mvpsales.github.ui.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvpsales.github.api.response.GenericErrorApiResponse
import com.mvpsales.github.api.response.GetNewsApiResponse
import com.mvpsales.github.entities.ArticleNews
import com.mvpsales.github.entities.GenericError
import com.mvpsales.github.entities.SearchNewsQuery
import com.mvpsales.github.repository.NewsRepository
import com.mvpsales.github.utils.DispatcherHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsListViewModel(
    private val searchTerm: String,
    private val newsRepository: NewsRepository,
    private val dispatcherHelper: DispatcherHelper
) : ViewModel() {

    private val _uiState: MutableStateFlow<NewsListUiState> = MutableStateFlow(NewsListUiState.Initial)
    val uiState: StateFlow<NewsListUiState> = _uiState.asStateFlow()

    fun getEverything(page: Int) {
        _uiState.update { NewsListUiState.Loading }
        viewModelScope.launch(dispatcherHelper.ioDispatcher()) {
            newsRepository.getEverything(
                SearchNewsQuery(
                    searchTerm = searchTerm,
                    sources = emptyList(),
                    page = page
                )
            ).collectLatest { result ->
                _uiState.update {
                    when {
                        result.isOk -> NewsListUiState.Loaded(result.value)
                        else -> NewsListUiState.Error(result.error)
                    }
                }
            }
        }
    }

    fun getHeadlines(page: Int) {
        _uiState.update { NewsListUiState.Loading }
        viewModelScope.launch(dispatcherHelper.ioDispatcher()) {
            newsRepository.getTopHeadlines(
                SearchNewsQuery(
                    searchTerm = searchTerm,
                    sources = emptyList(),
                    page = page
                )
            ).collectLatest { result ->
                _uiState.update {
                    when {
                        result.isOk -> NewsListUiState.Loaded(result.value)
                        else -> NewsListUiState.Error(result.error)
                    }
                }
            }
        }
    }

    sealed class NewsListUiState {
        data object Initial : NewsListUiState()
        data object Loading : NewsListUiState()
        data class Loaded(val data: List<ArticleNews>): NewsListUiState()
        data class Error(val error: GenericError): NewsListUiState()
    }
}