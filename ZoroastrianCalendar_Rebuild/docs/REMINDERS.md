# Sunrise reminder

The Settings screen offers an opt-in daily reminder scheduled around local sunrise. It is a generic prompt to open the calendar. It does not name a Zoroastrian observance because the calendar profile and event recurrence rules have not yet been approved.

## Privacy and setup

- The app requests notification access and foreground/while-in-use location only after the user enables the reminder.
- It takes a location fix when enabled, stores latitude/longitude locally, and does not request background location access or send coordinates to a server.
- When the app returns to the foreground, both platforms refresh the saved location when location access is still available. Otherwise, they keep using the last saved coordinates.
- Turning the setting off cancels pending reminders and removes the stored coordinates.

## Scheduling behavior

- Shared KMP code estimates apparent sunrise using NOAA's published equation-of-time, solar-declination, and sunrise hour-angle equations. Its approximate horizon uses 90.833 degrees to account for solar disk size and atmospheric refraction. This is an estimate; local atmospheric conditions and high latitude increase error. See [NOAA calculation details](https://gml.noaa.gov/grad/solcalc/solareqns.PDF) and [accuracy limits](https://gml.noaa.gov/grad/solcalc/calcdetails.html).
- Android schedules the next sunrise using an inexact, idle-aware alarm and re-arms after delivery, reboot, time change, timezone change, and app replacement. Android may defer the alarm; the app does not request special exact-alarm access.
- iOS queues the next 30 calculated sunrises as local calendar notifications and refreshes the rolling horizon when the app becomes active. If the user does not reopen the app, iOS coverage can end after that horizon. The app does not promise perpetual background replenishment.
- Both platforms may delay or suppress local notifications according to system settings, Focus modes, power policies, and notification authorization.
- Where the calculation finds no sunrise in the scheduling horizon, the app reports that no reminder was scheduled.

## Calendar content boundary

The supplied legacy event JSON does not identify its profile, reference year, recurrence rules, or approved sources. It cannot currently support a truthful event-specific daily alert. The current reminder says “open the calendar to see today's status”; it does not claim an event exists that day. Event titles can be added after Phase 01 approval and the shared event-occurrence engine is implemented.

## Platform references

- [Android alarms](https://developer.android.com/develop/background-work/services/alarms)
- [Android foreground location permission guidance](https://developer.android.com/develop/sensors-and-location/location/permissions/runtime)
- [Apple local notification scheduling](https://developer.apple.com/documentation/usernotifications/scheduling-a-notification-locally-from-your-app)
- [Apple calendar notification triggers](https://developer.apple.com/documentation/usernotifications/uncalendarnotificationtrigger)
- [Apple Core Location authorization](https://developer.apple.com/documentation/corelocation/cllocationmanager/requestwheninuseauthorization())
