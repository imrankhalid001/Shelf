# DATABASE.md — Shelf Local Database Specification (Room KMP)

## 1. Database Overview

**Shelf** uses **Room KMP** with SQLite as the underlying local storage engine.
The database name is `shelf_database.db`.

- **Persistence Goal**: Local database is the single source of truth for all user library data, notes, quotes, progress, sessions, goals, and history.
- **Observability**: All read operations exposed to repositories and use cases return Kotlin `Flow<T>`, ensuring immediate UI updates upon data mutation.

---

## 2. Entity Specifications

### 2.1 Table: `books`
Primary entity storing cataloged books.

```sql
CREATE TABLE IF NOT EXISTS `books` (
    `id` TEXT NOT NULL PRIMARY KEY,              -- Primary Key (workId or ISBN UUID)
    `work_id` TEXT NOT NULL,                     -- Open Library Work ID (e.g. OL12345W)
    `title` TEXT NOT NULL,                       -- Book Title
    `subtitle` TEXT,                             -- Book Subtitle
    `description` TEXT,                          -- Book Summary / Overview
    `cover_id` INTEGER,                          -- Open Library Cover ID
    `cover_url` TEXT,                            -- Direct Cover Image URL
    `first_publish_year` INTEGER,                -- Year of First Publication
    `page_count` INTEGER NOT NULL DEFAULT 0,     -- Total Page Count
    `isbn10` TEXT,                               -- ISBN-10 Code
    `isbn13` TEXT,                               -- ISBN-13 Code
    `subjects` TEXT NOT NULL,                    -- Comma-separated or JSON list of subjects/genres
    `publisher` TEXT,                            -- Primary Publisher Name
    `language` TEXT NOT NULL DEFAULT 'en',       -- ISO Language Code
    `created_at` INTEGER NOT NULL,               -- Epoch Timestamp (Millis)
    `updated_at` INTEGER NOT NULL                -- Epoch Timestamp (Millis)
);

CREATE INDEX IF NOT EXISTS `idx_books_work_id` ON `books` (`work_id`);
CREATE INDEX IF NOT EXISTS `idx_books_title` ON `books` (`title`);
```

### 2.2 Table: `authors`
Entity storing author information.

```sql
CREATE TABLE IF NOT EXISTS `authors` (
    `id` TEXT NOT NULL PRIMARY KEY,              -- Author ID (e.g. OL98765A)
    `name` TEXT NOT NULL,                        -- Author Full Name
    `bio` TEXT,                                  -- Author Biography
    `photo_url` TEXT,                            -- Author Photo Image URL
    `created_at` INTEGER NOT NULL                -- Epoch Timestamp (Millis)
);

CREATE INDEX IF NOT EXISTS `idx_authors_name` ON `authors` (`name`);
```

### 2.3 Table: `book_authors` (Junction Table)
Many-to-Many junction mapping books to authors.

```sql
CREATE TABLE IF NOT EXISTS `book_authors` (
    `book_id` TEXT NOT NULL,
    `author_id` TEXT NOT NULL,
    PRIMARY KEY (`book_id`, `author_id`),
    FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS `idx_book_authors_author_id` ON `book_authors` (`author_id`);
```

### 2.4 Table: `collections`
Custom user-created shelves/collections.

```sql
CREATE TABLE IF NOT EXISTS `collections` (
    `id` TEXT NOT NULL PRIMARY KEY,              -- UUID
    `name` TEXT NOT NULL,                        -- Collection Name
    `description` TEXT,                          -- Description
    `cover_book_id` TEXT,                        -- Optional Book ID used for cover display
    `created_at` INTEGER NOT NULL,               -- Epoch Timestamp (Millis)
    `updated_at` INTEGER NOT NULL                -- Epoch Timestamp (Millis)
);
```

### 2.5 Table: `collection_books` (Junction Table)
Many-to-Many junction mapping books to collections.

```sql
CREATE TABLE IF NOT EXISTS `collection_books` (
    `collection_id` TEXT NOT NULL,
    `book_id` TEXT NOT NULL,
    `added_at` INTEGER NOT NULL,
    PRIMARY KEY (`collection_id`, `book_id`),
    FOREIGN KEY (`collection_id`) REFERENCES `collections` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS `idx_collection_books_book_id` ON `collection_books` (`book_id`);
```

### 2.6 Table: `reading_progress`
Tracking current reading status and page counts for books in the library.

```sql
CREATE TABLE IF NOT EXISTS `reading_progress` (
    `book_id` TEXT NOT NULL PRIMARY KEY,
    `current_page` INTEGER NOT NULL DEFAULT 0,
    `total_pages` INTEGER NOT NULL DEFAULT 0,
    `percentage` REAL NOT NULL DEFAULT 0.0,
    `status` TEXT NOT NULL,                       -- 'WANT_TO_READ', 'READING', 'FINISHED', 'PAUSED', 'DROPPED'
    `started_at` INTEGER,                        -- Epoch Timestamp (Millis)
    `finished_at` INTEGER,                       -- Epoch Timestamp (Millis)
    `updated_at` INTEGER NOT NULL,               -- Epoch Timestamp (Millis)
    FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS `idx_reading_progress_status` ON `reading_progress` (`status`);
```

### 2.7 Table: `reading_sessions`
Log of individual reading sessions.

```sql
CREATE TABLE IF NOT EXISTS `reading_sessions` (
    `id` TEXT NOT NULL PRIMARY KEY,              -- UUID
    `book_id` TEXT NOT NULL,
    `started_at` INTEGER NOT NULL,               -- Epoch Timestamp (Millis)
    `ended_at` INTEGER NOT NULL,                 -- Epoch Timestamp (Millis)
    `duration_seconds` INTEGER NOT NULL,         -- Session Duration
    `pages_read` INTEGER NOT NULL,               -- Pages Read in Session
    FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS `idx_reading_sessions_book_id` ON `reading_sessions` (`book_id`);
CREATE INDEX IF NOT EXISTS `idx_reading_sessions_started_at` ON `reading_sessions` (`started_at`);
```

### 2.8 Table: `notes`
User notes attached to specific books.

```sql
CREATE TABLE IF NOT EXISTS `notes` (
    `id` TEXT NOT NULL PRIMARY KEY,              -- UUID
    `book_id` TEXT NOT NULL,
    `content` TEXT NOT NULL,
    `page_number` INTEGER,                       -- Page Reference
    `created_at` INTEGER NOT NULL,
    `updated_at` INTEGER NOT NULL,
    FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS `idx_notes_book_id` ON `notes` (`book_id`);
```

### 2.9 Table: `quotes`
Favorite book quotes attached to specific books.

```sql
CREATE TABLE IF NOT EXISTS `quotes` (
    `id` TEXT NOT NULL PRIMARY KEY,              -- UUID
    `book_id` TEXT NOT NULL,
    `text` TEXT NOT NULL,
    `page_number` INTEGER,                       -- Page Reference
    `created_at` INTEGER NOT NULL,
    FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS `idx_quotes_book_id` ON `quotes` (`book_id`);
```

### 2.10 Table: `favorites`
Quick reference favorite books.

```sql
CREATE TABLE IF NOT EXISTS `favorites` (
    `book_id` TEXT NOT NULL PRIMARY KEY,
    `created_at` INTEGER NOT NULL,
    FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE
);
```

### 2.11 Table: `search_history`
Search query log for debounced suggestions and history.

```sql
CREATE TABLE IF NOT EXISTS `search_history` (
    `id` TEXT NOT NULL PRIMARY KEY,              -- UUID
    `query` TEXT NOT NULL,
    `searched_at` INTEGER NOT NULL
);

CREATE INDEX IF NOT EXISTS `idx_search_history_searched_at` ON `search_history` (`searched_at`);
```

### 2.12 Table: `reading_goals`
Yearly reading goals and progress metrics.

```sql
CREATE TABLE IF NOT EXISTS `reading_goals` (
    `id` TEXT NOT NULL PRIMARY KEY,              -- UUID
    `year` INTEGER NOT NULL UNIQUE,              -- Calendar Year (e.g., 2026)
    `target_books` INTEGER NOT NULL,             -- Target Books Count
    `completed_books` INTEGER NOT NULL DEFAULT 0,-- Completed Count
    `created_at` INTEGER NOT NULL,
    `updated_at` INTEGER NOT NULL
);
```

### 2.13 Table: `recently_viewed`
Tracks recently viewed books for home carousel.

```sql
CREATE TABLE IF NOT EXISTS `recently_viewed` (
    `book_id` TEXT NOT NULL PRIMARY KEY,
    `viewed_at` INTEGER NOT NULL,
    FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS `idx_recently_viewed_viewed_at` ON `recently_viewed` (`viewed_at`);
```

---

## 3. DAOs & Query Contracts

The room database exposes 7 DAOs:
1. `BookDao`: CRUD, search, flow observers for library.
2. `AuthorDao`: CRUD and details lookup.
3. `CollectionDao`: Collection CRUD and join queries for collection books.
4. `ReadingProgressDao`: Status updates, page progression, status filtering.
5. `ReadingSessionDao`: Session logging, total reading duration, monthly activity aggregation.
6. `NoteQuoteDao`: Notes & Quotes CRUD by book ID.
7. `ReadingGoalDao`: Goal setting and year lookup.
