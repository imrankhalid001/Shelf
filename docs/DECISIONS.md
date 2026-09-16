# DECISIONS.md — Architectural Decision Records (ADRs)

This document records key architectural decisions made during the design and development of **Shelf — Personal Book Intelligence**.

---

## ADR 001: Hybrid KMP Architecture (Shared Logic + Native Compose/SwiftUI UIs)

- **Status**: Accepted
- **Context**: The project goal is to create a showcase portfolio app demonstrating Kotlin Multiplatform capability without compromising native platform user experiences.
- **Decision**: Share 100% of Domain models, Use Cases, Repository implementations, Room database, Ktor networking, DTOs, mappers, and unit tests in Kotlin (`commonMain`). Keep UI implementations 100% native using **Jetpack Compose on Android** and **SwiftUI on iOS**. Do NOT use Compose Multiplatform for iOS.
- **Consequences**:
  - *Positive*: Best possible native performance, native accessibility, native platform look and feel, excellent demonstration of KMP's core strength.
  - *Negative*: UI components must be written twice (once in Compose, once in SwiftUI).

---

## ADR 002: Room KMP over SQLDelight for Local Storage

- **Status**: Accepted
- **Context**: Shelf requires a robust local database for its offline-first architecture.
- **Decision**: Choose **Room KMP** (`androidx.room`) as the local database ORM.
- **Consequences**:
  - *Positive*: Official Android Jetpack KMP support, familiar `@Entity`, `@Dao`, `@Query` annotations, built-in Flow emissions, automatic compile-time query verification.
  - *Negative*: Requires experimental Room KMP Gradle plugin configuration.

---

## ADR 003: Open Library API for Book Discovery

- **Status**: Accepted
- **Context**: Shelf needs remote book search, metadata, and cover imagery without incurring paid API costs or requiring complex API key rotation.
- **Decision**: Use the public, free **Open Library REST API** (`https://openlibrary.org`).
- **Consequences**:
  - *Positive*: Completely free, open-source friendly, no API key required, extensive catalog of millions of books and authors.
  - *Negative*: Network response times can occasionally be slow; debounced search (400ms) and local Room caching are necessary.

---

## ADR 004: Offline-First Room Caching Pipeline

- **Status**: Accepted
- **Context**: The app must function seamlessly without internet access.
- **Decision**: Local Room database is the single source of truth. UI screens observe Room queries via Kotlin `Flow`. Remote network calls update Room entities asynchronously.
- **Consequences**:
  - *Positive*: Instant UI rendering from local database, 100% offline availability for library and reading stats.
  - *Negative*: Requires explicit mapping and upsert logic in repository implementations.

---

## ADR 005: Functional `AppResult<T>` and `AppError` Hierarchy

- **Status**: Accepted
- **Context**: Exception handling across KMP boundaries can lead to unhandled crashes if exceptions bubble up across Kotlin/Swift interop.
- **Decision**: Wrap all asynchronous operations in a sealed `AppResult<T>` functional wrapper with explicit `AppError` domain types.
- **Consequences**:
  - *Positive*: Type-safe error handling, smooth interop with Swift pattern matching, zero uncaught app crashes.
  - *Negative*: Slightly more boilerplate code wrapping return values.
