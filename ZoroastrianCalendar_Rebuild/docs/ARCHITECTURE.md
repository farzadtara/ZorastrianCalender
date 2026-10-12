# Zoroastrian Calendar — Architecture & Domain Contracts

**Revision:** 2.1 · **Date:** 2026-10-12
**Status:** initial native scaffold follows the user-selected Android Compose Multiplatform + iOS SwiftUI split; device/build validation remains open.

## 1. Architecture direction

Use a pure KMP `sharedLogic` module for domain and app catalog APIs, a Compose Multiplatform `sharedUI` module for Android screens, an Android host, and a native SwiftUI iOS host. Both screens use shared stable IDs and copy. Keep native notifications, widgets, and system integrations in their platform hosts. The UI split is the user's explicit choice; confirm accessibility and performance on devices before release.

The initial build pin is Kotlin 2.4.20, Compose Multiplatform 1.12.1, Android Gradle Plugin 9.3.0, and Gradle 9.7.0. This falls within the published [Kotlin Multiplatform compatibility matrix](https://kotlinlang.org/docs/multiplatform/multiplatform-compatibility-guide.html) and [Compose Multiplatform compatibility guidance](https://kotlinlang.org/docs/multiplatform/compose-compatibility-and-versioning.html). Android API 36, Android min API 24, and iOS 15 are capability-prototype settings, not final product-support decisions. Bundle IDs are provisional until the old app identity and signing access are known.

Use a small number of real build modules. Inside them, organize packages by domain and feature:

| Logical area | Responsibility | Must not own |
| --- | --- | --- |
| Shared calendar | Date types, profile rules, conversion, Nabur, occurrence generation | UI, OS permissions, device clock reads, network calls |
| Shared content | Versioned pack parsing, validation, sources, localized content, indexes | Store publishing, executable remote calendar rules |
| Shared persistence | Settings/reminder preferences and migration facade | Shared mutable widget-process state |
| Shared presentation | Observable screen state, user actions, formatting contracts | Android/iOS scheduling APIs |
| Shared UI | Calendar, observance, converter, and settings screens | Religious algorithms duplicated in composables |
| Android host | Lifecycle, notifications, shortcuts, Glance, platform persistence adapters | A second calendar algorithm |
| iOS host/extensions | Swift host, native notifications, sharing, WidgetKit, App Group access | A second calendar algorithm |

Use unidirectional state updates and immutable screen state. Prefer explicit construction and a few justified interfaces over a generic service registry, event bus, or plugin framework.

## 2. Build layout

The current KMP tooling should generate the actual build structure. Treat the original shared/composeApp/androidApp/iosApp layout as conceptual; use supported Android/iOS integration rather than copying an outdated build script.

- Shared core: calendar, content, persistence facades, and platform-callable APIs.
- Shared presentation/UI: reusable state and application screens.
- Android application: Android entry point and native integrations.
- iOS application: Xcode host and system integrations.
- iOS widget extension: SwiftUI views and a narrow shared-engine or snapshot reader.
- Content/fixtures: reviewed source packs, migration inputs, independent expected-date fixtures.
- Tooling: pack validation, CI, and release configuration.

Use supported Gradle/Xcode framework integration locally. XCFramework packaging is an optional distribution decision, not a reason to rebuild and check binaries into source control on every edit.

## 3. Calendar contract — required before algorithm implementation

Calendar profile and language are independent. A Persian UI does not identify a religious tradition, and the Solar Hijri calendar is not a synonym for Fasli.

Every supported profile must specify:

| Property | Required meaning |
| --- | --- |
| Profile ID and human name | Stable internal identity and reviewed display terminology |
| Rule version | Which conversion/recurrence rules produced a result |
| Era/year convention | How religious years are numbered and labeled |
| Epoch/reference anchors | Reviewed mappings between canonical civil dates and religious dates |
| Year start | Exact computation or authoritative table, with timezone/location convention if relevant |
| Ordinary date structure | Mah/Roj identities and order |
| Special days | Gatha and any leap/intercalary identity, position, and valid years |
| Intercalation policy | Year lengths and leap behavior for this profile |
| Supported interval | Civil and religious input limits; behavior outside them |
| Date boundary | Civil association used by date-only conversion and any distinct liturgical instant boundary |
| Nabur rule | Stable Roj-based rule plus any explicitly approved exceptions |
| Evidence | Fixture sources, reviewer, approval date, and unresolved limitations |

Do not implement all traditions as a fixed offset from one another without verifying their calendars and year conventions. Multiple reviewed profiles can be added later; a generic rule scripting engine is unnecessary for one launch profile.

The [calendar reference](https://www.heritageinstitute.com/zoroastrianism/calendar/index.htm) describes ordinary months, special days, and differing traditions. It does not settle the exact convention this product should implement. That decision requires the intended community's review.

### 3.1 Date representations

Use a canonical integer day representation, such as an epoch-day value, for date-only conversion and equality. It represents a day association, not an arbitrary UTC timestamp.

- Civil input: explicit calendar system, year, month, and day.
- Religious ordinary input: profile, era, year, Mah ID, and Roj ID.
- Religious special input: profile, era, year, and a special-day ID.
- Converted day: canonical day, civil representations, religious representation, Nabur result, and occurrences.
- “Today” resolution: injected instant/clock, chosen zone, and approved boundary policy.

An ordinary religious date and a Gatha/intercalary date should be different variants. Never encode Gatha as ordinary Roj 31, an invented month 13, or an implicit string suffix.

If liturgical day boundaries differ from the date-only civil association, document both. Do not silently change a date-only converter result based on the time of day. A boundary requiring solar/location calculations changes scope and must be reviewed.

### 3.2 Invariants and failures

- One valid date in a supported system/profile maps to one canonical day.
- Each supported canonical day has a valid representation; continuity holds across all year transitions.
- Ordinary Roj identity is independent of Persian/English display spelling.
- Conversion failures distinguish invalid date, unsupported interval, unsupported profile, and unapproved rules.
- Calendar rules never depend on event-title strings or the legacy holiday flag.
- Leap behavior is tested in each supported system; neither Solar Hijri nor religious leap behavior may be inferred from Gregorian year modulo four.
- Do not assume every Nowruz is Gregorian March 21.
- Do not assume a local day always lasts 24 hours.

### 3.3 Useful shared operations

Expose a small domain facade for:

- Validate and convert an explicit date to/from a canonical day.
- Resolve the current civil/religious date under approved settings.
- Get a day's religious identity and Nabur result.
- Generate event occurrences over a finite interval.
- Obtain view-ready day/month summaries.
- Produce reminder intents and lightweight widget summaries.

Keep the domain pure. Repositories supply content and settings; hosts supply clocks/zones and native effects.

## 4. Versioned content model

The supplied flat file is a legacy input format, not the long-term production schema.

| Record | Important fields and constraints |
| --- | --- |
| ContentPack | schemaVersion, contentVersion, requiredRuleVersion, supportedProfileIds, languages, sourceRefs, review metadata |
| RojDefinition | Stable ID/order, localized names, aliases, reviewed meaning if available |
| MahDefinition | Stable ID/order, localized names/aliases |
| SpecialDayDefinition | Stable ID, profile applicability, localized name, approved position/validity rule |
| EventDefinition | Stable ID, localized title/description, category, applicability, recurrence rule, holiday classification, source/review metadata |
| EventOccurrence | Event ID, profile/rule version, canonical start/end days, language availability |
| ReminderPreference | Stable selection/event ID, enabled state, time, lead time, quiet-hour and zone policy |
| LegacyProvenance | Original row/date/values, import version, correction record references |
| CorrectionRecord | Affected source rows/rules, before/after, reason, evidence, reviewer/date |

Model daily observances as a list. A single title field cannot represent overlapping pilgrimages, festivals, and Gahambars reliably.

### 4.1 Recurrence rules

Specify the calendar in which a rule is anchored:

- Reviewed civil annual date/range in Solar Hijri.
- Reviewed civil annual date/range in Gregorian.
- Reviewed religious Mah/Roj or special-day occurrence for a named profile.
- Reviewed explicit date/range exceptions for particular years.

Build only rule variants needed by reviewed V1 content. Validate start/end order, year crossings, applicable profiles, supported intervals, and multi-day behavior. Never decide recurrence solely from where an old row appears.

Event category, religious observance presence, and public/community holiday status are separate properties. An unknown holiday classification must remain unknown until reviewed.

### 4.2 Legacy boundary

Keep a dedicated legacy DTO with the exact fields day, month, dayName, isNabor, isZoroastrianHolyday, and zoroastrianHolydayTitle. Deserialize without interpreting the names as domain truth.

The importer:

1. Preserves every source row and raw value.
2. Associates source coordinates with a confirmed legacy convention.
3. Applies reviewed name/ID mappings and explicit corrections.
4. Curates combined titles into independent event definitions.
5. Records source and review status.
6. Produces a validated production pack and an import/correction report.

Do not mutate the legacy JSON to make it appear self-consistent. Do not bulk set all titled days to holiday=true.

## 5. Storage and offline behavior

For this dataset, a bundled JSON/content pack plus in-memory indexes is sufficient for immutable calendar content. Parse once per pack version; maintain a bounded cache for month summaries if profiling shows benefit.

Persist mutable preferences and follows separately through a small storage facade:

- Use compatible platform preference stores or a verified shared implementation.
- Introduce a relational store only when actual structured user data or queries justify it.
- Include a storage schema version and repeatable migration path.
- Define OS backup/restore behavior and test restored settings.
- Keep user settings independent of replaceable content versions.

No network call is required for first launch or normal V1 use. More app installations do not require scaling a calendar server because computation is local.

Future content updates should use authenticated distribution, schema/rule compatibility checks, atomic replacement, retained last-known-good content, and rollback. A checksum detects accidental corruption; authentic updates need a trusted/signature-verified channel. Remote content must not execute calendar code.

## 6. UI and native boundaries

Shared ViewModels/presenters expose immutable state and accept user actions. Native effects are narrow operations such as permission requests, notification scheduling, sharing, and external settings links.

UI formats domain results; it does not calculate religious dates. Platform formatting APIs may help presentation, but they must not silently introduce a different conversion convention from the shared engine.

Use a narrow Swift-callable facade for synchronous pure date queries and simple data snapshots. Select coroutine/Flow bridging only after the compatible toolchain is verified. Keep host lifecycle, cancellation, and threading explicit.

The shared Compose approach is the default. A screen-specific SwiftUI adaptation is justified only by measured accessibility, interaction, or performance needs. Shared calendar/content contracts remain unchanged.

## 7. Reminders

The shared layer determines intended occurrence dates and reminder preferences. Native schedulers translate those intents into OS requests.

- Resolve the occurrence's date and reminder wall-clock time in the selected zone.
- Define lead-time units as calendar days, not seconds.
- Deduplicate by stable event/occurrence/preferences identity.
- Specify multi-day behavior, quiet-hour adjustment, and overlap handling.
- Respect native pending-request capacity; schedule a finite horizon and replenish when permitted.
- Keep enabled, authorized, scheduled, and delivered concepts distinct. Delivery is not guaranteed by having submitted a request.
- Reconcile after relevant preference/profile/content/time changes and foreground entry.
- Handle Android reboot/replacement broadcasts and OS notification states where supported.
- Do not rely on iOS background execution to perpetually refill reminders if the user never reopens the app.

Android notification authorization and [iOS local notification scheduling](https://developer.apple.com/documentation/usernotifications/scheduling-a-notification-locally-from-your-app) are native responsibilities. [WorkManager execution can be deferred](https://developer.android.com/develop/background-work/background-tasks/persistent/getting-started/define-work); it is unsuitable as an exact wall-clock delivery guarantee. Exact-alarm use requires a separately justified product need and current policy review.

## 8. Widgets

Widgets operate independently of the application's in-memory state. Android documents this process boundary in [Glance state management](https://developer.android.com/develop/ui/compose/glance/glance-app-widget). iOS [App Groups](https://developer.apple.com/documentation/BundleResources/Entitlements/com.apple.security.application-groups) provide shared persistent inputs for the app and extension.

Preferred iOS approach: a lightweight shared-engine facade reads approved content/settings and produces requested-day summaries inside the extension. Phase 03 validates framework startup, size, threading, and memory.

Fallback only if measurement requires it: the app generates dated snapshots with explicit valid-from/valid-through, profile/zone/settings fingerprint, and engine/content versions. The provider reads them and shows a dated/expired state outside their supported horizon. It must not extend stale data or duplicate conversion logic in Swift.

For both platforms:

- Persistent settings and content identity are inputs; app-global memory is not.
- Widget summaries include canonical day, display date, religious identity, observances/Nabur, and relevant versions.
- Native providers generate date-boundary entries or request best-effort updates.
- Reload after app changes; avoid frequent polling for deterministic daily content.
- Validate old/new snapshot compatibility and atomic writes.
- Route taps using stable date/event/profile identifiers; reject malformed routes safely.
- Test widget content after delayed refresh, timezone change, and long periods without launching the app.

[WidgetKit uses timelines and refresh budgets](https://developer.apple.com/documentation/widgetkit/keeping-a-widget-up-to-date); neither platform should be advertised as guaranteeing a refresh at an exact second.

## 9. Testing strategy

| Layer | Verification |
| --- | --- |
| Calendar | Independently sourced fixtures, all supported-range round trips, continuity, leap/special/year boundaries, invalid inputs |
| Content | Schema/rule references, duplicate IDs, categories/languages, invalid ranges, overlapped event occurrences |
| Storage/migration | Legacy upgrade cases, repeated migration, backup restore, preference preservation |
| Presentation | Selected-date/state preservation, complete errors/empty states, locale/digit changes |
| Native integration | Permission transitions, scheduling/cancellation, cold/warm routes, widget process/snapshot behavior |
| Visual/accessibility | Manual device review, targeted stable screenshots, TalkBack/VoiceOver, large text, contrast, RTL |
| Release/performance | Release-build device timings, scroll traces, memory/battery, clean install and real upgrade |

Run shared tests on JVM and Kotlin/Native. Add native integration tests where OS behavior matters; a shared unit test cannot prove that a notification, widget, or accessibility tree works on-device.

## 10. Security, privacy, and operational simplicity

Keep the core account-free and local. Collect no precise location for V1 calendar browsing and no user religious preferences through unsolicited telemetry.

If diagnostics are added, make their purpose and collection clear, redact user-specific settings/content, and include build/engine/content versions without secrets. Store signing keys and credentials outside source control.

For future growth, expand reviewed data and profile rules first. Introduce a service only for a real service requirement such as curated remote publishing or sync; do not add microservices, distributed queues, or a generic event framework to a local calendar.
