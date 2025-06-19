package com.mvpsales.github.ui.sourceslist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BasicAlertDialog
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvpsales.github.domain.NewsSource
import com.mvpsales.github.domain.getLanguageCountryNames

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourcesListScreen(
    viewModel: SourcesListViewModel,
    onNavigateToNewsList: (NewsSource) -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState.value) {
        is SourcesListViewModel.UiState.Initial -> LaunchedEffect(true) {
            viewModel.getSources()
        }
        is SourcesListViewModel.UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is SourcesListViewModel.UiState.Loaded -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = { Text("Sources list") },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "backIcon")
                            }
                        }
                    )
                }
            ) { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(state.sources) { source ->
                            SourceContent(
                                source,
                                onNavigateToNewsList,
                                onAddSourceToFavourites = {
                                    viewModel.addFavouriteSource(it)
                                },
                                onRemoveSourceFromFavourites = {
                                    viewModel.removeFavouriteSource(it)
                                }
                            )
                        }
                    }
                }
            }
        }
        is SourcesListViewModel.UiState.Error -> {
            BasicAlertDialog(
                onDismissRequest = {
                    onNavigateBack()
                }
            ) {
                Text(state.error.message)
            }
        }
    }
}

@Composable
fun SourceContent(
    source: NewsSource,
    onNavigateToNewsList: (NewsSource) -> Unit,
    onAddSourceToFavourites: (NewsSource) -> Unit,
    onRemoveSourceFromFavourites: (NewsSource) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                onNavigateToNewsList(source)
            }
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(BorderStroke(2.dp, Color.DarkGray), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row {
            Text(
                modifier = Modifier.weight(1f)
                    .align(Alignment.CenterVertically),
                text = source.name,
                style = MaterialTheme.typography.headlineSmall
            )
            IconButton(
                modifier = Modifier.align(Alignment.Top),
                onClick = {
                    if (source.favourite) {
                        onRemoveSourceFromFavourites(source)
                    } else {
                        onAddSourceToFavourites(source)
                    }
                }
            ) {
                val icon = if (source.favourite) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Filled.FavoriteBorder
                }
                Icon(icon, "favourite", tint = Color.Red)
            }
        }
        val uriHandler = LocalUriHandler.current
        Text(
            modifier = Modifier.clickable {
                    uriHandler.openUri(source.url)
                }.padding(vertical = 4.dp),
            text = source.url,
            textAlign = TextAlign.End
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = source.description,
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = "Category: ${source.category}",
            style = MaterialTheme.typography.labelSmall
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            val (language, country) = source.getLanguageCountryNames()
            Text(
                modifier = Modifier.weight(1f),
                text = country,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                modifier = Modifier.weight(1f),
                text = "Language: $language",
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview
@Composable
fun SourceContentPreview() {
    SourceContent(
        NewsSource(
            id = "abc-news",
            name = "ABC News",
            description = "Your trusted source for breaking news, analysis, exclusive interviews, headlines, and videos at ABCNews.com.",
            url = "https://abcnews.go.com",
            category = "general",
            language = "en",
            country = "us",
            favourite = true
        ),
        onNavigateToNewsList = {},
        onAddSourceToFavourites = {},
        onRemoveSourceFromFavourites = {}
    )
}