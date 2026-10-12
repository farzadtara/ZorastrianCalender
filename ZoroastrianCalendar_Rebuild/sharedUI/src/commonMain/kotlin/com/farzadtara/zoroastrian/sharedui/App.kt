package com.farzadtara.zoroastrian.sharedui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farzadtara.zoroastrian.shared.AppCatalog
import com.farzadtara.zoroastrian.shared.AppMenuItem
import kotlinx.coroutines.launch

private val Background = Color(0xFF171D1D)
private val SurfaceColor = Color(0xFF222B2B)
private val TextColor = Color(0xFFF4F2E9)
private val MutedColor = Color(0xFFB5C0B9)
private val Accent = Color(0xFF20C7A7)
private val Warning = Color(0xFFE5BC68)

@Composable
fun App() {
    val catalog = remember { AppCatalog() }
    val menuItems = remember { catalog.menuItems() }
    val drawerState = androidx.compose.material3.rememberDrawerState(
        initialValue = androidx.compose.material3.DrawerValue.Closed,
    )
    val scope = rememberCoroutineScope()
    var selectedID by rememberSaveable { mutableStateOf("calendar") }
    var showExitMessage by rememberSaveable { mutableStateOf(false) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(colorScheme = androidx.compose.material3.darkColorScheme(
            primary = Accent,
            onPrimary = Color(0xFF06231D),
            background = Background,
            onBackground = TextColor,
            surface = SurfaceColor,
            onSurface = TextColor,
        )) {
            ModalNavigationDrawer(
                drawerState = drawerState,
                scrimColor = Color.Black.copy(alpha = .68f),
                drawerContent = {
                    ModalDrawerSheet(
                        modifier = Modifier.width(320.dp),
                        drawerContainerColor = Color(0xFF222222),
                        drawerTonalElevation = 0.dp,
                    ) {
                        DrawerHeader()
                        Column(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            menuItems.forEach { item ->
                                DrawerRow(
                                    item = item,
                                    selected = item.id == selectedID,
                                    onClick = {
                                        if (item.id == "exit") showExitMessage = true
                                        else selectedID = item.id
                                        scope.launch { drawerState.close() }
                                    },
                                )
                            }
                        }
                    }
                },
            ) {
                Scaffold(
                    containerColor = Background,
                    contentWindowInsets = WindowInsets.safeDrawing,
                    topBar = {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            Surface(
                                onClick = { scope.launch { drawerState.open() } },
                                modifier = Modifier.size(48.dp),
                                shape = RoundedCornerShape(15.dp),
                                color = SurfaceColor,
                                contentColor = Accent,
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("☰", fontSize = 23.sp, modifier = Modifier.semantics {
                                        contentDescription = "بازکردن منو"
                                    })
                                }
                            }
                            Column {
                                Text("گاه‌شمار زرتشتی", color = MutedColor, fontSize = 11.sp)
                                Text(catalog.screenTitle(selectedID), color = TextColor, fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    },
                    bottomBar = {
                        BottomNavigation(selectedID = selectedID, onSelect = { selectedID = it })
                    },
                ) { padding ->
                    ScreenContent(
                        destination = selectedID,
                        catalog = catalog,
                        modifier = Modifier.fillMaxSize().padding(padding),
                    )
                }
            }
        }
    }

    if (showExitMessage) {
        AlertDialog(
            onDismissRequest = { showExitMessage = false },
            title = { Text("خروج از برنامه") },
            text = { Text(catalog.exitMessage()) },
            confirmButton = {
                TextButton(onClick = { showExitMessage = false }) { Text("متوجه شدم") }
            },
        )
    }
}

@Composable
private fun DrawerHeader() {
    Box(modifier = Modifier.fillMaxWidth().height(176.dp).clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))) {
        Canvas(Modifier.fillMaxSize()) {
            drawRect(Color(0xFFF0AD26), style = Fill)
            drawCircle(Color(0xFFFFD65C), radius = size.minDimension * .12f, center = androidx.compose.ui.geometry.Offset(size.width * .78f, size.height * .28f))
            drawOval(Color(0xFFE78B1D), topLeft = androidx.compose.ui.geometry.Offset(-size.width * .1f, size.height * .58f), size = androidx.compose.ui.geometry.Size(size.width * 1.25f, size.height * .7f))
            drawOval(Color(0xFFCA6719), topLeft = androidx.compose.ui.geometry.Offset(size.width * .28f, size.height * .72f), size = androidx.compose.ui.geometry.Size(size.width, size.height * .55f))
            drawCircle(Color(0xFFC95225), radius = size.minDimension * .08f, center = androidx.compose.ui.geometry.Offset(size.width * .19f, size.height * .69f))
            drawCircle(Color(0xFFC95225), radius = size.minDimension * .1f, center = androidx.compose.ui.geometry.Offset(size.width * .71f, size.height * .67f))
            drawRect(Color(0xFFC95225), topLeft = androidx.compose.ui.geometry.Offset(size.width * .18f, size.height * .68f), size = androidx.compose.ui.geometry.Size(size.width * .025f, size.height * .24f))
            drawRect(Color(0xFFC95225), topLeft = androidx.compose.ui.geometry.Offset(size.width * .7f, size.height * .67f), size = androidx.compose.ui.geometry.Size(size.width * .03f, size.height * .25f))
        }
        Column(modifier = Modifier.align(Alignment.BottomStart).padding(18.dp)) {
            Text("گاه‌شمار زرتشتی", color = Color(0xFF2A2115), fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text("تقویم، مناسبت‌ها و ابزارها", color = Color(0xFF4B3212), fontSize = 11.sp)
        }
    }
}

@Composable
private fun DrawerRow(item: AppMenuItem, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(58.dp),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) Color(0xFF2C4842) else Color.Transparent,
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Text(item.symbol, color = Accent, fontSize = 23.sp, fontWeight = FontWeight.Medium)
            Text(item.title, color = TextColor, fontSize = 16.sp)
        }
    }
}

@Composable
private fun BottomNavigation(selectedID: String, onSelect: (String) -> Unit) {
    val items = listOf(
        Triple("calendar", "تقویم", "▦"),
        Triple("events", "مناسبت‌ها", "◷"),
        Triple("converter", "تبدیل", "↕"),
        Triple("settings", "تنظیمات", "⚙"),
    )
    NavigationBar(containerColor = Color(0xFF202727)) {
        items.forEach { (id, label, symbol) ->
            NavigationBarItem(
                selected = id == selectedID,
                onClick = { onSelect(id) },
                icon = { Text(symbol, fontSize = 20.sp) },
                label = { Text(label, fontSize = 10.sp) },
            )
        }
    }
}

@Composable
private fun ScreenContent(destination: String, catalog: AppCatalog, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            when (destination) {
                "calendar" -> CalendarOverview(catalog)
                "events" -> InfoCard("مناسبت‌ها", "محتوای قدیمی موجود است، اما قواعد تکرار و تاریخ‌های آینده هنوز تأیید نشده‌اند.")
                "converter" -> InfoCard("تبدیل تاریخ", "مبدل پس از انتخاب و بررسی قواعد تقویم در بخش مشترک فعال می‌شود.")
                "qibla" -> InfoCard("قبله‌نما", "برای این ابزار، دسترسی مکان فقط پس از درخواست و اجازه شما استفاده خواهد شد.")
                "settings" -> InfoCard("تنظیمات", "زبان، شیوه رقم‌ها، رنگ‌بندی، شروع هفته و یادآورها در این بخش قرار می‌گیرند.")
                "about" -> InfoCard("درباره", "نسخه آغازین برنامه زرتشتی؛ تقویم دینی تا زمان تأیید قواعد، غیرفعال است.")
                else -> CalendarOverview(catalog)
            }
        }
    }
}

@Composable
private fun CalendarOverview(catalog: AppCatalog) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("امروز در یک نگاه", color = MutedColor, fontSize = 12.sp)
        Surface(shape = RoundedCornerShape(22.dp), color = SurfaceColor) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("تاریخ مدنی امروز", color = MutedColor, fontSize = 12.sp)
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Text(catalog.todayCivilDate(), color = TextColor, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }
                Text("پروفایل تقویم زرتشتی در انتظار بازبینی", color = Warning, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        InfoCard("درباره تاریخ‌های زرتشتی", catalog.calendarReviewMessage())
        Text(
            "پس از تأیید قواعد، نمای ماهانه، نام روز، نابور و مناسبت‌های مرتبط به این صفحه افزوده می‌شود.",
            color = MutedColor,
            fontSize = 13.sp,
            lineHeight = 21.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(horizontal = 4.dp),
        )
    }
}

@Composable
private fun InfoCard(title: String, message: String) {
    Surface(shape = RoundedCornerShape(20.dp), color = SurfaceColor) {
        Column(modifier = Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, color = TextColor, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            Text(message, color = MutedColor, fontSize = 13.sp, lineHeight = 21.sp)
        }
    }
}
