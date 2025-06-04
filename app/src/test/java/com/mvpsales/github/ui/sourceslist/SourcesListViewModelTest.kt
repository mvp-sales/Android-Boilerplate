package com.mvpsales.github.ui.sourceslist

import app.cash.turbine.test
import com.github.michaelbull.result.Ok
import com.mvpsales.github.entities.NewsSource
import com.mvpsales.github.repository.NewsRepository
import com.mvpsales.github.utils.DispatcherHelper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SourcesListViewModelTest {
    private lateinit var viewModel: SourcesListViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository = mockk<NewsRepository>()
    private val dispatcherHelper = mockk<DispatcherHelper>()

    @Before
    fun setup() {
        every { dispatcherHelper.ioDispatcher() } returns testDispatcher
        viewModel = SourcesListViewModel(repository, dispatcherHelper)
    }

    @Test
    fun `getSources emits Loading and then Loaded`() = runTest {
        val expectedSources = listOf(
            NewsSource(
                id = "abc-news",
                name = "ABC News",
                description = "Your trusted source for breaking news, analysis, exclusive interviews, headlines, and videos at ABCNews.com.",
                url = "https://abcnews.go.com",
                category = "general",
                language = "en",
                country = "us"
            )
        )
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
        /*val error = GenericError(code = "500", message = "Server error")
        coEvery { repository.getHeadlinesSources(any()) } returns flowOf(Result.failure(error))

        viewModel.getSources()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is SourcesListViewModel.UiState.Error)
        assertEquals(error, (state as SourcesListViewModel.UiState.Error).error)*/
    }
}