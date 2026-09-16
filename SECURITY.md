# Security Policy

## Overview

**Shelf — Personal Book Intelligence** is designed with a strict **Privacy-First, Offline-First** philosophy. 

- All personal reading data, progress, notes, quotes, collections, and goals remain stored **exclusively in local Room database storage** on the user's device.
- No personal data, reading activity, or telemetry is collected, tracked, or transmitted to any remote servers.
- Remote network operations are strictly limited to read-only queries against public book discovery endpoints (Open Library API).

## Supported Versions

Only the current main branch and officially tagged release releases receive security updates.

| Version | Supported          |
| ------- | ------------------ |
| `main`  | :white_check_mark: |
| < 1.0.0 | :x:                |

## Reporting a Vulnerability

If you discover a security vulnerability or potential data leak within Shelf:

1. **Do NOT open a public GitHub Issue.**
2. Send a detailed report via email to `security@shelf.app` (or contact the maintainers directly on GitHub).
3. Include step-by-step reproduction instructions and proof-of-concept code where applicable.
4. We will acknowledge receipt within 48 hours and provide updates on resolution timeline.

## Data Protection Guidelines

- **No Hardcoded API Keys**: Public endpoints used (Open Library) do not require API keys. No private secrets or tokens must ever be committed into version control.
- **Local Storage Integrity**: Database files are stored inside sandboxed application storage on Android (`/data/data/com.shelf.personal.book/databases/`) and iOS (`Library/Application Support/`).
- **Dependencies**: All KMP, Android, and iOS dependencies are audited for known CVEs before adoption.
