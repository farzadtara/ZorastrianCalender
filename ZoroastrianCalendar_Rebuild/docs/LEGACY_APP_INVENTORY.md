# Legacy App Inventory from Supplied Screenshots

**Evidence:** eight Android screenshots supplied in the package root, dated in their filenames 2026-10-11.

**Scope:** visual observations only. The screenshots do not establish runtime behavior, data storage, or calendar correctness. Persian labels are paraphrased where useful.

## Observed screens

| Screenshot | Visible screen and evidence |
| --- | --- |
| [`SmartSelect_20261011_105855_Zoroastrian Calendar.jpg`](../SmartSelect_20261011_105855_Zoroastrian%20Calendar.jpg) | Calendar month view for Mehr 1405, with a second year value shown as 3764, a seven-column date grid, colored/underlined date markers, a selected date, and Calendar/Occasions tabs. A selected-day card shows dates in three calendars. |
| [`Screenshot_20261011_105920_Zoroastrian Calendar.jpg`](../Screenshot_20261011_105920_Zoroastrian%20Calendar.jpg) | Date converter with Solar Hijri, Gregorian, and Islamic Hijri choices. The input uses year/month/day selectors. The result shown is 19 Mehr 1405, 11 October 2026, and 29 Rabi al-Thani 1448. These displayed values are evidence of the UI, not an accuracy check. |
| [`Screenshot_20261011_105927_Zoroastrian Calendar.jpg`](../Screenshot_20261011_105927_Zoroastrian%20Calendar.jpg) | Qibla compass screen. A Persian notice says to enter a location in settings to show the sun/moon position and direction. |
| [`SmartSelect_20261011_105910_Zoroastrian Calendar.jpg`](../SmartSelect_20261011_105910_Zoroastrian%20Calendar.jpg) | Navigation drawer with Calendar, Date Conversion, Qibla, Settings, About, and Exit entries, plus a seasonal illustration. |
| [`Screenshot_20261011_110025_Zoroastrian Calendar.jpg`](../Screenshot_20261011_110025_Zoroastrian%20Calendar.jpg), [`Screenshot_20261011_110029_Zoroastrian Calendar.jpg`](../Screenshot_20261011_110029_Zoroastrian%20Calendar.jpg) | Widgets and Notifications settings. Visible options include widget text/background colors, date display style, widget clock, widget alignment and sizes, Iran time, alert enablement, Nabur alerts, event display, and lock-screen alerts. |
| [`Screenshot_20261011_110036_Zoroastrian Calendar.jpg`](../Screenshot_20261011_110036_Zoroastrian%20Calendar.jpg), [`SmartSelect_20261011_110019_Zoroastrian Calendar.jpg`](../SmartSelect_20261011_110019_Zoroastrian%20Calendar.jpg) | Interface and Calendar settings. Visible options include language, theme, Persian digits, occasion visibility, personal events, calendar display priority, astronomical information, week number, 24-hour time, Islamic-calendar adjustment, week start, and days off. |

## Migration candidates and limits

The screenshots identify settings and features that should be investigated for migration: language/theme and numeral preferences; calendar display and week settings; widget appearance, date/time options; alert preferences; and personal events. The screenshots do not show whether those values are persisted, how personal events are represented, or which settings are safe to migrate.

The calendar and converter screens show date mappings and event markers, but they do not prove the selected calendar profile, year convention, recurrence rules, or conversion accuracy. Keep the legacy JSON and its audit as separate evidence until those rules are reviewed.

### Conversion-provider discrepancy

The converter screenshot shows 11 October 2026 as 29 Rabi al-Thani 1448. The browser prototype's explicitly labeled Umm al-Qura provider returns 30 Rabi al-Thani 1448 for that same civil date. This is a one-day difference between two calendar conventions/providers, not a correction to either source. Resolve the intended Islamic date convention and obtain reviewer-approved fixtures before claiming parity.

## Listing search

[Gahshomar Zoroastrian Calendar on Google Play](https://play.google.com/store/apps/details?id=com.gaso.calendar) was considered because its listing describes features that overlap with the screenshots. The user confirmed that it is not this app. Its package ID and claims are excluded from the product inventory. The target app name and store listing remain unidentified.

## Preliminary parity matrix

This matrix records observations against the current proposal. It is not a product decision or migration approval.

| Area | Legacy evidence | Proposed V1 direction | Migration or decision needed |
| --- | --- | --- | --- |
| Calendar view | Solar Hijri month grid, marked dates, and a selected-day card with three displayed calendar dates. | Solar Hijri default with an optional Gregorian grid; religious date labeled separately. | Confirm legacy date rules and approved profile before importing mappings or marked dates. |
| Date converter | Solar Hijri, Gregorian, and Islamic Hijri choices with year/month/day selectors. | Gregorian, Solar Hijri, and the reviewer-approved religious profile. | The Islamic Hijri selector is not evidence of a Zoroastrian profile. Product owner must decide whether it belongs in V1. |
| Occasions and Nabur | Colored/underlined calendar markers; separate settings for occasions and Nabur alerts; legacy JSON has event titles and flags. | Reviewed event occurrences and opt-in Nabur reminders with separate holiday semantics. | Resolve source-row conflicts, titles, recurrence, and holiday meaning with a calendar reviewer. |
| Widgets and alerts | User-selectable colors, date style, clock, time zone, alignment, widget sizes, and alert visibility. | Native Android/iOS widgets and reminders with explicit permission and refresh status. | Identify stored preference keys and scheduled-request behavior before defining migration. |
| Personal events | A setting controls personal-event display; no event editor or stored records are shown. | Personal-event behavior is not explicitly settled in the current V1 scope. | Confirm whether personal events are in V1 and inspect the legacy storage/export format. |
| Qibla and astronomy | Qibla compass screen and an astronomy-information setting; location is requested for displayed positions. | Gah/location calculations are listed as later work; Qibla is not in the proposed V1 scope. | Product owner must decide whether to retain these features and whether location-dependent data/settings need migration. |
| Interface preferences | Language, theme, Persian digits, week start, week number, 24-hour time, and days-off settings are visible. | Persian-first with English support and configurable display preferences. | Capture actual values and persistence format; migrate only settings with an approved equivalent. |

The following discovery inputs remain unavailable in this package:

- Full app walkthrough or binary, including screens not captured here.
- Exact target app name/store listing, direct user feedback, and known-defect history.
- Signing ownership, release/version history, and Android/iOS application identities.
- Source code, preference/database formats, notification schedule details, and personal-event export behavior.
- Product-owner decisions and calendar-reviewer-approved profile rules and independent date fixtures.

## Phase status

These screenshots provide a partial visual inventory and preliminary parity matrix for P00-01 and P00-06. The similar Gahshomar listing was rejected by the user; the target listing remains unidentified. P00-01 remains open because the screenshots do not cover every screen or establish stored data and behavior, and direct user feedback and known-defect history are unavailable. P00-02 through P00-06 remain open; no signing details, migration data, user research, product approvals, or final parity decisions are inferred from screenshots.
