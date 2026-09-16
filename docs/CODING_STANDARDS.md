# CODING_STANDARDS.md — Shelf Engineering & Style Guidelines

## 1. General Principles

1. **Clean Code**: Prioritize code clarity, explicit dependencies, small focused classes, and self-documenting code.
2. **Immutability First**: Use `val` instead of `var` wherever possible. Data classes should be immutable.
3. **No Magic Values**: Raw numbers, raw color hexes, and raw hardcoded string literals are forbidden in UI components and domain logic. Use constants, string resources, or design tokens.
4. **Single Responsibility**: Classes, functions, Use Cases, ViewModels, Composables, and SwiftUI Views should have one clearly defined responsibility.

---

## 2. Kotlin Conventions (Shared & Android)

### 2.1 Package & File Organization
- Standard reverse domain: `com.shelf.*`
- Group files by clean architecture layer and feature module:
  - `com.shelf.domain.model.Book`
  - `com.shelf.domain.usecase.SearchBooksUseCase`
  - `com.shelf.data.repository.BookRepositoryImpl`

### 2.2 Coroutines & Flow Standards
- **Structured Concurrency**: Never use `GlobalScope`. Use `viewModelScope` on Android, `Task` on iOS, or structured scope in shared logic.
- **Dispatchers**: Never hardcode `Dispatchers.IO` or `Dispatchers.Default` inside use cases or repositories. Inject a `DispatcherProvider` interface to enable deterministic unit testing.
- **Flow Exposure**: Read-only flows must be exposed as `StateFlow<T>` or `Flow<T>`. Never expose `MutableStateFlow` outside of ViewModels/StateHolders.

```kotlin
// GOOD
private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

// BAD
val uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
```

---

## 3. Jetpack Compose Guidelines (Android UI)

1. **State Hoisting**: Composables should be stateless whenever possible. Pass state down and events up.
2. **Preview Annotations**: Every reusable component MUST provide `@Preview` composables for light and dark modes.
3. **Recomposition Safety**: Use immutable parameters (`@Immutable` / `@Stable` data classes or primitive types) to prevent unnecessary recompositions.

```kotlin
@Composable
fun ShelfBookCard(
    book: BookUiModel,
    onBookClick: (String) -> Unit,
    modifier: Modifier = Modifier
) { ... }
```

---

## 4. SwiftUI Guidelines (iOS UI)

1. **Observation**: Use Swift `@Observable` macro (iOS 17+) for StateHolders.
2. **Component Decomposition**: Break down large views into small subviews (`bookHeaderView`, `progressSectionView`).
3. **Preview Providers**: Provide `#Preview` declarations for Xcode Canvas previewing.
