package com.mvpsales.github.ui.newsdetail

import app.cash.turbine.test
import com.mvpsales.github.NewsDetail
import com.mvpsales.github.entities.ArticleNews
import com.mvpsales.github.entities.ArticleSource
import com.mvpsales.github.repository.NewsRepository
import com.mvpsales.github.ui.newssaved.NewsSavedViewModel
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
class NewsDetailViewModelTest {

    private lateinit var viewModel: NewsDetailViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository = mockk<NewsRepository>()
    private val dispatcherHelper = mockk<DispatcherHelper>()

    @Before
    fun setup() {
        every { dispatcherHelper.ioDispatcher() } returns testDispatcher
        viewModel = NewsDetailViewModel(repository, dispatcherHelper)
    }

    @Test
    fun `getArticle emits Loading and Loaded states`() = runTest {
        val articleUrl = "https://www.androidcentral.com/apps-software/android-central-best-of-2024-apps-services"
        val expected = ArticleNews(
            author = "shrutishekar@gmail.com (Shruti Shekar)",
            title = "Android Central's Best of 2024: Apps and Services",
            description = "Here are all the winners for Best Apps and Services for 2024!",
            url = articleUrl,
            urlToImage = "https://cdn.mos.cms.futurecdn.net/kWGZ6wr2t9dDGdmZW7pLEP-1200-80.jpg",
            publishedAt = "2025-01-01T13:00:00Z",
            content = "There have been some stellar apps and services that were released this year and I can wholeheartedly agree with every single one of the winners on this list. \r\nI am a bit biased here, but I am a huge… [+4354 chars]",
            source = ArticleSource(
                name = "Android Central",
                id = null
            )
        )
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
}