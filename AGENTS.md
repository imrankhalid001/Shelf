# AGENTS.md — AI Coding Agent Guidelines for Shelf

> **CRITICAL MANDATE**: Any AI Coding Agent (Gemini, Claude, Cursor, Copilot, etc.) working on this repository **MUST** read and strictly follow this document before reading or modifying any files in the project.

---

## 1. Project Purpose & Positioning

**Shelf — Personal Book Intelligence** is a premium, modern, offline-first personal book management and reading intelligence application built with **Kotlin Multiplatform (KMP)**.

- **Primary Goal**: Showcase modern production-grade KMP engineering where business logic, data persistence, network access, and domain algorithms are shared 100% in Kotlin, while Android and iOS retain **100% native platform UI** implementations.
- **Android UI**: Native Jetpack Compose.
- **iOS UI**: Native SwiftUI.
- **Strict Requirement**: **DO NOT** use Compose Multiplatform for iOS UI.

---

## 2. Core Architectural Philosophy

### 2.1 Layered Clean Architecture
The project strictly follows Clean Architecture principles:

```
Presentation (UI State, ViewModels/StateHolders)
    ↓
Domain (Entities/Models, UseCases, Repository Contracts)
    ↓
Data (Repository Implementations, Local Room DB, Ktor Remote API, DTOs, Mappers)
```

- **Domain Layer**: Pure Kotlin standard library + Coroutines/Flow. **Zero UI dependencies** (no Android framework, no Compose, no SwiftUI, no UIKit).
- **Data Layer**: Room KMP for local DB, Ktor Client for HTTP, kotlinx.serialization for JSON.
- **Presentation Layer**:
  - Android (`:app` or `androidMain` / presentation): Jetpack Compose + `androidx.lifecycle.ViewModel` + `StateFlow`.
  - iOS (`iosApp`): SwiftUI + Swift `Observable` / `ObservableObject` bridging to shared Kotlin UseCases/State.

---

## 3. Module Structure

```
Shelf/
├── app/                       # Native Android Application (Jetpack Compose UI & Android DI)
├── iosApp/                    # Native iOS Application (SwiftUI UI & iOS lifecycle)
├── shared/                    # KMP Shared Module
│   └── src/
│       ├── commonMain/        # Shared Domain, Data, Core, Repository, UseCases
│       ├── commonTest/        # Shared Unit Tests for Repositories, DB, UseCases, Utils
│       ├── androidMain/       # Android specific expect/actual (e.g., Room database builder)
│       └── iosMain/           # iOS specific expect/actual (e.g., Room database builder)
├── docs/                      # Technical Documentation & Specifications
├── AGENTS.md                  # Guidelines for AI Coding Agents
└── README.md                  # Main Repository Overview
```

---

## 4. Strict Rules for AI Agents

### 4.1 Things Agents Must NEVER Do

1. **NEVER** introduce Compose Multiplatform for iOS UI. iOS MUST remain 100% native SwiftUI.
2. **NEVER** duplicate business logic in Android or iOS platform modules. All business logic, caching rules, statistics calculations, validation, and data mapping MUST reside in `shared/src/commonMain`.
3. **NEVER** allow UI code (Composables or SwiftUI Views) to call the API or Room DB directly. Always flow through `ViewModel` → `UseCase` → `Repository`.
4. **NEVER** leak Data Transfer Objects (DTOs) or Room Entities into the Domain or Presentation layers. Always map DTOs/Entities to Domain Models via explicit mappers.
5. **NEVER** use `run_shell_command` with destructive file operations (`sed`, `awk`, shell redirection `>`) or manual reads (`cat`, `grep`) when built-in IDE tools (`read_file`, `replace_file_content`, `multi_replace_file_content`, `find_declaration`, `find_usages`, `find_files`, `grep`) are available.
6. **NEVER** break the offline-first principle. UI must observe local Room database state via Kotlin `Flow`, not wait directly on raw remote network calls.
7. **NEVER** commit unverified code that fails Gradle build (`./gradlew build`) or unit tests (`./gradlew test`).
8. **NEVER** hardcode raw magic numbers or raw colors inside Composables or SwiftUI Views. Always use the central `ShelfTheme` / Design System tokens.

---

## 5. Coding & Naming Standards

### 5.1 Kotlin (Shared & Android)
- **Formatting**: Strict Kotlin official code style.
- **Immutability**: Prefer `val` over `var`, immutable data classes, `List` over `MutableList`.
- **Coroutines & Flow**: Use structured concurrency. Pass `CoroutineDispatcher` via abstraction for testability. Never use `GlobalScope`.
- **Naming Conventions**:
  - Domain Models: `Book`, `Author`, `ReadingProgress`, `Collection`
  - DTOs: `BookDto`, `WorkDetailsDto`, `AuthorDto`
  - Room Entities: `BookEntity`, `AuthorEntity`, `NoteEntity`
  - Use Cases: `SearchBooksUseCase`, `UpdateReadingProgressUseCase` (verb + noun + UseCase)
  - Repositories: `BookRepository` (contract), `BookRepositoryImpl` (implementation)
  - Composables: `ShelfBookCard`, `ShelfTopBar`, `HomeScreen` (PascalCase)
  - ViewModels: `HomeViewModel`, `BookDetailsViewModel`

### 5.2 Swift (iOS UI)
- **Formatting**: Swift API Design Guidelines.
- **Observation**: Swift `@Observable` macro (iOS 17+) or `ObservableObject` with `@Published`.
- **Naming Conventions**:
  - Views: `BookCardView`, `BookDetailsView`, `HomeView`
  - ViewModels/StateHolders: `HomeStateHolder`, `BookDetailsStateHolder`

---

## 6. How to Execute Common Tasks

### 6.1 Adding a New Feature
1. Define/Update Domain Model in `shared/src/commonMain/kotlin/com/shelf/domain/model/`.
2. Define Repository contract in `shared/src/commonMain/kotlin/com/shelf/domain/repository/`.
3. Implement Use Case(s) in `shared/src/commonMain/kotlin/com/shelf/domain/usecase/`.
4. Add unit tests for Use Case in `shared/src/commonTest/kotlin/com/shelf/domain/usecase/`.
5. Wire Repository implementation in `shared/src/commonMain/kotlin/com/shelf/data/repository/`.
6. Implement Android ViewModel & Compose UI in `app/src/main/java/com/shelf/personal/book/`.
7. Implement iOS StateHolder & SwiftUI UI in `iosApp/iosApp/`.

### 6.2 Adding a New Database Entity
1. Define `@Entity` class in `shared/src/commonMain/kotlin/com/shelf/data/local/entity/`.
2. Define DAO interface in `shared/src/commonMain/kotlin/com/shelf/data/local/dao/`.
3. Add entity & DAO to `ShelfDatabase` in `shared/src/commonMain/kotlin/com/shelf/data/local/ShelfDatabase.kt`.
4. Bump database version and provide migration if changing schema.
5. Create Entity ↔ Domain mapper functions in `shared/src/commonMain/kotlin/com/shelf/data/mapper/`.
6. Add DAO integration unit tests in `commonTest`.

### 6.3 Adding a New API Endpoint
1. Define DTO data classes with `@Serializable` in `shared/src/commonMain/kotlin/com/shelf/data/remote/dto/`.
2. Add API method to `OpenLibraryApi` in `shared/src/commonMain/kotlin/com/shelf/data/remote/api/`.
3. Create DTO ↔ Domain mapper functions in `shared/src/commonMain/kotlin/com/shelf/data/mapper/`.
4. Update Repository implementation to fetch from network and sync into Room.
5. Add API deserialization tests in `commonTest`.

---

## 7. Testing Requirements

- **Domain Use Cases**: 100% test coverage target for business logic, streak calculation, statistics algorithms, and progress percentage calculations.
- **Repositories**: Test offline fallback, network sync, Room observation, and error mapping.
- **Database DAOs**: Test CRUD operations, custom queries, foreign key cascading, and flow emissions.
- **Run Command**: `./gradlew test` must pass before marking any task as complete.

---

## 8. Git & Commit Guidelines

- Work cleanly in milestone-driven commits.
- Format commit messages as `type(scope): message`.
- Maintain `CHANGELOG.md` when introducing user-facing features or major architectural shifts.
