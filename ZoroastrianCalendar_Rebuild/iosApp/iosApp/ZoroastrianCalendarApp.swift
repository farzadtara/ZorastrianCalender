import SwiftUI

@main
struct ZoroastrianCalendarApp: App {
    @Environment(\.scenePhase) private var scenePhase
    @StateObject private var sunriseReminder = SunriseReminderManager()

    var body: some Scene {
        WindowGroup {
            ContentView(sunriseReminder: sunriseReminder)
                .onChange(of: scenePhase) { phase in
                    if phase == .active { sunriseReminder.refreshIfEnabled() }
                }
        }
    }
}
