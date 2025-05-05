package com.mvpsales.github.ui.newstabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.mvpsales.github.api.response.ArticleNewsApiResponse
import com.mvpsales.github.ui.newslist.NewsListScreen
import com.mvpsales.github.ui.newslist.NewsListType
import com.mvpsales.github.ui.newslist.NewsListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsTabsScreen(
    searchTerm: String,
    newsListViewModel: NewsListViewModel,
    headlinesViewModel: NewsListViewModel,
    onNavigateToNewsDetail: (ArticleNewsApiResponse) -> Unit,
    onNavigateBack: () -> Unit
) {
    val tabs = listOf("News", "Headlines")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("$searchTerm news results") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "backIcon")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
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
                    0 -> NewsListScreen(
                        newsListViewModel,
                        onNavigateToNewsDetail,
                        NewsListType.ALL_NEWS
                    )

                    1 -> NewsListScreen(
                        headlinesViewModel,
                        onNavigateToNewsDetail,
                        NewsListType.HEADLINES
                    )
                }
            }
        }
    }
}