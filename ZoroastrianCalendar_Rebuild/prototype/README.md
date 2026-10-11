# Interactive mobile prototype

This is a dependency-free, installable mobile web prototype for reviewing the first app journeys from the legacy screenshots. It is not a production native app or a validated religious calendar.

## Run locally

Run a static server from `ZoroastrianCalendar_Rebuild`, then open `http://localhost:8000/prototype/`:

```powershell
py -m http.server 8000
```

On Android or iOS, serve the same files over HTTPS and use the browser's install/add-to-home-screen action. Browser location access works only from localhost or a secure origin.

## Included

- Persian RTL month grid with today, selection, and source-data markers
- Gregorian, Solar Hijri, and Umm al-Qura display/conversion using the browser's `Intl` calendar data
- Searchable legacy observance index
- Qibla bearing from an explicit location permission; device heading depends on browser sensor support
- Local display preferences and an offline app-shell cache

The yearless legacy JSON is shown only as source data. It does not define a calendar profile, era, leap rules, event recurrence, or approved religious-day behavior. Reminder switches are visual preferences only and do not schedule notifications. The prototype does not yet replace the planned Android/iOS KMP project.

The old converter screenshot reports 11 October 2026 as 29 Rabi al-Thani 1448; the browser's Umm al-Qura data reports the following day. That provider discrepancy is recorded in [LEGACY_APP_INVENTORY.md](../docs/LEGACY_APP_INVENTORY.md) and needs a product/calendar decision.

For a small built-in conversion check, open `http://localhost:8000/prototype/?selfcheck=1`. It checks the Solar Hijri conversion for 2026-10-11, its reverse lookup, invalid Solar Hijri and Gregorian dates, and the legacy-record lookup.
