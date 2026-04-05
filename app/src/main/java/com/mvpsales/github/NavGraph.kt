package com.mvpsales.github

import android.os.Bundle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.mvpsales.github.api.response.ArticleNewsDto
import com.mvpsales.github.domain.ArticleNews
import com.mvpsales.github.ui.newsdetail.NewsDetailScreen
import com.mvpsales.github.ui.newslist.NewsListScreen
import com.mvpsales.github.ui.newssaved.NewsSavedScreen
import com.mvpsales.github.ui.newssearch.NewsSearchScreen
import com.mvpsales.github.ui.sourceslist.SourcesListScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
object NewsSearch

@Serializable
data class NewsList(val searchTerm: String, val sourceId: String, val searchOnlyFavouriteSources: Boolean)

@Serializable
data class NewsDetail(val articleAsJsonString: String)

@Serializable
object NewsSaved

@Serializable
object SourcesList

data object NewsSearch3
data class NewsList3(val searchTerm: String, val sourceId: String, val searchOnlyFavouriteSources: Boolean)
data object NewsSaved3
data object SourcesList3
data class NewsDetail3(val article: ArticleNews)


@Composable
fun NewsApp(modifier: Modifier) {
    val backStack = remember { mutableStateListOf<Any>(NewsSearch3) }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is NewsSearch3 -> NavEntry(key) {
                    NewsSearchScreen(
                        onNavigateToNewsList = { searchTerm, searchFavouriteSourcesOnly ->
                            backStack.add(NewsList3(searchTerm, "", searchFavouriteSourcesOnly))
                        },
                        onNavigateToSavedNewsList = {
                            backStack.add(NewsSaved3)
                        },
                        onNavigateToSourcesList = {
                            backStack.add(SourcesList3)
                        }
                    )
                }
                is NewsSaved3 -> NavEntry(key) {
                    NewsSavedScreen(
                        viewModel = koinViewModel(),
                        onNavigateToNewsDetail = { article ->
                            //val json = Json.encodeToString(article)
                            //navController.navigate(route = NewsDetail(articleAsJsonString = json))
                            backStack.add(NewsDetail3(article))
                        },
                        onNavigateBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }
                is SourcesList3 -> NavEntry(key) {
                    SourcesListScreen(
                        viewModel = koinViewModel(),
                        onNavigateToNewsList = { source ->
                            backStack.add(NewsList3("", source.id, false))
                        },
                        onNavigateBack = { backStack.removeLastOrNull() }
                    )
                }
                is NewsList3 -> NavEntry(key) {
                    NewsListScreen(
                        viewModel = koinViewModel(
                            parameters = { parametersOf(key.searchTerm, key.sourceId, key.searchOnlyFavouriteSources) },
                            key = "latestKey_${key.searchTerm}_${key.sourceId}_${key.searchOnlyFavouriteSources}"
                        ),
                        onNavigateToNewsDetail = { article ->
                            //val json = Json.encodeToString(article)
                            //navController.navigate(route = NewsDetail(articleAsJsonString = json))
                            backStack.add(NewsDetail3(article))
                        },
                        onNavigateBack = { backStack.removeLastOrNull() }
                    )
                }
                is NewsDetail3 -> NavEntry(key) {
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

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier) {
    NavHost(navController = navController, startDestination = NewsSearch, modifier = modifier) {
        composable<NewsSearch> {
            NewsSearchScreen(
                onNavigateToNewsList = { searchTerm, searchFavouriteSourcesOnly ->
                    navController.navigate(route = NewsList(searchTerm, "", searchFavouriteSourcesOnly))
                },
                onNavigateToSavedNewsList = {
                    navController.navigate(route = NewsSaved)
                },
                onNavigateToSourcesList = {
                    navController.navigate(route = SourcesList)
                }
            )
        }
        composable<NewsSaved> {
            NewsSavedScreen(
                viewModel = koinViewModel(),
                onNavigateToNewsDetail = { article ->
                    val json = Json.encodeToString(article)
                    navController.navigate(route = NewsDetail(articleAsJsonString = json))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<NewsDetail> { backStackEntry ->
            val route: NewsDetail = backStackEntry.toRoute()
            val article: ArticleNews = Json.decodeFromString(route.articleAsJsonString)
            NewsDetailScreen(
                article,
                viewModel = koinViewModel(),
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable<SourcesList> { backStackEntry ->
            SourcesListScreen(
                viewModel = koinViewModel(),
                onNavigateToNewsList = { source ->
                    navController.navigate(route = NewsList("", source.id, false))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable<NewsList> { backStackEntry ->
            val route: NewsList = backStackEntry.toRoute()
            NewsListScreen(
                viewModel = koinViewModel(
                    parameters = { parametersOf(route.searchTerm, route.sourceId, route.searchOnlyFavouriteSources) },
                    key = "latestKey"
                ),
                onNavigateToNewsDetail = { article ->
                    val json = Json.encodeToString(article)
                    navController.navigate(route = NewsDetail(articleAsJsonString = json))
                },
                onNavigateBack = { navController.popBackStack() }
            )

        }
        /*composable(
            "details/{article}",
            arguments = listOf(
                navArgument("article") {
                    type = ArticleNewsParamType()
                }
            )
        ) {
            val article = it.arguments?.getParcelable<ArticleNewsApiResponse>("article")
            NewsDetailScreen(article!!)
        }*/
    }
}

class ArticleNewsParamType : NavType<ArticleNewsDto>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): ArticleNewsDto? =
        bundle.getParcelable(key)

    override fun parseValue(value: String): ArticleNewsDto =
        Json.decodeFromString(value)

    override fun put(bundle: Bundle, key: String, value: ArticleNewsDto) =
        bundle.putParcelable(key, value)
}