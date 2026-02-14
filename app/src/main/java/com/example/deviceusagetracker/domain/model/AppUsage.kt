package com.example.deviceusagetracker.domain.model

import android.graphics.drawable.Drawable

data class AppUsage(
    val icon: Drawable?,
    val packageName: String,
    val appName: String,
    val category: AppCategory,
    val usageMinutes: Long
)
