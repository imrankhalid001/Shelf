# TECH_STACK.md — Shelf Technology Stack & Dependencies

## 1. Technology Selection Overview

The technology stack for **Shelf** is chosen to maximize code sharing efficiency, preserve native user experience, ensure fast build times, and maintain zero dependency on proprietary third-party servers.

---

## 2. Core Libraries & Version Matrix

| Category | Library | Version | Platform / Module | Justification |
| -------- | ------- | ------- | ----------------- | ------------- |
| **Language** | Kotlin | `2.1.10` | All Modules | Core KMP language. |
| **Language** | Swift | `5.10` / Swift 6 | iOS (`iosApp`) | Native iOS language. |
| **Build Tool** | Gradle | `8.11` | Root / Shared / Android | Official Android & KMP build engine. |
| **Database** | Room KMP | `2.7.0-alpha13`+ | `shared` (commonMain) | Official KMP multiplatform SQLite ORM from Android Jetpack. |
| **SQLite Driver** | `sqlite-bundled` | `2.5.0-alpha13`+ | `shared` | Bundled SQLite driver for cross-platform DB engine. |
| **Networking** | Ktor Client | `3.0.3` | `shared` (commonMain) | Lightweight, non-blocking asynchronous KMP HTTP client. |
| **Network Engine** | Ktor OkHttp / Darwin | `3.0.3` | `androidMain` / `iosMain` | Native underlying engine for Android and iOS. |
| **Serialization** | `kotlinx.serialization` | `1.8.0` | `shared` | Official Kotlin multiplatform JSON serializer. |
| **Concurrency** | `kotlinx-coroutines` | `1.10.1` | `shared` | Asynchronous programming and reactive streams. |
| **Date & Time** | `kotlinx-datetime` | `0.6.1` | `shared` | Official KMP date/time utility library. |
| **Android UI** | Jetpack Compose BOM | `2025.02.00` | `:app` | Modern declarative UI framework for Android. |
| **Android DI** | Koin for Kotlin | `4.0.2` | `:app` / `shared` | Lightweight Dependency Injection for KMP & Compose. |
| **Image Loading** | Coil 3 | `3.1.0` | `:app` (Android) / KMP | KMP-compatible image loading library with disk caching. |
| **iOS UI** | SwiftUI | iOS 17.0+ | `iosApp` | Apple's native declarative UI framework. |
| **Testing** | `kotlin.test` | Included | `commonTest` | Multiplatform unit testing framework. |
| **Turbine** | `app.cash.turbine` | `1.2.0` | `commonTest` | Testing utility for Kotlin Flow. |

---

## 3. Rationale for Dependency Selection

### Why Room KMP instead of SQLDelight?
Android Jetpack Room now natively supports Kotlin Multiplatform. Using Room KMP allows us to leverage familiar `@Entity`, `@Dao`, and `@Query` annotations, compile-time SQL validation, automatic Flow integration, and seamless migrations across both Android and iOS without writing raw `.sq` files.

### Why Ktor Client 3.x?
Ktor is the standard HTTP client for Kotlin Multiplatform. It provides multiplatform JSON serialization plugins, configurable timeout settings, logging plugins, and uses native network stacks (`OkHttp` on Android, `NSURLSession`/`Darwin` on iOS).

### Why Native SwiftUI on iOS instead of Compose Multiplatform?
The primary technical showcase of Shelf is demonstrating clear architectural separation between shared Kotlin data/business logic and native platform UIs. SwiftUI delivers 100% native iOS design language, native fluid gestures, Dynamic Type integration, and native accessibility.
