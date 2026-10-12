# iOS host

Open `ZoroastrianCalendar.xcodeproj` on macOS and run the `ZoroastrianCalendar` scheme. Xcode's `Build SharedLogic framework` phase calls the Gradle wrapper and links the KMP `SharedLogic` framework into the SwiftUI app.

For command-line simulator builds:

```sh
xcodebuild -project iosApp/ZoroastrianCalendar.xcodeproj \
  -scheme ZoroastrianCalendar \
  -destination 'generic/platform=iOS Simulator' \
  CODE_SIGNING_ALLOWED=NO build
```

This folder uses a prototype bundle identifier and iOS 15 deployment target. Confirm both against existing store/signing records and supported-user devices before release. iOS compilation and device review require macOS/Xcode and have not been performed in the current Windows workspace.
