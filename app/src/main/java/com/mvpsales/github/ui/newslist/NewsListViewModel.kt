package com.mvpsales.github.ui.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvpsales.github.api.response.ApiResult
import com.mvpsales.github.api.response.GenericErrorApiResponse
import com.mvpsales.github.api.response.GetNewsApiResponse
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

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val dispatcherHelper: DispatcherHelper
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun getEverything() {
        viewModelScope.launch(dispatcherHelper.ioDispatcher()) {
            newsRepository.getEverything(1)
                .collectLatest { result ->
                    _uiState.update {
                        when (result) {
                            is ApiResult.Loading -> UiState.Loading
                            is ApiResult.Success -> UiState.Loaded(result.data)
                            is ApiResult.Error -> UiState.Error(result.error)
                        }
                    }
                }
        }
    }

    sealed class UiState {
        data object Initial : UiState()
        data object Loading : UiState()
        data class Loaded(val data: GetNewsApiResponse): UiState()
        data class Error(val error: GenericErrorApiResponse): UiState()
    }
}