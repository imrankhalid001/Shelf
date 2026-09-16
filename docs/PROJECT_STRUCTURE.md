# PROJECT_STRUCTURE.md — Detailed Directory & Package Map

## 1. Top-Level Repository Directory Map

```
Shelf/
├── .github/                   # GitHub Actions CI/CD workflows
├── app/                       # Android Application Module (Jetpack Compose UI)
├── iosApp/                    # iOS Application Module (SwiftUI UI Xcode project)
├── shared/                    # Shared Kotlin Multiplatform Core Module
│   ├── build.gradle.kts
│   └── src/
│       ├── commonMain/        # 100% Shared Kotlin Code
│       ├── commonTest/        # Shared Unit Tests
│       ├── androidMain/       # Android Platform Specific Drivers & Implementations
│       └── iosMain/           # iOS Platform Specific Drivers & Implementations
├── docs/                      # Complete Product & Engineering Architecture Specs
├── gradle/                    # Gradle Wrapper and Version Catalog (libs.versions.toml)
├── .gitignore
├── AGENTS.md                  # Mandatory AI Agent Guidelines
├── CHANGELOG.md               # Application Release Changelog
├── CONTRIBUTING.md            # Guidelines for Open-Source Contributors
├── LICENSE                    # MIT License
├── README.md                  # Main GitHub Portfolio Readme
└── SECURITY.md                # Security & Data Privacy Policy
```

---

## 2. Shared KMP Module Structure (`shared/src/commonMain/kotlin/com/shelf/`)

```
com.shelf/
├── core/                      # Cross-cutting Shared Core Utilities
│   ├── database/              # Room Database Provider & Drivers
│   ├── dispatcher/            # DispatcherProvider abstraction for Coroutines
│   ├── error/                 # AppError domain error hierarchy
│   ├── logger/                # KMP Logger abstraction
│   ├── network/               # Ktor Client Builder & HTTP Engine setup
│   ├── result/                # AppResult<T> functional wrapper
│   └── utils/                 # DateUtils, StringUtils, StatsCalculator
├── data/                      # Shared Data Layer
│   ├── local/
│   │   ├── dao/               # Room DAOs (BookDao, ReadingProgressDao, etc.)
│   │   ├── entity/            # Room Entity Data Classes (BookEntity, NoteEntity, etc.)
│   │   └── ShelfDatabase.kt   # RoomDatabase Abstract Definition
│   ├── remote/
│   │   ├── api/               # Ktor OpenLibraryApi interface & implementation
│   │   └── dto/               # Serialized DTO data classes (BookDto, AuthorDto, etc.)
│   ├── mapper/                # DTO ↔ Entity ↔ Domain Bidirectional Mappers
│   └── repository/            # Repository Implementations (BookRepositoryImpl, etc.)
└── domain/                    # Shared Domain Layer (Pure Business Logic)
    ├── model/                 # Pure Kotlin Domain Entities (Book, Author, Session, Goal)
    ├── repository/            # Repository Interfaces (BookRepository, StatsRepository)
    └── usecase/               # Single-responsibility Use Cases (SearchBooksUseCase, etc.)
```

---

## 3. Android Application Structure (`app/src/main/java/com/shelf/personal/book/`)

```
com.shelf.personal.book/
├── MainActivity.kt            # Main Single Activity
├── ShelfApplication.kt       # Application Subclass (Koin initialization)
├── di/                        # Android DI Modules (ViewModel & Screen wiring)
├── navigation/                # Jetpack Compose Navigation Graph & Destinations
├── ui/
│   ├── components/            # Reusable Shelf Compose UI Components
│   │   ├── ShelfBookCard.kt
│   │   ├── ShelfTopBar.kt
│   │   ├── ShelfProgressBar.kt
│   │   └── ShelfEmptyState.kt
│   ├── screens/               # Feature Screens
│   │   ├── home/              # HomeScreen & HomeViewModel
│   │   ├── search/            # SearchScreen & SearchViewModel
│   │   ├── details/           # BookDetailsScreen & BookDetailsViewModel
│   │   ├── library/           # LibraryScreen & LibraryViewModel
│   │   ├── collections/       # CollectionsScreen & CollectionsViewModel
│   │   └── stats/             # StatsScreen & StatsViewModel
│   └── theme/                 # Design System Tokens, Theme, Colors, Type, Shape
```

---

## 4. iOS Application Structure (`iosApp/iosApp/`)

```
iosApp/
├── App/
│   ├── iOSApp.swift           # Main Application Entry Point
│   └── AppDelegate.swift      # Lifecycle Delegate
├── DependencyInjection/       # Swift KMP Service Locator / DI Bridge
├── Navigation/                # SwiftUI NavigationStack Router
├── UI/
│   ├── Components/            # Reusable SwiftUI Components (BookCardView, TopBarView)
│   ├── Views/                 # Feature Views (HomeView, SearchView, BookDetailsView)
│   └── StateHolders/          # Swift @Observable State Holders bridging KMP UseCases
└── Resources/                 # Assets.xcassets, Colors, Localizable.strings
```
