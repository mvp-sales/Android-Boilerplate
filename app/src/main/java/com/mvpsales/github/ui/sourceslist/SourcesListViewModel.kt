package com.mvpsales.github.ui.sourceslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvpsales.github.domain.GenericError
import com.mvpsales.github.domain.NewsSource
import com.mvpsales.github.domain.SearchSourcesQuery
import com.mvpsales.github.repository.NewsRepository
import com.mvpsales.github.repository.SourcesRepository
import com.mvpsales.github.utils.DispatcherHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SourcesListViewModel(
    private val sourcesRepository: SourcesRepository,
    private val dispatcherHelper: DispatcherHelper
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun getSources() {
        viewModelScope.launch(dispatcherHelper.ioDispatcher()) {
            _uiState.update { UiState.Loading }
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

    fun addFavouriteSource(source: NewsSource) {
        viewModelScope.launch(dispatcherHelper.ioDispatcher()) {
            val currentSources = (_uiState.value as UiState.Loaded).sources
            _uiState.update { UiState.Loading }
            sourcesRepository.addFavouriteSource(source)
            _uiState.update {
                val updatedSources = currentSources.map { sourceIt ->
                    if (sourceIt.id == source.id) source.copy(favourite = true) else sourceIt
                }
                UiState.Loaded(updatedSources)
            }
        }
    }

    fun removeFavouriteSource(source: NewsSource) {
        viewModelScope.launch(dispatcherHelper.ioDispatcher()) {
            val currentSources = (_uiState.value as UiState.Loaded).sources
            _uiState.update { UiState.Loading }
            sourcesRepository.removeSourceFromFavourite(source)
            _uiState.update {
                val updatedSources = currentSources.map { sourceIt ->
                    if (sourceIt.id == source.id) source.copy(favourite = false) else sourceIt
                }
                UiState.Loaded(updatedSources)
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