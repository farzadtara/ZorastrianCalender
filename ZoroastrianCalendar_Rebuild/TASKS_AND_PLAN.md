# Zoroastrian Calendar — Implementation Plan & Task Breakdown

**Revision:** 2.0  
**Date:** 2026-10-11  
**Targets:** Android and iOS  
**State:** proposed delivery plan; all implementation tasks remain open.

## 1. Desired outcome

Rebuild the existing app as a reliable daily calendar with a clear Persian interface, accurate date conversions, useful observance information, controllable reminders, and native home-screen widgets.

The product should answer four questions quickly:

1. What is today's Zoroastrian day name and date?
2. Is today a Nabur day, and what observances occur today?
3. What is coming next, and how can I receive a reminder?
4. What is this date in the other supported calendars?

Accuracy, readability, offline access, and platform behavior are release requirements. Decorative features follow those requirements.

## 2. Changes from the original plan

| Original gap | Revised requirement |
| --- | --- |
| Assumes a generic 365-day engine | Define the actual profile, epoch, year start, ordinary days, Gatha days, and intercalation before coding |
| Uses the JSON as a complete calendar specification | Audit and preserve it as legacy input; replace assumptions with reviewed rules and sourced fixtures |
| Starts UI implementation before design validation | Add user journeys, three visual directions, an interactive prototype, and accessibility evaluation |
| Specifies a tri-wheel converter without validating it | Use explicit calendar selection, clear input, immediate results, and an accessible alternative to wheels |
| Treats shared Material 3 UI as sufficient on both platforms | Test shared UI on both systems and use native integration where required |
| Schedules widget updates as background sync | Use local data and native refresh mechanisms; model operating-system delays |
| Omits migration and notifications | Add legacy upgrade compatibility, local reminders, permission states, and rescheduling |
| Ends with package export | Add beta testing, store submission work, production rollout, and a correction process |

## 3. Planning assumptions and decision gates

These are proposed defaults, not facts inferred from the missing old application.

| Decision | Proposed direction | Owner and resolution |
| --- | --- | --- |
| First-release audience | Persian-speaking users of the existing app; English UI also supported | Product owner, Phase 00 |
| Religious calendar profile | One reviewed profile aligned with the intended community; investigate the original Fasli assumption | Product owner + calendar reviewer, Phase 01 |
| Main calendar grid | Solar Hijri by default, with Gregorian grid option; show religious date as a separately labeled date | Product/design + calendar reviewer, Phases 01–02 |
| Religious year numbering | Explicit era and year convention; do not infer it from language or civil year | Calendar reviewer, Phase 01 |
| Supported civil-date interval | Publish a finite, independently validated interval | Calendar reviewer + engineering, Phase 01 |
| Day boundary | Define civil date, liturgical boundary, and notification time independently | Calendar reviewer + engineering, Phase 01 |
| Time zone | Device zone by default; optional fixed zone for travel; display when fixed | Product + engineering, Phases 01–02 |
| UI technology | KMP + Compose Multiplatform, with native integration and a tested fallback for specific screens | Engineering, Phase 03 |
| Minimum OS versions and tooling | Choose a compatible stable matrix using old-user device coverage and release requirements | Engineering, Phase 03 |
| Existing app identity | Preserve Android application ID/signing and any existing iOS identity for an upgrade | Product/release owner, Phase 00 |
| Launch business model | Calendar core available without an account; monetization requires a separate decision | Product owner, Phase 00 |
| Content rights | Verify ownership/redistribution of legacy content, translations, fonts, and illustrations | Product/content owner, Phases 00–01 |

Calendar decisions block production calendar behavior. They do not prevent building design prototypes with clearly marked sample dates.

## 4. Release scope

### V1 — required

| Area | First-release behavior |
| --- | --- |
| Calendar | Today summary, month browsing, accessible agenda alternative, jump to date, return to today |
| Day details | Roj/Mah or special-day identity, separately labeled civil dates, Nabur, multiple observances |
| Observances | Upcoming list, search, category filters, Nabur list, multi-day ranges, event details |
| Converter | Gregorian ↔ Solar Hijri ↔ the reviewed religious profile, including supported special dates |
| Preferences | Language, numeral style, theme, week start, calendar display, and time-zone behavior |
| Reminders | Opt-in Nabur and observance reminders, local scheduling, lead time, preferred time, status |
| Widgets | Small and medium Android/iOS widgets; dated content, accessible contrast, correct deep links |
| System integration | App-icon long-press shortcuts, native text sharing, notification/widget routes |
| Quality | Offline core, Persian RTL, English LTR, large text, VoiceOver/TalkBack, dark mode |
| Upgrade | Verified migration for the legacy data actually present; release as an upgrade where possible |

English UI does not imply fully translated religious content. Each event declares available languages; show its reviewed original title when a translation is absent. Do not invent meanings or translations.

### V1.1 — after evidence from launch

- Additional independently verified traditions, such as Shahenshahi or Kadmi, with visible profile selection.
- Native calendar export or ICS sharing, with duplicate prevention and permission handling.
- More widget layouts, lock-screen support where available, and per-widget preferences.
- Richer sourced explanations, pronunciation/audio, bookmarks, or user notes if users need them.
- Optional authenticated content-pack updates with verification and rollback.

### Later, only with demonstrated demand

- Gah calculation and optional solar/location functions.
- Cross-device sync or accounts.
- Community submissions, administrative publishing tools, and regional event feeds.
- Watch apps, Live Activities, desktop/web companions, subscriptions, or social features.

The original Gah feature is retained as future work because astronomical inputs and tradition-specific rules need separate validation. Its inclusion in V1 would require an explicit scope and schedule change.

## 5. Delivery sequence

| Phase | Main output | Dependency |
| --- | --- | --- |
| 00 | Legacy inventory and product decisions | Supplied planning files and access to the old app |
| 01 | Approved calendar specification, content rules, and fixtures | Phase 00 |
| 02 | Tested UX prototype and design system | Phase 00; sample data allowed while Phase 01 proceeds |
| 03 | Working KMP project and real-device capability prototype | Phase 00; partial Phase 01/02 sufficient for prototype |
| 04 | Verified shared calendar engine and converter | Phase 01 + Phase 03 |
| 05 | Versioned content, preferences, and local storage | Phase 01 + Phase 03 |
| 06 | Complete primary app journeys | Phase 02 + Phase 04 + Phase 05 |
| 07 | Native reminders and system routes | Phase 04 + Phase 05; screen integration from Phase 06 |
| 08 | Native Android and iOS widgets | Phase 03 capability decision + Phase 04 + Phase 05 |
| 09 | Migration, accessibility, performance, and regression evidence | Implementation phases; checks also run continuously |
| 10 | Beta, store-ready builds, and release | Phase 09 |
| 11 | Operational handoff and next-release priorities | Phase 10 |

Engineering and design may overlap where dependencies permit. Calendar approval and release gates remain sequential.

## 6. Roles and effort

Use roles rather than assuming a large team: product owner, calendar/content reviewer, designer, KMP/Android engineer, iOS engineer, and QA/release owner. One person may cover several roles, but calendar approval needs a knowledgeable reviewer.

**Working estimate:** 12–18 calendar weeks for two mobile engineers, a part-time designer, dedicated QA capacity during stabilization, and timely access to a calendar reviewer. This is a planning estimate, not a commitment. Re-estimate after Phases 00–03; a solo developer, missing signing access, unresolved calendar conventions, or adding Gah/multiple traditions extends delivery.

| Workstream | Indicative effort | Scheduling note |
| --- | --- | --- |
| Discovery and calendar/content decisions | 2–4 person-weeks | Must start first |
| UX research, prototype, and design system | 2–3 person-weeks | Can overlap domain review |
| Tooling, capability prototype, and CI | 1–2 person-weeks | Must include macOS/iOS validation |
| Calendar engine and content/persistence | 4–6 person-weeks | Depends on verified rules |
| Application screens and localization | 4–6 person-weeks | Uses approved interaction design |
| Notifications, widgets, and native integration | 3–5 person-weeks | Requires work on both native platforms |
| Migration, QA, beta, and release | 3–5 person-weeks | Include contingency and store review |

## Phase 00 — Product discovery and legacy inventory

**Purpose:** understand the existing users, preserve upgrade paths, and agree what the first release promises.

- [ ] **P00-01** Inspect the old application, screenshots, store listing, source code if available, and user feedback. Inventory each screen, feature, setting, widget, reminder, and known defect.

  Current evidence is summarized in [LEGACY_APP_INVENTORY.md](docs/LEGACY_APP_INVENTORY.md). P00-01 remains open because the screenshots are partial, the target store listing is unidentified, and direct user feedback is unavailable.
- [ ] **P00-02** Record Android application ID, signing ownership, version history, store access, and any iOS bundle ID. Check whether preserving identity is possible.
- [ ] **P00-03** Inventory legacy preferences, databases, notifications, and exported data. Distinguish data that can be migrated from data not yet available.
- [ ] **P00-04** Interview 5–8 representative users, including frequent users and people with large-text or assistive-technology needs. Capture the daily tasks they actually perform.
- [ ] **P00-05** Confirm audience, languages, V1 scope, calendar reviewer, release ownership, content rights, and launch business model. Record unresolved items with an owner.
- [ ] **P00-06** Build a parity matrix: legacy behavior, proposed behavior, migration action, and intentionally changed behavior. Record the rationale for removing or changing features.

  A preliminary, screenshot-based matrix is recorded in [LEGACY_APP_INVENTORY.md](docs/LEGACY_APP_INVENTORY.md). P00-06 remains open until legacy behavior and proposed scope are verified and approved.

**Outputs:** legacy inventory, parity matrix, decision register, approved V1 scope.

**Acceptance gate:** product and engineering understand what must survive an upgrade; the reviewer and signing/access dependencies are identified. Missing artifacts are recorded explicitly rather than assumed.

## Phase 01 — Calendar specification and data remediation

**Purpose:** turn incomplete legacy mappings into a trustworthy domain contract.

- [ ] **P01-01** Specify profile ID/name, year era, year-start calculation or table, epoch/reference dates, supported interval, date boundaries, and leap/intercalary policy.
- [ ] **P01-02** Define ordinary Roj/Mah IDs, reviewed names/aliases, Gatha identities, and any separate leap-day identity. Special days must not become an invented ordinary month/day.
- [ ] **P01-03** Confirm the legacy JSON coordinate system and year applicability. Review every row with a rule conflict; use the findings in [CALENDAR_DATA_AUDIT.md](docs/CALENDAR_DATA_AUDIT.md).

  The documented structural audit was rerun on 2026-10-11 and its row counts, month layout, day-name cycle, Nabur flags, and title/holiday counts match [CALENDAR_DATA_AUDIT.md](docs/CALENDAR_DATA_AUDIT.md). P01-03 remains open pending confirmation of the coordinate system/year applicability and reviewer decisions on the flagged rows.
- [ ] **P01-04** Resolve the Gosh flag at legacy 02/13 and the special-day Nabur flag at 12/26. Review the meaning of the seven true holiday flags. Preserve raw values and document each approved change.
- [ ] **P01-05** Curate event IDs, titles, categories, recurrence calendar, ranges, holiday semantics, regional/profile applicability, sources, language availability, and content review status.
- [ ] **P01-06** Create independent expected-date fixtures from reviewed calendars. Cover regular dates, all Gatha days, any leap/intercalary day, year transitions, and festival/Nabur boundaries in more than one year.
- [ ] **P01-07** Document discrepancies between external references. Choose the convention used by this product and identify the approving reviewer; do not combine conflicting conventions.
- [ ] **P01-08** Define acceptance examples and invalid-input behavior for every converter direction, special-date input, and unsupported profile/range.

**Outputs:** approved calendar contract, correction log, content model, reviewed fixtures, declared support interval.

**Acceptance gate:** the reviewer approves the rules and fixture sources. The original JSON alone is insufficient. Without approval, production conversion and religious-day claims remain blocked.

## Phase 02 — UI/UX research, prototype, and design system

**Purpose:** choose an interface through concrete journeys and user testing.

- [ ] **P02-01** Map onboarding, today lookup, month browsing, event discovery, Nabur lookup, conversion, reminder setup, widget entry, and settings changes.
- [ ] **P02-02** Produce low-fidelity layouts for the four primary destinations: Calendar, Observances, Converter, and Settings. Integrate today's summary into Calendar.
- [ ] **P02-03** Explore three visual directions from [UI_UX_SPEC.md](docs/UI_UX_SPEC.md). Compare the same screens in each direction, in light/dark and Persian/English.
- [ ] **P02-04** Define reusable colors, typography, spacing, icons, surfaces, calendar states, form errors, motion, and native adaptations. Review any religious symbolism.
- [ ] **P02-05** Build an interactive high-fidelity prototype including error, empty, permission-denied, large-text, special-day, and multi-event states.
- [ ] **P02-06** Run moderated usability sessions with 5–8 representative users. Observe task completion, confusion about date systems, mistaken taps, and difficulty setting reminders.
- [ ] **P02-07** Revise the design, freeze the V1 screen/state inventory, and supply component specs, content hierarchy, accessibility semantics, and assets.

Prototype evidence: a dependency-free mobile web prototype is available in [prototype](prototype/). It covers Calendar, Observances, Converter, Qibla, and Settings using the screenshots as partial references, and marks imported religious dates as unverified. P02-05 is partially explored; visual-direction comparison, reviewer input, accessibility review, and usability sessions remain open, so Phase 02 is not complete.

**Outputs:** selected visual direction, prototype, design tokens, screen/state specifications, usability findings.

**Acceptance gate:** users can identify today's Roj/Nabur, find the next observance, convert a date, and set a reminder without coaching in at least 80% of observed attempts. This is a directional small-sample target, not a population estimate. No critical date-system confusion or accessibility obstacle remains unresolved.

## Phase 03 — Project foundation and capability prototype

**Purpose:** prove the technical approach before implementing all screens.

- [ ] **P03-01** Generate a current supported KMP project and record Kotlin, Compose, Gradle, Android plugin/SDK, Xcode, Swift, and minimum OS compatibility. Pin a tested stable matrix.
- [ ] **P03-02** Establish logical shared calendar/content/storage packages, shared presentation/UI, Android host, iOS host, and native widget targets. Avoid creating a Gradle module for every screen.
- [ ] **P03-03** Configure supported Gradle-to-Xcode integration for local builds. Produce an XCFramework only if the chosen integration or distribution workflow needs it.
- [ ] **P03-04** Build a thin vertical prototype: Persian today summary, a populated month grid, one converter input, a native notification route, and one widget on each platform.
- [ ] **P03-05** Test RTL/bidirectional text, Persian glyph shaping, 200% text, VoiceOver/TalkBack, back navigation, keyboard handling, and release-build performance on real devices.
- [ ] **P03-06** Test a narrow shared-engine bridge inside the iOS widget extension. Measure initialization and memory before choosing direct computation or generated snapshots.
- [ ] **P03-07** Set up reproducible local setup, format/lint checks, shared tests, Android builds, and macOS CI for iOS compilation/tests. Keep signing credentials out of the repository.
- [ ] **P03-08** Record the shared/native UI decision, measured constraints, reference devices, build commands, and performance budgets. Use a native screen only where the prototype shows a material issue.

**Outputs:** working scaffold, device evidence, compatibility matrix, CI, architecture decisions, setup README.

**Acceptance gate:** the prototype runs on Android and iOS devices and demonstrates the required text and native integration. A simulator-only build or an Android-only demonstration does not close this gate.

The current browser prototype does not close P03-01 or P03-02. The native KMP project and Android/iOS SDK setup remain outstanding.

## Phase 04 — Shared calendar engine and date converter

**Purpose:** provide one deterministic implementation for the app, reminders, and widgets.

- [ ] **P04-01** Implement immutable civil and religious date types, profile identity, era identity, ordinary/special-day variants, and typed validation errors.
- [ ] **P04-02** Implement Gregorian and Solar Hijri conversion using a selected, licensed, validated algorithm/library. Document the supported interval and leap-year behavior.
- [ ] **P04-03** Implement the reviewed religious profile's date mapping. Keep profile rules separate from localization and UI.
- [ ] **P04-04** Implement Roj/Mah lookup, Gatha/intercalary behavior, and the approved Nabur rule using stable IDs.
- [ ] **P04-05** Implement occurrence generation for single-day and multi-day observances using the recurrence calendar specified for each event.
- [ ] **P04-06** Implement date conversion through a canonical day representation and return clear invalid-date, unsupported-date, and unsupported-profile results.
- [ ] **P04-07** Inject clock and time zone when resolving “today.” Use calendar-date operations for day changes rather than adding a fixed 24 hours.
- [ ] **P04-08** Run sourced fixture checks, full supported-range round trips, continuity tests, invalid-date cases, year boundaries, and timezone boundary tests on shared targets.

**Outputs:** pure engine, converter, occurrence generator, shared tests, platform-callable facade.

**Acceptance gate:** all reviewed expected-date fixtures pass; no supported date is missing or maps ambiguously; round-trip and boundary checks pass on JVM and Kotlin/Native. Round trips cannot replace independent reference-date tests.

## Phase 05 — Content, preferences, and local storage

**Purpose:** ship reviewed content that works immediately and can evolve without corrupting user settings.

- [ ] **P05-01** Define a versioned content-pack schema with separate schema, content, and engine-rule versions. Include profile applicability, sources, and review status.
- [ ] **P05-02** Implement a lossless legacy import boundary. Retain source row/date/values; curate combined titles into separate event records after review.
- [ ] **P05-03** Validate IDs, profile/rule references, dates/ranges, categories, languages, and field semantics at build time. Reject invalid production packs.
- [ ] **P05-04** Bundle the reviewed initial pack. Build local date/category/search indexes and resolve multiple occurrences per day.
- [ ] **P05-05** Persist language, numerals, theme, week start, grid calendar, time zone, onboarding version, followed events, and reminder preferences.
- [ ] **P05-06** Normalize Persian/Arabic search variants and whitespace in the search index without rewriting authoritative display text.
- [ ] **P05-07** Version local settings/storage and test upgrade migrations. Store user preferences separately from replaceable bundled content.
- [ ] **P05-08** Expose content version/source information and a correction-feedback route. Keep optional future content downloads outside the required launch path.

**Outputs:** validated bundled pack, local repositories/indexes, persistent settings, migration tests.

**Acceptance gate:** first launch in airplane mode loads the calendar and content; preferences survive restarts; malformed content fails validation; migrating or replacing content retains event-follow/reminder settings.

## Phase 06 — Application screens and primary journeys

**Purpose:** implement the approved interactions with complete states.

- [ ] **P06-01** Build skippable onboarding with language and a clearly named reviewed profile. Ask for notification permission only when the user enables reminders.
- [ ] **P06-02** Build Calendar with immediate today summary, month/year navigation, grid-calendar label, today reset, month grid, and accessible agenda mode.
- [ ] **P06-03** Build day details with independently labeled dates, Roj/Mah or special-day identity, Nabur explanation, all observances, and clear reminder/share actions.
- [ ] **P06-04** Build Observances with upcoming events, category filters, search, Nabur view, multi-day grouping, and honest empty/result states.
- [ ] **P06-05** Build event details with title, date/range, category, reviewed explanation, source, available language, and follow/reminder controls.
- [ ] **P06-06** Build Converter with source-calendar selection, accessible date entry, religious special-day input, immediate labeled results, reset-to-today, and text sharing.
- [ ] **P06-07** Build Settings and reminder preferences, including permission status and settings links, language/numerals, appearance, week start, fixed-zone indication, content version, and support.
- [ ] **P06-08** Restore navigation/selected-date state appropriately after process death; preserve selection when changing grid calendar or language. Translate routes through stable IDs.
- [ ] **P06-09** Verify all states in the design specification on both platforms: dark/light, RTL/LTR, compact/large screen, large text, no event, multiple events, invalid input, and denied permissions.

**Outputs:** complete V1 app journeys and screen-state evidence.

**Acceptance gate:** every required journey can be completed on each platform, offline, using touch and assistive technology. Visible dates and labels agree with the engine; no religious day is inferred in UI code.

## Phase 07 — Local reminders and native system integration

**Purpose:** make reminders useful, transparent, and consistent with the chosen date policy.

- [ ] **P07-01** Create a shared reminder-intent model for Nabur/category/event selection, lead time, preferred local time, quiet hours, and time-zone policy.
- [ ] **P07-02** Define scheduling rules for multi-day events: once before the range by default; daily reminders only when explicitly enabled. Specify collision/quiet-hour behavior.
- [ ] **P07-03** Implement native notification authorization, Android channels and denied states, and iOS authorization states. Calendar use remains available without notification permission.
- [ ] **P07-04** Implement platform scheduling from shared occurrences. Respect pending-request capacity, cancellation, stable identifiers, and a rolling horizon; show what is actually scheduled.
- [ ] **P07-05** Reconcile reminders after foreground entry, settings/profile/content changes, clock/zone changes, and Android reboot/app replacement where supported.
- [ ] **P07-06** Prevent duplicate requests and obsolete reminders. Recompute from local dates rather than repeating fixed-duration alarms across seasonal clock changes.
- [ ] **P07-07** Implement cold/warm deep links from notifications, widgets, and app-icon long-press shortcuts to Converter, Nabur, and the intended day/event.
- [ ] **P07-08** Test disabled/re-enabled permissions, capacity exhaustion, restricted background execution, long periods without opening the app, event overlaps, and quiet-hour conflicts.

**Outputs:** native schedulers, shared scheduling decisions, permission UI, routes, reliability evidence.

**Acceptance gate:** scheduled requests match the selected rules without duplicates; canceled/changed preferences remove old requests; the app reports permission/capacity issues accurately. Do not promise indefinite reminder coverage or exact delivery when the OS can defer or suppress it.

## Phase 08 — Native Android and iOS widgets

**Purpose:** provide readable, trustworthy date information outside the app.

- [ ] **P08-01** Specify small and medium widgets in both languages/themes: civil date, religious identity, Nabur badge, and limited observance text appropriate to space.
- [ ] **P08-02** Implement Android Glance widgets with persistent inputs, responsive layouts, empty/error states, and correct destination links.
- [ ] **P08-03** Implement SwiftUI/WidgetKit providers and configure a product-owned App Group on both app and extension. The original group identifier is a placeholder until verified.
- [ ] **P08-04** Use the Phase 03 bridge decision. Prefer lightweight access to the shared engine; if snapshots are required, specify their supported horizon and expired/missing behavior.
- [ ] **P08-05** Generate date-boundary timelines or best-effort scheduled refreshes from local data. Request refresh after relevant app settings/content changes; avoid per-minute polling.
- [ ] **P08-06** Add launcher/home-screen long-press actions with platform-appropriate terminology. Enable interactive widget actions only where supported and needed.
- [ ] **P08-07** Test multiple widget instances, app not running, resize, dark mode, RTL, calendar/year rollover, time-zone change, and delayed system refresh.

**Outputs:** Android/iOS small and medium widgets, entitlements, update strategy, native accessibility checks.

**Acceptance gate:** the provider computes or selects the correct data for the requested date using persisted settings; the displayed date is explicit; stale/expired inputs never masquerade as a verified current date. System refresh timing is documented as best effort.

## Phase 09 — Upgrade migration and production quality

**Purpose:** make the rebuild safe for existing users and measurable on real devices.

- [ ] **P09-01** Implement and test migrations from the supported legacy versions using representative stored data. Preserve supported settings and reminder choices; re-request OS authorization where required.
- [ ] **P09-02** Classify old/new date differences as approved corrections, unsupported legacy behavior, or regressions. Keep an explanation for changed observance dates/flags.
- [ ] **P09-03** Run shared, native integration, migration, and key-journey UI tests in CI. Add targeted regression cases for defects found during review.
- [ ] **P09-04** Complete manual TalkBack/VoiceOver, large-text, reduced-motion, contrast, keyboard, and RTL/bidirectional-text audits.
- [ ] **P09-05** Measure release-build startup, month changes, conversion, scroll smoothness, memory, and battery on named Android/iOS reference devices.
- [ ] **P09-06** Test airplane mode, denied permissions, process termination, low memory, device restart, latest and minimum supported OS, tablet layouts, and time-zone/day changes.
- [ ] **P09-07** Review privacy behavior, local storage/backup policy, dependency licenses, diagnostic redaction, and any optional telemetry. Remove unnecessary permissions and secrets.
- [ ] **P09-08** Fix all calendar correctness, data-loss, blocking accessibility, crash, and primary-journey defects before beta approval.

**Outputs:** migration evidence, automated results, device matrix, accessibility and performance reports.

**Acceptance gate:** release criteria in Section 7 are met or explicitly revised with evidence before release. No unresolved critical calendar or data-loss defect is accepted.

## Phase 10 — Beta, store preparation, and release

**Purpose:** release the verified product through the existing upgrade channel where possible.

- [ ] **P10-01** Prepare signed Android App Bundle and iOS archive; validate application identities, extension entitlements, production configuration, and version numbering.
- [ ] **P10-02** Run Android beta and TestFlight with representative users, including existing-app upgrade cases. Collect feedback with clear build and content versions.
- [ ] **P10-03** Prepare accurate Persian/English store copy, real screenshots, icon/assets, support details, privacy disclosures, accessibility information, and release notes.
- [ ] **P10-04** Recheck current Google Play/App Store SDK, submission, permission, privacy, and testing requirements at release time. Do not freeze store requirements from this planning date.
- [ ] **P10-05** Complete a release rehearsal: clean install, upgrade, reminder setup, widget placement, airplane-mode use, and deep-link entry on production builds.
- [ ] **P10-06** Submit builds, handle review feedback, and use a monitored/staged rollout where available. Record the shipped engine/content versions and approved calendar profile.
- [ ] **P10-07** Prepare a remediation plan: pause rollout, preserve settings, revert a separately updateable pack when possible, and ship a forward hotfix for installed binaries.

**Outputs:** beta findings, store-ready assets/builds, production release record, remediation plan.

**Acceptance gate:** beta has no unresolved release blockers; production builds match verified builds; disclosures match actual behavior; release ownership and support route are active. Installed store binaries cannot be assumed to support instant rollback.

## Phase 11 — Maintenance and expansion

**Purpose:** keep calendar trust and code maintainability after launch.

- [ ] **P11-01** Establish a calendar/content correction process with evidence, reviewer, affected profiles/dates, correction notes, and fixture updates.
- [ ] **P11-02** Document how to build, sign, test, update content, add a reviewed profile, diagnose reminders/widgets, and migrate settings.
- [ ] **P11-03** Monitor reports of wrong dates, crashes, failed upgrades, stale widgets, and confusing reminders; include engine/content/OS versions in user-approved diagnostics.
- [ ] **P11-04** Rank V1.1 work using user feedback and actual support burden. Require separate acceptance criteria for every additional tradition or Gah feature.
- [ ] **P11-05** Review dependencies and OS compatibility periodically; preserve the fixture suite when changing calendar algorithms or date libraries.

**Outputs:** operational runbook, correction ownership, support process, prioritized next-release backlog.

**Acceptance gate:** another developer can reproduce a build and a content correction; the product has an identified owner for calendar accuracy.

## 7. Quality and release targets

These are proposed project targets, not results already achieved. Record the test devices, datasets, builds, and measurement methods in Phase 03. Tighten or adjust budgets using evidence.

| Area | Acceptance target |
| --- | --- |
| Accuracy | 100% of reviewer-approved independent fixtures pass; supported interval has no conversion gaps or ambiguous dates |
| Data | Every production event has a stable ID, recurrence calendar, source/review status, and defined holiday semantics |
| Offline | Calendar, bundled content, search, conversion, and settings work on first launch without a network |
| Startup | Today screen interactive within 1.5 seconds at p95 on agreed reference devices in a release build |
| Month navigation | Visible month change within 100 ms at p95 after content is loaded |
| Conversion | Shared date conversion within 10 ms at p95, excluding UI animation and system startup |
| Smoothness | No repeated stalls during normal month browsing/list scrolling; investigate captured frame traces |
| Accessibility | No critical obstacle with VoiceOver/TalkBack; usable at 200% text and platform accessibility sizes using agenda fallback |
| Touch targets | Aim for at least 48 dp on Android and 44 pt on iOS; choose agenda/reflow where seven grid columns cannot fit |
| Contrast | Project baseline: 4.5:1 normal text, 3:1 large text and meaningful non-text indicators |
| Localization | Primary journeys and dates verified in Persian/English and Persian/Latin digits; no clipped or reordered mixed text |
| Reminder integrity | No duplicate or obsolete scheduled requests in tested scenarios; accurate permission/horizon status |
| Widget integrity | Correct provider data for requested date; explicit date and safe stale/expired handling |
| Upgrade | Supported legacy data preserved or deliberately translated; repeat migration does not duplicate or reset it |
| Reliability | Zero open critical/high defects affecting calendar accuracy, data loss, crashes, or required journeys |
| Privacy | Required core works without account or tracking; permissions and any diagnostics are documented accurately |

A calendar whose conversions are wrong cannot be released merely because its interface is polished.

## 8. Main risks and responses

| Risk | Response |
| --- | --- |
| Wrong calendar/tradition assumed from legacy JSON | Approve the profile/rule contract and independent fixtures before calendar implementation |
| Legacy year/leap behavior is undocumented | Inspect the old engine and trusted annual calendars; document gaps and accepted corrections |
| Holiday flag has multiple possible meanings | Preserve its raw value until reviewed; keep observance and holiday/work policy separate |
| Shared UI fails Persian/accessibility needs on iOS | Verify in Phase 03; adapt the affected screen natively with the same shared engine/state contract |
| iOS widget bridge has excessive initialization/memory cost | Measure early; select bounded generated snapshots if required, with visible expiry behavior |
| Background work fails to run at exact boundaries | Native timelines/scheduling, foreground reconciliation, explicit dates, honest status |
| Existing signing/app identity is unavailable | Resolve early; document new-install impact and available export/import migration |
| Feature growth delays accuracy and launch | Keep multiple traditions, Gah, accounts, and community feeds behind later milestones |
| Religious content lacks provenance or rights | Require sources, review ownership, and redistribution verification before shipping |

## 9. Working rules for implementation

1. Read this plan and the relevant supporting specification before starting a phase.
2. Do not invent calendar rules, meanings, leap dates, or review approvals.
3. Keep calculations shared and platform integrations native. Avoid duplicate date algorithms in Swift/widgets/UI.
4. Keep raw imported content and approved corrections distinguishable.
5. Preserve application identity and user data where supported; migrations must be repeatable.
6. Use minimal dependencies and package boundaries justified by testing, reuse, or platform integration.
7. Add tests for calendar rules, migrations, and important failure paths. Use visual/manual checks for low-impact styling.
8. Close a task only with reviewable output and the appropriate verification evidence.
9. Report completed task IDs, relevant checks, unresolved decisions, and the next dependency.
10. Recheck platform/library/store requirements during implementation and before release.

## 10. Supporting specifications

- [UI/UX specification](docs/UI_UX_SPEC.md)
- [Architecture and calendar/data contracts](docs/ARCHITECTURE.md)
- [Legacy data audit](docs/CALENDAR_DATA_AUDIT.md)
- [References and evidence limits](docs/SOURCES.md)
