package com.mvpsales.github.ui.sourceslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvpsales.github.api.response.NewsSourceApiResponse
import com.mvpsales.github.ui.newssaved.NewsContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourcesListScreen(
    viewModel: SourcesListViewModel,
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
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(state.sources) { source ->
                            SourceContent(source)
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
fun SourceContent(source: NewsSourceApiResponse) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(source.name)
        Text(source.description)
        Text(source.url)
        Text(source.category)
        Text("Country: ${source.country}/Language: ${source.language}")
    }
}