# ROADMAP.md — Future Product & Feature Roadmap

## 1. Scope Boundaries

**Shelf** prioritizes a tight, highly polished MVP focused on core book intelligence, reading tracking, offline library management, and reading goals. Advanced features are scheduled for Post-MVP releases.

---

## 2. Release Roadmap Timeline

```
     v0.1.0-alpha                 v1.0.0 (MVP Release)              v1.5.0 (Post-MVP)
───────────┼───────────────────────────────┼────────────────────────────────┼───────────►
 Architecture & Specs              Core Offline Library,             Barcode Scanner,
 KMP Infrastructure,               Search, Goal Tracker,             CSV Import/Export,
 Room DB & Open Library API         Stats, Compose & SwiftUI UIs     Widgets & Cloud Sync
```

---

## 3. Detailed Feature Breakdown

### Phase 1: MVP Core (Current Target)
- [x] Complete architecture specification, design system, and documentation.
- [x] Shared KMP module with Room KMP, Ktor 3.x, and Clean Architecture Use Cases.
- [x] Native Android Jetpack Compose UI with Material 3 design tokens.
- [x] Native iOS SwiftUI UI with Apple Human Interface Guidelines styling.
- [x] Open Library search, book details, author info, and cover caching.
- [x] Library status management (`Want to Read`, `Reading`, `Finished`, `Paused`, `Dropped`).
- [x] Page reading progress tracking and reading session logger.
- [x] Yearly reading goal target setting and progress visualization.
- [x] Custom book collections and personal notes & quotes.
- [x] Local reading streak calculator and reading intelligence statistics.
- [x] On-device privacy-first recommendation engine.

### Phase 2: Post-MVP Expansion (v1.5.0+)
- [ ] **ISBN Barcode Scanner**: Camera-based barcode scanner (using CameraX on Android, DataScannerViewController on iOS) to instantly scan physical book covers/ISBN barcodes.
- [ ] **Goodreads & CSV Import/Export**: Import reading history and ratings from Goodreads CSV export files; export local Shelf database to JSON/CSV backup.
- [ ] **Home Screen Widgets**: Android App Widgets & iOS WidgetKit widgets displaying active reading progress, daily reading streak, and yearly goal completion.
- [ ] **Apple Books & Kindle Integration**: Deep linking into e-reader apps for active books.
- [ ] **Audiobook Time Tracking**: Support listening time logging (hours/minutes) in addition to page counts.
- [ ] **Encrypted Cloud Sync (Optional)**: Optional end-to-end encrypted iCloud / Google Drive database sync for multi-device power users.
