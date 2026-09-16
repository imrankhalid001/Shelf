# Contributing to Shelf

Thank you for your interest in contributing to **Shelf — Personal Book Intelligence**!

Shelf is a showcase Kotlin Multiplatform (KMP) open-source portfolio project demonstrating clean architecture with **Shared Kotlin Business Logic** and **Native UIs** (Jetpack Compose on Android, SwiftUI on iOS).

---

## 1. Code of Conduct

We expect all contributors to maintain a professional, respectful, and inclusive environment.

---

## 2. Core Architectural Philosophy

Before contributing code, please review our core architectural rules:

1. **Shared Logic in Kotlin**: Domain models, use cases, repository contracts and implementations, Room database, Ktor network client, DTOs, entities, logic utilities, and business unit tests MUST live in `shared/src/commonMain` and `shared/src/commonTest`.
2. **Native UI Only**: 
   - Android UI MUST be built using **Jetpack Compose**.
   - iOS UI MUST be built using **SwiftUI**.
   - Do NOT introduce Compose Multiplatform for iOS UI.
3. **Offline-First**: Local Room database is always the source of truth for user-owned data. Network calls update Room; UI observes Room.
4. **Clean Architecture**: Presentation → Domain → Data. Domain has zero dependencies on UI frameworks.

---

## 3. Getting Started

### Prerequisites

- **JDK 17** or higher
- **Android Studio** (2024.1+ or modern Ladybug/Meerkat/Baklava builds)
- **Xcode 15+** (for building `iosApp` on macOS)
- **CocoaPods** or Swift Package Manager (as configured in `iosApp`)

### Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/shelf.git
   cd shelf
   ```
2. Open the project root in Android Studio.
3. Sync Gradle and build the project:
   ```bash
   ./gradlew build
   ```
4. Run Android app:
   ```bash
   ./gradlew :app:assembleDebug
   ```
5. Run unit tests:
   ```bash
   ./gradlew test
   ```

---

## 4. Commit Convention

We follow conventional commit format:

- `feat:` A new feature
- `fix:` A bug fix
- `refactor:` Code change that neither fixes a bug nor adds a feature
- `docs:` Documentation changes
- `test:` Adding or updating tests
- `build:` Changes to build scripts or dependencies
- `chore:` Maintenance tasks

*Example*: `feat(domain): add UpdateReadingProgressUseCase and tests`

---

## 5. Pull Request Guidelines

1. Ensure all tests pass: `./gradlew test`
2. Ensure documentation is updated if adding or modifying features.
3. Keep PRs focused on a single issue or milestone item.
4. Reference the corresponding issue or milestone in your PR description.

---

## 6. AI Agent Guidelines

If you are using AI agents (such as Gemini in Android Studio, Claude, Cursor, GitHub Copilot), you **must** read and adhere strictly to [`AGENTS.md`](./AGENTS.md).
