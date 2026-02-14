package com.example.deviceusagetracker.domain.model

import android.graphics.drawable.Drawable

data class CategoryUsage(
    val category: AppCategory,
    val totalUsageMinutes: Long,
    val limitMinutes: Long = 120, // Default for now (2 hours)
    val remainingMinutes: Long = 0,
    val isLimitReached: Boolean = false
)
