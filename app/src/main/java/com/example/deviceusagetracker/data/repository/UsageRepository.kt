package com.example.deviceusagetracker.data.repository

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.example.deviceusagetracker.data.manager.UsageStatsHelper
import com.example.deviceusagetracker.data.mapper.AppCategoryMapper
import com.example.deviceusagetracker.domain.model.AppUsage
import com.example.deviceusagetracker.domain.model.CategoryUsage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.deviceusagetracker.domain.model.AppCategory

class UsageRepository(
    private val context: Context,
    private val usageStatsHelper: UsageStatsHelper
) {

    private val defaultLimits = mapOf(
        AppCategory.SOCIAL_MEDIA to 120L,
        AppCategory.ENTERTAINMENT to 180L,
        AppCategory.EDUCATION to 240L,
        AppCategory.OTHER to 300L
    )
    suspend fun getTodayCategoryUsage(): List<CategoryUsage> =
        withContext(Dispatchers.IO) {

            val appUsageList = getTodayAppUsageInternal()

            val grouped = appUsageList.groupBy { it.category }

            grouped.map { (category, apps) ->

                val totalMinutes = apps.sumOf { it.usageMinutes }

                val limit = defaultLimits[category] ?: 0L

                val remaining = (limit - totalMinutes).coerceAtLeast(0)

                val isReached = totalMinutes >= limit && limit > 0


                CategoryUsage(
                    category = category,
                    totalUsageMinutes = totalMinutes,
                    limitMinutes = limit,
                    remainingMinutes = remaining,
                    isLimitReached = isReached
                )
            }.sortedByDescending { it.totalUsageMinutes }
        }

    suspend fun getTodayAppUsage(): List<AppUsage> =
        withContext(Dispatchers.IO) {
            getTodayAppUsageInternal()
        }

    private fun getTodayAppUsageInternal(): List<AppUsage> {

        val packageManager = context.packageManager
        val statsList = usageStatsHelper.getUsageStats()

        Log.d("TAG", "getTodayAppUsageInternal: $statsList")

        return statsList.mapNotNull { stat ->


            val usageMillis = stat.totalTimeInForeground
            if (usageMillis <= 0) return@mapNotNull null

            val packageName = stat.packageName

            val appName = getAppName(packageManager, packageName)

            val category = AppCategoryMapper.getCategory(packageName)

            val icon = getAppIcon(packageManager, packageName)

            AppUsage(
                icon = icon,
                packageName = packageName,
                appName = appName,
                category = category,
                usageMinutes = usageMillis / 1000 / 60
            )
        }.sortedByDescending { it.usageMinutes }
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

    private fun getAppIcon(
        packageManager: PackageManager,
        packageName: String
    ) = try {
        val appInfo = packageManager.getApplicationInfo(packageName, 0)
        packageManager.getApplicationIcon(appInfo)
    } catch (e: Exception) {
        null
    }

}
