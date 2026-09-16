# UI_UX.md — Screen Hierarchy, Flows & States

## 1. Screen Map & Navigation Hierarchy

Shelf consists of 5 core navigation tabs plus details and workflow screens:

```
Shelf Application Root
├── 1. Home Tab (Dashboard)
│   ├── Book Details Screen
│   ├── Author Details Screen
│   └── Reading Session Logger
├── 2. Search Tab
│   ├── Search Results List
│   └── Book Details Screen
├── 3. Library Tab
│   ├── Status Filter Tabs (Want to Read, Reading, Finished, All)
│   └── Book Details Screen
├── 4. Collections Tab
│   └── Collection Details Screen
└── 5. Profile & Intelligence Tab
    ├── Reading Statistics Screen
    ├── Reading Goal Config
    └── Settings Screen
```

---

## 2. Core Screen Specifications

### 2.1 Home Screen (Personal Dashboard)
- **Header**: Warm greeting ("Good evening, Imran"), date, reading streak badge ("🔥 14 Days").
- **Continue Reading Section**: Prominent card featuring currently active book with cover image, progress bar, percentage ("72%"), page indicator ("Page 180 / 250"), and a quick "Log Session" button.
- **Library Quick Access**: Horizontal cards for `Want to Read`, `Reading`, `Finished`.
- **Yearly Goal Card**: Progress bar showing completed vs target books ("23 / 30 books - 77%").
- **Recently Viewed**: Horizontal carousel of recently inspected books.
- **Recommended for You**: On-device recommendations based on favorite subjects/genres.

---

### 2.2 Book Details Screen (Showcase Screen)
- **Hero Section**: Large book cover image with shadow elevation, expanded title, author name (clickable to Author Details), publication year, and primary action button ("Start Reading" / "Update Progress").
- **Status Selector**: Segmented control or dropdown to change status (`Want to Read`, `Reading`, `Finished`, `Paused`, `Dropped`).
- **Reading Progress Card**: Current page input slider, page +/- stepper, percentage complete bar.
- **Action Buttons**: Favorite toggle (Heart), Add to Collection, Share Quote.
- **Overview & Metadata**: Collapsible book description, genres/subjects chips, page count, publisher, ISBN-13.
- **Notes & Quotes Tabs**: Inline list of user notes and quotes for this book with an "Add Note" FAB/button.

---

### 2.3 Search Screen
- **SearchBar**: Full-width input with debounced instant search, clear icon, and voice/ISBN button.
- **Search History**: Chip list of recent query terms with "Clear History" button.
- **Results List**: List cards showing book thumbnail, title, author, publish year, subject chips, and quick "Add to Library" action button.
- **States**: Skeleton shimmer during loading, empty result message, network error retry banner.

---

## 3. UI State Matrix Guidelines

Every feature screen MUST explicitly implement 4 UI states:

1. **Loading State**: Custom skeleton shimmer cards matching layout geometry (never generic spinners).
2. **Content State**: Fully populated data list/cards with smooth entrance animations.
3. **Empty State**: Friendly graphic icon, helpful explanation text, and clear primary CTA button (e.g., "No books in library yet -> Explore Search").
4. **Error State**: Non-blocking error banner or card with retry action button and localized error message.
