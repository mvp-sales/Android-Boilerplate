package com.mvpsales.github

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mvpsales.github.api.response.ArticleNewsApiResponse
import com.mvpsales.github.ui.newsdetail.NewsDetailScreen
import com.mvpsales.github.ui.newslist.NewsListScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
object NewsList

@Serializable
data class NewsDetail(val articleAsJsonString: String)

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NewsList) {
        composable<NewsList> {
            NewsListScreen(
                viewModel = hiltViewModel(),
                onNavigateToNewsDetail = { article ->
                    val json = Json.encodeToString(article)
                    navController.navigate(route = NewsDetail(articleAsJsonString = json))
                }
            )
        }
        composable<NewsDetail> { backStackEntry ->
            val route: NewsDetail = backStackEntry.toRoute()
            val article: ArticleNewsApiResponse = Json.decodeFromString(route.articleAsJsonString)
            NewsDetailScreen(article)
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