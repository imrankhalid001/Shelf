# ERROR_HANDLING.md — Error Model & Result Architecture

## 1. Unified Error Model Philosophy

**Shelf** relies on a structured functional error model (`AppResult<T>`) rather than throwing uncaught exceptions across architectural layers.

- **Domain Layer**: Functions return `AppResult<T>` or `Flow<AppResult<T>>`.
- **UI Layer**: Maps `AppError` cases to clear, actionable, user-friendly localized UI error states.
- **Zero Raw Exceptions**: Network timeouts, database constraint errors, and validation failures are caught at the repository boundary and mapped into sealed `AppError` domain instances.

---

## 2. Sealed Hierarchy Specification

```kotlin
sealed interface AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>
    data class Error(val error: AppError) : AppResult<Nothing>
    data object Loading : AppResult<Nothing>
}

sealed interface AppError {
    sealed interface Network : AppError {
        data object NoInternet : Network
        data object Timeout : Network
        data class ServerError(val code: Int, val message: String?) : Network
        data class Serialization(val message: String) : Network
    }

    sealed interface Database : AppError {
        data class ReadFailed(val cause: Throwable) : Database
        data class WriteFailed(val cause: Throwable) : Database
        data object NotFound : Database
    }

    sealed interface Validation : AppError {
        data class InvalidPageNumber(val maxPages: Int) : Validation
        data object EmptyQuery : Validation
        data object DuplicateCollectionName : Validation
    }

    data class Unknown(val cause: Throwable?) : AppError
}
```

---

## 3. Layered Error Handling Flow

```
Remote API / Local Room DB Exception
                 │
                 ▼
Data Layer (RepositoryImpl)
Catch Throwable → Map to AppError (e.g., AppError.Network.NoInternet)
Wrap in AppResult.Error(AppError...)
                 │
                 ▼
Domain Layer (UseCase)
Applies domain retry or fallback logic if applicable
                 │
                 ▼
Presentation Layer (ViewModel / StateHolder)
Maps AppError to UiState.Error(message = "No internet connection. Showing cached library.")
                 │
                 ▼
UI Screen (Compose / SwiftUI)
Renders non-intrusive snackbar / banner with Retry CTA button
```

---

## 4. UI Error Presentation Rules

1. **Non-Blocking Warnings**: Network sync failures MUST NOT prevent viewing locally cached data. Display a subtle top warning banner ("Offline - displaying cached library").
2. **Actionable Retries**: Every full-screen error state MUST feature a primary "Try Again" CTA button.
3. **No Raw Tech Jargon**: Never present raw stack traces, HTTP status codes like `HTTP 500 Internal Server Error`, or `NullPointerException` text to the user.
