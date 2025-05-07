package com.mvpsales.github.ui.newsdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvpsales.github.api.response.ArticleNewsApiResponse
import com.mvpsales.github.db.ArticleNewsEntity
import com.mvpsales.github.db.ArticleSourceNews
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

    fun saveArticle(article: ArticleNewsApiResponse) {
        _uiState.update { UiState.Loading }
        viewModelScope.launch(dispatcherHelper.ioDispatcher()) {
            newsRepository.saveArticle(article.toEntity())
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

private fun ArticleNewsApiResponse.toEntity() = ArticleNewsEntity(
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content,
    source = ArticleSourceNews(
        source.id,
        source.name
    )
)