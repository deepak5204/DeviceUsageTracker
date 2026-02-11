package com.example.deviceusagetracker.domain.model

data class AppUsage(
    val packageName: String,
    val appName: String,
    val category: AppCategory,
    val usageMinutes: Long
)
