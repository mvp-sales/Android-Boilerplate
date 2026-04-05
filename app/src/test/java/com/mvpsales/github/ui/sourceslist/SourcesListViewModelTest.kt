package com.mvpsales.github.ui.sourceslist

import app.cash.turbine.test
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.mvpsales.github.domain.GenericError
import com.mvpsales.github.domain.NewsSource
import com.mvpsales.github.repository.SourcesRepository
import com.mvpsales.github.utils.DispatcherHelper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SourcesListViewModelTest {
    private lateinit var viewModel: SourcesListViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository = mockk<SourcesRepository>()
    private val dispatcherHelper = mockk<DispatcherHelper>()

    @BeforeEach
    fun setup() {
        every { dispatcherHelper.ioDispatcher() } returns testDispatcher
        viewModel = SourcesListViewModel(repository, dispatcherHelper)
    }

    @Test
    fun `getSources emits Loading and then Loaded`() = runTest {
        val expectedSources = listOf(mockk<NewsSource>())
        coEvery { repository.getHeadlinesSources(any()) } returns flowOf(Ok(expectedSources))

        viewModel.uiState.test {
            assertEquals(SourcesListViewModel.UiState.Initial, awaitItem()) // assert initial state
            viewModel.getSources()

            assertEquals(SourcesListViewModel.UiState.Loading, awaitItem())
            val loaded = awaitItem()
            assertTrue(loaded is SourcesListViewModel.UiState.Loaded)
            assertEquals(expectedSources, (loaded as SourcesListViewModel.UiState.Loaded).sources)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getSources emits Error on failure`() = runTest {
        val error = mockk<GenericError>()
        coEvery { repository.getHeadlinesSources(any()) } returns flowOf(Err(error))

        viewModel.uiState.test {
            assertEquals(SourcesListViewModel.UiState.Initial, awaitItem()) // assert initial state

            viewModel.getSources()

            assertEquals(SourcesListViewModel.UiState.Loading, awaitItem())
            val errorItem = awaitItem()
            assertTrue(errorItem is SourcesListViewModel.UiState.Error)
            assertEquals(error, (errorItem as SourcesListViewModel.UiState.Error).error)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
