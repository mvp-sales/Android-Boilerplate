package com.mvpsales.github.ui.sourceslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvpsales.github.entities.GenericError
import com.mvpsales.github.entities.NewsSource
import com.mvpsales.github.entities.SearchSourcesQuery
import com.mvpsales.github.repository.NewsRepository
import com.mvpsales.github.utils.DispatcherHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SourcesListViewModel(
    private val sourcesRepository: NewsRepository,
    private val dispatcherHelper: DispatcherHelper
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun getSources() {
        _uiState.update { UiState.Loading }
        viewModelScope.launch(dispatcherHelper.ioDispatcher()) {
            sourcesRepository.getHeadlinesSources(query = SearchSourcesQuery(""))
                .collectLatest { result ->
                    _uiState.update {
                        when {
                            result.isOk -> UiState.Loaded(result.value)
                            else -> UiState.Error(result.error)
                        }
                    }
                }
        }
    }

    sealed class UiState {
        data object Initial: UiState()
        data object Loading: UiState()
        data class Loaded(val sources: List<NewsSource>): UiState()
        data class Error(val error: GenericError): UiState()
    }
}