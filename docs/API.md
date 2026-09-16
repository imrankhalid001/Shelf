# API.md — Open Library Remote API Integration Specification

## 1. API Strategy & Endpoint Policy

**Shelf** relies on the free, public **Open Library API** (`https://openlibrary.org`) for all remote book discovery, search, author information, and cover imagery.

### Policy Rules:
- **No API Key Required**: Endpoints are completely public.
- **No Scrape Policy**: Strictly use official Open Library REST JSON APIs.
- **Cache-First**: Responses from API are saved to local Room database before returning to domain use cases.
- **HTTP Engine**: Ktor Client 3.x with multiplatform JSON serialization.

---

## 2. API Endpoints Specification

### 2.1 Book Search Endpoint
- **URL**: `GET https://openlibrary.org/search.json`
- **Parameters**:
  - `q`: Search query string (title, author, subject)
  - `title`: Optional specific title search
  - `author`: Optional specific author search
  - `page`: Page number (1-indexed)
  - `limit`: Number of items per page (default: 20)
  - `fields`: `key,title,author_name,author_key,first_publish_year,cover_i,edition_count,isbn,subject`

#### Example Response Mapping (`SearchResponseDto`):
```json
{
  "numFound": 150,
  "start": 0,
  "docs": [
    {
      "key": "/works/OL27517W",
      "title": "Atomic Habits",
      "author_name": ["James Clear"],
      "author_key": ["OL7510103A"],
      "first_publish_year": 2018,
      "cover_i": 10522434,
      "edition_count": 12,
      "isbn": ["9780735211292"],
      "subject": ["Self-Help", "Personal Development", "Psychology"]
    }
  ]
}
```

---

### 2.2 Work Details Endpoint
- **URL**: `GET https://openlibrary.org/works/{workId}.json`
- **Example**: `GET https://openlibrary.org/works/OL27517W.json`
- **Response**: Description, subjects, covers array, created/modified dates.

### 2.3 Author Details Endpoint
- **URL**: `GET https://openlibrary.org/authors/{authorId}.json`
- **Example**: `GET https://openlibrary.org/authors/OL7510103A.json`
- **Response**: Author name, bio string or object (`{"type": "/type/text", "value": "..."}`), birth date, photos array.

---

### 2.4 Cover Image URL Format
Open Library Cover CDN URLs:
- **By Cover ID**: `https://covers.openlibrary.org/b/id/{cover_i}-{size}.jpg`
- **Sizes**:
  - `S`: Small thumbnail (40x60px)
  - `M`: Medium (180x280px) - used in lists & grids
  - `L`: Large (400x600px) - used in Book Details hero section

---

## 3. Rate Limiting, Error Handling & Pagination

1. **Debounced Search**: Search query inputs are debounced by **400ms** in ViewModels/UseCases to prevent unnecessary API load.
2. **Network Error Resilience**: HTTP 429 (Too Many Requests) or HTTP 5xx errors are caught by Ktor client wrapper and mapped to standard `AppError.Network` domain exceptions.
3. **Pagination**: Search results use integer offset pagination (`page=1, 2, ...`). Use cases handle page concatenation seamlessly.
