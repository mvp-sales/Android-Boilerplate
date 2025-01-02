package com.mvpsales.github

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mvpsales.github.ui.newslist.NewsListScreen
import kotlinx.serialization.Serializable

@Serializable
object NewsList

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NewsList) {
        composable<NewsList> {
            NewsListScreen(hiltViewModel())
        }
    }
}