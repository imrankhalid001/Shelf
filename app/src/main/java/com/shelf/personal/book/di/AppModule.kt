package com.shelf.personal.book.di

import com.shelf.core.dispatcher.DefaultDispatcherProvider
import com.shelf.core.dispatcher.DispatcherProvider
import com.shelf.data.local.ShelfDatabase
import com.shelf.data.local.getDatabaseBuilder
import com.shelf.data.remote.api.OpenLibraryApi
import com.shelf.data.remote.api.OpenLibraryApiImpl
import com.shelf.data.remote.createKtorClient
import com.shelf.data.repository.BookRepositoryImpl
import com.shelf.data.repository.CollectionRepositoryImpl
import com.shelf.data.repository.StatsRepositoryImpl
import com.shelf.domain.repository.BookRepository
import com.shelf.domain.repository.CollectionRepository
import com.shelf.domain.repository.StatsRepository
import com.shelf.domain.usecase.CreateCollectionUseCase
import com.shelf.domain.usecase.GetBookDetailsUseCase
import com.shelf.domain.usecase.GetCollectionsUseCase
import com.shelf.domain.usecase.GetLibraryUseCase
import com.shelf.domain.usecase.GetReadingGoalUseCase
import com.shelf.domain.usecase.GetReadingStatisticsUseCase
import com.shelf.domain.usecase.GetRecommendationsUseCase
import com.shelf.domain.usecase.LogReadingSessionUseCase
import com.shelf.domain.usecase.SearchBooksUseCase
import com.shelf.domain.usecase.SetReadingGoalUseCase
import com.shelf.domain.usecase.UpdateReadingProgressUseCase
import com.shelf.personal.book.ui.screens.collections.CollectionsViewModel
import com.shelf.personal.book.ui.screens.details.BookDetailsViewModel
import com.shelf.personal.book.ui.screens.home.HomeViewModel
import com.shelf.personal.book.ui.screens.library.LibraryViewModel
import com.shelf.personal.book.ui.screens.search.SearchViewModel
import com.shelf.personal.book.ui.screens.stats.StatsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider() }

    // Database
    single<ShelfDatabase> { getDatabaseBuilder(androidContext()).build() }
    single { get<ShelfDatabase>().bookDao() }
    single { get<ShelfDatabase>().readingProgressDao() }
    single { get<ShelfDatabase>().noteQuoteDao() }
    single { get<ShelfDatabase>().collectionDao() }
    single { get<ShelfDatabase>().readingGoalDao() }
    single { get<ShelfDatabase>().readingSessionDao() }

    // Network
    single { createKtorClient() }
    single<OpenLibraryApi> { OpenLibraryApiImpl(get()) }

    // Repositories
    single<BookRepository> {
        BookRepositoryImpl(
            bookDao = get(),
            readingProgressDao = get(),
            noteQuoteDao = get(),
            openLibraryApi = get(),
            dispatchers = get()
        )
    }
    single<CollectionRepository> {
        CollectionRepositoryImpl(
            collectionDao = get(),
            dispatchers = get()
        )
    }
    single<StatsRepository> {
        StatsRepositoryImpl(
            readingGoalDao = get(),
            readingSessionDao = get(),
            readingProgressDao = get(),
            dispatchers = get()
        )
    }

    // Use Cases
    factory { SearchBooksUseCase(get()) }
    factory { GetLibraryUseCase(get()) }
    factory { UpdateReadingProgressUseCase(get()) }
    factory { GetReadingStatisticsUseCase(get()) }
    factory { GetBookDetailsUseCase(get()) }
    factory { GetCollectionsUseCase(get()) }
    factory { CreateCollectionUseCase(get()) }
    factory { LogReadingSessionUseCase(get(), get()) }
    factory { SetReadingGoalUseCase(get()) }
    factory { GetReadingGoalUseCase(get()) }
    factory { GetRecommendationsUseCase(get()) }

    // ViewModels
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { LibraryViewModel(get()) }
    viewModel { BookDetailsViewModel(get(), get(), get()) }
    viewModel { CollectionsViewModel(get(), get(), get()) }
    viewModel { StatsViewModel(get(), get(), get()) }
}
