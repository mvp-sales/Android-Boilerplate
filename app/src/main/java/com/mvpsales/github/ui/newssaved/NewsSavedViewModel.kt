package com.mvpsales.github.ui.newssaved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvpsales.github.entities.ArticleNews
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

    private val _uiState: MutableStateFlow<NewsSavedUiState> = MutableStateFlow(NewsSavedUiState.Initial)
    val uiState: StateFlow<NewsSavedUiState> = _uiState.asStateFlow()

    fun getSavedNews() {
        _uiState.update { NewsSavedUiState.Loading }
        viewModelScope.launch(dispatcherHelper.ioDispatcher()) {
            newsRepository.getSavedArticles()
                .collectLatest { result ->
                    _uiState.update { NewsSavedUiState.Loaded(result) }
                }
        }
    }

    fun resetState() {
        if (_uiState.value != NewsSavedUiState.Initial) {
            _uiState.update { NewsSavedUiState.Initial }
        }
    }

    sealed class NewsSavedUiState {
        data object Initial : NewsSavedUiState()
        data object Loading : NewsSavedUiState()
        data class Loaded(val data: List<ArticleNews>): NewsSavedUiState()
    }
}