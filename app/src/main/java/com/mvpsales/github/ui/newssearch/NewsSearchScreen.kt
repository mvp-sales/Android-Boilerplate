package com.mvpsales.github.ui.newssearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsSearchScreen(
    onNavigateToNewsList: (String) -> Unit
) {
    // Controls expansion state of the search bar
    //var expanded = remember { mutableStateOf(false) }
    //val textFieldState = remember { TextFieldState() }
    var searchTerm = remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = Modifier.background(Color.White)
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
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    onNavigateToNewsList(searchTerm.value.text)
                },
                content = {
                    Text("Search news")
                }
            )
        }

        /*SearchBar(
            modifier = Modifier.align(Alignment.Center),
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                    onSearch = {
                        onNavigateToNewsList(textFieldState.text.toString())
                        expanded.value = false
                    },
                    expanded = expanded.value,
                    onExpandedChange = { expanded.value = it },
                    placeholder = { Text("Search") }
                )
            },
            expanded = expanded.value,
            onExpandedChange = { expanded.value = it }
        ) {}*/
    }
}

@Preview
@Composable
fun NewsSearchScreenPreview() {
    NewsSearchScreen {  }
}