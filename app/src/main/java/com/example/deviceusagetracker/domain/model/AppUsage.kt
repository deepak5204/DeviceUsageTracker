package com.example.deviceusagetracker.domain.model

import android.graphics.drawable.Drawable

data class AppUsage(
    val icon: Drawable?,
    val packageName: String,
    val appName: String,
    val category: AppCategory,
    val usageMinutes: Long,
    val limitMinutes: Int = 120,
) {
    val remainingMinutes: Int
        get() = (limitMinutes - usageMinutes).coerceAtLeast(0).toInt()

    val progress: Float
        get() = usageMinutes / limitMinutes.toFloat()

    val status: UsageStatus
        get() = when {
            usageMinutes >= limitMinutes -> UsageStatus.LIMIT_REACHED
            usageMinutes >= limitMinutes * 0.6 -> UsageStatus.WARNING
            else -> UsageStatus.SAFE
        }
}


enum class UsageStatus {
    SAFE, WARNING, LIMIT_REACHED
}