package com.mvpsales.github.ui.newssaved

import app.cash.turbine.test
import com.mvpsales.github.domain.ArticleNews
import com.mvpsales.github.repository.NewsRepository
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
class NewsSavedViewModelTest {
    private lateinit var viewModel: NewsSavedViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository = mockk<NewsRepository>()
    private val dispatcherHelper = mockk<DispatcherHelper>()

    @BeforeEach
    fun setup() {
        every { dispatcherHelper.ioDispatcher() } returns testDispatcher
        viewModel = NewsSavedViewModel(repository, dispatcherHelper)
    }

    @Test
    fun `getSavedNews emits Loading and Loaded states`() = runTest {
        val expected = listOf(mockk<ArticleNews>())
        coEvery { repository.getSavedArticles() } returns flowOf(expected)

        viewModel.uiState.test {
            assertEquals(NewsSavedViewModel.UiState.Initial, awaitItem()) // assert initial state
            viewModel.getSavedNews()

            assertEquals(NewsSavedViewModel.UiState.Loading, awaitItem())
            val loaded = awaitItem()
            assertTrue(loaded is NewsSavedViewModel.UiState.Loaded)
            assertEquals(expected, (loaded as NewsSavedViewModel.UiState.Loaded).data)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
