package com.mvpsales.github.ui.newsdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvpsales.github.entities.ArticleNews
import com.mvpsales.github.repository.NewsRepository
import com.mvpsales.github.utils.DispatcherHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.String

class NewsDetailViewModel(
    private val newsRepository: NewsRepository,
    private val dispatcherHelper: DispatcherHelper
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun saveArticle(article: ArticleNews) {
        _uiState.update { UiState.Loading }
        viewModelScope.launch(dispatcherHelper.ioDispatcher()) {
            newsRepository.saveArticle(article)
            _uiState.update { UiState.Loaded(true) }
        }
    }

    fun deleteArticle(articleUrl: String) {
        _uiState.update { UiState.Loading }
        viewModelScope.launch(dispatcherHelper.ioDispatcher()) {
            newsRepository.deleteArticle(articleUrl)
            _uiState.update { UiState.Loaded(false) }
        }
    }

    fun getArticle(articleUrl: String) {
        _uiState.update { UiState.Loading }
        viewModelScope.launch(dispatcherHelper.ioDispatcher()) {
            newsRepository.getArticle(articleUrl).collect { savedArticle ->
                _uiState.update { UiState.Loaded(isArticleSaved = savedArticle != null) }
            }
        }
    }

    sealed class UiState {
        data object Initial: UiState()
        data object Loading: UiState()
        data class Loaded(val isArticleSaved: Boolean): UiState()
    }
}