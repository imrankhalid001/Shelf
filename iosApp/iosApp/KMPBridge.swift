import Foundation
import Shared

@MainActor
final class KMPBridge: ObservableObject {
    static let shared = KMPBridge()

    let database: ShelfDatabase
    let bookRepository: BookRepositoryImpl
    let collectionRepository: CollectionRepositoryImpl
    let statsRepository: StatsRepositoryImpl

    let searchBooksUseCase: SearchBooksUseCase
    let getLibraryUseCase: GetLibraryUseCase
    let updateReadingProgressUseCase: UpdateReadingProgressUseCase
    let getReadingStatisticsUseCase: GetReadingStatisticsUseCase
    let getBookDetailsUseCase: GetBookDetailsUseCase
    let getCollectionsUseCase: GetCollectionsUseCase
    let createCollectionUseCase: CreateCollectionUseCase
    let logReadingSessionUseCase: LogReadingSessionUseCase
    let setReadingGoalUseCase: SetReadingGoalUseCase
    let getReadingGoalUseCase: GetReadingGoalUseCase
    let getRecommendationsUseCase: GetRecommendationsUseCase

    private init() {
        let dbBuilder = DatabaseBuilder_iosKt.getDatabaseBuilder()
        self.database = dbBuilder.build()

        let bookDao = database.bookDao()
        let readingProgressDao = database.readingProgressDao()
        let noteQuoteDao = database.noteQuoteDao()
        let collectionDao = database.collectionDao()
        let readingGoalDao = database.readingGoalDao()
        let readingSessionDao = database.readingSessionDao()

        let ktorClient = KtorClientFactoryKt.createKtorClient()
        let api = OpenLibraryApiImpl(client: ktorClient)
        let dispatchers = DefaultDispatcherProvider()

        self.bookRepository = BookRepositoryImpl(
            bookDao: bookDao,
            readingProgressDao: readingProgressDao,
            noteQuoteDao: noteQuoteDao,
            openLibraryApi: api,
            dispatchers: dispatchers,
            readingSessionDao: readingSessionDao
        )

        self.collectionRepository = CollectionRepositoryImpl(
            collectionDao: collectionDao,
            dispatchers: dispatchers
        )

        self.statsRepository = StatsRepositoryImpl(
            readingGoalDao: readingGoalDao,
            readingSessionDao: readingSessionDao,
            readingProgressDao: readingProgressDao,
            dispatchers: dispatchers
        )

        self.searchBooksUseCase = SearchBooksUseCase(bookRepository: bookRepository)
        self.getLibraryUseCase = GetLibraryUseCase(bookRepository: bookRepository)
        self.updateReadingProgressUseCase = UpdateReadingProgressUseCase(bookRepository: bookRepository)
        self.getReadingStatisticsUseCase = GetReadingStatisticsUseCase(statsRepository: statsRepository)
        self.getBookDetailsUseCase = GetBookDetailsUseCase(bookRepository: bookRepository)
        self.getCollectionsUseCase = GetCollectionsUseCase(collectionRepository: collectionRepository)
        self.createCollectionUseCase = CreateCollectionUseCase(collectionRepository: collectionRepository)
        self.logReadingSessionUseCase = LogReadingSessionUseCase(statsRepository: statsRepository, bookRepository: bookRepository)
        self.setReadingGoalUseCase = SetReadingGoalUseCase(statsRepository: statsRepository)
        self.getReadingGoalUseCase = GetReadingGoalUseCase(statsRepository: statsRepository)
        self.getRecommendationsUseCase = GetRecommendationsUseCase(bookRepository: bookRepository)
    }
}

class SwiftFlowCollector: NSObject, Kotlinx_coroutines_coreFlowCollector {
    private let callback: (Any?) -> Void

    init(callback: @escaping (Any?) -> Void) {
        self.callback = callback
    }

    func emit(value: Any?) async throws {
        callback(value)
    }
}

extension Kotlinx_coroutines_coreFlow {
    func collectValues(onEmit: @escaping (Any?) -> Void) async throws {
        try await self.collect(collector: SwiftFlowCollector(callback: onEmit))
    }
}
