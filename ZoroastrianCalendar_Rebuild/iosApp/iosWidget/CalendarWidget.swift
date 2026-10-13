import SwiftUI
import WidgetKit

private let widgetBackground = Color(red: 23.0 / 255, green: 35.0 / 255, blue: 31.0 / 255)
private let primaryText = Color(red: 244.0 / 255, green: 242.0 / 255, blue: 233.0 / 255)
private let mutedText = Color(red: 181.0 / 255, green: 192.0 / 255, blue: 185.0 / 255)
private let accent = Color(red: 131.0 / 255, green: 226.0 / 255, blue: 194.0 / 255)

private struct CalendarWidgetEntry: TimelineEntry {
    let date: Date
    let civilDate: String
}

private struct CalendarWidgetProvider: TimelineProvider {
    func placeholder(in context: Context) -> CalendarWidgetEntry {
        entry(for: Date())
    }

    func getSnapshot(in context: Context, completion: @escaping (CalendarWidgetEntry) -> Void) {
        completion(entry(for: Date()))
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<CalendarWidgetEntry>) -> Void) {
        let now = Date()
        let calendar = Calendar.current
        let nextDay = calendar.date(byAdding: .day, value: 1, to: calendar.startOfDay(for: now))
            ?? now.addingTimeInterval(24 * 60 * 60)
        completion(Timeline(entries: [entry(for: now), entry(for: nextDay)], policy: .after(nextDay)))
    }

    private func entry(for date: Date) -> CalendarWidgetEntry {
        let formatter = DateFormatter()
        formatter.calendar = Calendar(identifier: .gregorian)
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.timeZone = .current
        formatter.dateFormat = "yyyy-MM-dd"
        return CalendarWidgetEntry(date: date, civilDate: formatter.string(from: date))
    }
}

private struct CalendarWidgetView: View {
    let entry: CalendarWidgetEntry
    @Environment(\.widgetFamily) private var family

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("گاه‌شمار زرتشتی")
                .font(.custom("Shabnam-Bold", size: 14, relativeTo: .headline))
                .foregroundStyle(accent)

            Text("تاریخ مدنی امروز")
                .font(.custom("Shabnam", size: 12, relativeTo: .caption))
                .foregroundStyle(mutedText)

            Text(entry.civilDate)
                .font(.system(size: family == .systemMedium ? 30 : 24, weight: .bold, design: .rounded))
                .foregroundStyle(primaryText)
                .environment(\.layoutDirection, .leftToRight)
                .minimumScaleFactor(0.75)
                .lineLimit(1)

            Spacer(minLength: 0)

            Text(family == .systemMedium
                 ? "قواعد و تاریخ‌های زرتشتی هنوز بازبینی نشده‌اند"
                 : "تاریخ دینی در انتظار بازبینی")
                .font(.custom("Shabnam", size: 11, relativeTo: .caption))
                .foregroundStyle(mutedText)
                .lineLimit(2)
        }
        .padding(16)
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
        .widgetBackground(widgetBackground)
        .environment(\.layoutDirection, .rightToLeft)
    }
}

private extension View {
    @ViewBuilder
    func widgetBackground(_ color: Color) -> some View {
        if #available(iOSApplicationExtension 17.0, *) {
            containerBackground(for: .widget) { color }
        } else {
            background(color)
        }
    }
}

@main
struct ZoroastrianCalendarWidget: Widget {
    var body: some WidgetConfiguration {
        StaticConfiguration(kind: "ZoroastrianCalendarWidget", provider: CalendarWidgetProvider()) { entry in
            CalendarWidgetView(entry: entry)
        }
        .configurationDisplayName("گاه‌شمار زرتشتی")
        .description("تاریخ مدنی امروز و وضعیت بازبینی تقویم")
        .supportedFamilies([.systemSmall, .systemMedium])
    }
}
