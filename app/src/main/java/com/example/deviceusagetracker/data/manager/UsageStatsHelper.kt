package com.example.deviceusagetracker.data.manager

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log
import java.util.Calendar

class UsageStatsHelper(private val context: Context) {

    fun getUsageStats(): List<UsageStats> {

        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

//        val endTime = System.currentTimeMillis()
//        val startTime = endTime - (24 * 60 * 60 * 1000) // Last 24 hours
//
//        val stats = usageStatsManager.queryUsageStats(
//            UsageStatsManager.INTERVAL_DAILY,
//            startTime,
//            endTime
//        )

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        val statsMap = usageStatsManager.queryAndAggregateUsageStats(
            startTime,
            endTime
        )

        val stats = statsMap.values.toList()


        return stats ?: emptyList()
    }

    fun logUsageStats() {
        val stats = getUsageStats()

        stats.forEach {
            Log.d(
                "USAGE_STATS",
                "Package: ${it.packageName}, Time: ${it.totalTimeInForeground}"
            )
        }
    }
}
