package com.mvpsales.github.ui.newssaved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvpsales.github.domain.ArticleNews
import com.mvpsales.github.repository.NewsRepository
import com.mvpsales.github.utils.DispatcherHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewsSavedViewModel(
    private val newsRepository: NewsRepository,
    private val dispatcherHelper: DispatcherHelper
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun getSavedNews() {
        _uiState.update { UiState.Loading }
        viewModelScope.launch(dispatcherHelper.ioDispatcher()) {
            newsRepository.getSavedArticles()
                .collectLatest { result ->
                    _uiState.update { UiState.Loaded(result) }
                }
        }
    }

    fun resetState() {
        if (_uiState.value != UiState.Initial) {
            _uiState.update { UiState.Initial }
        }
    }

    sealed class UiState {
        data object Initial : UiState()
        data object Loading : UiState()
        data class Loaded(val data: List<ArticleNews>): UiState()
    }
}