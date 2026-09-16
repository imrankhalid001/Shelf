package com.shelf.personal.book.ui.screens.library

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shelf.personal.book.ui.components.ShelfBookCard
import com.shelf.personal.book.ui.components.ShelfEmptyState
import com.shelf.personal.book.ui.components.ShelfTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun LibraryScreen(
    onBookClick: (String) -> Unit = {},
    viewModel: LibraryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ShelfTopBar(title = "My Library")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (uiState.books.isEmpty()) {
                ShelfEmptyState(
                    title = "Your Library is Empty",
                    subtitle = "Search for books and save them to build your personal shelf."
                )
            } else {
                LazyColumn {
                    items(uiState.books, key = { it.id }) { book ->
                        ShelfBookCard(
                            book = book,
                            onBookClick = onBookClick,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
