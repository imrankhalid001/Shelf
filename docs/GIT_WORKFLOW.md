# GIT_WORKFLOW.md — Version Control, Branching & Commit Conventions

## 1. Commit Message Standard

We adhere strictly to the **Conventional Commits** specification (`v1.0.0`).

### Format:
```text
<type>(<scope>): <short summary in imperative present tense>

[optional body describing technical details or rationale]

[optional footer(s), e.g., Closes #12, Milestone-4]
```

### Approved Types:
- `feat`: A new feature or user-facing capability.
- `fix`: A bug fix.
- `docs`: Documentation changes only.
- `style`: Formatting, spacing, linting changes (no code logic change).
- `refactor`: Code change that neither fixes a bug nor adds a feature.
- `perf`: A code change that improves performance.
- `test`: Adding missing tests or correcting existing tests.
- `build`: Changes that affect the build system or external dependencies.
- `chore`: Maintenance tasks, update build configurations.

### Examples:
- `feat(domain): implement UpdateReadingProgressUseCase and unit tests`
- `fix(data): resolve Room foreign key cascade deletion for book notes`
- `docs(arch): update database schema specification for reading_goals`
- `test(commonTest): add streak calculator edge cases for leap years`

---

## 2. Branching Strategy

- `main`: Production-ready, fully tested code. Every commit on `main` must compile (`./gradlew build`) and pass tests (`./gradlew test`).
- `feature/<milestone-number>-<short-description>`: Branch for implementing specific milestone features (e.g., `feature/m5-room-database`, `feature/m10-home-screen`).
- `fix/<short-description>`: Branch for bug fixes.

---

## 3. Pull Request Requirements

1. **Clean History**: Rebase feature branch on latest `main` before opening PR.
2. **Automated Verification**: Gradle build and unit tests must pass cleanly.
3. **No Unrelated Changes**: Keep PRs scoped directly to the relevant task or milestone.
