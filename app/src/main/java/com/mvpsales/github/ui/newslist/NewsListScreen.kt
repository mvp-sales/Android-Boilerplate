package com.mvpsales.github.ui.newslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.mvpsales.github.api.response.ArticleNewsApiResponse
import com.mvpsales.github.api.response.ArticleSourceNewsApiResponse
import com.mvpsales.github.api.response.formatPublishedDate

enum class NewsListType {
    ALL_NEWS, HEADLINES
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(
    viewModel: NewsListViewModel,
    onNavigateToNewsDetail: (ArticleNewsApiResponse) -> Unit,
    listType: NewsListType
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when(val state = uiState.value) {
        is NewsListViewModel.NewsListUiState.Initial -> LaunchedEffect(true) {
            when (listType) {
                NewsListType.ALL_NEWS -> viewModel.getEverything()
                NewsListType.HEADLINES -> viewModel.getHeadlines()
            }
        }
        is NewsListViewModel.NewsListUiState.Loaded -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.data.articles) { article ->
                    NewsContent(article, onNavigateToNewsDetail)
                }
            }
        }
        is NewsListViewModel.NewsListUiState.Error -> {
            BasicAlertDialog(
                onDismissRequest = {
                    viewModel.getEverything()
                }
            ) {
                Text(state.error.message)
            }
        }
        is NewsListViewModel.NewsListUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun NewsContent(
    article: ArticleNewsApiResponse,
    onNavigateToNewsDetail: (ArticleNewsApiResponse) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
            .clickable { onNavigateToNewsDetail(article) }
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth().height(160.dp),
            model = article.urlToImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = BrushPainter(
                Brush.linearGradient(
                    listOf(
                        Color(color = 0xFFFF0000),
                        Color(color = 0xFFDDDDDD),
                    )
                )
            )
        )
        Text(
            article.title,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                article.source.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                article.formatPublishedDate("dd MMM yyyy") ?: "",
                modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun NewsComponentPreview() {
    NewsContent(
        ArticleNewsApiResponse(
            author = "shrutishekar@gmail.com (Shruti Shekar)",
            title = "Android Central's Best of 2024: Apps and Services",
            description = "Here are all the winners for Best Apps and Services for 2024!",
            url = "https://www.androidcentral.com/apps-software/android-central-best-of-2024-apps-services",
            urlToImage = "https://cdn.mos.cms.futurecdn.net/kWGZ6wr2t9dDGdmZW7pLEP-1200-80.jpg",
            publishedAt = "2025-01-01T13:00:00Z",
            content = "There have been some stellar apps and services that were released this year and I can wholeheartedly agree with every single one of the winners on this list. \r\nI am a bit biased here, but I am a huge… [+4354 chars]",
            source = ArticleSourceNewsApiResponse(
                name = "Android Central",
                id = null
            )
        ),
        onNavigateToNewsDetail = { _ -> }
    )
}