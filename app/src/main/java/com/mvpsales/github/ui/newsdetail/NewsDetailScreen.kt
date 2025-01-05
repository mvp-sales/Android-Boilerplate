package com.mvpsales.github.ui.newsdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mvpsales.github.api.response.ArticleNewsApiResponse

@Composable
fun NewsDetailScreen(article: ArticleNewsApiResponse) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(article.title, modifier = Modifier.align(Alignment.Center))
    }
}