package com.shelf.personal.book.ui.screens.details

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.shelf.core.result.AppResult
import com.shelf.domain.model.Book
import com.shelf.domain.model.ReadingStatus
import com.shelf.personal.book.ui.components.ShelfEmptyState
import com.shelf.personal.book.ui.components.ShelfProgressBar
import com.shelf.personal.book.ui.components.ShelfTopBar
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    bookId: String,
    onBackClick: () -> Unit = {},
    viewModel: BookDetailsViewModel = koinViewModel()
) {
    LaunchedEffect(bookId) {
        viewModel.loadBook(bookId)
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ShelfTopBar(title = "Book Details")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            when (val result = uiState.bookResult) {
                is AppResult.Success -> {
                    val book = result.data
                    BookDetailsContent(
                        book = book,
                        onSaveClick = { viewModel.saveBook(book) },
                        onStatusChange = { newStatus -> viewModel.updateStatus(book.id, newStatus) },
                        onProgressChange = { newPage ->
                            viewModel.updateProgress(book.id, newPage, book.pageCount)
                        }
                    )
                }
                is AppResult.Error -> {
                    ShelfEmptyState(
                        title = "Book Not Found",
                        subtitle = "Could not load details for this book."
                    )
                }
                is AppResult.Loading -> {
                    Text("Loading book details...")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookDetailsContent(
    book: Book,
    onSaveClick: () -> Unit,
    onStatusChange: (ReadingStatus) -> Unit,
    onProgressChange: (Int) -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = book.coverUrl,
                contentDescription = book.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 140.dp, height = 210.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = book.title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (book.subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = book.subtitle!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (book.firstPublishYear != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "First published in ${book.firstPublishYear}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Save to Library")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Read / Borrow Free on Open Library Button
            OutlinedButton(
                onClick = {
                    val openLibraryUrl = "https://openlibrary.org/works/${book.workId}"
                    uriHandler.openUri(openLibraryUrl)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.AutoStories,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Read Free on Open Library 📖")
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Progress Section
    val currentProgress = book.readingProgress
    val currentPage = currentProgress?.currentPage ?: 0
    val totalPages = if (book.pageCount > 0) book.pageCount else 100
    var sliderValue by remember(currentPage) { mutableFloatStateOf(currentPage.toFloat()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Reading Progress",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Page ${sliderValue.toInt()} / $totalPages (${((sliderValue / totalPages) * 100).toInt()}%)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            ShelfProgressBar(progressPercentage = sliderValue / totalPages)
            Spacer(modifier = Modifier.height(8.dp))
            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                onValueChangeFinished = { onProgressChange(sliderValue.toInt()) },
                valueRange = 0f..totalPages.toFloat()
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Status Selector Section
    Text(
        text = "Reading Status",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReadingStatus.entries.forEach { status ->
            val isSelected = currentProgress?.status == status
            val labelText = when (status) {
                ReadingStatus.WANT_TO_READ -> "To Read"
                ReadingStatus.READING -> "Reading"
                ReadingStatus.FINISHED -> "Finished"
                ReadingStatus.PAUSED -> "Paused"
                ReadingStatus.DROPPED -> "Dropped"
            }
            FilterChip(
                selected = isSelected,
                onClick = { onStatusChange(status) },
                label = {
                    Text(
                        text = labelText,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            )
        }
    }
}
