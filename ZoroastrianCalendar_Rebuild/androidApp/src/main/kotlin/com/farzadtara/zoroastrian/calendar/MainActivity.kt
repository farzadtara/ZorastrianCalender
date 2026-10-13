package com.farzadtara.zoroastrian.calendar

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.farzadtara.zoroastrian.sharedui.App

class MainActivity : ComponentActivity() {
    private var sunriseEnabled by mutableStateOf(false)
    private var sunriseStatus by mutableStateOf("یادآوری روزانه هنگام طلوع آفتاب")

    private val notificationPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) requestLocationPermission()
        else sunriseStatus = "برای نمایش یادآوری، اجازه اعلان‌ها را فعال کنید."
    }

    private val locationPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grants ->
        if (grants.values.any { it }) findLocationAndSchedule()
        else sunriseStatus = "برای محاسبه زمان طلوع، اجازه مکان را فعال کنید."
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        sunriseEnabled = SunriseReminder.isEnabled(this)
        if (sunriseEnabled && !SunriseReminder.reschedule(this)) {
            sunriseStatus = "زمان طلوع برای مکان ذخیره‌شده پیدا نشد؛ یادآوری را دوباره فعال کنید."
        } else if (sunriseEnabled) {
            sunriseStatus = "یادآوری طلوع آفتاب فعال است."
        }
        setContent {
            App(
                sunriseReminderEnabled = sunriseEnabled,
                sunriseReminderStatus = sunriseStatus,
                onSunriseReminderToggle = ::setSunriseReminder,
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (sunriseEnabled) {
            if (!getSystemService(NotificationManager::class.java).areNotificationsEnabled()) {
                sunriseStatus = "اعلان‌های برنامه خاموش‌اند؛ برای دریافت یادآوری، آن‌ها را در تنظیمات دستگاه فعال کنید."
            } else {
                val hasLocation = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                if (hasLocation) findLocationAndSchedule()
                else {
                    SunriseReminder.reschedule(this)
                    sunriseStatus = "دسترسی مکان برداشته شده؛ زمان‌بندی از مکان ذخیره‌شده روی دستگاه استفاده می‌کند."
                }
            }
        }
    }

    private fun setSunriseReminder(enabled: Boolean) {
        if (!enabled) {
            SunriseReminder.cancel(this)
            sunriseEnabled = false
            sunriseStatus = "یادآوری طلوع آفتاب خاموش است."
            return
        }

        sunriseStatus = "برای فعال‌سازی یادآوری روزانه، اجازه اعلان و مکان را بررسی کنید."
        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        val hasLocation = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (hasLocation) {
            findLocationAndSchedule()
        } else {
            locationPermissions.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }

    private fun findLocationAndSchedule() {
        sunriseStatus = "در حال دریافت مکان فعلی برای محاسبه طلوع…"
        SunriseLocationRequest(this) { location, error ->
            if (location == null) {
                sunriseStatus = error ?: "مکان دستگاه در دسترس نیست؛ دوباره تلاش کنید."
            } else {
                val notificationManager = getSystemService(NotificationManager::class.java)
                if (Build.VERSION.SDK_INT >= 26) {
                    notificationManager.createNotificationChannel(
                        NotificationChannel("sunrise_calendar", "یادآوری طلوع آفتاب", NotificationManager.IMPORTANCE_DEFAULT),
                    )
                }
                if (SunriseReminder.schedule(this, location)) {
                    sunriseEnabled = true
                    sunriseStatus = "یادآوری روزانه طلوع زمان‌بندی شد؛ مکان فقط روی دستگاه ذخیره می‌شود."
                } else {
                    SunriseReminder.cancel(this)
                    sunriseStatus = "برای این مکان زمان طلوع محاسبه نشد؛ در نواحی قطبی ممکن است طلوع روزانه نباشد."
                }
            }
        }.request()
    }
}
