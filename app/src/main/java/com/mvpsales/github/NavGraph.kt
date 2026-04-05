package com.mvpsales.github

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.mvpsales.github.domain.ArticleNews
import com.mvpsales.github.ui.newsdetail.NewsDetailScreen
import com.mvpsales.github.ui.newslist.NewsListScreen
import com.mvpsales.github.ui.newssaved.NewsSavedScreen
import com.mvpsales.github.ui.newssearch.NewsSearchScreen
import com.mvpsales.github.ui.sourceslist.SourcesListScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

data object NewsSearch
data class NewsList(val searchTerm: String, val sourceId: String, val searchOnlyFavouriteSources: Boolean)
data object NewsSaved
data object SourcesList
data class NewsDetail(val article: ArticleNews)


@Composable
fun NewsApp(modifier: Modifier) {
    val backStack = remember { mutableStateListOf<Any>(NewsSearch) }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is NewsSearch -> NavEntry(key) {
                    NewsSearchScreen(
                        onNavigateToNewsList = { searchTerm, searchFavouriteSourcesOnly ->
                            backStack.add(NewsList(searchTerm, "", searchFavouriteSourcesOnly))
                        },
                        onNavigateToSavedNewsList = {
                            backStack.add(NewsSaved)
                        },
                        onNavigateToSourcesList = {
                            backStack.add(SourcesList)
                        }
                    )
                }
                is NewsSaved -> NavEntry(key) {
                    NewsSavedScreen(
                        viewModel = koinViewModel(),
                        onNavigateToNewsDetail = { article ->
                            backStack.add(NewsDetail(article))
                        },
                        onNavigateBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }
                is SourcesList -> NavEntry(key) {
                    SourcesListScreen(
                        viewModel = koinViewModel(),
                        onNavigateToNewsList = { source ->
                            backStack.add(NewsList("", source.id, false))
                        },
                        onNavigateBack = { backStack.removeLastOrNull() }
                    )
                }
                is NewsList -> NavEntry(key) {
                    NewsListScreen(
                        viewModel = koinViewModel(
                            parameters = { parametersOf(key.searchTerm, key.sourceId, key.searchOnlyFavouriteSources) },
                            key = "latestKey_${key.searchTerm}_${key.sourceId}_${key.searchOnlyFavouriteSources}"
                        ),
                        onNavigateToNewsDetail = { article ->
                            backStack.add(NewsDetail(article))
                        },
                        onNavigateBack = { backStack.removeLastOrNull() }
                    )
                }
                is NewsDetail -> NavEntry(key) {
                    NewsDetailScreen(
                        key.article,
                        viewModel = koinViewModel(),
                        onNavigateBack = { backStack.removeLastOrNull() }
                    )
                }
                else -> NavEntry(Unit) { Text("Unknown route") }
            }
        }
    )
}