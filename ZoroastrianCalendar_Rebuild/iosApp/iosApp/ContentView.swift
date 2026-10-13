import SwiftUI
import SharedLogic

private let pageBackground = Color(red: 16.0 / 255, green: 24.0 / 255, blue: 23.0 / 255)
private let cardBackground = Color(red: 23.0 / 255, green: 35.0 / 255, blue: 31.0 / 255)
private let raisedBackground = Color(red: 30.0 / 255, green: 44.0 / 255, blue: 39.0 / 255)
private let teal = Color(red: 131.0 / 255, green: 226.0 / 255, blue: 194.0 / 255)
private let textPrimary = Color(red: 244.0 / 255, green: 242.0 / 255, blue: 233.0 / 255)
private let textMuted = Color(red: 181.0 / 255, green: 192.0 / 255, blue: 185.0 / 255)

struct ContentView: View {
    private let catalog = AppCatalog()
    @State private var selectedID = "calendar"
    @State private var drawerPresented = false
    @State private var showExitMessage = false

    private var menuItems: [AppMenuItem] { catalog.menuItems() }

    var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .leading) {
                VStack(spacing: 0) {
                    header
                    ScrollView {
                        page.padding(.horizontal, 18).padding(.top, 20).padding(.bottom, 32)
                    }
                    bottomNavigation
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(pageBackground.ignoresSafeArea())
                .accessibilityHidden(drawerPresented)

                if drawerPresented {
                    Button(action: closeDrawer) {
                        Color.black.opacity(0.68).ignoresSafeArea()
                    }
                        .buttonStyle(.plain)
                        .accessibilityLabel("بستن منو")

                    DrawerView(items: menuItems, selectedID: selectedID) { item in
                        if item.id == "exit" {
                            showExitMessage = true
                        } else {
                            selectedID = item.id
                        }
                        closeDrawer()
                    }
                    .frame(width: min(geometry.size.width * 0.76, 360), height: geometry.size.height)
                    .background(cardBackground)
                    .transition(.move(edge: .leading))
                    .accessibilityAddTraits(.isModal)
                }
            }
            .environment(\.layoutDirection, .rightToLeft)
            .animation(.easeOut(duration: 0.22), value: drawerPresented)
        }
        .preferredColorScheme(.dark)
        .alert("خروج از برنامه", isPresented: $showExitMessage) {
            Button("متوجه شدم", role: .cancel) { }
        } message: {
            Text(catalog.exitMessage())
        }
    }

    private var header: some View {
        HStack(spacing: 14) {
            Button {
                drawerPresented = true
            } label: {
                Image(systemName: "line.3.horizontal")
                    .font(.system(size: 19, weight: .medium))
                    .foregroundStyle(teal)
                    .frame(width: 48, height: 48)
                    .background(cardBackground, in: RoundedRectangle(cornerRadius: 15))
            }
            .accessibilityLabel("بازکردن منو")

            VStack(alignment: .leading, spacing: 2) {
                Text("گاه‌شمار زرتشتی").shabnam(12, relativeTo: .caption).foregroundStyle(textMuted)
                Text(catalog.screenTitle(id: selectedID)).shabnam(20, relativeTo: .title3, weight: .bold).foregroundStyle(textPrimary)
            }
            Spacer(minLength: 0)
        }
        .padding(.horizontal, 18)
        .padding(.top, 10)
    }

    private var bottomNavigation: some View {
        HStack(spacing: 4) {
            ForEach(menuItems.filter { ["calendar", "events", "converter", "qibla", "settings"].contains($0.id) }, id: \.id) { item in
                Button { selectedID = item.id } label: {
                    VStack(spacing: 4) {
                        Image(systemName: item.systemImage).font(.system(size: 19, weight: .medium))
                        Text(item.title).shabnam(10, relativeTo: .caption, weight: selectedID == item.id ? .bold : .regular)
                    }
                    .foregroundStyle(selectedID == item.id ? teal : textMuted)
                    .frame(maxWidth: .infinity, minHeight: 54)
                    .background(selectedID == item.id ? teal.opacity(0.12) : .clear, in: RoundedRectangle(cornerRadius: 14))
                    .contentShape(Rectangle())
                }
                .buttonStyle(.plain)
                .accessibilityAddTraits(selectedID == item.id ? .isSelected : [])
            }
        }
        .padding(.horizontal, 10)
        .padding(.top, 7)
        .padding(.bottom, 4)
        .background(cardBackground.ignoresSafeArea(edges: .bottom))
        .overlay(alignment: .top) { Rectangle().fill(Color.white.opacity(0.06)).frame(height: 1) }
    }

    @ViewBuilder
    private var page: some View {
        switch selectedID {
        case "calendar": calendarPage
        case "events": infoCard("مناسبت‌ها", "محتوای قدیمی موجود است، اما قواعد تکرار و تاریخ‌های آینده هنوز تأیید نشده‌اند.")
        case "converter": infoCard("تبدیل تاریخ", "مبدل پس از انتخاب و بررسی قواعد تقویم در بخش مشترک فعال می‌شود.")
        case "qibla": infoCard("قبله‌نما", "برای این ابزار، دسترسی مکان فقط پس از درخواست و اجازه شما استفاده خواهد شد.")
        case "settings": infoCard("تنظیمات", "زبان، شیوه رقم‌ها، رنگ‌بندی، شروع هفته و یادآورها در این بخش قرار می‌گیرند.")
        case "about": infoCard("درباره", "نسخه آغازین برنامه زرتشتی؛ تقویم دینی تا زمان تأیید قواعد، غیرفعال است.")
        default: calendarPage
        }
    }

    private var calendarPage: some View {
        VStack(alignment: .leading, spacing: 14) {
            Text("امروز در یک نگاه").shabnam(15, relativeTo: .subheadline).foregroundStyle(textMuted)
            VStack(alignment: .leading, spacing: 10) {
                Text("تاریخ مدنی امروز").shabnam(15, relativeTo: .subheadline).foregroundStyle(textMuted)
                Text(catalog.todayCivilDate())
                    .shabnam(34, relativeTo: .largeTitle, weight: .bold)
                    .foregroundStyle(textPrimary)
                    .environment(\.layoutDirection, .leftToRight)
                Text("پروفایل تقویم زرتشتی در انتظار بازبینی")
                    .shabnam(15, relativeTo: .subheadline, weight: .bold)
                    .foregroundStyle(Color(red: 229.0 / 255, green: 188.0 / 255, blue: 104.0 / 255))
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(20)
            .background(
                LinearGradient(colors: [Color(red: 29.0 / 255, green: 49.0 / 255, blue: 40.0 / 255), cardBackground], startPoint: .topTrailing, endPoint: .bottomLeading),
                in: RoundedRectangle(cornerRadius: 24)
            )
            .overlay(RoundedRectangle(cornerRadius: 24).stroke(Color.white.opacity(0.06), lineWidth: 1))

            infoCard("درباره تاریخ‌های زرتشتی", catalog.calendarReviewMessage())
            Text("پس از تأیید قواعد، نمای ماهانه، نام روز، نابور و مناسبت‌های مرتبط به این صفحه افزوده می‌شود.")
                .shabnam(16, relativeTo: .body)
                .foregroundStyle(textMuted)
                .lineSpacing(6)
                .padding(.horizontal, 4)
        }
    }

    private func infoCard(_ title: String, _ message: String) -> some View {
        VStack(alignment: .leading, spacing: 10) {
            Text(title).shabnam(19, relativeTo: .headline, weight: .bold).foregroundStyle(textPrimary)
            Text(message).shabnam(16, relativeTo: .body).foregroundStyle(textMuted).lineSpacing(7)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(18)
        .background(raisedBackground, in: RoundedRectangle(cornerRadius: 21))
        .overlay(RoundedRectangle(cornerRadius: 21).stroke(Color.white.opacity(0.05), lineWidth: 1))
    }

    private func closeDrawer() {
        withAnimation(.easeOut(duration: 0.22)) { drawerPresented = false }
    }
}

private struct DrawerView: View {
    let items: [AppMenuItem]
    let selectedID: String
    let onSelect: (AppMenuItem) -> Void

    var body: some View {
        VStack(spacing: 0) {
            DrawerBanner()
            ScrollView {
                VStack(spacing: 8) {
                    ForEach(items, id: \.id) { item in
                        Button { onSelect(item) } label: {
                            HStack(spacing: 18) {
                                Image(systemName: item.systemImage)
                                    .font(.title2.weight(.medium))
                                    .foregroundStyle(teal)
                                    .frame(width: 34)
                                Text(item.title)
                                    .shabnam(19, relativeTo: .title3)
                                    .foregroundStyle(textPrimary)
                                Spacer(minLength: 0)
                            }
                            .padding(.horizontal, 16)
                            .frame(height: 62)
                            .background(item.id == selectedID ? Color.white.opacity(0.07) : .clear, in: RoundedRectangle(cornerRadius: 13))
                            .contentShape(Rectangle())
                        }
                        .buttonStyle(.plain)
                        .accessibilityAddTraits(item.id == selectedID ? .isSelected : [])
                    }
                }
                .padding(.horizontal, 14)
                .padding(.top, 14)
            }
            Spacer(minLength: 0)
        }
        .background(cardBackground)
    }
}

private struct DrawerBanner: View {
    var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .bottomLeading) {
                LinearGradient(colors: [Color(red: 1, green: 0.81, blue: 0.34), Color(red: 0.91, green: 0.56, blue: 0.12)], startPoint: .top, endPoint: .bottom)
                Circle().fill(Color(red: 1, green: 0.87, blue: 0.42)).frame(width: 72, height: 72).position(x: geometry.size.width * 0.76, y: 43)
                Ellipse().fill(Color(red: 0.91, green: 0.52, blue: 0.1)).frame(width: geometry.size.width * 1.3, height: 95).offset(x: -geometry.size.width * 0.08, y: 30)
                Ellipse().fill(Color(red: 0.79, green: 0.39, blue: 0.1)).frame(width: geometry.size.width * 0.9, height: 74).offset(x: geometry.size.width * 0.43, y: 44)
                VStack(alignment: .leading, spacing: 3) {
                    Text("گاه‌شمار زرتشتی").shabnam(19, relativeTo: .headline, weight: .bold)
                    Text("تقویم، مناسبت‌ها و ابزارها").shabnam(12, relativeTo: .caption)
                }
                .foregroundStyle(Color(red: 0.19, green: 0.14, blue: 0.08))
                .padding(18)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .clipped()
        }
        .frame(height: 176)
        .clipShape(RoundedRectangle(cornerRadius: 18))
    }
}

private extension View {
    func shabnam(_ size: CGFloat, relativeTo style: Font.TextStyle = .body, weight: Font.Weight = .regular) -> some View {
        let name = weight == .bold ? "Shabnam-Bold" : "Shabnam"
        return font(.custom(name, size: size, relativeTo: style))
    }
}
