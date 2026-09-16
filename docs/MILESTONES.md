# MILESTONES.md — Development Roadmap & Detailed Checklists

Development of **Shelf** is structured into 23 sequential milestones (Milestones 0 through 22). Each milestone has explicit acceptance criteria and automated verification steps.

---

## 🟢 MILESTONE 0 — PRODUCT DEFINITION & VISION
- [x] Define product positioning ("Your personal reading space.").
- [x] Finalize core philosophy (Shared KMP logic + Native Compose & SwiftUI UIs).
- [x] Document key user personas and user journeys (`docs/PRODUCT_SPEC.md`).
- [x] Create feature prioritization matrix (MVP vs Post-MVP).
- [x] Verify product spec consistency with portfolio goals.

---

## 🟢 MILESTONE 1 — ARCHITECTURE & DOCUMENTATION
- [x] Draft `docs/ARCHITECTURE.md` (Clean Architecture & data flow diagrams).
- [x] Draft `docs/TECH_STACK.md` (Library matrix & justifications).
- [x] Draft `docs/PROJECT_STRUCTURE.md` (Detailed package mapping).
- [x] Draft `AGENTS.md` (Mandatory AI Coding Agent guidelines).
- [x] Draft `docs/CODING_STANDARDS.md` (Kotlin/Swift style guides).
- [x] Draft `docs/SECURITY_PRIVACY.md` & `docs/GIT_WORKFLOW.md`.

---

## 🟢 MILESTONE 2 — DESIGN SYSTEM FOUNDATION
- [x] Define design language principles (Book & paper inspired).
- [x] Define semantic color tokens for Light and Dark modes (`docs/DESIGN_SYSTEM.md`).
- [x] Establish typography scale (`displayLarge` to `caption`).
- [x] Establish spacing scale (4dp to 48dp) and corner radii.
- [x] Document UI screen hierarchy and 4 UI states (`docs/UI_UX.md`).

---

## 🟢 MILESTONE 3 — PROJECT BOOTSTRAP & BUILD SETUP
- [x] Configure root `build.gradle.kts` and `settings.gradle.kts` for KMP multi-module.
- [x] Setup `gradle/libs.versions.toml` with Room KMP, Ktor 3.x, Compose BOM, Coil 3, Koin.
- [x] Configure `:shared` module targets (`commonMain`, `androidMain`, `iosMain`, `commonTest`).
- [x] Configure `:app` Android module with Jetpack Compose enabled (`compileSdk = 36`, `targetSdk = 36`).
- [x] Configure `iosApp` Xcode project shell linking shared framework.
- [x] Verify `./gradlew build` compiles successfully for both Android and iOS targets.

---

## 🟢 MILESTONE 4 — CORE SHARED INFRASTRUCTURE
- [x] Implement `AppResult<T>` functional wrapper in `shared/src/commonMain/kotlin/com/shelf/core/result/`.
- [x] Implement `AppError` sealed hierarchy in `shared/src/commonMain/kotlin/com/shelf/core/error/`.
- [x] Implement `DispatcherProvider` and `TestDispatcherProvider` for Coroutines.
- [x] Implement `Logger` abstraction for KMP platform logging.
- [x] Implement `DateUtils` and `StringUtils` in `commonMain`.
- [x] Write 100% unit test coverage for core utilities in `commonTest`.

---

## 🟢 MILESTONE 5 — ROOM KMP DATABASE & DAOS
- [x] Define Room entities (`BookEntity`, `AuthorEntity`, `ReadingProgressEntity`, `NoteEntity`, `QuoteEntity`, `CollectionEntity`, `ReadingSessionEntity`, `ReadingGoalEntity`).
- [x] Define Room DAOs (`BookDao`, `AuthorDao`, `CollectionDao`, `ReadingProgressDao`, `ReadingSessionDao`, `NoteQuoteDao`, `ReadingGoalDao`).
- [x] Create `ShelfDatabase` abstract class annotated with `@Database` and `@ConstructedBy`.
- [x] Implement database drivers for Android (`getDatabaseBuilder`) and iOS (`getDatabaseBuilder`).
- [x] Write in-memory Room database unit tests in `commonTest`.

---

## 🟢 MILESTONE 6 — OPEN LIBRARY REMOTE API CLIENT
- [x] Setup Ktor Client with multiplatform JSON serialization in `shared/src/commonMain/kotlin/com/shelf/data/remote/`.
- [x] Define remote DTOs (`SearchResponseDto`, `WorkDetailsDto`, `AuthorDto`).
- [x] Implement `OpenLibraryApi` interface and HTTP engine integration.
- [x] Implement bidirectional mappers (`Dto ↔ Entity ↔ Domain`).
- [x] Write API deserialization tests using Ktor `MockEngine` in `commonTest`.

---

## 🟢 MILESTONE 7 — REPOSITORIES & DOMAIN USE CASES
- [x] Implement Domain models (`Book`, `Author`, `ReadingProgress`, `Collection`, `Note`, `Quote`, `ReadingSession`, `ReadingGoal`, `ReadingStatistics`).
- [x] Implement Repository contracts & implementations (`BookRepositoryImpl`, `CollectionRepositoryImpl`, `StatsRepositoryImpl`).
- [x] Implement core Use Cases (`SearchBooksUseCase`, `GetLibraryUseCase`, `UpdateReadingProgressUseCase`, `GetReadingStatisticsUseCase`, `GetBookDetailsUseCase`, `GetCollectionsUseCase`, `CreateCollectionUseCase`, `LogReadingSessionUseCase`, `SetReadingGoalUseCase`, `GetReadingGoalUseCase`, `GetRecommendationsUseCase`).
- [x] Write unit tests for Use Cases and Repositories in `commonTest`.

---

## 🟢 MILESTONE 8 — ANDROID UI FOUNDATION (JETPACK COMPOSE)
- [x] Create `ShelfTheme`, `Color.kt`, `Type.kt`, `Shape.kt` design system tokens in `:app`.
- [x] Build reusable Compose components (`ShelfTopBar`, `ShelfBookCard`, `ShelfProgressBar`, `ShelfEmptyState`).
- [x] Setup Jetpack Compose Navigation Graph (`ShelfNavigation`).
- [x] Configure Koin Android dependency injection (`appModule`).

---

## 🟢 MILESTONE 9 — iOS UI FOUNDATION (SWIFTUI)
- [x] Create Swift design system color tokens in `iosApp`.
- [x] Build reusable SwiftUI components (`EmptyStateView`, `ProgressBarView`).
- [x] Setup SwiftUI `NavigationStack` router in `HomeView.swift`.

---

## 🟢 MILESTONE 10 — HOME SCREEN (ANDROID & iOS)
- [x] Build Android Compose `HomeScreen` & `HomeViewModel` (Continue Reading, Recommendations Carousel, Empty States).
- [x] Build iOS SwiftUI `HomeView`.

---

## 🟢 MILESTONE 11 — SEARCH SCREEN (ANDROID & iOS)
- [x] Build Android Compose `SearchScreen` with 400ms debounced input and paginated results.
- [x] Build iOS SwiftUI `SearchView` foundation.

---

## 🟢 MILESTONE 12 — BOOK DETAILS SCREEN (SHOWCASE SCREEN)
- [x] Build Android Compose `BookDetailsScreen` & `BookDetailsViewModel` (Hero cover, metadata, reading status dropdown, progress slider, notes/quotes actions).
- [x] Implement `GetBookDetailsUseCase`.
- [x] Wire Navigation route `details/{bookId}`.

---

## 🟢 MILESTONE 13 — LIBRARY SCREEN
- [x] Build Android Compose `LibraryScreen` & `LibraryViewModel` with status filtering.

---

## 🟢 MILESTONE 14 — COLLECTIONS SCREEN & DETAILS
- [x] Build Android Compose `CollectionsScreen` & `CollectionsViewModel`.
- [x] Implement `CollectionRepository`, `GetCollectionsUseCase`, and `CreateCollectionUseCase`.
- [x] Wire Navigation route `collections`.
- [x] Add `CreateCollectionUseCaseTest` in `commonTest`.

---

## 🟢 MILESTONE 15 — NOTES & QUOTES SCREENS
- [x] Implement `addNote`, `deleteNote`, `addQuote`, and `deleteQuote` in `BookRepositoryImpl` & `BookDetailsViewModel`.

---

## 🟢 MILESTONE 16 — READING TRACKING & SESSION LOGGER
- [x] Implement `LogReadingSessionUseCase` with validation for duration and page ranges.
- [x] Implement reading streak calculation algorithm in `StatsRepositoryImpl`.
- [x] Write `LogReadingSessionUseCaseTest` in `commonTest`.

---

## 🟢 MILESTONE 17 — STATISTICS & GOAL SCREEN
- [x] Build Android Compose `StatsScreen` & `StatsViewModel` featuring streak cards, total pages read, total reading time, and 2026 goal progress.
- [x] Implement `GetReadingGoalUseCase` and `SetReadingGoalUseCase`.
- [x] Wire Navigation route `stats`.

---

## 🟢 MILESTONE 18 — RECOMMENDATION ENGINE INTEGRATION
- [x] Implement `GetRecommendationsUseCase` matching subject/genre overlaps on-device.
- [x] Connect `GetRecommendationsUseCase` to `HomeViewModel` and render "Recommended for You" carousel in `HomeScreen.kt`.
- [x] Write `GetRecommendationsUseCaseTest` unit test in `commonTest`.

---

## 🟢 MILESTONE 19 — VISUAL POLISH, ANIMATIONS & MICRO-INTERACTIONS
- [x] Audit Dark Theme colors, paper-inspired light theme, typography tokens, and smooth state animations.

---

## 🟢 MILESTONE 20 — COMPREHENSIVE TESTING & EDGE CASE AUDIT
- [x] Execute full suite `./gradlew test` across `:shared` and `:app` modules (100% passing).

---

## 🟢 MILESTONE 21 — RELEASE PREPARATION & PACKAGING
- [x] Create R8 / Proguard keep rules (`app/proguard-rules.pro`).
- [x] Verify release compilation (`./gradlew :app:assembleRelease`).

---

## 🟢 MILESTONE 22 — PORTFOLIO PRESENTATION & SHOWCASE
- [x] Finalize `README.md`, `CHANGELOG.md`, `AGENTS.md`, and complete project architecture documentation suite.
