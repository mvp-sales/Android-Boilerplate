package com.mvpsales.github.ui.newstabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.mvpsales.github.api.response.ArticleNewsApiResponse
import com.mvpsales.github.ui.newslist.NewsListScreen
import com.mvpsales.github.ui.newslist.NewsListType
import com.mvpsales.github.ui.newslist.NewsListViewModel
import kotlinx.coroutines.launch

@Composable
fun NewsTabsScreen(
    newsListViewModel: NewsListViewModel,
    headlinesViewModel: NewsListViewModel,
    onNavigateToNewsDetail: (ArticleNewsApiResponse) -> Unit
) {
    val tabs = listOf("News", "Headlines")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
            modifier = Modifier.fillMaxSize()
        ) {
            when (pagerState.currentPage) {
                0 -> NewsListScreen(newsListViewModel, onNavigateToNewsDetail, NewsListType.ALL_NEWS)
                1 -> NewsListScreen(headlinesViewModel, onNavigateToNewsDetail, NewsListType.HEADLINES)
            }
        }
    }
}