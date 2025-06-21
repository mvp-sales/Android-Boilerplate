package com.mvpsales.github.ui.newslist

import app.cash.turbine.test
import com.github.michaelbull.result.Ok
import com.mvpsales.github.domain.ArticleNews
import com.mvpsales.github.domain.NewsPage
import com.mvpsales.github.repository.NewsRepository
import com.mvpsales.github.utils.DispatcherHelper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@OptIn(ExperimentalCoroutinesApi::class)
class NewsListViewModelTest {
    private lateinit var viewModel: NewsListViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository = mockk<NewsRepository>()
    private val dispatcherHelper = mockk<DispatcherHelper>()

    @BeforeEach
    fun setup() {
        every { dispatcherHelper.ioDispatcher() } returns testDispatcher
        viewModel = NewsListViewModel("", "", repository, dispatcherHelper)
    }

    @ParameterizedTest(name = "should emit Loading and Loaded states from Initial state")
    @EnumSource(NewsListType::class)
    fun `fetchNews should emit Loading and Loaded states from Initial state`(newsListType: NewsListType) = runTest {
        val expected = listOf(mockk<ArticleNews>())
        val newsPage = NewsPage(
            articles = expected,
            page = 1,
            totalResults = 1
        )

        coEvery { repository.getNews(any()) } returns flowOf(Ok(newsPage))

        viewModel.uiState.test {
            Assertions.assertEquals(
                NewsListViewModel.UiState.Initial,
                awaitItem()
            ) // assert initial state
            viewModel.fetchNews(newsListType)

            Assertions.assertEquals(NewsListViewModel.UiState.Loading, awaitItem())
            val loaded = awaitItem()
            Assertions.assertTrue(loaded is NewsListViewModel.UiState.Loaded)
            loaded as NewsListViewModel.UiState.Loaded
            Assertions.assertEquals(expected, loaded.data)
            Assertions.assertEquals(newsListType, loaded.newsType)
            Assertions.assertEquals(newsPage.page, loaded.lastLoadedPage)
            Assertions.assertEquals(newsPage.totalResults, loaded.totalResultsCount)
            Assertions.assertTrue(loaded.isLoadingMore.not())
        }
    }
}