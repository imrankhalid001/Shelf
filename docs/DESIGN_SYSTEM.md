# DESIGN_SYSTEM.md — Shelf Design System & Visual Foundation

## 1. Design Language & Philosophy

The **Shelf Design System** is built on five core design principles:

1. **Book & Paper Inspired**: Warm off-white surfaces reminiscent of fine book paper; deep charcoal ink typography.
2. **Editorial Hierarchy**: Large bold titles, generous whitespace, clear structural separation.
3. **Tactile & Responsive**: Subtle press states, smooth spring transitions, elegant elevation cards.
4. **Content-First Focus**: Book covers are high-contrast visual anchors; chrome UI is lightweight and unobtrusive.
5. **Universal Consistency**: Both Jetpack Compose (Android) and SwiftUI (iOS) render identical visual design tokens while using platform-native components.

---

## 2. Color System & Semantic Tokens

Raw hex colors MUST NOT be hardcoded in UI code. Always use semantic color tokens.

### 2.1 Color Palette Values
- **Paper Warm Light (`#FDFBF7`)**: Primary background for light mode.
- **Deep Ink (`#1A1918`)**: Primary text and high-emphasis dark surface.
- **Charcoal Surface (`#242321`)**: Elevated surface in dark mode.
- **Muted Amber (`#D97706` / `#F59E0B`)**: Primary accent color inspired by classic book spines and bookmarks.
- **Warm Neutral (`#78716C`)**: Secondary text and subtle borders.
- **Warm Border (`#E7E5E4`)**: Surface dividers and subtle card outlines.

### 2.2 Semantic Color Token Mapping

| Token Name | Light Mode Value | Dark Mode Value | Usage |
| ---------- | ---------------- | --------------- | ----- |
| `background` | `#FDFBF7` (Paper Light) | `#121110` (Dark Ink) | Main screen background |
| `surface` | `#FFFFFF` (Pure White) | `#1E1D1B` (Dark Surface) | Cards, sheets, dialogs |
| `surfaceElevated` | `#F5F3EF` (Warm Elevated) | `#2A2825` (Elevated Charcoal) | Floating cards, top bars |
| `primaryText` | `#1A1918` (Deep Ink) | `#F5F3EF` (Paper High) | Titles, primary copy |
| `secondaryText` | `#78716C` (Warm Grey) | `#A8A29E` (Muted Warm) | Author, metadata, captions |
| `accent` | `#D97706` (Amber 600) | `#F59E0B` (Amber 500) | Buttons, progress indicators |
| `accentSurface` | `#FEF3C7` (Amber 100) | `#451A03` (Deep Amber Surface) | Chips, highlights |
| `divider` | `#E7E5E4` (Border Light) | `#2E2C29` (Border Dark) | Separators, subtle strokes |
| `error` | `#DC2626` (Red 600) | `#EF4444` (Red 500) | Error banners, delete actions |
| `success` | `#16A34A` (Green 600) | `#22C55E` (Green 500) | Completed goals, streaks |

---

## 3. Typography Scale

Shelf uses a clean sans-serif system font (Roboto/Inter on Android, San Francisco on iOS) paired with elegant editorial weights.

| Token | Size / Line Height | Weight | Usage |
| ----- | ------------------ | ------ | ----- |
| `displayLarge` | 34sp / 40sp | Bold | Large editorial titles |
| `headlineMedium` | 24sp / 30sp | SemiBold | Screen section headers |
| `titleLarge` | 20sp / 26sp | SemiBold | Book titles in cards |
| `titleMedium` | 16sp / 22sp | Medium | Subtitles, authors |
| `bodyLarge` | 16sp / 24sp | Normal | Book descriptions, notes |
| `bodyMedium` | 14sp / 20sp | Normal | Metadata, list items |
| `labelLarge` | 14sp / 18sp | Medium | Action buttons, chips |
| `caption` | 12sp / 16sp | Normal | Timestamps, page counts |

---

## 4. Spacing Scale

All layout paddings, margins, and gaps MUST use the standard spacing scale:

```
4dp / 4pt   - Extra Small (micro gaps)
8dp / 8pt   - Small (chip padding, icon gaps)
12dp / 12pt - Compact (card internal padding)
16dp / 16pt - Medium (standard screen padding, list gaps)
20dp / 20pt - Large (section gaps)
24dp / 24pt - Extra Large (hero section spacing)
32dp / 32pt - Double Extra Large
40dp / 40pt - Section margins
48dp / 48pt - Minimum touch target height
```

---

## 5. Shape & Corner Radii

- **Small Radius (`8dp / 8pt`)**: Buttons, text fields, chips.
- **Medium Radius (`12dp / 12pt`)**: Book cover images, note cards, stats cards.
- **Large Radius (`16dp / 16pt`)**: Collection cards, bottom sheets, dialogs.
- **Pill Radius (`999dp / 999pt`)**: Badge indicators, status chips.

---

## 6. Motion & Elevation

- **Press Scale**: Tactile button press scales element to `0.97` with spring dampening.
- **Screen Transitions**: Horizontal slide + subtle crossfade (`300ms` duration).
- **Progress Animation**: Smooth animated bar progression when page count updates.
