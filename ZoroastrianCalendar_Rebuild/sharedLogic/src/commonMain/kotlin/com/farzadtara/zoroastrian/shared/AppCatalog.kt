package com.farzadtara.zoroastrian.shared

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

data class AppMenuItem(
    val id: String,
    val title: String,
    val systemImage: String,
    val symbol: String,
)

/** Stable app navigation copy and safe calendar status shared by both native hosts. */
class AppCatalog {
    fun menuItems(): List<AppMenuItem> = listOf(
        AppMenuItem("calendar", "تقویم", "calendar", "▦"),
        AppMenuItem("converter", "تبدیل تاریخ", "arrow.left.arrow.right", "↕"),
        AppMenuItem("qibla", "قبله‌نما", "location.north.circle.fill", "⌖"),
        AppMenuItem("settings", "تنظیمات", "gearshape.fill", "⚙"),
        AppMenuItem("about", "درباره", "info.circle.fill", "ⓘ"),
        AppMenuItem("exit", "خروج", "xmark.circle.fill", "×"),
    )

    fun screenTitle(id: String): String = menuItems().firstOrNull { it.id == id }?.title
        ?: if (id == "events") "مناسبت‌ها" else "گاه‌شمار زرتشتی"

    fun todayCivilDate(): String = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()

    fun calendarReviewMessage(): String =
        "قواعد و تاریخ‌های زرتشتی هنوز بازبینی نشده‌اند؛ این نسخه فعلاً تاریخ دینی را تأییدشده نشان نمی‌دهد."

    fun exitMessage(): String =
        "برای بستن برنامه، آن را از فهرست برنامه‌های اخیر دستگاه ببندید."
}
