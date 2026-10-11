# References and Evidence Limits

**Reviewed for planning:** 2026-10-11.

Platform documentation informs the engineering plan. Cultural references identify conventions and questions for expert review; they do not replace product-specific approval or independently verified date fixtures.

## Platform references

| Reference | Planning relevance |
| --- | --- |
| [Kotlin — Stability of supported platforms](https://kotlinlang.org/docs/multiplatform/supported-platforms.html) | Android/iOS support for KMP and Compose; retain the proposed stack but validate app-specific requirements |
| [JetBrains — New default KMP project structure](https://blog.jetbrains.com/kotlin/2026/05/new-kmp-default-structure/) | Generate a supported current scaffold rather than freezing the original conceptual folder layout |
| [Android — Manage and update GlanceAppWidget](https://developer.android.com/develop/ui/compose/glance/glance-app-widget) | Persistent widget inputs, update requests, process lifetime, and avoiding excessive refreshes |
| [Android — Define work requests](https://developer.android.com/develop/background-work/background-tasks/persistent/getting-started/define-work) | Background scheduling can be deferred; periodic work is not an exact delivery mechanism |
| [Android — Notification runtime permission](https://developer.android.com/develop/ui/compose/notifications/notification-permission) | Native permission states and user-initiated authorization |
| [Android — Schedule alarms](https://developer.android.com/develop/background-work/services/alarms) | Separate decisions and constraints for exact/time-sensitive alarms |
| [Android — Accessibility API defaults](https://developer.android.com/develop/ui/compose/accessibility/api-defaults) | Accessible semantics and the 48 dp touch-target baseline |
| [Apple — UI Design Dos and Don'ts](https://developer.apple.com/design/tips/) | Readability, interaction, and the 44 pt touch-target baseline |
| [Apple — Accessibility](https://developer.apple.com/design/human-interface-guidelines/accessibility) | Platform accessibility design and evaluation |
| [Apple — Scheduling local notifications](https://developer.apple.com/documentation/usernotifications/scheduling-a-notification-locally-from-your-app) | Native local-notification requests and triggers |
| [Apple — Keeping a widget up to date](https://developer.apple.com/documentation/widgetkit/keeping-a-widget-up-to-date) | Timelines, reload policy, and refresh budgets |
| [Apple — App Groups Entitlement](https://developer.apple.com/documentation/BundleResources/Entitlements/com.apple.security.application-groups) | Shared persistent inputs for the app and widget extension |

Platform documentation can change. Resolve dependency versions and minimum OS support during the capability prototype, then recheck store submission policies immediately before release.

## Cultural/calendar references

| Reference | Use and limitation |
| --- | --- |
| [Zoroastrian Heritage — Calendar organization, K. E. Eduljee](https://www.heritageinstitute.com/zoroastrianism/calendar/index.htm) | Background on ordinary months, Gatha/intercalary days, year numbering, and Fasli/Shenshai/Kadmi differences; not an approval of the legacy JSON or a universal conversion algorithm |
| [Amordad — Nabur/meatless-food community event report, 2024-03-05](https://en.amordadnews.com/155092) | Identifies Vahman/Bahman, Mah, Gosh, and Ram in the described community practice; does not settle the special-day flag in the supplied file |

A religious-calendar reviewer should select primary community calendars or other authoritative records for the actual profile and years supported by this product. Record the exact source, date mapping, convention, and reviewer for each fixture.

## Project evidence

- Supplied README(8).md: baseline KMP/Compose proposal and native widget goals.
- Supplied TASKS_AND_PLAN.md: baseline six-phase implementation outline.
- Supplied zoroastrian_events.json: all 365 rows audited; unmodified copy included.
- Supplied legacy Android screenshots: partial visual feature inventory in [LEGACY_APP_INVENTORY.md](LEGACY_APP_INVENTORY.md).
- Similar Google Play listing for Gahshomar Zoroastrian Calendar (`com.gaso.calendar`): [listing](https://play.google.com/store/apps/details?id=com.gaso.calendar); the user confirmed it is not the target app, so it is excluded from product evidence.

The old app binary/source, target store listing, direct user feedback, actual device demographics, signing identities, profile specification, translations, and approved annual reference calendars were not supplied. Tasks depending on these are explicit discovery/review work.

All scope choices, palette candidates, usability targets, effort estimates, and performance budgets in this package are proposed project decisions. They are not claims made by the linked documentation and not measurements already achieved.
