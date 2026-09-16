# Shelf — Personal Book Intelligence

> *"Your personal reading space."*

[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/docs/multiplatform.html)
[![Android](https://img.shields.io/badge/Android-Jetpack%20Compose-3DDC84?logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![iOS](https://img.shields.io/badge/iOS-SwiftUI-000000?logo=swift&logoColor=white)](https://developer.apple.com/xcode/swiftui/)
[![Room KMP](https://img.shields.io/badge/Database-Room%20KMP-4285F4?logo=sqlite&logoColor=white)](https://developer.android.com/kotlin/multiplatform/room)
[![Ktor](https://img.shields.io/badge/Network-Ktor%203.x-087CFA?logo=ktor&logoColor=white)](https://ktor.io/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**Shelf** is a premium, modern, offline-first personal book management and reading intelligence application built with **Kotlin Multiplatform (KMP)**. 

The core philosophy of Shelf is to demonstrate how Kotlin Multiplatform can seamlessly unify domain logic, local data storage, network synchronization, and analytics across platforms, while allowing **Android and iOS to maintain 100% native UI implementations** using **Jetpack Compose** and **SwiftUI**, respectively.

---

## 📸 Screenshots & Preview

| Android (Jetpack Compose) | iOS (SwiftUI) |
| :-----------------------: | :-----------: |
| *Home & Reading Progress* | *Home & Reading Progress* |
| ![Android Home](docs/assets/android_home_placeholder.png) | ![iOS Home](docs/assets/ios_home_placeholder.png) |
| *Book Details & Notes* | *Book Details & Notes* |
| ![Android Details](docs/assets/android_details_placeholder.png) | ![iOS Details](docs/assets/ios_details_placeholder.png) |

---

## ✨ Features

- 🔍 **Book Discovery & Search**: Query millions of books and authors via the public Open Library API with debounced search and cached history.
- 📚 **Personal Library Management**: Track books across customized reading statuses (`Want to Read`, `Reading`, `Finished`, `Paused`, `Dropped`).
- ⏱️ **Reading Sessions & Progress Tracking**: Log active reading sessions, monitor current page, calculate reading speeds, and maintain reading streaks.
- 🎯 **Yearly Reading Goals**: Set annual target book counts, monitor percentage completion, and view monthly progress metrics.
- 🏷️ **Custom Collections**: Create curated shelves/collections (e.g., *Productivity*, *Fiction*, *2026 Tech Stack*).
- 📝 **Notes & Quotes**: Save page-specific notes and memorable quotes for every book.
- 📊 **Reading Intelligence & Analytics**: Visualize reading time, pages read per month, favorite authors, and top genres with lightweight native visualizations.
- 💡 **Local Recommendation Engine**: Privacy-preserving on-device book suggestions based on favorite genres, subjects, and reading history.
- ✈️ **100% Offline-First**: Your personal library, progress, notes, quotes, and goals remain accessible anywhere without an internet connection.

---

## 🏛️ Architecture Overview

Shelf follows **Clean Architecture** and the **Repository Pattern**.

```mermaid
graph TD
    subgraph "Native UI Layer"
        AC[Android - Jetpack Compose]
        IS[iOS - SwiftUI]
    end

    subgraph "State / Presentation Layer"
        AVM[Android ViewModels / StateFlow]
        ISH[iOS StateHolders / Swift Observable]
    end

    subgraph "Shared Kotlin Domain Layer (commonMain)"
        UC[Use Cases]
        DM[Domain Models]
        RC[Repository Contracts]
    end

    subgraph "Shared Kotlin Data Layer (commonMain)"
        RI[Repository Implementations]
        RMDB[(Room KMP Database)]
        KTOR[Ktor HTTP Client]
        OLAPI[Open Library Remote API]
    end

    AC --> AVM
    IS --> ISH
    AVM --> UC
    ISH --> UC
    UC --> DM
    UC --> RC
    RC <|.. RI
    RI --> RMDB
    RI --> KTOR
    KTOR --> OLAPI
```

### Why Native UI + Shared KMP?
Rather than forcing a unified UI renderer across platforms, Shelf showcases the **hybrid KMP pattern**:
1. **Shared Logic**: Domain algorithms, database persistence, state transitions, API mapping, and unit tests are written once in Kotlin (`commonMain`).
2. **Native UX**: Android leverages full Material 3 Compose ecosystems, while iOS leverages native SwiftUI gestures, navigation, and typography.

---

## 🛠️ Tech Stack

### Shared Layer (`shared`)
- **Language**: Kotlin 2.1+ (Kotlin Multiplatform)
- **Database**: Room KMP (SQLite local persistence)
- **Networking**: Ktor Client 3.x (HTTP engine)
- **Serialization**: `kotlinx.serialization` (JSON parsing)
- **Concurrency**: Kotlin Coroutines & `Flow`
- **DateTime**: `kotlinx-datetime`
- **Testing**: `kotlin.test`, `kotlinx-coroutines-test`, Mockative / Turbine

### Android App (`app`)
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: `androidx.lifecycle.ViewModel` + `StateFlow`
- **Navigation**: Jetpack Compose Navigation
- **Image Loading**: Coil 3.x (KMP compatible)
- **Dependency Injection**: Koin / Hilt

### iOS App (`iosApp`)
- **UI Framework**: SwiftUI (iOS 17+ focus)
- **Architecture**: Swift `@Observable` / `ObservableObject` bridging Kotlin `Flow` & UseCases
- **Navigation**: `NavigationStack`
- **Image Loading**: AsyncImage / Nuke

---

## 📲 Offline-First Strategy

Shelf treats the local **Room KMP database as the single source of truth** for all user-owned data:

```
Network Call → Update Room DB → Room emits Flow → UI updates automatically
```

1. **Local Reads**: UI screens observe Room database queries directly via `Flow`.
2. **Background Sync**: Network responses populate or update local entities.
3. **Resilience**: If offline, search operates on local cache; all library, tracking, note-taking, and stats features remain 100% operational.

---

## 📂 Project Structure

```
Shelf/
├── app/                       # Android App (Jetpack Compose UI)
├── iosApp/                    # iOS App (SwiftUI UI)
├── shared/                    # KMP Shared Module
│   └── src/
│       ├── commonMain/
│       │   ├── kotlin/com/shelf/
│       │   │   ├── core/      # Database, Network, Result, Logger, Utils
│       │   │   ├── data/      # Entities, DTOs, DAOs, Repositories, Mappers
│       │   │   └── domain/    # Models, UseCases, Repository Contracts
│       │   └── commonTest/    # Comprehensive Shared Unit Tests
│       ├── androidMain/       # Android database drivers & platform hooks
│       └── iosMain/           # iOS database drivers & platform hooks
├── docs/                      # Architectural Specs & Documentation
│   ├── PRODUCT_SPEC.md
│   ├── ARCHITECTURE.md
│   ├── TECH_STACK.md
│   ├── DATABASE.md
│   ├── API.md
│   ├── DESIGN_SYSTEM.md
│   ├── UI_UX.md
│   ├── CODING_STANDARDS.md
│   ├── PROJECT_STRUCTURE.md
│   ├── TESTING.md
│   ├── ERROR_HANDLING.md
│   ├── OFFLINE_FIRST.md
│   ├── SECURITY_PRIVACY.md
│   ├── GIT_WORKFLOW.md
│   ├── MILESTONES.md
│   ├── DECISIONS.md
│   └── ROADMAP.md
├── AGENTS.md                  # Guidelines for AI Coding Agents
├── CONTRIBUTING.md            # Guidelines for open-source contributors
└── README.md                  # Project overview
```

---

## 🚀 Getting Started

### Prerequisites
- **JDK 17+**
- **Android Studio** (2024.1+)
- **Xcode 15+** (macOS required for iOS build)

### Building the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/shelf.git
   cd shelf
   ```

2. Build the shared module & run Android tests:
   ```bash
   ./gradlew test
   ```

3. Run Android Application:
   Open project in Android Studio and run `:app` on an emulator or physical device.

4. Run iOS Application:
   Open `iosApp/iosApp.xcodeproj` in Xcode and press **Cmd + R**.

---

## 🗺️ Roadmap & Milestones

Development is tracked through 23 structured milestones (Milestone 0 to Milestone 22). See [`docs/MILESTONES.md`](docs/MILESTONES.md) for full status.

- [x] **Milestone 0**: Product Definition & Spec
- [x] **Milestone 1**: Architecture & Technical Docs
- [x] **Milestone 2**: Design System & Tokens
- [ ] **Milestone 3**: KMP Project Bootstrap & CI
- [ ] **Milestone 4**: Core Shared Infrastructure
- [ ] **Milestone 5**: Room KMP Database & DAOs
- [ ] **Milestone 6**: Open Library API Client
- [ ] **Milestone 7**: Repositories & Domain UseCases
- [ ] **Milestone 8-9**: Android & iOS UI Foundations
- [ ] **Milestone 10-18**: Feature Screens (Home, Search, Details, Library, Stats, etc.)
- [ ] **Milestone 19-22**: Polish, Testing, Release Prep & Portfolio Showcase

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
