package com.farzadtara.zoroastrian.calendar

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.background
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.cornerRadius
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.TextAlign
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import java.time.LocalDate

private val widgetBackground = ColorProvider(Color(0xFF17231F))
private val primaryText = ColorProvider(Color(0xFFF4F2E9))
private val mutedText = ColorProvider(Color(0xFFB5C0B9))
private val accent = ColorProvider(Color(0xFF83E2C2))

class CalendarWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Responsive(
        setOf(DpSize(180.dp, 140.dp), DpSize(280.dp, 140.dp)),
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val date = LocalDate.now().toString()
        provideContent {
            WidgetContent(date)
        }
    }
}

@Composable
private fun WidgetContent(date: String) {
    val isMedium = LocalSize.current.width >= 240.dp

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(widgetBackground)
            .cornerRadius(22.dp)
            .clickable(actionStartActivity<MainActivity>())
            .padding(16.dp),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.End,
    ) {
        Text(
            text = "گاه‌شمار زرتشتی",
            style = TextStyle(color = accent, fontSize = 13.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.End),
        )
        Spacer(GlanceModifier.height(8.dp))
        Text(
            text = date,
            style = TextStyle(color = primaryText, fontSize = if (isMedium) 26.sp else 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.End),
        )
        Spacer(GlanceModifier.height(8.dp))
        Text(
            text = if (isMedium) {
                "قواعد و تاریخ‌های زرتشتی هنوز بازبینی نشده‌اند"
            } else {
                "تاریخ دینی در انتظار بازبینی"
            },
            style = TextStyle(color = mutedText, fontSize = 12.sp, textAlign = TextAlign.End),
        )
    }
}

class CalendarWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CalendarWidget()
}
