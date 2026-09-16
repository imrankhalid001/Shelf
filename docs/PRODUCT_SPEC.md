# PRODUCT_SPEC.md — Shelf Product Specification

## 1. Product Identification & Positioning

- **Product Name**: Shelf
- **Subtitle**: Personal Book Intelligence
- **Tagline**: *"Your personal reading space."*
- **Positioning**: A premium, offline-first personal book management and reading intelligence application for mobile platforms.
- **Form Factor**: Native Mobile Applications (Android & iOS).
- **Target Audience**: Book lovers, avid readers, researchers, productivity enthusiasts, students, and personal knowledge managers who desire a beautifully crafted personal library experience without cloud dependency or mandatory subscription models.

---

## 2. Product Vision & Core Philosophy

Shelf bridges the aesthetic elegance of **Apple Books**, the social/cataloging depth of **Goodreads**, and the structured note-taking capabilities of **Notion** into a modern, unified mobile application.

### Core Philosophy
1. **Shared KMP Core**: All domain rules, data models, room persistence, networking, state mapping, and recommendation logic are shared across platforms in Kotlin.
2. **Native UX Excellence**: Android uses Jetpack Compose; iOS uses SwiftUI. Each UI respects platform-native interaction paradigms, fluid animations, and accessibility standards.
3. **Offline-First Data Sovereignty**: The user owns their data. Local SQLite database (via Room KMP) is the single source of truth.
4. **Privacy First**: Zero user tracking, zero mandatory account creation, zero proprietary backend requirement for core features.

---

## 3. Key User Personas & User Journeys

### Persona A: The Avid Reader (Elena, 28)
- **Goal**: Wants to track 30+ books read per year, maintain daily reading streaks, log progress, and record memorable quotes.
- **Pain Point**: Goodreads app is cluttered and outdated; web connections at coffee shops/trains fail.
- **User Journey**: Opens Shelf → Views Home dashboard → Taps active book "Atomic Habits" → Logs page 180 → Starts a 25-minute reading session timer → Finished session → Streak increments to 14 days → Goal progress updates automatically.

### Persona B: The Researcher / Non-Fiction Collector (David, 35)
- **Goal**: Organizes books into specialized topic collections ("Productivity", "System Architecture") and records detailed notes with page references.
- **Pain Point**: Wants instant retrieval of quotes and notes offline without needing an active internet connection.
- **User Journey**: Searches Open Library for a classic work → Saves to local library → Creates collection "Distributed Systems" → Adds page notes → Uses search/filter inside library to find notes instantly.

---

## 4. Feature Requirements Matrix

| Feature Area | Description | Priority (MVP vs Post-MVP) |
| ------------ | ----------- | -------------------------- |
| **Book Discovery & Search** | Query Open Library API by title, author, subject, ISBN; debounced search; cached search history. | **MVP** |
| **Personal Library** | Organize books into standard statuses (`Want to Read`, `Reading`, `Finished`, `Paused`, `Dropped`). | **MVP** |
| **Reading Progress** | Update current page / total pages, percentage complete, visual progress bar, mark finished. | **MVP** |
| **Reading Sessions** | Timer-based or manual reading session logger with page count and time duration tracking. | **MVP** |
| **Reading Streak** | Track current and longest daily reading streak based on logged sessions. | **MVP** |
| **Yearly Reading Goals** | Set target number of books for the current calendar year; visualize completion status. | **MVP** |
| **Custom Collections** | Create, edit, delete collections; add and remove books to/from collections with custom covers. | **MVP** |
| **Notes & Quotes** | Attach page-referenced notes and formatted quote cards to specific books. | **MVP** |
| **Reading Intelligence / Stats** | Charts and metrics for pages read, books completed, top genres, top authors, monthly distribution. | **MVP** |
| **Local Recommendations** | Genre and subject-based deterministic recommendation engine running locally inside Domain layer. | **MVP** |
| **Recently Viewed** | Track recently viewed books and authors for quick navigation. | **MVP** |
| **Dark Theme** | Full native dark mode support respecting semantic color design tokens. | **MVP** |
| **Barcode / ISBN Scanner** | Camera-based ISBN barcode scanner to quickly add physical books. | Post-MVP |
| **Goodreads / CSV Import/Export** | Import reading history from Goodreads CSV export; backup local DB to JSON/CSV. | Post-MVP |
| **Home Screen Widgets** | Android App Widgets & iOS WidgetKit support for streak and goal tracking. | Post-MVP |

---

## 5. Non-Functional Requirements

1. **Performance**: 
   - App cold startup time < 1.2s on modern devices.
   - Smooth 60fps/120fps scrolling in Jetpack Compose LazyColumns and SwiftUI ScrollViews.
   - Room DB queries off main thread using Coroutines `Dispatchers.IO` / Flow.
2. **Offline Availability**: 
   - 100% of Library, Progress, Goals, Collections, Notes, Quotes, and Stats features work without network access.
3. **Storage Overhead**: 
   - App binary size kept minimal (< 25MB on Android, < 30MB on iOS).
   - Efficient cover image caching with disk LRU cache size limits.
4. **Accessibility**: 
   - Minimum touch targets (48dp on Android, 44pt on iOS).
   - WCAG AAA contrast for text.
   - Support for dynamic font scaling (Font Scale / Dynamic Type).
