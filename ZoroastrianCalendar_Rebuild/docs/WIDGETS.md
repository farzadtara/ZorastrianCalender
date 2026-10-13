# Home-screen widgets

The Android Glance widget and iOS WidgetKit extension provide small and medium home-screen layouts. Both show today's Gregorian civil date in the device time zone, a clear note that the religious calendar is awaiting review, and open the app when tapped.

## Add the widget

- **Android:** open the app once, long-press an empty area of the home screen, choose **Widgets**, find **گاه‌شمار زرتشتی**, then drag it onto the screen. Resize it for the wider layout.
- **iPhone/iPad:** open the app once, long-press the home screen, tap **+**, search for **گاه‌شمار زرتشتی**, choose a size, and tap **Add Widget**.

## Current limits

- Widgets show only the civil date. They do not show a Zoroastrian day, Nabur, or observances until the calendar profile and independent date fixtures are approved.
- Android requests an hourly widget update; iOS provides a next-local-midnight timeline entry. Both operating systems may defer updates.
- The widgets do not yet read persisted calendar preferences or per-widget settings. The iOS extension therefore has no App Group entitlement; a verified product-owned group ID is needed when shared settings or calendar snapshots are introduced.
- Android Glance currently uses the system Persian typeface because Glance does not support custom app fonts. The iOS widget bundles Shabnam.
- Phase 08 remains open pending reviewed data, a shared-data design, time-zone and stale-data handling, and on-device checks. See [TASKS_AND_PLAN.md](../TASKS_AND_PLAN.md#phase-08--native-android-and-ios-widgets).

## Platform references

- [Android Glance: create and configure an app widget](https://developer.android.com/develop/ui/compose/glance/create-app-widget)
- [Android Glance: manage widget state and updates](https://developer.android.com/develop/ui/compose/glance/glance-app-widget)
- [Apple: create a WidgetKit extension](https://developer.apple.com/documentation/widgetkit/creating-a-widget-extension)
- [Apple: keep widgets up to date](https://developer.apple.com/documentation/widgetkit/keeping-a-widget-up-to-date/)
