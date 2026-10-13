package com.farzadtara.zoroastrian.calendar

import android.Manifest
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.farzadtara.zoroastrian.shared.SunriseCalculator
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

internal object SunriseReminder {
    private const val PREFS = "sunrise_reminder"
    private const val ALARM_REQUEST = 4107

    fun isEnabled(context: Context): Boolean = preferences(context).getBoolean("enabled", false)

    private fun setEnabled(context: Context, enabled: Boolean) {
        preferences(context).edit().putBoolean("enabled", enabled).apply()
    }

    fun schedule(context: Context, location: Location): Boolean {
        preferences(context).edit()
            .putBoolean("enabled", true)
            .putFloat("latitude", location.latitude.toFloat())
            .putFloat("longitude", location.longitude.toFloat())
            .apply()
        return scheduleNext(context)
    }

    fun cancel(context: Context) {
        setEnabled(context, false)
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        alarmManager.cancel(alarmIntent(context))
        preferences(context).edit().remove("latitude").remove("longitude").apply()
    }

    fun reschedule(context: Context): Boolean = scheduleNext(context)

    private fun scheduleNext(context: Context): Boolean {
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        if (!isEnabled(context)) {
            alarmManager.cancel(alarmIntent(context))
            return false
        }
        val prefs = preferences(context)
        if (!prefs.contains("latitude") || !prefs.contains("longitude")) {
            alarmManager.cancel(alarmIntent(context))
            return false
        }
        val latitude = prefs.getFloat("latitude", Float.NaN).toDouble()
        val longitude = prefs.getFloat("longitude", Float.NaN).toDouble()
        val nextSunriseMillis = nextSunriseMillis(latitude, longitude, System.currentTimeMillis())
        if (nextSunriseMillis == null) {
            alarmManager.cancel(alarmIntent(context))
            return false
        }
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextSunriseMillis, alarmIntent(context))
        return true
    }

    private fun nextSunriseMillis(latitude: Double, longitude: Double, afterMillis: Long): Long? {
        val zone = ZoneId.systemDefault()
        var date = Instant.ofEpochMilli(afterMillis).atZone(zone).toLocalDate()
        val calculator = SunriseCalculator()
        repeat(370) {
            val utcDate = date.atTime(LocalTime.NOON).atZone(zone).withZoneSameInstant(ZoneOffset.UTC).toLocalDate()
            val epochSeconds = calculator.sunriseEpochSeconds(
                utcDate.year,
                utcDate.monthValue,
                utcDate.dayOfMonth,
                latitude,
                longitude,
            )
            if (!epochSeconds.isNaN()) {
                val sunriseMillis = (epochSeconds * 1000.0).toLong()
                if (sunriseMillis > afterMillis + 30_000) return sunriseMillis
            }
            date = date.plusDays(1)
        }
        return null
    }

    private fun alarmIntent(context: Context): PendingIntent = PendingIntent.getBroadcast(
        context,
        ALARM_REQUEST,
        Intent(context, SunriseReminderReceiver::class.java).setAction(ACTION_SUNRISE),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )

    private fun preferences(context: Context) = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    const val ACTION_SUNRISE = "com.farzadtara.zoroastrian.calendar.SUNRISE"
}

internal class SunriseReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == SunriseReminder.ACTION_SUNRISE) {
            postReminder(context)
        }
        SunriseReminder.reschedule(context)
    }

    @Suppress("DEPRECATION")
    private fun postReminder(context: Context) {
        if (Build.VERSION.SDK_INT >= 33 &&
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return

        val manager = context.getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= 26) {
            manager.createNotificationChannel(
                NotificationChannel(CHANNEL_ID, "یادآوری طلوع آفتاب", NotificationManager.IMPORTANCE_DEFAULT),
            )
        }
        if (!manager.areNotificationsEnabled()) return

        val openApp = PendingIntent.getActivity(
            context,
            4108,
            Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val notification = (if (Build.VERSION.SDK_INT >= 26) {
            Notification.Builder(context, CHANNEL_ID)
        } else {
            Notification.Builder(context)
        })
            .setSmallIcon(android.R.drawable.ic_menu_today)
            .setContentTitle("گاه‌شمار زرتشتی")
            .setContentText("برای دیدن وضعیت امروز در تقویم زرتشتی، برنامه را باز کنید.")
            .setContentIntent(openApp)
            .setAutoCancel(true)
            .build()
        manager.notify(4109, notification)
    }

    private companion object {
        const val CHANNEL_ID = "sunrise_calendar"
    }
}

internal class SunriseReminderSystemReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (SunriseReminder.isEnabled(context)) SunriseReminder.reschedule(context)
    }
}

internal class SunriseLocationRequest(
    private val context: MainActivity,
    private val onResult: (Location?, String?) -> Unit,
) {
    fun request() {
        val fine = context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (!fine && !coarse) {
            onResult(null, "برای محاسبه طلوع، اجازه دسترسی به مکان لازم است.")
            return
        }

        val manager = context.getSystemService(LocationManager::class.java)
        val providers = if (fine) {
            listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
        } else {
            listOf(LocationManager.NETWORK_PROVIDER, LocationManager.PASSIVE_PROVIDER)
        }
        val provider = providers.firstOrNull { runCatching { manager.isProviderEnabled(it) }.getOrDefault(false) }
        if (provider == null) {
            onResult(null, "مکان‌یابی دستگاه خاموش است؛ آن را روشن کنید و دوباره تلاش کنید.")
            return
        }

        runCatching { manager.getLastKnownLocation(provider) }.getOrNull()?.let { cached ->
            if (System.currentTimeMillis() - cached.time < LOCATION_MAX_AGE_MS) {
                onResult(cached, null)
                return
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            manager.getCurrentLocation(provider, null, context.mainExecutor) { location ->
                if (location == null) onResult(null, "مکان دستگاه در دسترس نیست؛ دوباره تلاش کنید.")
                else onResult(location, null)
            }
        } else {
            requestSingleLocation(manager, provider)
        }
    }

    @Suppress("DEPRECATION")
    private fun requestSingleLocation(manager: LocationManager, provider: String) {
        val handler = Handler(Looper.getMainLooper())
        lateinit var listener: LocationListener
        listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                manager.removeUpdates(this)
                handler.removeCallbacksAndMessages(this)
                onResult(location, null)
            }

            override fun onProviderDisabled(provider: String) {
                manager.removeUpdates(this)
                handler.removeCallbacksAndMessages(this)
                onResult(null, "مکان دستگاه در دسترس نیست؛ دوباره تلاش کنید.")
            }

            override fun onProviderEnabled(provider: String) = Unit

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) = Unit
        }
        manager.requestSingleUpdate(provider, listener, Looper.getMainLooper())
        handler.postAtTime({
            manager.removeUpdates(listener)
            onResult(null, "دریافت مکان بیش از حد طول کشید؛ دوباره تلاش کنید.")
        }, listener, android.os.SystemClock.uptimeMillis() + LOCATION_TIMEOUT_MS)
    }

    private companion object {
        const val LOCATION_MAX_AGE_MS = 30 * 60 * 1000L
        const val LOCATION_TIMEOUT_MS = 20_000L
    }
}
