# SECURITY_PRIVACY.md — Privacy, Security & Data Sovereignty

## 1. Privacy Commitments

**Shelf** is built with absolute commitment to user privacy and data sovereignty:

1. **Zero Tracking & Telemetry**: Shelf contains NO analytics SDKs (no Firebase Analytics, Mixpanel, Amplitude, Segment, or Google Analytics).
2. **Zero Advertisements**: Shelf contains NO ad networks or tracking pixels.
3. **No Account Required**: The user does not need to log in, register an account, or share an email address to use 100% of Shelf's features.
4. **100% Local Storage**: All personal reading logs, goal targets, page counts, notes, and quotes are saved in an unencrypted SQLite database located strictly inside the app's sandboxed private storage.

---

## 2. Network Communications Audit

- **Only Endpoint Contacted**: Open Library Public API (`https://openlibrary.org` and `https://covers.openlibrary.org`).
- **Data Sent**: Search terms typed into the search bar, work IDs requested for details, and cover IDs requested for image downloading.
- **Data NOT Sent**: User identity, reading progress, personal notes, quotes, device identifiers, IP logs, or personal reading habits.
- **HTTPS Enforced**: All HTTP communication is encrypted via TLS 1.3/1.2 over HTTPS. Cleartext HTTP (`http://`) is strictly disabled in Android Manifest and iOS App Transport Security (`NSAppTransportSecurity`).

---

## 3. Sandbox Storage Security

- **Android Storage**: Database files saved in `/data/data/com.shelf.personal.book/databases/shelf_database.db`. Protected by Android OS multi-user process isolation permissions (`mode_private`).
- **iOS Storage**: Database files saved in `Library/Application Support/shelf_database.db`. Protected by iOS sandbox data protection API.
