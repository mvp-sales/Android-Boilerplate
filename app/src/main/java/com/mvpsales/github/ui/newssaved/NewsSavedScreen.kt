package com.mvpsales.github.ui.newssaved

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.mvpsales.github.entities.ArticleNews
import com.mvpsales.github.entities.formatPublishedDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsSavedScreen(
    viewModel: NewsSavedViewModel,
    onNavigateToNewsDetail: (ArticleNews) -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.resetState()
    }

    when(val state = uiState.value) {
        is NewsSavedViewModel.UiState.Initial -> LaunchedEffect(true) {
            viewModel.getSavedNews()
        }
        is NewsSavedViewModel.UiState.Loaded -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = { Text("Saved news") },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "backIcon")
                            }
                        }
                    )
                }
            ) { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(state.data) { article ->
                            NewsContent(article, onNavigateToNewsDetail)
                        }
                    }
                }
            }
        }
        is NewsSavedViewModel.UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun NewsContent(
    article: ArticleNews,
    onNavigateToNewsDetail: (ArticleNews) -> Unit
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