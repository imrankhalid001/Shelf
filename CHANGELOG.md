# Changelog

All notable changes to **Shelf — Personal Book Intelligence** will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.0.0] - 2026-03-30

### Added
- **100% Shared KMP Business Core**:
  - Clean Architecture layers (Domain, Data, Core) written 100% in Kotlin Multiplatform (`commonMain`).
  - Room KMP local database with 13 normalized entities (`BookEntity`, `AuthorEntity`, `ReadingProgressEntity`, `ReadingSessionEntity`, `NoteEntity`, `QuoteEntity`, `CollectionEntity`, `ReadingGoalEntity`, etc.).
  - Open Library REST API integration via Ktor Client 3.x with multiplatform JSON serialization.
  - Functional `AppResult<T>` and `AppError` sealed hierarchy.
  - On-device local recommendation engine based on subject/genre overlaps.
  - Real-time reading streak calculator and reading session logger.
  - Yearly reading goal tracker.
- **Native Platform UIs**:
  - **Android**: Native Jetpack Compose UI with Material 3 paper-inspired theme, bottom navigation, and Koin DI.
  - **iOS**: Native SwiftUI foundation (`HomeView`, `EmptyStateView`, `ProgressBarView`).
- **Automated Testing**:
  - 24 unit tests in `commonTest` covering Use Cases, Repository caching, API deserialization, Date/String utilities, and Room DAOs.
