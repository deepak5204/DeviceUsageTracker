package com.example.deviceusagetracker.data.repository

import android.content.Context
import android.content.pm.PackageManager
import com.example.deviceusagetracker.data.manager.UsageStatsHelper
import com.example.deviceusagetracker.data.mapper.AppCategoryMapper
import com.example.deviceusagetracker.domain.model.AppUsage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsageRepository(
    private val context: Context,
    private val usageStatsHelper: UsageStatsHelper
) {

    suspend fun getTodayAppUsage(): List<AppUsage> = withContext(Dispatchers.IO) {

        val packageManager = context.packageManager

        val statsList = usageStatsHelper.getUsageStats()

        statsList.mapNotNull { stat ->

            val usageMillis = stat.totalTimeInForeground
            if (usageMillis <= 0) return@mapNotNull null

            val packageName = stat.packageName

            val appName = getAppName(packageManager, packageName)

            val category = AppCategoryMapper.getCategory(packageName)

            AppUsage(
                packageName = packageName,
                appName = appName,
                category = category,
                usageMinutes = usageMillis / 1000 / 60
            )
        }
            .sortedByDescending { it.usageMinutes }
    }

    private fun getAppName(
        packageManager: PackageManager,
        packageName: String
    ): String {
        return try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            packageName
        }
    }
}