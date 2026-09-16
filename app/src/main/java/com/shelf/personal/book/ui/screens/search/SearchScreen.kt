package com.shelf.personal.book.ui.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shelf.core.result.AppResult
import com.shelf.personal.book.ui.components.ShelfBookCard
import com.shelf.personal.book.ui.components.ShelfEmptyState
import com.shelf.personal.book.ui.components.ShelfTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    onBookClick: (String) -> Unit = {},
    viewModel: SearchViewModel = koinViewModel()
) {
    val query by viewModel.query.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ShelfTopBar(title = "Search Books")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.onQueryChanged(it) },
                label = { Text("Search by title or author...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            when (val result = uiState.searchResult) {
                is AppResult.Success -> {
                    if (result.data.isEmpty()) {
                        ShelfEmptyState(
                            title = if (query.isBlank()) "Search Open Library" else "No Books Found",
                            subtitle = if (query.isBlank()) "Type a book title above to explore." else "Try searching for a different keyword."
                        )
                    } else {
                        LazyColumn {
                            items(result.data, key = { it.id }) { book ->
                                ShelfBookCard(
                                    book = book,
                                    onBookClick = onBookClick,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
                is AppResult.Error -> {
                    ShelfEmptyState(
                        title = "Network Error",
                        subtitle = "Unable to connect to Open Library. Showing cached items."
                    )
                }
                is AppResult.Loading -> {
                    Text(text = "Searching Open Library...")
                }
            }
        }
    }
}
