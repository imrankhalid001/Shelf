# TESTING.md — Testing Strategy & Quality Assurance Plan

## 1. Testing Philosophy

**Shelf** emphasizes a robust automated testing strategy centered on **Shared Business Logic** in `commonTest`.

- **Coverage Goal**: High coverage (>85%) on Use Cases, Domain Business Rules, Streak Calculations, Statistics Aggregators, DTO Mappers, and Offline Caching Repositories.
- **Fast Feedback**: Unit tests in `commonTest` execute in seconds without needing an Android emulator or iOS simulator.

---

## 2. Test Architecture & Libraries

| Scope | Location | Framework / Tool | Test Targets |
| ----- | -------- | ---------------- | ------------ |
| **Domain Use Cases** | `shared/src/commonTest` | `kotlin.test` + Fakes | Business rules, goal calculations, validation |
| **Streak & Stats** | `shared/src/commonTest` | `kotlin.test` | Reading streak algorithms, monthly statistics |
| **Repository Caching** | `shared/src/commonTest` | Turbine + Fakes | Room flow observation, network fallback |
| **Room DAOs** | `shared/src/commonTest` | Room KMP In-Memory | SQL query verification, foreign key cascades |
| **API Serialization** | `shared/src/commonTest` | Ktor MockEngine | JSON deserialization from Open Library responses |
| **Android ViewModels** | `app/src/test` | `kotlinx-coroutines-test` | UI state transformations, search debouncing |
| **Compose UI** | `app/src/androidTest` | `compose-test-manifest` | Critical UI component interaction tests |

---

## 3. Sample Test Cases

### 3.1 Testing Reading Streak Calculation Logic

```kotlin
class StreakCalculatorTest {

    private val calculator = ReadingStreakCalculator()

    @Test
    fun calculateStreak_consecutiveDays_returnsCorrectStreakCount() {
        val today = LocalDate(2026, 3, 30)
        val sessionDates = listOf(
            LocalDate(2026, 3, 30),
            LocalDate(2026, 3, 29),
            LocalDate(2026, 3, 28)
        )

        val streak = calculator.calculateCurrentStreak(sessionDates, today)

        assertEquals(3, streak.currentStreakDays)
    }

    @Test
    fun calculateStreak_missedDay_resetsStreakToZero() {
        val today = LocalDate(2026, 3, 30)
        val sessionDates = listOf(
            LocalDate(2026, 3, 28), // Missed 29th
            LocalDate(2026, 3, 27)
        )

        val streak = calculator.calculateCurrentStreak(sessionDates, today)

        assertEquals(0, streak.currentStreakDays)
    }
}
```

### 3.2 Testing Repository Flow Emission with Turbine

```kotlin
@Test
fun getBookDetails_emitsCachedDataFirst_thenUpdatesFromRemote() = runTest {
    val fakeDao = FakeBookDao()
    val fakeApi = FakeOpenLibraryApi()
    val repository = BookRepositoryImpl(fakeDao, fakeApi, TestDispatcherProvider())

    repository.observeBookDetails("OL12345W").test {
        val initialItem = awaitItem()
        assertTrue(initialItem is AppResult.Success)
        assertEquals("Cached Title", initialItem.data.title)

        cancelAndIgnoreRemainingEvents()
    }
}
```

---

## 4. Execution Commands

- Run all KMP unit tests:
  ```bash
  ./gradlew test
  ```
- Run Android unit tests:
  ```bash
  ./gradlew :app:testDebugUnitTest
  ```
- Run Android instrumentation tests:
  ```bash
  ./gradlew :app:connectedDebugAndroidTest
  ```
