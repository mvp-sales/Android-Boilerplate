package com.mvpsales.github.ui.newsdetail

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
class NewsDetailViewModelTest {

    private lateinit var viewModel: NewsDetailViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository = mockk<NewsRepository>()
    private val dispatcherHelper = mockk<DispatcherHelper>()

    @BeforeEach
    fun setup() {
        every { dispatcherHelper.ioDispatcher() } returns testDispatcher
        viewModel = NewsDetailViewModel(repository, dispatcherHelper)
    }

    @Test
    fun `getArticle emits Loading and Loaded states`() = runTest {
        val articleUrl = "http://example.com"
        val expected = mockk<ArticleNews>()
        coEvery { repository.getArticle(articleUrl) } returns flowOf(expected)

        viewModel.uiState.test {
            assertEquals(NewsDetailViewModel.UiState.Initial, awaitItem()) // assert initial state
            viewModel.getArticle(articleUrl)

            assertEquals(NewsDetailViewModel.UiState.Loading, awaitItem())
            val loaded = awaitItem()
            assertTrue(loaded is NewsDetailViewModel.UiState.Loaded)
            assertTrue((loaded as NewsDetailViewModel.UiState.Loaded).isArticleSaved)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `saveArticle emits Loading and Loaded states`() = runTest {
        val article = mockk<ArticleNews>()

        coEvery { repository.saveArticle(article) } returns Unit

        viewModel.uiState.test {
            assertEquals(NewsDetailViewModel.UiState.Initial, awaitItem()) // assert initial state
            viewModel.saveArticle(article)

            assertEquals(NewsDetailViewModel.UiState.Loading, awaitItem())
            val loaded = awaitItem()
            assertTrue(loaded is NewsDetailViewModel.UiState.Loaded)
            assertTrue((loaded as NewsDetailViewModel.UiState.Loaded).isArticleSaved)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleteArticle emits Loading and Loaded states`() = runTest {
        val articleUrl = "http://example.com"

        coEvery { repository.deleteArticle(articleUrl) } returns Unit

        viewModel.uiState.test {
            assertEquals(NewsDetailViewModel.UiState.Initial, awaitItem()) // asserts initial state
            viewModel.deleteArticle(articleUrl)

            assertEquals(NewsDetailViewModel.UiState.Loading, awaitItem())
            val loaded = awaitItem()
            assertTrue(loaded is NewsDetailViewModel.UiState.Loaded)
            assertTrue((loaded as NewsDetailViewModel.UiState.Loaded).isArticleSaved.not())

            cancelAndIgnoreRemainingEvents()
        }
    }
}
