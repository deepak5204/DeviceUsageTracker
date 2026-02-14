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

class UsageRepository(
    private val context: Context,
    private val usageStatsHelper: UsageStatsHelper
) {

    suspend fun getTodayCategoryUsage(): List<CategoryUsage> =
        withContext(Dispatchers.IO) {

            val appUsageList = getTodayAppUsageInternal()

            val grouped = appUsageList.groupBy { it.category }

            grouped.map { (category, apps) ->

                val totalMinutes = apps.sumOf { it.usageMinutes }

                CategoryUsage(
                    category = category,
                    totalUsageMinutes = totalMinutes
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
