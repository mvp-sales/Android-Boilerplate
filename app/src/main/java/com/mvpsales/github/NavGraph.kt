package com.mvpsales.github

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mvpsales.github.api.response.ArticleNewsApiResponse
import com.mvpsales.github.entities.ArticleNews
import com.mvpsales.github.ui.newsdetail.NewsDetailScreen
import com.mvpsales.github.ui.newssaved.NewsSavedScreen
import com.mvpsales.github.ui.newssearch.NewsSearchScreen
import com.mvpsales.github.ui.newstabs.NewsTabsScreen
import com.mvpsales.github.ui.sourceslist.SourcesListScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.Source
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
object NewsSearch

@Serializable
object NewsList

@Serializable
data class NewsTabs(val searchTerm: String, val sourceId: String)

@Serializable
data class NewsDetail(val articleAsJsonString: String)

@Serializable
object NewsSaved

@Serializable
object SourcesList

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier) {
    NavHost(navController = navController, startDestination = NewsSearch, modifier = modifier) {
        composable<NewsSearch> {
            NewsSearchScreen(
                onNavigateToNewsList = { searchTerm ->
                    navController.navigate(route = NewsTabs(searchTerm, ""))
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
        composable<NewsTabs> { backStackEntry ->
            val route: NewsTabs = backStackEntry.toRoute()
            NewsTabsScreen(
                searchTerm = route.searchTerm,
                newsListViewModel = koinViewModel(
                    parameters = { parametersOf(route.searchTerm, route.sourceId) },
                    key = "latestKey"
                ),
                headlinesViewModel = koinViewModel(
                    parameters = { parametersOf(route.searchTerm, route.sourceId) },
                    key = "headlinesKey"
                ),
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
                    navController.navigate(route = NewsTabs("", source.id))
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

class ArticleNewsParamType : NavType<ArticleNewsApiResponse>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): ArticleNewsApiResponse? =
        bundle.getParcelable(key)

    override fun parseValue(value: String): ArticleNewsApiResponse =
        Json.decodeFromString(value)

    override fun put(bundle: Bundle, key: String, value: ArticleNewsApiResponse) =
        bundle.putParcelable(key, value)
}