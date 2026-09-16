package com.shelf.personal.book.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
fun HomeScreen(
    onBookClick: (String) -> Unit = {},
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ShelfTopBar(title = "Shelf")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (uiState.currentlyReadingBook != null) {
                Text(
                    text = "Continue Reading",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                ShelfBookCard(
                    book = uiState.currentlyReadingBook!!,
                    onBookClick = onBookClick
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (uiState.recommendedBooks.isNotEmpty()) {
                Text(
                    text = "Recommended for You",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow {
                    items(uiState.recommendedBooks, key = { it.id }) { book ->
                        ShelfBookCard(
                            book = book,
                            onBookClick = onBookClick,
                            modifier = Modifier
                                .padding(end = 8.dp)
                        )
                    }
                }
            } else if (uiState.currentlyReadingBook == null) {
                ShelfEmptyState(
                    title = "Your Personal Reading Space",
                    subtitle = "Search for books to add them to your library and start tracking progress."
                )
            }
        }
    }
}
