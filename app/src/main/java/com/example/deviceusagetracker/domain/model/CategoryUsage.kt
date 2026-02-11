package com.example.deviceusagetracker.domain.model

data class CategoryUsage(
    val category: AppCategory,
    val totalUsageMinutes: Long,
    val limitMinutes: Long = 120 // Default for now (2 hours)
)
