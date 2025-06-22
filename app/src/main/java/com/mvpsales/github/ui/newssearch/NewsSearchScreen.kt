package com.mvpsales.github.ui.newssearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsSearchScreen(
    onNavigateToNewsList: (String, Boolean) -> Unit,
    onNavigateToSavedNewsList: () -> Unit,
    onNavigateToSourcesList: () -> Unit
) {
    var searchTerm = remember { mutableStateOf(TextFieldValue("")) }
    var showOnlyFromFavouriteSources by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("News Search") }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.padding(padding)
                .background(Color.White)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    "Search news by term",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                OutlinedTextField(
                    modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth(),
                    value = searchTerm.value,
                    onValueChange = { newTerm ->
                        searchTerm.value = newTerm
                    },
                    placeholder = { Text("Search") }
                )
                Row {
                    Checkbox(
                        checked = showOnlyFromFavouriteSources,
                        onCheckedChange = { checked ->
                            showOnlyFromFavouriteSources = checked
                        }
                    )
                    Text(
                        "Show only from favourite sources",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        onNavigateToNewsList(searchTerm.value.text, showOnlyFromFavouriteSources)
                    },
                    content = {
                        Text("Search news")
                    }
                )
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = onNavigateToSavedNewsList,
                    content = {
                        Text("Saved news list")
                    }
                )
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = onNavigateToSourcesList,
                    content = {
                        Text("Show sources list")
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun NewsSearchScreenPreview() {
    NewsSearchScreen(
        onNavigateToNewsList = { _, _ -> },
        onNavigateToSavedNewsList = { },
        onNavigateToSourcesList = { }
    )
}