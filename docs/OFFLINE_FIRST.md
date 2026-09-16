# OFFLINE_FIRST.md — Offline-First Architecture & Caching Strategy

## 1. Core Principle & Architectural Vision

**Shelf** is designed **Offline-First**.

- **Single Source of Truth**: The local Room KMP database is the sole authority for all user-owned state (Library books, Reading Progress, Reading Sessions, Notes, Quotes, Collections, Yearly Goals, and Statistics).
- **Network Role**: The Open Library remote API is strictly an enrichment and discovery service. Remote data is fetched to populate or update local entities; the UI never waits directly on network calls for essential display.

---

## 2. Data Synchronization & Cache Flow Pipeline

```
              ┌──────────────────────────────────────────┐
              │           Native UI Component            │
              └────────────────────┬─────────────────────┘
                                   │ Observes Flow
                                   ▼
              ┌──────────────────────────────────────────┐
              │           Room Local Database            │
              └────────────────────▲─────────────────────┘
                                   │ Upserts Entity
                                   │
              ┌────────────────────┴─────────────────────┐
              │          Repository Layer                │
              └────────────────────▲─────────────────────┘
                                   │ Fetches JSON DTO
                                   │
              ┌────────────────────┴─────────────────────┐
              │        Open Library Remote API           │
              └──────────────────────────────────────────┘
```

---

## 3. Detailed Data Offline Rules

| Data Category | Storage Strategy | Offline Capability | Remote Sync Behavior |
| ------------- | ---------------- | ------------------ | -------------------- |
| **Personal Library & Progress** | Stored in Room `books` & `reading_progress` | **100% Offline** | Created locally instantly; optional background metadata refresh. |
| **Notes & Quotes** | Stored in Room `notes` & `quotes` | **100% Offline** | Never synced remotely; strictly local user data. |
| **Collections** | Stored in Room `collections` & `collection_books` | **100% Offline** | Strictly local user data. |
| **Reading Sessions & Goals** | Stored in Room `reading_sessions` & `reading_goals` | **100% Offline** | Strictly local analytics. |
| **Search Queries** | Cached in Room `search_history` & `books` | **Hybrid** | Search operates on local DB when offline; queries remote API when online. |
| **Book Covers** | Disk LRU cache via Coil 3 (Android) / Native Cache (iOS) | **Cached Offline** | Pre-cached image files displayed offline; default placeholder if missing. |

---

## 4. Conflict Resolution Strategy

- **User Local Edits Dominate**: If a user edits a book's local title, page count, progress, or personal notes, local values are NEVER overwritten by remote API refreshes.
- **Entity Immutability**: `BookEntity` fields owned by the user (`created_at`, custom status) remain untouched during background catalog synchronization.
