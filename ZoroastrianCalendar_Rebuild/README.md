# Zoroastrian Calendar — iOS & Android Rebuild

A proposed rebuild of the existing Zoroastrian calendar as a polished, accessible mobile application for daily dates, observances, Nabur, conversion, reminders, and native widgets.

**Plan revision:** 2.0 · **Reviewed:** 2026-10-11 · **Status:** planning package with an interactive mobile web prototype; native application implementation has not started.

## Product direction

Open the app and immediately understand today's civil date, Zoroastrian day name, Nabur status, and observances. Browse dates, find upcoming events, and set a reminder with a small number of clear actions. The calendar and shipped content work offline.

The first-release proposal is Persian first, with English interface support and a single reviewed calendar profile. Additional traditions are a later expansion, each with its own verified rules. This audience and profile proposal must be confirmed during discovery.

## Technical direction

- Kotlin Multiplatform for a deterministic shared calendar engine, content, settings, and presentation state.
- Compose Multiplatform for application screens, subject to an early iOS/Android accessibility and performance prototype.
- Native Android and iOS adapters for notifications, shortcuts, sharing, and system integration.
- Jetpack Glance on Android and SwiftUI/WidgetKit on iOS for home-screen widgets.
- Bundled, versioned calendar content and local persistence. A backend is optional future work.

Android and iOS are listed as stable targets for KMP and Compose in [Kotlin's platform support documentation](https://kotlinlang.org/docs/multiplatform/supported-platforms.html). Stability does not replace testing this app's Persian text, calendar layout, or native extensions.

## Important data finding

The supplied JSON has 365 unique day/month records with month lengths matching a non-leap Solar Hijri year. Its first 360 rows repeat the 30 ordinary day names; the final five contain special Gatha names. This is evidence of an existing civil-to-religious mapping, not a complete specification for every year or tradition.

Before implementation, establish its calendar profile, year numbering, year-start rule, leap-day behavior, event recurrence rules, and holiday-flag meaning. Preserve the original data while reviewing corrections. See the [data audit](docs/CALENDAR_DATA_AUDIT.md).

## Documents

| File | Purpose |
| --- | --- |
| [TASKS_AND_PLAN.md](TASKS_AND_PLAN.md) | Priorities, decisions, 12 delivery phases, task IDs, acceptance gates, and release criteria |
| [UI_UX_SPEC.md](docs/UI_UX_SPEC.md) | Screen requirements, visual directions, RTL, accessibility, interactions, and design validation |
| [ARCHITECTURE.md](docs/ARCHITECTURE.md) | Shared/native boundaries, calendar contract, content model, persistence, reminders, and widgets |
| [CALENDAR_DATA_AUDIT.md](docs/CALENDAR_DATA_AUDIT.md) | Reproducible findings from the supplied JSON and required remediation |
| [LEGACY_APP_INVENTORY.md](docs/LEGACY_APP_INVENTORY.md) | Partial feature and settings inventory from supplied legacy app screenshots |
| [prototype/README.md](prototype/README.md) | How to run the mobile prototype and its current limitations |
| [SOURCES.md](docs/SOURCES.md) | Primary platform documentation and cultural references used to inform the plan |
| [zoroastrian_events.json](data/legacy/zoroastrian_events.json) | Unmodified legacy input, retained for migration and comparison |
| [audit_summary.json](data/legacy/audit_summary.json) | Machine-readable structural audit of the legacy input |

## Starting the project

Complete Phase 00 and the calendar decisions in Phase 01 before treating a date mapping as correct. Explore design while those decisions are being resolved. A dependency-free mobile web prototype is available under [prototype](prototype/); it supports interaction review but does not satisfy the planned Android/iOS capability prototype.

Each phase in the task plan has an explicit completion gate. Checked tasks require reviewable evidence; a successful round-trip conversion alone is not proof of calendar accuracy.

Run the prototype from this package directory with `py -m http.server 8000`, then open `http://localhost:8000/prototype/`. Native Android/iOS build commands remain open until Phase 03 selects and validates a toolchain.
