# Zoroastrian Calendar — UI/UX Specification

**Revision:** 2.0 · **Date:** 2026-10-11  
**Status:** design brief and acceptance specification; final screen designs are future deliverables.

## 1. Experience principles

The interface should feel calm, clear, and useful every day. Calendar information is the product; decoration supports it.

- Lead with today's date and religious identity, then show what needs attention.
- Label Gregorian, Solar Hijri, and the religious profile explicitly. Similar month names do not make the calendars interchangeable.
- Reveal explanations on demand; preserve a compact daily overview.
- Show observances, Nabur, selection, and today through more than color.
- Let users browse, convert, and read immediately, without an account or permission prompt.
- Respect Persian typography and religious terminology. Use reviewed cultural symbols sparingly.
- Preserve familiar back navigation, system sheets, notification authorization, and home-screen behavior on each platform.

## 2. Primary navigation

Use four destinations on phones; adapt to a rail/sidebar when screen size makes it useful.

| Destination | Purpose | Main actions |
| --- | --- | --- |
| Calendar / تقویم | Today and date browsing | Change month, choose date, return to today, open details |
| Observances / مناسبت‌ها | Upcoming events and Nabur | Search, filter, choose Nabur view, open event |
| Converter / تبدیل تاریخ | Date translation | Select source calendar, enter date, review/share results |
| Settings / تنظیمات | Display and reminder preferences | Language, digits, theme, week start, time zone, reminders |

The Persian labels are proposed UI copy and need language review. “Today” is integrated into Calendar rather than adding a fifth tab. Nabur remains prominent within Observances and via a direct shortcut.

In RTL, mirror navigation placement and directional controls appropriately; establish chronology and swipe behavior through testing. Never reverse the internal date model or arbitrarily flip numeric date strings.

## 3. Screen specifications

### 3.1 First launch and onboarding

- Start with the detected language, allow changing it, and provide a clear continue/skip action.
- Explain the reviewed religious profile in everyday terms. Offer only profiles that are actually supported.
- If V1 has one profile, show its name and explain how to change relevant settings; avoid a redundant selection step.
- Use a short explanation of Nabur and observance indicators rather than a long tutorial carousel.
- Keep the app usable if the user skips all optional steps.
- Offer reminder setup after the user experiences the calendar or taps a reminder action.
- Do not request location for the V1 calendar; solar/Gah features have separate scope.
- On upgrade, retain reviewed preferences and show a brief relevant “What changed” explanation.

**Acceptance:** calendar access requires at most two lightweight setup decisions; no permission is a condition of browsing.

### 3.2 Calendar

Information order:

1. Compact title and clear primary-calendar label.
2. Today summary: civil date, prominent religious day identity, and Nabur/event badges.
3. Month/year controls, explicit previous/next actions, jump-to-date, and a reachable today reset.
4. Weekday headings and month grid.
5. Selected-day summary and observances.

Grid rules:

- Make the civil day number the primary cell content. Show the full Roj name in the selected-day summary rather than forcing long names into every cell.
- Differentiate today, selected day, adjacent-month day, Nabur, and event presence with documented states.
- Use a limited marker count plus an overflow count where several events coincide.
- Provide a legend or an accessible explanation for markers.
- Do not label religious observances as official public holidays without an approved classification.
- Keep selected date stable when switching Gregorian/Solar Hijri grids.
- Provide an accessible agenda alternative with chronological dates and readable labels.
- At compact widths or large text sizes, reflow or use agenda mode instead of shrinking targets or overlapping them.
- Allow month navigation through visible controls as well as gestures. Gestures cannot be the only way to change a month.

**Acceptance:** a user can identify today and selected date without relying on color, reach a day without accidental adjacent taps, and return to today from any browsed month.

### 3.3 Day details

Use a full-screen view or an expandable sheet that remains usable with large text.

- Lead with the selected date and its religious identity.
- Show the religious profile and year era in human-readable language.
- Display Gregorian and Solar Hijri dates as separate labeled rows.
- On Gatha/intercalary days, display the reviewed special-day name rather than an ordinary Roj/Mah placeholder.
- Show Nabur as a meaningful badge plus a short reviewed explanation.
- List every event on that date separately, with category and range information.
- Indicate “Day 2 of 5” only when the source defines a five-day range.
- Offer a contextual reminder action and native text sharing.
- Use “No observances listed for this date” for an empty day; this does not assert that no observance exists anywhere.

**Acceptance:** overlapping events are all visible and individually actionable. A special day is distinguishable from an ordinary religious month day.

### 3.4 Observances and Nabur

- Default to an upcoming agenda grouped by civil date.
- Search reviewed names, aliases, and available descriptions.
- Use category chips for festivals, Gahambars, pilgrimages, remembrance, and Nabur only after content classification is approved.
- Include an obvious Nabur view/filter and show the next Nabur date.
- Summarize a multi-day event as one range in the upcoming list; show each day in Calendar. Deduplicate event cards.
- Include a clear-filters action and visible selected-filter state.
- Support browsing beyond “today” within the verified date interval.
- Provide distinct messages for no upcoming results, no search matches, and no results after filtering.
- Do not use the legacy holiday boolean as the only visibility filter.

**Acceptance:** a user can find the next Nabur day and an observance by name without reading a full year.

### 3.5 Event details

- Show title, category, civil date/range, applicable religious profile, and reviewed religious date/range.
- Provide a concise description only when sourced and reviewed.
- Offer follow/reminder controls with meaningful status.
- Put references and content provenance in an expandable section.
- If English content is absent, show the reviewed original title and a short language indication.
- Preserve source terminology; do not generate religious instructions from the title.
- Keep recurrence/profile information clear when two similarly named festivals differ.

**Acceptance:** users know which occurrence they are viewing and whether a reminder is active, denied, or not scheduled.

### 3.6 Converter

Replace the mandatory tri-wheel concept with a structured flow:

1. Select the source calendar.
2. Enter or choose a valid date with explicit year/month/day labels.
3. See the other two labeled results immediately once input is complete and valid.

Requirements:

- Display the religious profile and era when religious dates are entered or shown.
- For religious input, support ordinary dates and separate Gatha/intercalary selections. Do not invent month 13.
- Offer a native-style picker where it works correctly and a readable field-based alternative.
- Accept Persian, Arabic, and Latin digits in numeric fields; apply the selected display numeral style to output.
- Use clear day/month names to avoid ambiguous numeric formats.
- Validate without silently changing an invalid day when month/year changes.
- Show a precise error for invalid day, unsupported year, or an unavailable profile.
- Provide reset-to-today and text sharing through the system share sheet.
- Keep all result dates anchored to the same canonical day and state the selected time zone only when relevant to “today.”
- Preserve entered date when changing language or digits.

**Acceptance:** users can tell which calendar they entered and which each result belongs to. Assistive technology can edit every field without a wheel gesture.

### 3.7 Reminder preferences

- Use opt-in controls for Nabur, categories, and individual followed events.
- Let the user choose reminder time and lead time in everyday language.
- Default multi-day events to one reminder before the start. Offer daily reminders explicitly.
- Show a sample notification using reviewed sample content.
- Provide permission state and a system-settings link when the OS blocks notifications.
- Show the next scheduled reminder and the actual scheduling horizon when the platform's pending capacity limits coverage.
- Explain any unscheduled requests with a short reason and recovery action.
- Offer quiet hours, with a defined and visible shift/skip behavior.
- Avoid repeatedly asking for a permission the user has denied.

**Acceptance:** an enabled preference and a successfully scheduled request are not presented as the same state. Users can stop reminders from the app.

### 3.8 Settings and About

Group settings by purpose: display, dates/time zone, reminders, and app/content information.

- Language and numeral choices are independent.
- Support light, dark, and system appearance.
- Default Persian week start to Saturday as a proposal; allow user choice and preserve it independently of language.
- Label fixed time zone clearly and allow returning to the device zone.
- Make primary grid calendar and religious profile separate concepts.
- Show content source/version in About rather than on everyday calendar cards.
- Provide support, correction feedback, and privacy information.
- Confirm broad resets because they remove preferences. Simple display changes apply immediately.

**Acceptance:** changing a display setting retains the selected date, event follows, and reminder choices.

### 3.9 Widgets and app-icon shortcuts

| Surface | Content | Interaction |
| --- | --- | --- |
| Small widget | Explicit date, religious day identity, concise Nabur/event marker | Open that day |
| Medium widget | Date, day identity, Nabur, limited event summary or next event | Open day or visible event |
| Long-press Converter shortcut | Platform shortcut label/icon | Open Converter with today selected |
| Long-press Nabur shortcut | Platform shortcut label/icon | Open upcoming Nabur view |

- Avoid dense month grids in small widgets.
- Keep long titles readable with a sensible maximum line count; retain full titles in the app.
- For expired/missing data, show an honest dated state and an “Open app” action.
- Test actual widget sizes and launcher behavior, not only design-frame dimensions.
- Use static/deep-link interactions as the baseline; add buttons only where platform/OS support and usefulness justify them.

## 4. Visual direction

Explore the same Calendar, Day Details, and Converter screens in three directions before selecting one.

| Direction | Character | Use |
| --- | --- | --- |
| A — Warm paper | Warm neutral surfaces, strong ink text, restrained copper accent, soft geometry | Recommended starting direction for a calm daily calendar |
| B — Evening | Deep neutral surfaces, warm light accents, strong date hierarchy | Compare as a distinct visual identity, not merely dark-mode screenshots |
| C — Contemporary minimal | Clean white/neutral surfaces, clear lines, limited accent, generous typography | Compare for maximum simplicity and compact information |

These are proposals, not an existing brand specification. The recommended starting palette:

| Semantic token | Light candidate | Dark candidate |
| --- | --- | --- |
| Background | #F7F3E8 | #141D23 |
| Surface | #FFFFFF | #202C34 |
| Primary text | #24323C | #F7F3E8 |
| Secondary text | #58646C | #B5C0C8 |
| Primary action | #9C432C | #F2B49B |
| Nabur label | #276452 | #A6D4BF |
| Festival label | #81520C | #EACB8B |
| Selection fill | #EEDCCE | #45352F |

Validate actual foreground/background pairs in the implementation, including selected/disabled/focus states. The candidate palette is not an accessibility certification. Decorative colors do not automatically qualify as text colors.

Design requirements:

- Use a licensed Persian font with correct shaping and clear numerals; choose it through real-device review.
- Start body text near 16–17 platform units and scale with the user's preferences. Avoid compact labels that require disabling text scaling.
- Use a spacing scale based on 4/8 units, with sufficient separation around controls.
- Use consistent icon stroke and restrained surface elevation.
- Allow one strong focal point per screen: date identity on Calendar, event title on details, entered date on Converter.
- Use subtle, short transitions; honor reduced-motion settings. Avoid decorative looping animations in daily use.
- Review any Faravahar, fire, temple, or other religious imagery with the content/cultural reviewer.

## 5. RTL, bidirectional text, and localization

- Use logical start/end spacing and alignment rather than hard-coded left/right values.
- Mirror directional arrows where their meaning follows reading direction; do not mirror non-directional icons.
- Define weekday column order and chronology for each locale/week-start setting.
- Test Persian strings mixed with Gregorian dates, English titles, punctuation, parentheses, and Latin digits.
- Use locale-aware formatting and bidirectional isolation where appropriate.
- Preserve diacritics, source spelling, and cultural terminology in display text.
- Normalize Arabic/Persian character variants for search only.
- Allow translated labels to wrap naturally; test long event names and special-day names.
- Language changes must not alter the underlying selected date or calendar profile.
- Capture both RTL and LTR screen references for developer verification.

## 6. Accessibility

The project targets at least 48 dp touch regions on Android and 44 pt on iOS, consistent with [Android accessibility guidance](https://developer.android.com/develop/ui/compose/accessibility/api-defaults) and [Apple's design tips](https://developer.apple.com/design/tips/).

- All controls have meaningful accessible labels, values, and selected/disabled state.
- A calendar cell exposes date, selection/today status, Nabur, and event count as a coherent announcement.
- Screen-reader focus follows the visible information order in both languages.
- Provide visible month-navigation buttons and an agenda alternative.
- Support 200% text and platform accessibility sizes without hiding primary actions.
- Do not squeeze seven columns into non-overlapping minimum targets when the available width is insufficient. Reduce safe outer margins, reflow, or use agenda mode.
- Use the project contrast baseline of 4.5:1 normal text and 3:1 large text/meaningful indicators.
- Provide text/shape/icon distinctions for statuses; color alone is insufficient.
- Preserve focus when lists refresh or filters change; announce important results without repeated noise.
- Honor reduced motion; haptics and audio are optional reinforcement.
- Test on real VoiceOver/TalkBack sessions, including input errors, sheets, picker alternatives, and widget entry.

## 7. Required screen states

| Context | States to design and test |
| --- | --- |
| Calendar | Today selected, another day selected, adjacent month, no event, many events, Gatha/intercalary date |
| Events | Upcoming results, search matches, no matches, all filters cleared, multi-day overlap |
| Converter | Valid ordinary date, special date, incomplete input, invalid date, out-of-range year |
| Notifications | Not requested, allowed, denied, limited scheduling horizon, unscheduled item |
| Widgets | Normal content, multiple events, missing data, expired snapshot, long title |
| Layout | Persian/English, light/dark, normal/large text, compact phone, tablet/foldable |
| Upgrade | Settings preserved, approved calendar correction, migration unavailable for a known data type |

Bundled local content should not show network-loading spinners. Use loading states only for actual work that needs them.

## 8. Design deliverables and validation

Deliver user journeys, low-fidelity layouts, three comparable visual directions, the selected design system, a clickable prototype, reusable components, screen states, exports with redistribution rights, and developer-ready interaction/accessibility notes.

Use the same four tasks during prototype and beta evaluation:

1. Identify today's religious day name and Nabur status.
2. Find a named upcoming observance and understand its date range.
3. Convert a selected date into the other supported calendars.
4. Enable and then cancel a reminder, including a permission-denied scenario.

Record task success, time, confusion, mistaken taps, and participant comments. Revisit labels or layout where users confuse civil and religious dates. Prototype testing is directional; it does not replace calendar correctness review or production device testing.
